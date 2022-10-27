package service.taskManager;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    void printHistoryElement();

    void printAllElement();

    Task getTaskById(int id);

    Task getEpicTaskById(int id);

    Task getSubTaskById(int id);

    List<Task> listElement();

    Task updateTask(Task task, Task newTask);

    Task updateEpicTask(EpicTask task, EpicTask newTask);

    Task updateSubtaskTask(SubTask task, SubTask newSubTask);

    void changeSubtaskStatus(int id, Status status);

    void changeEpicStatus(Integer id, Status status);

    void changeTaskStatus(Integer id, Status status);

    void removeByID(Integer id);

    void cleanRepository();

    void addTask(Task task);

    void addEpicTask(EpicTask epicTask);

    void addSubTask(EpicTask epicTask, SubTask subtask);

    List<SubTask> getSubListOfEpic(EpicTask task);
}

