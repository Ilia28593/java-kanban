import model.*;
import repository.NewRepository;
import service.TaskManager;

public class Manager {
    TaskManager taskManager = new TaskManager();
    NewRepository repository = new NewRepository();


    public void printAllElement() {
        if (repository.getDefaultTaskList() != null) {
            for (DefaultTask task : repository.getDefaultTaskList()) {
                System.out.println(task);
            }
        }
        if (repository.getEpicTaskList() != null) {
            for (EpicTask epicTasks : repository.getEpicTaskList()) {
                System.out.println(epicTasks);
            }
        }
        if (repository.getSubtaskList() != null) {
            for (Subtask epicTasks : repository.getSubtaskList()) {
                System.out.println(epicTasks);
            }
        }
    }

    public void addEpicDefaultTask(TaskInter tasks) {
        taskManager.setAllTasksInRepository(tasks);
    }

    public void addSubTask(EpicTask epicTask, Subtask subtask) {
        taskManager.addSubTask(epicTask, subtask);
    }

    public void changeStatus(int id, Status status) {
        taskManager.changeStatus(id, status);
    }

    public void remove(int id) {
        taskManager.removeFromID(id);
    }
}


