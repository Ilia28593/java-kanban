package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TaskManager {

    private final Repository repository = new Repository();

    private Task getTaskById(int id) {
        Optional<Task> defaultTask = repository.getDefaultTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        return defaultTask.orElse(null);
    }

    private SubTask getSubTaskById(int id) {
        Optional<SubTask> subtask = repository.getSubtaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        return subtask.orElse(null);
    }

    private EpicTask getEpicTaskById(int id) {
        Optional<EpicTask> epicTask = repository.getEpicTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        return epicTask.orElse(null);
    }

    public List<Task> getTaskTypes() {
        return repository.getDefaultTaskList();
    }

    public List<EpicTask> getEpicTaskTypes() {
        return repository.getEpicTaskList();
    }

    public List<SubTask> getSubTaskTypes() {
        return repository.getSubtaskList();
    }

    public void updateTask(Task task, Task newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
    }

    public void updateEpicTask(EpicTask task, EpicTask newTask) {
        task.setNameTask(newTask.getNameTask() != null ? newTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newTask.getTaskDetail() != null ? newTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newTask.getStatus() != null ? newTask.getStatus() : task.getStatus());
    }

    public void updateSubtaskTask(SubTask task, SubTask newSubTask) {
        task.setNameTask(newSubTask.getNameTask() != null ? newSubTask.getNameTask() : task.getNameTask());
        task.setTaskDetail(newSubTask.getTaskDetail() != null ? newSubTask.getTaskDetail() : task.getTaskDetail());
        task.setStatus(newSubTask.getStatus() != null ? newSubTask.getStatus() : task.getStatus());
        task.setEpicId(newSubTask.getEpicId() != 0 ? newSubTask.getEpicId() : task.getEpicId());
    }

    public void changeSubtaskStatus(Integer id, Status status) {
        SubTask subTask = (SubTask) getSubTaskById(id);
        subTask.setStatus(status);
        int epicId = subTask.getEpicId();
        searchStatusDoneInChild(getEpicTaskById(epicId));
    }


    public void changeEpicStatus(Integer id, Status status) {
        EpicTask epicTask = getEpicTaskById(id);
        if (status.equals(Status.IN_PROGRESS) && epicTask.getStatus().equals(Status.NEW)) {
            epicTask.setStatus(status);
            if (epicTask.getSubtaskIds() != null) {
                for (Integer idSubtask : epicTask.getSubtaskIds()) {
                    SubTask subtask = (SubTask) getSubTaskById(idSubtask);
                    subtask.setStatus(Status.IN_PROGRESS);
                }
            }
        } else if (status.equals(Status.NEW)) {
            epicTask.setStatus(status);
            if (epicTask.getSubtaskIds() != null) {
                for (Integer idSubtask : epicTask.getSubtaskIds()) {
                    SubTask subtask = (SubTask) getTaskById(idSubtask);
                    subtask.setStatus(Status.NEW);
                }
            }
        }
    }

    public void changeTaskStatus(Integer id, Status status) {
        Task task = getTaskById(id);
        task.setStatus(status);
    }

    public void searchStatusDoneInChild(EpicTask task) {
        boolean checkAllSubtaskIsDone = repository.getSubtaskList().stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .map(SubTask::getStatus)
                .allMatch(Predicate.isEqual(Status.DONE));

        if (checkAllSubtaskIsDone) {
            task.setStatus(Status.DONE);
        }
    }

    public void removeTaskID(Integer id) {
        Task taskFromId = getTaskById(id);
        repository.getDefaultTaskList().remove(taskFromId);
    }

    public void removeSubTaskID(Integer id) {
        SubTask taskFromId = getSubTaskById(id);
        repository.getSubtaskList().remove(taskFromId);
    }

    public void removeEpicID(Integer id) {
        EpicTask taskFromId = getEpicTaskById(id);
        repository.getEpicTaskList().remove(taskFromId);
    }

    public void addTask(Task task) {
        repository.addDefaultTaskList(task);
    }

    public void addEpicTask(EpicTask epicTask) {
        repository.addEpicTaskList(epicTask);
    }

    public void addSubTask(EpicTask epicTask, SubTask subtask) {
        epicTask.addSubtask(subtask);
        repository.addSubtaskList(subtask);
    }
}

