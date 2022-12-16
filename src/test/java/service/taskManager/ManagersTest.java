package service.taskManager;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ManagersTest {

    private TaskManager managerDefault;
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
        epicTask = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        epicTask1 = new EpicTask("Прочитать статью", " Сделать заметки", Status.NEW);
        subTask = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        subTask1 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);
        task = new Task("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        task1 = new Task("Посчитать количество гостей", "Сделать рассадку", Status.NEW);
    }

    @AfterEach
    public void clear(){
        managerDefault.cleanRepository();
    }

    @Test
    public void testCheckInMemoryTaskManagerFromSubTaskIdEpicAndEpicTaskChangeStatus() {
        assertTrue(taskManagerTest.checkIdEpicInSubTask(managerDefault, epicTask, subTask, subTask1), "Не все SubTask содержат ссылки на EpicTask");
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerCheckGetTaskById() {
        taskManagerTest.testGetTaskById(managerDefault, task);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestGetEpicTaskById() {
        taskManagerTest.testGetEpicTaskById(managerDefault, epicTask);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestGetSubTaskById() {
        taskManagerTest.testGetSubTaskById(managerDefault, subTask, epicTask);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestUpdateTask() {
          taskManagerTest.testUpdateTask(managerDefault, task, task1);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestUpdateEpicTask() {
        taskManagerTest.testUpdateEpicTask(managerDefault, epicTask, epicTask1);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestUpdateSubTask() {
        taskManagerTest.testUpdateSubTask(managerDefault, epicTask, subTask, subTask1);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestRemoveByID() {
        taskManagerTest.testRemoveByID(managerDefault, epicTask, subTask, task);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestCleanRepository() {
        taskManagerTest.testCleanRepository(managerDefault, epicTask, subTask, task);
    }

    @Test
    public void testInMemoryTaskManagerAndFileBackedTasksManagerTestTime() {
        taskManagerTest.testSetEpicTaskTimeToCompleted(managerDefault, epicTask, subTask, task);
    }

}

