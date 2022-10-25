import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import repository.Repository;
import service.InMemoryHistoryManager;
import service.Managers;


public class Manager {
    private final Managers managers = new Managers();
    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private final Repository repository = new Repository();


    public void printAllElement() {
        for (Task task : managers.getDefault().listElement()) {
            System.out.println(task);
        }
    }

    public void printHistoryElement() {
        managers.getHistory().getHistoryList().forEach(System.out::println);
    }

    public void deleteHistoryElement(int id){
        if(managers.getDefault().getEpicTaskById(id) != null) {
            EpicTask epicTask = (EpicTask) managers.getDefault().getEpicTaskById(id);
            epicTask.getSubtaskIds().forEach(ids -> managers.getHistory().remove(ids));
        }
        managers.getHistory().remove(id);
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
            managers.getHistory().add(managers.getDefault().getEpicTaskById(id));
        } else {
            System.out.println("Статус поменяется на Done автоматически, после выполнения всех SubTask ");
        }
    }

    public void changeSubTaskStatus(int id, Status status) {
        managers.getHistory().add(managers.getDefault().getSubTaskById(id));
        managers.getDefault().changeSubtaskStatus(id, status);
    }

    public void removeById(int id) {
        deleteHistoryElement(id);
        managers.getDefault().removeByID(id);
    }

}


