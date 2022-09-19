import model.*;
import repository.NewRepository;
import service.Service;

import java.util.List;

public class Manager {
    private static final Service service = new Service();


    public void printAllElement() {
        List<DefaultTask> defaultTasks = NewRepository.getDefaultTaskList();
        if (defaultTasks != null) {
            for (DefaultTask task : defaultTasks) {
                System.out.println(task);
            }
        }
        List<EpicTask> epicTask = NewRepository.getEpicTaskList();
        if (epicTask != null) {
            for (EpicTask epicTasks : epicTask) {
                System.out.println(epicTasks);
                for (Subtask subtaskTasks : epicTasks.getSubtask()) {
                    System.out.println(subtaskTasks);
                }
            }
        }
    }

    public void addEpicDefaultTask(List<TaskInter> tasks) {
        service.setAllTasksInRepository(tasks);
    }

    public void addSubTask(EpicTask epicTask, Subtask subtask) {
        service.addSubTask(epicTask, subtask);
    }

    public void changeStatus(int id, Status status) {
        service.changeStatus(id, status);
    }

    public void remove(int id) {
        service.removeFromID(id);
    }
}


