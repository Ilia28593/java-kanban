import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import repository.Repository;
import service.Managers;


public class Manager {
    private final Managers managers = new Managers();
    private final Repository repository = new Repository();


    public void printAllElement() {
        for (Task task : managers.getDefault().listElement()) {
            System.out.println(task);
        }
    }

    public void printHistoryElement() {
        for (Task task : managers.getHistory().getHistory()) {
            System.out.println(task);
        }
    }

    public void addEpicTask(EpicTask tasks) {
        managers.getDefault().addEpicTask(tasks);
        managers.getHistory().add(tasks);
    }

    public void addSubTask(EpicTask epicTask, SubTask tasks) {
        managers.getDefault().addSubTask(epicTask, tasks);
        managers.getHistory().add(tasks);
    }

    public void changeEpicStatus(int id, Status status) {
        if (status.equals(Status.IN_PROGRESS) || status.equals(Status.NEW)) {
            managers.getDefault().changeEpicStatus(id, status);
            managers.getHistory().add(managers.getDefault().getTaskById(id));
        } else {
            System.out.println("Статус поменяется на Done автоматически, после выполнения всех SubTask ");
        }
    }

    public void changeSubTaskStatus(int id, Status status) {
        managers.getHistory().add(managers.getDefault().getTaskById(id));
        managers.getDefault().changeSubtaskStatus(id, status);
    }

    public void removeById(int id) {
        if (!repository.getTaskList().isEmpty() && repository.getTaskList().stream().anyMatch(t -> t.getId() == id)) {
            managers.getDefault().removeByID(repository.getTaskList(), id);
        } else if (!repository.getSubtaskList().isEmpty() && repository.getSubtaskList().stream().anyMatch(t -> t.getId() == id)) {
            managers.getDefault().removeByID(repository.getSubtaskList(), id);
        } else if (!repository.getEpicTaskList().isEmpty() && repository.getEpicTaskList().stream().anyMatch(t -> t.getId() == id)) {
            managers.getDefault().removeByID(repository.getEpicTaskList(), id);
        }
    }

}


