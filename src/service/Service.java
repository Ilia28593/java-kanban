package service;

import entity.Status;
import entity.Task;
import entity.Type;
import repository.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service {

    Repository repository = new Repository();

    public List<Task> getTaskTypes(Type type) {
        return repository.getTasks().stream().filter(t -> t.getType().equals(type)).collect(Collectors.toList());
    }

    public Task getTaskFromId(Integer id) {
        Optional<Task> task = repository.getTasks().stream()
                .filter(i -> i.getId().equals(id)).findFirst();
        if (task.isEmpty()) {
            List<Task> tasks = repository.getTasks().stream()
                    .filter(i -> i.getType().equals(Type.EPIC))
                    .collect(Collectors.toList());
            for (Task task1 : tasks) {
                Optional<Task> childTask = task1.getTasksPart().stream()
                        .filter(i -> i.getId().equals(id)).findFirst();
                if (childTask.isPresent()) {
                    task = childTask;
                }
            }
        }

        if (task.isPresent()) {
            return task.get();
        } else {
            System.out.printf("Объект по %s id не найден.%n", id);
            return null;
        }
    }

    public void updateTask(Integer id, Task task) {
        Task taskFromId = getTaskFromId(id);
        if (taskFromId != null) {
            taskFromId.setNameTask(task.getNameTask() != null ? task.getNameTask() : taskFromId.getNameTask());
            taskFromId.setTaskDetail(task.getTaskDetail() != null ? task.getTaskDetail() : taskFromId.getTaskDetail());
            taskFromId.setType(task.getType() != null ? task.getType() : taskFromId.getType());
            taskFromId.setStatus(task.getStatus() != null ? task.getStatus() : taskFromId.getStatus());
        }
    }

    public void changeStatus(Integer id, Status status) {
        Task taskFromId = getTaskFromId(id);
        if (taskFromId.getType().equals(Type.EPIC)) {
            if (status.equals(Status.IN_PROGRESS) && taskFromId.getStatus().equals(Status.NEW)) {
                taskFromId.setStatus(status);
                if (!(taskFromId.getTasksPart() == null)) {
                    taskFromId.getTasksPart().forEach(t -> t.setStatus(Status.IN_PROGRESS));
                }
            }
        } else {
            taskFromId.setStatus(status);
        }
        if (!(taskFromId.getParentId() == 0)) {
            searchStatusDoneInChild(getTaskFromId(taskFromId.getParentId()));
        }
    }

    public void searchStatusDoneInChild(Task taskFromId) {
        if (taskFromId.getTasksPart().stream()
                .map(Task::getStatus)
                .allMatch(Predicate.isEqual(Status.DONE))) {
            taskFromId.setStatus(Status.DONE);
        }
    }


    public void removeFromID(Integer id) {
        Task task = getTaskFromId(id);
        if (task != null) {
            if (!repository.getTasks().remove(task)) {
                repository.getTasks().stream()
                        .filter(t -> Objects.equals(t.getType(), Type.EPIC))
                        .forEach(t -> t.getTasksPart().remove(task));
            }
        }

    }

    public List<Task> epicSubclass(Task epicTask) {
        if (epicTask.getType().equals(Type.EPIC)) {
            return epicTask.getTasksPart();
        } else {
            System.out.println("Переданный объект не имеет статус Epic");
            return null;
        }
    }

    public void setAllTasksInRepository(List<Task> tasks) {
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                switch (task.getType()) {
                    case EPIC:
                    case DEFAULT: {
                        repository.setTask(task);
                        break;
                    }
                    case SUBTASK: {
                        Task task1 = repository.getTasks().stream()
                                .filter(t -> t.getId().equals(task.getParentId()))
                                .findFirst().orElseThrow();
                        task1.setTasksPart(task);
                        break;
                    }

                }
            }
        }
    }
}

