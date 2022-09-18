import entity.Status;
import entity.Task;
import repository.Repository;
import service.Service;

import java.util.List;

public class Manager {
    private static final Repository repository = new Repository();
    private static final Service service = new Service();


    public void printAllElement() {
        List<Task> tasks = repository.getTasks();
        for (Task task : tasks) {
            if (!(task.getTasksPart() == null)) {
                System.out.println(task);
                task.getTasksPart().forEach(System.out::println);
            }
        }
    }

    public void addAllTask(List<Task> tasks) {
        try {
            service.setAllTasksInRepository(tasks);
        } catch (Exception e) {
            System.out.println("Error with addAllTask");
            e.printStackTrace();
        }
    }

    public void changeStatus(int id, Status status) {
        service.changeStatus(id, status);
    }

    public void remove(int id) {
        service.removeFromID(id);
    }

}

