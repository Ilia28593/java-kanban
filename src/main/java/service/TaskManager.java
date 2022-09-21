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

public class TaskManager {

    private final Repository repository = new Repository();

    public Task getTaskById(int id) {
        Optional<Task> defaultTask = repository.getTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        Optional<EpicTask> epicTask = repository.getEpicTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        Optional<SubTask> subtask = repository.getSubtaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        if (defaultTask.isPresent()) {
            return defaultTask.get();
        } else if (epicTask.isPresent()) {
            return epicTask.get();
        } else if (subtask.isPresent()) {
            return subtask.get();
        } else {
            System.out.println("*** НЕТ");
        }
        return null;
    }

    public List<Task> listElement() {
        return new ArrayList<>() {{
            addAll(repository.getTaskList());
            addAll(repository.getEpicTaskList());
            addAll(repository.getSubtaskList());
        }};
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
        SubTask subTask = (SubTask) getTaskById(id);
        assert subTask != null;
        subTask.setStatus(status);
        int epicId = subTask.getEpicId();
        searchStatusDoneInChild((EpicTask) getTaskById(epicId));
    }


    public void changeEpicStatus(Integer id, Status status) {
        EpicTask epicTask = (EpicTask) getTaskById(id);
        if (epicTask != null) {
            if (status.equals(Status.IN_PROGRESS) && Objects.requireNonNull(epicTask).getStatus().equals(Status.NEW)) {
                epicTask.setStatus(status);
                if (epicTask.getSubtaskIds() != null) {
                    for (Integer idSubtask : epicTask.getSubtaskIds()) {
                        SubTask subtask = (SubTask) getTaskById(idSubtask);
                        assert subtask != null;
                        subtask.setStatus(Status.IN_PROGRESS);
                    }
                }
            } else if (status.equals(Status.NEW)) {
                epicTask.setStatus(status);
                if (epicTask.getSubtaskIds() != null) {
                    for (Integer idSubtask : epicTask.getSubtaskIds()) {
                        SubTask subtask = (SubTask) getTaskById(idSubtask);
                        assert subtask != null;
                        subtask.setStatus(Status.NEW);
                    }
                }
            }
        }
    }

    public void changeTaskStatus(Integer id, Status status) {
        Objects.requireNonNull(getTaskById(id)).setStatus(status);
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

    public void removeByID(List listRepository, Integer id) {
        listRepository.remove(getTaskById(id));
    }

    public void cleanRepository(List listRepository) {
        listRepository.clear();
    }

    public void addTask(Task task) {
        repository.addTaskList(task);
    }

    public void addEpicTask(EpicTask epicTask) {
        repository.addEpicTaskList(epicTask);
    }

    public void addSubTask(EpicTask epicTask, SubTask subtask) {
        epicTask.addSubtask(subtask);
        repository.addSubtaskList(subtask);
    }

    public List<SubTask> getSubListOfEpic(EpicTask task) {
        return repository.getSubtaskList()
                .stream()
                .filter(t -> task.getSubtaskIds().contains(t.getId()))
                .collect(Collectors.toList());
    }
}

