package service.taskManager;

import junit.framework.TestCase;
import model.EpicTask;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.io.File;

public class FileBackedTasksManagerTest extends TestCase {

    private final File f = new File("back-up Test.csv");
    private final TaskManager manager = FileBackedTasksManager.loadFromFile(f);
    private final TaskManager fileBackedTasksManager = Managers.getFileBacked(f);

    private EpicTask epicTask;
    private SubTask subTask;
    private SubTask subTask1;

    @BeforeEach
    public void beforeEach() {
        epicTask = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        subTask = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        subTask1 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);
        fileBackedTasksManager.addEpicTask(epicTask);
        fileBackedTasksManager.addSubTask(epicTask, subTask);
        fileBackedTasksManager.addSubTask(epicTask, subTask1);
    }

    @Test
    void checkCorrectWriteAndReadFromFileBackedTasksManager() {
        fileBackedTasksManager.getEpicTaskById(epicTask.getId());
        fileBackedTasksManager.getSubTaskById(subTask.getId());
        fileBackedTasksManager.getSubTaskById(subTask1.getId());
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(f);
        Assertions.assertEquals(epicTask, manager.getEpicTaskById(epicTask.getId()), "Задачи не совпадают.");
        Assertions.assertEquals(subTask, manager.getSubTaskById(subTask.getId()), "Задачи не совпадают.");
        Assertions.assertEquals(subTask1, manager.getSubTaskById(subTask1.getId()), "Задачи не совпадают.");
    }

    @Test
    void checkCorrectReadFromCleanFileBackedTasksManager() {
        fileBackedTasksManager.cleanRepository();
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(f);
        Assertions.assertEquals(manager.listElement().size(), 0, "что -то осталось при загрузке пустого листа");
    }
}