package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.taskManager.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicTaskTest {

    private TaskManager manager;
    private EpicTask driving;
    private SubTask driving1;
    private SubTask driving2;


    @BeforeEach
    public void beforeAll() {
        manager = Managers.getDefaultTaskManager();
        driving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        driving1 = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        driving2 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);
        manager.addEpicTask(driving);
        manager.addSubTask(driving.getId(), driving1);
        manager.addSubTask(driving.getId(), driving2);
    }

    @Test
    public void testEpicCheckSubtaskStatusNew() {
        boolean b = driving.getSubtaskIds().stream().allMatch(s -> manager.getSubTaskById(s).getStatus().equals(Status.NEW));
        assertTrue(b);
    }

    @Test
    public void testEpicCheckSubtaskStatusDone() {
        driving.getSubtaskIds().forEach(s -> manager.changeSubtaskStatus(s, Status.DONE));
        boolean b = driving.getSubtaskIds().stream().allMatch(s -> manager.getSubTaskById(s).getStatus().equals(Status.DONE));
        assertTrue(b);
        assertEquals(Status.DONE, driving.getStatus());
    }

    @Test
    public void testEpicCheckSubtaskStatusDoneAndNew() {
        manager.changeSubtaskStatus(driving1.getId(), Status.DONE);
        assertEquals(Status.DONE, driving1.getStatus());
        assertEquals(Status.NEW, driving2.getStatus());
        assertEquals(Status.NEW, driving.getStatus());
    }

    @Test
    public void testEpicCheckSubtaskStatusIN_PROGRESS() {
        driving.getSubtaskIds().forEach(s -> manager.changeSubtaskStatus(s, Status.IN_PROGRESS));
        boolean b = driving.getSubtaskIds().stream().allMatch(s -> manager.getSubTaskById(s).getStatus().equals(Status.IN_PROGRESS));
        assertTrue(b);
        assertEquals(Status.IN_PROGRESS, driving1.getStatus());
        assertEquals(Status.IN_PROGRESS, driving2.getStatus());

    }
}