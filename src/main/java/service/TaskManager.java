package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    Task getTaskById(int id);

    List<Task> listElement();

    void updateTask(Task task, Task newTask);

    void updateEpicTask(EpicTask task, EpicTask newTask);

    void updateSubtaskTask(SubTask task, SubTask newSubTask);

    void changeSubtaskStatus(Integer id, Status status);

    void changeEpicStatus(Integer id, Status status);

    void changeTaskStatus(Integer id, Status status);

    void searchStatusDoneInChild(EpicTask task);

    void removeByID(List listRepository, Integer id);

    void cleanRepository(List listRepository);

    void addTask(Task task);

    void addEpicTask(EpicTask epicTask);

    void addSubTask(EpicTask epicTask, SubTask subtask);

    List<SubTask> getSubListOfEpic(EpicTask task);
}

