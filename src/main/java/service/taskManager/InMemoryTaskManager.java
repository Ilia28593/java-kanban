package service.taskManager;

import lombok.NoArgsConstructor;
import model.*;
import repository.Repository;
import service.exception.IncorrectIdException;
import service.exception.TimeIntervalIsUsedException;
import service.Managers;
import service.historyManager.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@NoArgsConstructor
public class InMemoryTaskManager implements TaskManager {

    protected Repository repository = new Repository();
    protected HistoryManager managerHistory = Managers.getHistory();

    @Override
    public Task getTaskById(int id) {
        if (repository.getTaskMap().containsKey(id)) {
            managerHistory.add(repository.getTaskMap().get(id));
            return repository.getTaskMap().get(id);
        } else {
            throw new IncorrectIdException("Task from this id no search");
        }
    }

    @Override
    public Task getEpicTaskById(int id) {
        if (repository.getEpicTaskMap().containsKey(id)) {
            managerHistory.add(repository.getEpicTaskMap().get(id));
            return repository.getEpicTaskMap().get(id);
        } else {
            throw new IncorrectIdException("Task from this id no search");
        }
    }

    @Override
    public Task getSubTaskById(int id) {
        if (repository.getSubTaskMap().containsKey(id)) {
            managerHistory.add(repository.getSubTaskMap().get(id));
            return repository.getSubTaskMap().get(id);
        } else {
            throw new IncorrectIdException("Task from this id no search");
        }
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
        task.setStart(newTask.getStart());
        task.setDurationMinutes(newTask.getDurationMinutes());
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
        boolean checkAllSubtaskIsDone = repository.getSubTaskMap().values().stream()
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
        if (!repository.getTaskMap().values().isEmpty() && repository.getTaskMap().containsKey(id)) {
            removeTask(id);
        } else if (!repository.getSubTaskMap().values().isEmpty() && repository.getSubTaskMap().containsKey(id)) {
            removeSubTask(id);
        } else if (!repository.getEpicTaskMap().values().isEmpty() && repository.getEpicTaskMap().containsKey(id)) {
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
        repository.getTaskMap().keySet().forEach(t -> {
            managerHistory.remove(t);
        });
        new Task(0);
        repository.cleanRepository();

    }

    @Override
    public void addTask(Task task) {
        if (checkFreeTimeInTask(task.getStart(), task.getDurationMinutes().toMinutes(), task.getId())) {
            if (!repository.getTaskMap().containsKey(task.getId())) {
                task.setType(Type.TASK);
                repository.addTaskMap(task);
                setTaskTime(task.getId(), task.getStart(), task.getDurationMinutes().toMinutes());
            } else {
                addTask(new Task(task.getNameTask(), task.getTaskDetail(), task.getStatus()));
            }
        }
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
//        if (checkFreeTimeInTask(epicTask.getStart(), epicTask.getDurationMinutes().toMinutes(), epicTask.getId())) {
            if (!repository.getEpicTaskMap().containsKey(epicTask.getId())) {
                epicTask.setType(Type.EPIC);
                repository.addEpicTaskMap(epicTask);
//                repository.addSorted(epicTask);
//                setEpicTaskTime(epicTask.getId(), epicTask.getStart(), epicTask.getDurationMinutes().toMinutes());
            } else {
                addEpicTask(new EpicTask(epicTask.getNameTask(), epicTask.getTaskDetail(), epicTask.getStatus()));
            }
  //      }
    }

    @Override
    public void addSubTask(int id, SubTask subtask) {
        if (checkFreeTimeInTask(subtask.getStart(), subtask.getDurationMinutes().toMinutes(), subtask.getId())) {
            if (!repository.getSubTaskMap().containsKey(subtask.getId())) {
                checkStatusInFamilyEpic(id, subtask);
                repository.getEpicTaskMap().get(id).addSubtask(subtask);
                subtask.setType(Type.SUBTASK);
                repository.addSubtaskMap(subtask);
                setSubTaskTime(subtask.getId(), subtask.getStart(), subtask.getDurationMinutes().toMinutes());
            } else {
                addSubTask(id, new SubTask(subtask.getNameTask(), subtask.getTaskDetail(), subtask.getStatus()));
            }
        }
    }

    private void checkStatusInFamilyEpic(int epicTaskId, SubTask subtask) {
        EpicTask epicTask1 = repository.getEpicTaskMap().get(epicTaskId);
        if (epicTask1.getStatus().equals(Status.DONE)) {
            epicTask1.setStatus(Status.IN_PROGRESS);
            subtask.setStatus(Status.IN_PROGRESS);
            managerHistory.add(epicTask1);
            managerHistory.add(subtask);
        } else if (epicTask1.getStatus().equals(Status.IN_PROGRESS)) {
            subtask.setStatus(Status.IN_PROGRESS);
            managerHistory.add(subtask);
        }
    }

    public List<Task> getListTask() {
        return new ArrayList<>(repository.getTaskMap().values());
    }

    @Override
    public List<EpicTask> getListEpicTask() {
        return new ArrayList<>(repository.getEpicTaskMap().values());
    }

    @Override
    public List<SubTask> getListSubTask() {
        return new ArrayList<>(repository.getSubTaskMap().values());
    }

    public void setEpicTaskTime(int id, LocalDateTime localDateTime, Long minutes) {
        if (checkFreeTimeInTask(localDateTime, minutes, id)) {
            getEpicTaskById(id).setStart(localDateTime);
            planedFinishEpicTask(id);
            repository.addSorted(getEpicTaskById(id));
        }
    }

    public void setTaskTime(int id, LocalDateTime localDateTime, Long minutes) {
        if (checkFreeTimeInTask(localDateTime, minutes, id)) {
            repository.getTaskMap().get(id).setStart(localDateTime);
            repository.getTaskMap().get(id).setDurationMinutes(Duration.ofMinutes(minutes));
            repository.getTaskMap().get(id).setFinish(localDateTime.plusMinutes(minutes));
            repository.addSorted(repository.getTaskMap().get(id));
        }
    }

    public void setSubTaskTime(int id, LocalDateTime localDateTime, Long minutes) {
        SubTask subTask = repository.getSubTaskMap().get(id);
        planedFinishEpicTask(subTask.getEpicId());
        if (checkFreeTimeInTask(localDateTime, minutes, id)) {
            subTask.setStart(localDateTime);
            subTask.setDurationMinutes(Duration.ofMinutes(minutes));
            repository.getSubTaskMap().get(id).setFinish(localDateTime.plusMinutes(minutes));
            repository.addSorted(subTask);
        } else {
            throw new TimeIntervalIsUsedException("This time is used another Task");
        }
    }

    private void planedFinishEpicTask(int id) {
        EpicTask epicTask = repository.getEpicTaskMap().get(id);
        LocalDateTime finish = epicTask.getStart().plusMinutes(epicTask.getDurationMinutes().toMinutes());
        epicTask.getSubtaskIds().forEach(s -> finish.plusMinutes(repository.getSubTaskMap()
                .get(s).getDurationMinutes().toMinutes()));
        epicTask.setFinish(finish);
    }

    public boolean checkFreeTimeInTask(LocalDateTime localDateTime, Long duration, int taskId) {
        LocalDateTime start = localDateTime;
        LocalDateTime finish = start.plusMinutes(duration);
        boolean flag = true;
        if (!start.equals(LocalDateTime.of(1993, 11, 2, 11, 30))) {
            while (start.isBefore(finish)) {
                LocalDateTime finalCorrectStart = transformToCorrectTime(start);
                if (repository.getCheckFreeTime().keySet().parallelStream().anyMatch(s -> s.equals(finalCorrectStart))) {
                    if (repository.getCheckFreeTime().get(transformToCorrectTime(start)) == taskId) {
                        start = start.plusMinutes(15L);
                    } else {
                        flag = false;
                        break;
                    }
                } else {
                    repository.getCheckFreeTime().put(transformToCorrectTime(start), taskId);
                    start = start.plusMinutes(15L);
                }
            }
        }
        return flag;
    }

    private LocalDateTime transformToCorrectTime(LocalDateTime minute) {
        LocalTime timeOfNew = LocalTime.of(minute.toLocalTime().getHour(), timeInterval15Min(minute.toLocalTime().getMinute()));
        return LocalDateTime.of(minute.toLocalDate(), timeOfNew);
    }

    private int timeInterval15Min(int minute) {
        if (minute < 15) {
            return 10;
        } else if (minute < 30) {
            return 20;
        } else if (minute < 45) {
            return 30;
        } else {
            return 40;
        }
    }

    public void printAllElement() {
        repository.listElement().forEach(System.out::println);
    }

    public void printHistoryElement() {
        managerHistory.getHistoryList().forEach(System.out::println);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(repository.getSortedTaskTree());
    }
}