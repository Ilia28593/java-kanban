package service.taskManager;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import service.historyManager.HistoryManager;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    void setEpicTaskTime(int id, LocalDateTime localDateTime, Long minutes);

    void setTaskTime(int id, LocalDateTime localDateTime, Long minutes);

    void setSubTaskTime(int id, LocalDateTime localDateTime, Long minutes);

    void printHistoryElement();

    void printAllElement();

    Task getTaskById(int id);

    Task getEpicTaskById(int id);

    Task getSubTaskById(int id);

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

    void addSubTask(int epicTask, SubTask subtask);

    List<Task> getListTask();

    List<EpicTask> getListEpicTask();

    List<SubTask> getListSubTask();

    List<SubTask> getSubListOfEpic(int task);

    HistoryManager getManagerHistory();
}

