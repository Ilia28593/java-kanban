package service.taskManager;

import model.*;
import repository.Repository;
import service.historyManager.HistoryManager;
import service.historyManager.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected Repository repository = new Repository();
    protected HistoryManager managerHistory = new InMemoryHistoryManager();

    public InMemoryTaskManager() {
    }

    @Override
    public Task getTaskById(int id) {
        Task task = null;
        if (repository.getTaskMap().containsKey(id)) {
            task = repository.getTaskMap().get(id);
        }
        managerHistory.add(task);
        return task;
    }

    @Override
    public Task getEpicTaskById(int id) {
        EpicTask epicTask = null;
        if (repository.getEpicTaskMap().containsKey(id)) {
            epicTask = repository.getEpicTaskMap().get(id);
            managerHistory.add(epicTask);
        }
        return epicTask;
    }

    @Override
    public Task getSubTaskById(int id) {
        SubTask subTask = null;
        if (repository.getSubTaskMap().containsKey(id)) {
            subTask = repository.getSubTaskMap().get(id);
        }
        return subTask;
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
        updateAndCheckParts(task, newTask);
        managerHistory.add(task);
        return task;
    }

    @Override
    public Task updateEpicTask(EpicTask task, EpicTask newTask) {
        updateAndCheckParts(task, newTask);
        managerHistory.add(task);
        return task;
    }

    @Override
    public Task updateSubtaskTask(SubTask task, SubTask newSubTask) {
        updateAndCheckParts(task, newSubTask);
        task.setEpicId(newSubTask.getEpicId() != 0 ? newSubTask.getEpicId() : task.getEpicId());
        managerHistory.add(task);
        return task;
    }

    private void updateAndCheckParts(Task task, Task newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
    }

    @Override
    public void changeSubtaskStatus(int id, Status status) {
        if (getSubTaskById(id) != null) {
            getSubTaskById(id).setStatus(status);
            searchStatusDoneInChild(repository.getEpicTaskMap().get(((SubTask) getSubTaskById(id)).getEpicId()));
            managerHistory.add(getSubTaskById(id));
        }
    }

    @Override
    public void changeEpicStatus(Integer id, Status status) {
        if (status.equals(Status.IN_PROGRESS) && getEpicTaskById(id).getStatus().equals(Status.NEW) &&
                getEpicTaskById(id) != null) {
            CheckStatusInSubTask(Status.IN_PROGRESS, (EpicTask) getEpicTaskById(id));
        } else if (status.equals(Status.NEW) && getEpicTaskById(id) != null) {
            CheckStatusInSubTask(Status.NEW, (EpicTask) getEpicTaskById(id));
        }
    }

    private void CheckStatusInSubTask(Status status, EpicTask epicTask) {
        epicTask.setStatus(status);
        managerHistory.add(epicTask);
        if (epicTask.getSubtaskIds() != null) {
            SetNewStatusInSubTask(epicTask, status);
        }
    }

    private void SetNewStatusInSubTask(EpicTask epicTask, Status status) {
        epicTask.getSubtaskIds().forEach(id -> {
            getSubTaskById(id).setStatus(status);
            managerHistory.add(getSubTaskById(id));
        });
    }

    @Override
    public void changeTaskStatus(Integer id, Status status) {
        if (getSubTaskById(id) != null) {
            getTaskById(id).setStatus(status);
            managerHistory.add(getTaskById(id));
        }
    }

    public void searchStatusDoneInChild(EpicTask task) {
        boolean checkAllSubtaskIsDone = repository.getSubtaskList().stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .map(SubTask::getStatus)
                .allMatch(Predicate.isEqual(Status.DONE));
        if (checkAllSubtaskIsDone) {
            task.setStatus(Status.DONE);
            managerHistory.add(task);
        }
    }

    @Override
    public void removeByID(Integer id) {
        if (!repository.getTaskList().isEmpty() && repository.getTaskMap().containsKey(id)) {
            removeTask(id);
        } else if (!repository.getSubtaskList().isEmpty() && repository.getSubTaskMap().containsKey(id)) {
            removeSubTask(id);
        } else if (!repository.getEpicTaskList().isEmpty() && repository.getEpicTaskMap().containsKey(id)) {
            removeElementInEpicTask(id);
        }
    }

    private void removeTask(Integer id) {
        repository.getTaskMap().remove(id);
        managerHistory.remove(id);
    }

    private void removeSubTask(Integer id) {
        repository.getSubTaskMap().remove(id);
        managerHistory.remove(id);
    }


    private void removeElementInEpicTask(Integer id) {
        ((EpicTask) getEpicTaskById(id)).getSubtaskIds().forEach(ids -> {
            managerHistory.remove(ids);
            removeSubTask(ids);
        });
        managerHistory.remove(id);
        repository.getEpicTaskMap().remove(id);
    }


    @Override
    public void cleanRepository() {
        repository.getTaskMap().keySet().forEach(e -> managerHistory.remove(e));
        repository.getSubTaskMap().keySet().forEach(s -> managerHistory.remove(s));
        repository.getTaskMap().keySet().forEach(t -> managerHistory.remove(t));
        repository.getEpicTaskList().clear();
        repository.getSubtaskList().clear();
        repository.getTaskList().clear();

    }

    @Override
    public void addTask(Task task) {
        if (!repository.getTaskMap().containsKey(task.getId())) {
            task.setType(Type.TASK);
            repository.addTaskMap(task);
        } else {
            addTask(new Task(task.getNameTask(), task.getTaskDetail(), task.getStatus()));
        }
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        if (!repository.getEpicTaskMap().containsKey(epicTask.getId())) {
            epicTask.setType(Type.EPIC);
            repository.addEpicTaskMap(epicTask);
        } else {
            addEpicTask(new EpicTask(epicTask.getNameTask(), epicTask.getTaskDetail(), epicTask.getStatus()));
        }
    }

    @Override
    public void addSubTask(EpicTask epicTask, SubTask subtask) {
        if (!repository.getSubTaskMap().containsKey(subtask.getId())) {
            checkStatusInFamilyEpic(epicTask, subtask);
            epicTask.addSubtask(subtask);
            subtask.setType(Type.SUBTASK);
            repository.addSubtaskMap(subtask);
        } else {
            addSubTask(epicTask, new SubTask(subtask.getNameTask(), subtask.getTaskDetail(), subtask.getStatus()));
        }
    }

    private void checkStatusInFamilyEpic(EpicTask epicTask, SubTask subtask) {
        if (epicTask.getStatus().equals(Status.DONE)) {
            epicTask.setStatus(Status.IN_PROGRESS);
            subtask.setStatus(Status.IN_PROGRESS);
            managerHistory.add(epicTask);
            managerHistory.add(subtask);
        } else if (epicTask.getStatus().equals(Status.IN_PROGRESS)) {
            subtask.setStatus(Status.IN_PROGRESS);
            managerHistory.add(subtask);
        }
    }

    @Override
    public List<SubTask> getSubListOfEpic(EpicTask task) {
        return repository.getSubtaskList().stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .collect(Collectors.toList());
    }

    public void printAllElement() {
        listElement().forEach(System.out::println);
    }

    public void printHistoryElement() {
        managerHistory.getHistoryList().forEach(System.out::println);
    }
}