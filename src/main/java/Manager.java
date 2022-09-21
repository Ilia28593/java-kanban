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
        for (Task task : taskManager.listElement()) {
            System.out.println(task);
        }
    }

    public void addEpicTask(EpicTask tasks) {
        taskManager.addEpicTask(tasks);
    }

    public void addSubTask(EpicTask epicTask, SubTask tasks) {
        taskManager.addSubTask(epicTask, tasks);
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

    public void removeById(int id) {
        if (repository.getTaskList().contains(taskManager.getTaskById(id))) {
            taskManager.removeByID(repository.getTaskList(), id);
        } else if (repository.getSubtaskList().contains((SubTask) taskManager.getTaskById(id))) {
            taskManager.removeByID(repository.getSubtaskList(), id);
        } else if (repository.getEpicTaskList().contains((EpicTask) taskManager.getTaskById(id))) {
            taskManager.removeByID(repository.getEpicTaskList(), id);
        } else {
            System.out.println("Указанного id нету");
        }
    }

}


