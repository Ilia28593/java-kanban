package service;

import model.*;
import repository.NewRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TaskManager {

    static NewRepository newRepository = new NewRepository();

    private TaskInter getTaskById(int id) {
        Optional<DefaultTask> defaultTask = newRepository.getDefaultTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        Optional<EpicTask> epicTask = newRepository.getEpicTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        Optional<Subtask> subtask = newRepository.getSubtaskList().stream()
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

    public List<? extends TaskInter> getTaskTypes(String type) {
        List<? extends TaskInter> contain = null;
        switch (type) {
            case "DEFAULT": {
                contain = newRepository.getDefaultTaskList();
                break;
            }
            case "EPIC": {
                contain = newRepository.getEpicTaskList();
                break;
            }
            case "SUBTASK": {
                contain = newRepository.getSubtaskList();
                break;
            }
        }
        return contain;
    }

    public void updateEpicDefaultTask(Integer id, String newName, String newDetails, Status newStatus) {
        TaskInter taskFromId = getTaskById(id);
        if (taskFromId != null) {
            if (taskFromId instanceof DefaultTask) {
                ((DefaultTask) taskFromId)
                        .setNameTask(newName != null ? newName : ((DefaultTask) taskFromId)
                                .getNameTask());
                ((DefaultTask) taskFromId)
                        .setTaskDetail(newDetails != null ? newDetails : ((DefaultTask) taskFromId)
                                .getTaskDetail());
                ((DefaultTask) taskFromId)
                        .setStatus(newStatus != null ? newStatus : ((DefaultTask) taskFromId)
                                .getStatus());
            }
        }
    }

    public void updateSubtaskTask(Integer id, String newName, String newDetails, Status newStatus, int perentId) {
        TaskInter taskFromId = getTaskById(id);
        if (taskFromId != null) {
            if (taskFromId instanceof Subtask) {
                ((Subtask) taskFromId)
                        .setNameTask(newName != null ? newName : ((Subtask) taskFromId)
                                .getNameTask());
                ((Subtask) taskFromId)
                        .setTaskDetail(newDetails != null ? newDetails : ((Subtask) taskFromId)
                                .getTaskDetail());
                ((Subtask) taskFromId)
                        .setStatus(newStatus != null ? newStatus : ((Subtask) taskFromId)
                                .getStatus());
                ((Subtask) taskFromId)
                        .setEpicId(perentId != 0 ? perentId : ((Subtask) taskFromId).getEpicId());
            }
        }
    }

    public void changeStatus(Integer id, Status status) {
        TaskInter taskFromId = getTaskById(id);
        if (taskFromId instanceof EpicTask) {
            if (status.equals(Status.IN_PROGRESS) && ((EpicTask) taskFromId).getStatus().equals(Status.NEW)) {
                ((EpicTask) taskFromId).setStatus(status);
                if (!(((EpicTask) taskFromId).getIdSubtasks() == null)) {
                    for (Integer idSubtask : ((EpicTask) taskFromId).getIdSubtasks()) {
                        Subtask subtask = (Subtask) getTaskById(idSubtask);
                        assert subtask != null;
                        subtask.setStatus(Status.IN_PROGRESS);
                    }
                }
            }
        } else if (taskFromId instanceof DefaultTask) {
            ((DefaultTask) taskFromId).setStatus(status);
        } else if (taskFromId instanceof Subtask) {
            ((Subtask) taskFromId).setStatus(status);
            int epicId = ((Subtask) taskFromId).getEpicId();
            searchStatusDoneInChild(getTaskById(epicId));
        }
    }

    public void searchStatusDoneInChild(TaskInter task) {
        if (task instanceof EpicTask) {
            EpicTask epicTask = (EpicTask) task;

            boolean checkAllSubtaskIsDone = newRepository.getSubtaskList().stream()
                    .filter(t -> epicTask.getIdSubtasks().contains(t.getId()))
                    .map(Subtask::getStatus)
                    .allMatch(Predicate.isEqual(Status.DONE));

            if (checkAllSubtaskIsDone) {
                epicTask.setStatus(Status.DONE);
            }
        }
    }

    public void removeFromID(Integer id) {
        TaskInter taskFromId = getTaskById(id);
        if (taskFromId != null) {
            if (taskFromId instanceof DefaultTask) {
                newRepository.getDefaultTaskList().remove(taskFromId);
            } else if (taskFromId instanceof EpicTask) {
                newRepository.getEpicTaskList().remove(taskFromId);
            } else if (taskFromId instanceof Subtask) {
                newRepository.getSubtaskList().remove(taskFromId);
            }
        }
    }


    public void setAllTasksInRepository(TaskInter task) {
        if (task != null) {
            if (task instanceof DefaultTask) {
                newRepository.setDefaultTaskList((DefaultTask) task);
            } else if (task instanceof EpicTask) {
                newRepository.setEpicTaskList((EpicTask) task);
            }
        }
    }

    public void addSubTask(EpicTask epicTask, Subtask subtask) {
        epicTask.setIdSubtasks(subtask);
        newRepository.setSubtaskList(subtask);
    }
}

