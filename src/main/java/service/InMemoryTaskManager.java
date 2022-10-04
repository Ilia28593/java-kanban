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

    public Repository repository = new Repository();

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
    public void updateTask(Task task, Task newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
    }

    @Override
    public void updateEpicTask(EpicTask task, EpicTask newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
    }

    @Override
    public void updateSubtaskTask(SubTask task, SubTask newSubTask) {
        task.setNameTask(newSubTask.getNameTask() != null ? newSubTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newSubTask.getTaskDetail() != null ? newSubTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newSubTask.getStatus() != null ? newSubTask.getStatus() : task.getStatus());
        task.setEpicId(newSubTask.getEpicId() != 0 ? newSubTask.getEpicId() : task.getEpicId());
    }

    @Override
    public void changeSubtaskStatus(Integer id, Status status) {
        SubTask subTask = (SubTask) getSubTaskById(id);
        if (subTask != null) {
            subTask.setStatus(status);
            int epicId = subTask.getEpicId();
            searchStatusDoneInChild((EpicTask) getEpicTaskById(epicId));
        }
    }

    @Override
    public void changeEpicStatus(Integer id, Status status) {
        EpicTask epicTask = (EpicTask) getEpicTaskById(id);
        if (epicTask != null) {
            if (status.equals(Status.IN_PROGRESS) && Objects.requireNonNull(epicTask).getStatus().equals(Status.NEW)) {
                epicTask.setStatus(status);
                if (epicTask.getSubtaskIds() != null) {
                    for (Integer idSubtask : epicTask.getSubtaskIds()) {
                        SubTask subtask = (SubTask) getSubTaskById(idSubtask);
                        assert subtask != null;
                        subtask.setStatus(Status.IN_PROGRESS);
                    }
                }
            } else if (status.equals(Status.NEW)) {
                epicTask.setStatus(status);
                if (epicTask.getSubtaskIds() != null) {
                    for (Integer idSubtask : epicTask.getSubtaskIds()) {
                        SubTask subtask = (SubTask) getSubTaskById(idSubtask);
                        assert subtask != null;
                        subtask.setStatus(Status.NEW);
                    }
                }
            }
        }
    }

    @Override
    public void changeTaskStatus(Integer id, Status status) {
        Objects.requireNonNull(getTaskById(id)).setStatus(status);
    }

    @Override
    public void searchStatusDoneInChild(EpicTask task) {
        boolean checkAllSubtaskIsDone = repository.getSubtaskList().stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .map(SubTask::getStatus)
                .allMatch(Predicate.isEqual(Status.DONE));

        if (checkAllSubtaskIsDone) {
            task.setStatus(Status.DONE);
        }
    }

    @Override
    public void removeByID(Integer id) {
        if (!repository.getTaskList().isEmpty() && repository.getTaskList().stream().anyMatch(t -> t.getId() == id)) {
           repository.getTaskList().remove(getTaskById(id));
        } else if (!repository.getSubtaskList().isEmpty() && repository.getSubtaskList().stream().anyMatch(t -> t.getId() == id)) {
            repository.getSubtaskList().remove((SubTask) getSubTaskById(id));
        } else if (!repository.getEpicTaskList().isEmpty() && repository.getEpicTaskList().stream().anyMatch(t -> t.getId() == id)) {
            repository.getEpicTaskList().remove((EpicTask) getEpicTaskById(id));
        }
    }

    @Override
    public void cleanRepository() {
        repository.getEpicTaskList().clear();
        repository.getSubtaskList().clear();
        repository.getTaskList().clear();
    }

    @Override
    public void addTask(Task task) {
        repository.addTaskList(task);
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        repository.addEpicTaskList(epicTask);
    }

    @Override
    public void addSubTask(EpicTask epicTask, SubTask subtask) {
        if(epicTask.getStatus() == Status.DONE){
            epicTask.setStatus(Status.IN_PROGRESS);
            subtask.setStatus(Status.IN_PROGRESS);
        } else if(epicTask.getStatus() == Status.IN_PROGRESS){
            subtask.setStatus(Status.IN_PROGRESS);
        }
        epicTask.addSubtask(subtask);
        repository.addSubtaskList(subtask);
    }

    @Override
    public List<SubTask> getSubListOfEpic(EpicTask task) {
        if(task!=null) {
            return repository.getSubtaskList()
                    .stream()
                    .filter(t -> task.getSubtaskIds().contains(t.getId()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
