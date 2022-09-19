package service;

import model.*;
import repository.NewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service {

    static NewRepository newRepository = new NewRepository();

    private static TaskInter getTaskById(int id) {
        Optional<DefaultTask> defaultTask = NewRepository.getDefaultTaskList().stream()
                .filter(t -> t.getId() == id).findFirst();
        if (defaultTask.isPresent()) {
            return defaultTask.get();
        } else {
            Optional<EpicTask> epicTask = NewRepository.getEpicTaskList().stream()
                    .filter(t -> t.getId() == id).findFirst();
            if (epicTask.isPresent()) {
                return epicTask.get();
            } else {
                Optional<Subtask> subtask = NewRepository.getEpicTaskList().stream()
                        .map(EpicTask::getSubtask)
                        .flatMap(t -> t.stream()
                                .filter(ta -> ta.getId() == id))
                        .findFirst();
                if (subtask.isPresent()) {
                    return subtask.get();
                } else {
                    System.out.println("*** НЕТ");
                }
            }
        }
        return null;
    }

    public List<? extends TaskInter> getTaskTypes(String type) {
        List<? extends TaskInter> contain = null;
        switch (type) {
            case "DEFAULT": {
                contain = NewRepository.getDefaultTaskList();
                break;
            }
            case "EPIC": {
                contain = NewRepository.getEpicTaskList();
                break;
            }
            case "SUBTASK": {
                List<List<Subtask>> collect = NewRepository.getEpicTaskList().stream()
                        .map(EpicTask::getSubtask).collect(Collectors.toList());
                List<Subtask> subtaskList = new ArrayList<>();
                for (List<Subtask> subtasks : collect) {
                    subtaskList.addAll(subtasks);
                }
                contain = subtaskList;
                break;
            }
        }
        return contain;
    }

    public void updateDefaultTask(Integer id, String newName, String newDetails, Status newStatus) {
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

    public void updateEpicTask(Integer id, String newName, String newDetails, Status newStatus) {
        TaskInter taskFromId = getTaskById(id);
        if (taskFromId != null) {
            if (taskFromId instanceof EpicTask) {
                ((EpicTask) taskFromId)
                        .setNameTask(newName != null ? newName : ((EpicTask) taskFromId)
                                .getNameTask());
                ((EpicTask) taskFromId)
                        .setTaskDetail(newDetails != null ? newDetails : ((EpicTask) taskFromId)
                                .getTaskDetail());
                ((EpicTask) taskFromId)
                        .setStatus(newStatus != null ? newStatus : ((EpicTask) taskFromId)
                                .getStatus());
            }
        }
    }

    public void updateEpicTask(Integer id, String newName, String newDetails, Status newStatus, int perentId) {
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
            if (status.equals(Status.InPROGRESS) && ((EpicTask) taskFromId).getStatus().equals(Status.NEW)) {
                ((EpicTask) taskFromId).setStatus(status);
                if (!(((EpicTask) taskFromId).getSubtask() == null)) {
                    ((EpicTask) taskFromId).getSubtask().forEach(t -> t.setStatus(Status.InPROGRESS));
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

    public void searchStatusDoneInChild(TaskInter taskFromId) {
        if (taskFromId instanceof EpicTask) {
            if (((EpicTask) taskFromId).getSubtask().stream()
                    .map(Subtask::getStatus)
                    .allMatch(Predicate.isEqual(Status.DONE))) {
                ((EpicTask) taskFromId).setStatus(Status.DONE);
            }
        }
    }

    public void removeFromID(Integer id) {
        TaskInter taskFromId = getTaskById(id);
        if (taskFromId != null) {
            if (taskFromId instanceof DefaultTask) {
                NewRepository.getDefaultTaskList().remove(taskFromId);
            } else if (taskFromId instanceof EpicTask) {
                NewRepository.getEpicTaskList().remove(taskFromId);
            } else if (taskFromId instanceof Subtask) {
                NewRepository.getEpicTaskList().forEach(s -> s.getSubtask().remove(taskFromId));
            }
        }
    }
    public void addSubTask(EpicTask epicTask, Subtask subtask){
           epicTask.setSubtask(subtask);
    }


    public void setAllTasksInRepository(List<TaskInter> tasks) {
        if (!tasks.isEmpty()) {
            for (TaskInter task : tasks) {
                if (task instanceof DefaultTask) {
                    newRepository.setDefaultTaskList((DefaultTask) task);
                } else if (task instanceof EpicTask) {
                    newRepository.setEpicTaskList((EpicTask) task);
                }
            }
        }
    }
}

