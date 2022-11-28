package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.taskManager.TaskManager;

import java.io.File;

public class ManagersTest {
    private final File f = new File("back-up Test.csv");

    private TaskManager managerDefault;
    private TaskManager fileBackedTasksManager;
    private EpicTask epicTask;
    private EpicTask epicTask1;
    private SubTask subTask;
    private SubTask subTask1;
    private Task task;
    private Task task1;
    private final TaskManagerTest taskManagerTest = new TaskManagerTest();


    @BeforeEach
    public void beforeEach() {
        managerDefault = Managers.getDefaultTaskManager();
        fileBackedTasksManager = Managers.getFileBacked(f);
        epicTask = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        epicTask1 = new EpicTask("Прочитать статью", " Сделать заметки", Status.NEW);
        subTask = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        subTask1 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);
        task = new Task("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        task1 = new Task("Посчитать количество гостей", "Сделать рассадку", Status.NEW);

    }

    @Test
    public void testCheckInMemoryTaskManagerFromSubTaskIdEpicAndEpicTaskChangeStatus() {
        Assert.assertTrue("Не все SubTask содержат ссылки на EpicTask",
                taskManagerTest.checkIdEpicInSubTask(managerDefault, epicTask, subTask, subTask1));
        Assert.assertTrue("Задачи не совпадают.", taskManagerTest.changeStatusInSubTas(fileBackedTasksManager, epicTask,subTask,subTask1));
    }

    @Test
    public void testCheckFromSubTaskIdEpicTaskAndEpicTaskChangeStatus() {
        Assert.assertTrue("Не все SubTask содержат ссылки на EpicTask",
                taskManagerTest.checkIdEpicInSubTask(fileBackedTasksManager, epicTask, subTask, subTask1));
        Assert.assertTrue("Задачи не совпадают.", taskManagerTest.changeStatusInSubTas(managerDefault, epicTask,subTask,subTask1));
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerCheckGetTaskById() {
        taskManagerTest.testGetTaskById(managerDefault, task);
        taskManagerTest.testGetTaskById(fileBackedTasksManager, task);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestGetEpicTaskById() {
        taskManagerTest.testGetEpicTaskById(managerDefault, epicTask);
        taskManagerTest.testGetEpicTaskById(fileBackedTasksManager, epicTask);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestGetSubTaskById() {
        taskManagerTest.testGetSubTaskById(managerDefault, subTask, epicTask);
        taskManagerTest.testGetSubTaskById(fileBackedTasksManager, subTask, epicTask);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestUpdateTask() {
        taskManagerTest.testUpdateTask(managerDefault, task, task1);
        taskManagerTest.testUpdateTask(fileBackedTasksManager, task, task1);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestUpdateEpicTask() {
        taskManagerTest.testUpdateEpicTask(managerDefault, epicTask, epicTask1);
        taskManagerTest.testUpdateEpicTask(fileBackedTasksManager, epicTask, epicTask1);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestUpdateSubTask() {
        taskManagerTest.testUpdateSubTask(managerDefault, epicTask, subTask, subTask1);
        taskManagerTest.testUpdateSubTask(fileBackedTasksManager, epicTask, subTask, subTask1);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestRemoveByID() {
        taskManagerTest.testRemoveByID(managerDefault, epicTask, subTask, task);
        taskManagerTest.testRemoveByID(fileBackedTasksManager, epicTask, subTask, task);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestCleanRepository() {
        taskManagerTest.testCleanRepository(managerDefault, epicTask, subTask, task);
        taskManagerTest.testCleanRepository(fileBackedTasksManager, epicTask, subTask, task);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestTime() {
        taskManagerTest.testSetEpicTaskTimeToCompleted(managerDefault,epicTask,subTask,task);
    }

}

