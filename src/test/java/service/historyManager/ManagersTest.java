package service.historyManager;

import junit.framework.TestCase;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.util.List;

public class ManagersTest extends TestCase {

    private HistoryManager managerHistory;
    private Task task;
    private Task task1;
    private Task task2;

    @BeforeEach
    public void beforeEach() {
        managerHistory = Managers.getHistory();
        this.task = new Task("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        this.task1 = new Task("Празднование  с Новым годом", "Организовать вискарик ^_^ ", Status.NEW);
        this.task2 = new Task("Празднование с Рождеством", "Организовать вкусную еду", Status.NEW);
    }


    @Test
    void addAndAddDoubleTask() {
        managerHistory.add(task);
        managerHistory.add(task);
        final List<Task> history = managerHistory.getHistoryList();
        Assertions.assertNotNull(history, "История пустая.");
        Assertions.assertEquals(1, history.size(), "История пустая.");
    }

    @Test
    void removeFirstElement() {
        managerHistory.add(task);
        managerHistory.add(task1);
        managerHistory.add(task2);
        managerHistory.remove(task.getId());
        final List<Task> history = managerHistory.getHistoryList();
        Assertions.assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    void removeCenterElement() {
        managerHistory.add(task);
        managerHistory.add(task1);
        managerHistory.add(task2);
        managerHistory.remove(task1.getId());
        final List<Task> history = managerHistory.getHistoryList();
        Assertions.assertEquals(2, history.size(), "История не пустая.");
    }
    @Test
    void removeEndElement() {
        managerHistory.add(task);
        managerHistory.add(task1);
        managerHistory.add(task2);
        managerHistory.remove(task2.getId());
        final List<Task> history = managerHistory.getHistoryList();
        Assertions.assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    public void removeUnderSizeHistory2() {
        managerHistory.add(task);
        managerHistory.remove(2);
        final List<Task> history = managerHistory.getHistoryList();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void shouldThrowRemoveUnderSizeHistory0() {
        managerHistory.add(task);
        managerHistory.remove(0);
        final List<Task> history = managerHistory.getHistoryList();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }

}