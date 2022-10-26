package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Repository repository = new Repository();
    private final Managers managers = new Managers();

    @Override
    public Task getTaskById(int id) {
        Optional<Task> defaultTask = repository.getTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        return defaultTask.orElse(null);
    }

    @Override
    public Task getEpicTaskById(int id) {
        Optional<EpicTask> epicTask = repository.getEpicTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        return epicTask.orElse(null);
    }

    @Override
    public Task getSubTaskById(int id) {
        Optional<SubTask> subtask = repository.getSubtaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        return subtask.orElse(null);
    }

    @Override
    public List<Task> listElement() {
        return new ArrayList<>() {{
            addAll(repository.getTaskList());
            addAll(repository.getEpicTaskList());
            addAll(repository.getSubtaskList());
        }};
    }

    @Override
    public Task updateTask(Task task, Task newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
        managers.getHistory().add(task);
        return task;
    }

    @Override
    public Task updateEpicTask(EpicTask task, EpicTask newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
        managers.getHistory().add(task);
        return task;
    }

    @Override
    public Task updateSubtaskTask(SubTask task, SubTask newSubTask) {
        task.setNameTask(newSubTask.getNameTask() != null ? newSubTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newSubTask.getTaskDetail() != null ? newSubTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newSubTask.getStatus() != null ? newSubTask.getStatus() : task.getStatus());
        task.setEpicId(newSubTask.getEpicId() != 0 ? newSubTask.getEpicId() : task.getEpicId());
        managers.getHistory().add(task);
        return task;
    }

    @Override
    public void changeSubtaskStatus(int id, Status status) {
        SubTask subTask = (SubTask) getSubTaskById(id);
        if (subTask != null) {
            subTask.setStatus(status);
            int epicId = subTask.getEpicId();
            searchStatusDoneInChild((EpicTask) getEpicTaskById(epicId));
        }
        managers.getHistory().add(subTask);
    }

    @Override
    public void changeEpicStatus(Integer id, Status status) {
        EpicTask epicTask = (EpicTask) getEpicTaskById(id);
        if (epicTask != null) {
            if (status.equals(Status.IN_PROGRESS) && Objects.requireNonNull(epicTask).getStatus().equals(Status.NEW)) {
                CheckStatusInSubTask(Status.IN_PROGRESS, epicTask);
            } else if (status.equals(Status.NEW)) {
                CheckStatusInSubTask(Status.NEW, epicTask);
            }
        }
        managers.getHistory().add(epicTask);
    }

    private void CheckStatusInSubTask(Status status, EpicTask epicTask) {
        epicTask.setStatus(status);
        if (epicTask.getSubtaskIds() != null) {
            SetNewStatusInSubTask(epicTask, status);
        }
    }

    private void SetNewStatusInSubTask(EpicTask epicTask, Status status) {
        epicTask.getSubtaskIds().forEach(id -> {
            getSubTaskById(id).setStatus(status);
            managers.getHistory().add(getSubTaskById(id));
        });
    }

    @Override
    public void changeTaskStatus(Integer id, Status status) {
        Objects.requireNonNull(getTaskById(id)).setStatus(status);
        managers.getHistory().add(getTaskById(id));
    }

    @Override
    public void searchStatusDoneInChild(EpicTask task) {
        boolean checkAllSubtaskIsDone = repository.getSubtaskList().stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .map(SubTask::getStatus)
                .allMatch(Predicate.isEqual(Status.DONE));

        if (checkAllSubtaskIsDone) {
            task.setStatus(Status.DONE);
            managers.getHistory().add(task);
        }
    }

    @Override
    public void removeByID(Integer id) {
        if (!repository.getTaskList().isEmpty() && getTaskById(id) != null) {
            removeTask(id);
        } else if (!repository.getSubtaskList().isEmpty() && getSubTaskById(id) != null) {
            removeSubTask(id);
        } else if (!repository.getEpicTaskList().isEmpty() && getEpicTaskById(id) != null) {
            removeElementInEpicTask(id);

        }
    }

    private void removeTask(Integer id) {
        repository.getTaskList().remove(getTaskById(id));
        managers.getHistory().remove(id);
    }

    private void removeSubTask(Integer id) {
        repository.getSubtaskList().remove((SubTask) getSubTaskById(id));
        managers.getHistory().remove(id);
    }


    private void removeElementInEpicTask(Integer id) {
        EpicTask epicTask = (EpicTask) getEpicTaskById(id);
        epicTask.getSubtaskIds().forEach(ids -> {
            managers.getHistory().remove(ids);
            removeByID(ids);
        });
        managers.getHistory().remove(epicTask.getId());
        repository.getEpicTaskList().remove((EpicTask) getEpicTaskById(id));
    }


    @Override
    public void cleanRepository() {
        repository.getEpicTaskList().forEach(e -> managers.getHistory().remove(e.getId()));
        repository.getSubtaskList().forEach(s -> managers.getHistory().remove(s.getId()));
        repository.getTaskList().forEach(t -> managers.getHistory().remove(t.getId()));
        repository.getEpicTaskList().clear();
        repository.getSubtaskList().clear();
        repository.getTaskList().clear();

    }

    @Override
    public void addTask(Task task) {
        repository.addTaskList(task);
        managers.getHistory().add(task);
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        repository.addEpicTaskList(epicTask);
        managers.getHistory().add(epicTask);
    }

    @Override
    public void addSubTask(EpicTask epicTask, SubTask subtask) {
        checkStatusInFamilyEpic(epicTask, subtask);
        epicTask.addSubtask(subtask);
        repository.addSubtaskList(subtask);
        managers.getHistory().add(subtask);
    }

    private void checkStatusInFamilyEpic(EpicTask epicTask, SubTask subtask) {
        if (epicTask.getStatus() == Status.DONE) {
            epicTask.setStatus(Status.IN_PROGRESS);
            subtask.setStatus(Status.IN_PROGRESS);
            managers.getHistory().add(epicTask);
            managers.getHistory().add(subtask);
        } else if (epicTask.getStatus() == Status.IN_PROGRESS) {
            subtask.setStatus(Status.IN_PROGRESS);
            managers.getHistory().add(subtask);
        }
    }

    @Override
    public List<SubTask> getSubListOfEpic(EpicTask task) {
        if (task != null) {
            return repository.getSubtaskList()
                    .stream()
                    .filter(t -> task.getSubtaskIds().contains(t.getId()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public void printAllElement() {
        for (Task task : listElement()) {
            System.out.println(task);
        }
    }

    public void printHistoryElement() {
        for (Task task : managers.getHistory().getHistoryList()) {
            System.out.println(task);
        }
    }


}
