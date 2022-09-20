import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import repository.Repository;
import service.TaskManager;

public class Manager {
    private final TaskManager taskManager = new TaskManager();
    private final Repository repository = new Repository();


    public void printAllElement() {
        if (repository.getDefaultTaskList() != null) {
            for (Task task : repository.getDefaultTaskList()) {
                System.out.println(task);
            }
        }
        if (repository.getEpicTaskList() != null) {
            for (EpicTask epicTasks : repository.getEpicTaskList()) {
                System.out.println(epicTasks);
            }
        }
        if (repository.getSubtaskList() != null) {
            for (SubTask epicTasks : repository.getSubtaskList()) {
                System.out.println(epicTasks);
            }
        }
    }

    public void addEpicTask(EpicTask tasks) {
        taskManager.addEpicTask(tasks);
    }

    public void addSubTask(EpicTask epicTask, SubTask tasks) {
        taskManager.addSubTask(epicTask, tasks);
    }

    public void addTask(Task tasks) {
        taskManager.addTask(tasks);
    }

    public void changeTaskStatus(int id, Status status) {
        taskManager.changeTaskStatus(id, status);
    }

    public void changeEpicStatus(int id, Status status) {
        if (status.equals(Status.IN_PROGRESS) || status.equals(Status.NEW)) {
            taskManager.changeEpicStatus(id, status);
        } else {
            System.out.println("Статус поменяется на Done автоматически, после выполнения всех SubTask ");
        }
    }

    public void changeSubTaskStatus(int id, Status status) {
        taskManager.changeSubtaskStatus(id, status);
    }

    public void removeTask(int id) {
        taskManager.removeTaskID(id);
    }

    public void removeEpic(int id) {
        taskManager.removeEpicID(id);
    }

    public void removeSubTask(int id) {
        taskManager.removeSubTaskID(id);
    }
}


