package service.taskManager;

import model.*;
import repository.Repository;
import service.Exception.ManagerException;
import service.Managers;
import service.historyManager.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected Repository repository = new Repository();
    protected HistoryManager managerHistory = Managers.getHistory();

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
        managerHistory.add(subTask);
        return subTask;
    }

    @Override
    public List<Task> listElement() {
        return new ArrayList<>() {{
            addAll(repository.getTaskList());
            addAll(repository.getEpicTaskList());
            addAll(repository.getSubTaskList());
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
            checkStatusInSubTask(Status.IN_PROGRESS, (EpicTask) getEpicTaskById(id));
        } else if (status.equals(Status.NEW) && getEpicTaskById(id) != null) {
            checkStatusInSubTask(Status.NEW, (EpicTask) getEpicTaskById(id));
        }
    }

    private void checkStatusInSubTask(Status status, EpicTask epicTask) {
        epicTask.setStatus(status);
        managerHistory.add(epicTask);
        if (epicTask.getSubtaskIds() != null) {
            setNewStatusInSubTask(epicTask, status);
        }
    }

    private void setNewStatusInSubTask(EpicTask epicTask, Status status) {
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
        boolean checkAllSubtaskIsDone = repository.getSubTaskList().stream()
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
        } else if (!repository.getSubTaskList().isEmpty() && repository.getSubTaskMap().containsKey(id)) {
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
        repository.getEpicTaskMap().keySet().forEach(e -> managerHistory.remove(e));
        repository.getSubTaskMap().keySet().forEach(s -> managerHistory.remove(s));
        repository.getTaskMap().keySet().forEach(t -> managerHistory.remove(t));
        repository.getEpicTaskMap().clear();
        repository.getSubTaskMap().clear();
        repository.getTaskMap().clear();
        repository.getSortedTaskTree().clear();
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
            repository.addSorted(epicTask);
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
        return repository.getSubTaskList().stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .collect(Collectors.toList());
    }

    public List<Task> getListTask() {
        return repository.getTaskList();
    }

    @Override
    public List<EpicTask> getListEpicTask() {
        return repository.getEpicTaskList();
    }

    @Override
    public List<SubTask> getListSubTask() {
        return repository.getSubTaskList();
    }

    public void setEpicTaskTime(int id, LocalDateTime localDateTime, Long minutes) {
        if (checkRepositoryTime(id, localDateTime, minutes)) {
            EpicTask epicTask = (EpicTask) getEpicTaskById(id);
            epicTask.setStart(localDateTime);
            epicTask.setDurationMinutes(minutes);
            planedFinishEpicTask(id);
            addRepositoryTimeEpic(id);
            repository.addSorted(epicTask);
        }
    }

    public void setTaskTime(int id, LocalDateTime localDateTime, Long minutes) {
        if (checkRepositoryTime(id, localDateTime, minutes)) {
            Task task = getTaskById(id);
            task.setStart(localDateTime);
            task.setDurationMinutes(minutes);
            addRepositoryTimeTask(id);
            repository.addSorted(task);
        }
    }

    public void setSubTaskTime(int id, LocalDateTime localDateTime, Long minutes) {
        SubTask subTask = (SubTask) getSubTaskById(id);
        if (checkRepositoryTime(subTask.getEpicId(), localDateTime, minutes)) {
            subTask.setStart(localDateTime);
            subTask.setDurationMinutes(minutes);
            planedFinishEpicTask(subTask.getEpicId());
            addRepositoryTimeEpic(subTask.getEpicId());
            repository.addSorted(subTask);
        }
    }

    private boolean checkRepositoryTime(int id, LocalDateTime timeStart, Long minutes) {
        LocalDateTime timeInterval = timeStart;
        boolean checkAdd = true;
        boolean b = repository.getTimeCompletedTaskMap().containsKey(timeInterval);
        try {
            while (timeInterval.isBefore(timeStart.plusMinutes(minutes))) {
                if (b) {
                    if (repository.getTimeCompletedTaskMap().get(getTaskById(id).getStart()) != id) {
                        System.out.println("Данное время уже зарезервировано для выполнения другой задачи");
                        checkAdd = false;
                        break;
                    }
                } else {
                    timeInterval = timeInterval.plusMinutes(1);
                }
            }
        } catch (NullPointerException e) {
            throw new ManagerException("This time use in another Task");
        }
        return checkAdd;
    }

    private void addRepositoryTimeEpic(int id) {
        LocalDateTime timeInterval = getEpicTaskById(id).getStart();
        boolean b = repository.getTimeCompletedTaskMap().containsKey(timeInterval);
        while (timeInterval.isBefore(getEpicTaskById(id).getFinish())) {
            if (b) {
                if (repository.getTimeCompletedTaskMap().get(timeInterval) == id) {
                    repository.getTimeCompletedTaskMap().replace(timeInterval, id);
                } else {
                    repository.addTimeCompletedTaskMap(timeInterval, id);
                }
            } else {
                repository.addTimeCompletedTaskMap(timeInterval, id);
                timeInterval = timeInterval.plusMinutes(1);
            }
        }
    }

    private void addRepositoryTimeTask(int id) {
        LocalDateTime timeInterval = getTaskById(id).getStart();
        while (timeInterval.isBefore(getTaskById(id).getFinish())) {
            if (repository.getTimeCompletedTaskMap().get(getTaskById(id).getStart()) == id) {
                repository.getTimeCompletedTaskMap().replace(timeInterval, id);
            } else {
                repository.addTimeCompletedTaskMap(timeInterval, id);
            }
            timeInterval = timeInterval.plus(Duration.ofMinutes(1));
        }
    }

    private void planedFinishEpicTask(int id) {
        EpicTask epicTask = (EpicTask) getEpicTaskById(id);
        epicTask.setFinish(epicTask.getStart().plus(epicTask.getDurationMinutes()));
        epicTask.getSubtaskIds().forEach(s ->
                epicTask.setFinish(epicTask.getFinish().plus(getSubTaskById(s).getDurationMinutes())));
    }

    public void printAllElement() {
        listElement().forEach(System.out::println);
    }

    public void printHistoryElement() {
        managerHistory.getHistoryList().forEach(System.out::println);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(repository.getSortedTaskTree());
    }
}