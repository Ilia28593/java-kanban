package service.taskManager;

import api.KVServer;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskManagerTest {
    private KVServer server;
    private TaskManager httpTaskManager;

    @BeforeEach
    public void beforeEach() throws IOException {
        String urlToServer = "http://localhost:8078";
        server = new KVServer();
        server.start();
        httpTaskManager = Managers.getDefault(urlToServer);
        httpTaskManager.cleanRepository();
        httpTaskManager.addTask(new Task("Режим салатики ^_^", "Оливье и селедка под шубой", Status.NEW,
                LocalDateTime.of(2022, 12, 31, 18, 0), Duration.ofMinutes(45)));
        httpTaskManager.addEpicTask(new EpicTask("-_-", "=)", Status.NEW));
        httpTaskManager.addSubTask(2, new SubTask("O_o", ":)", Status.NEW,
                LocalDateTime.of(2022, 12, 13, 18, 0), Duration.ofMinutes(30), 2));
        httpTaskManager.addSubTask(2, new SubTask("+_+", "|_|", Status.NEW,
                LocalDateTime.of(2022, 11, 24, 14, 0), Duration.ofMinutes(37), 2));
    }

    private boolean compareExpectedAndActualNumberOfTasks(int expected, int result) {
        return expected == result;
    }

    @AfterEach
    public void afterEachHttp() {
        httpTaskManager.cleanRepository();
        server.stop();
    }

    @Test
    void serverWithTasksAndHistory() {
        httpTaskManager.getTaskById(1);
        httpTaskManager.getEpicTaskById(2);
        httpTaskManager.getSubTaskById(4);
        assertTrue(compareExpectedAndActualNumberOfTasks(1, httpTaskManager.getListTask().size()));
        assertEquals(2, httpTaskManager.getListSubTask().size());
        assertTrue(compareExpectedAndActualNumberOfTasks(1, httpTaskManager.getListEpicTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(3,
                httpTaskManager.getManagerHistory().getHistoryList().size()));
    }

    @Test
    void serverNoTasksCreated() {
        httpTaskManager.cleanRepository();
        assertTrue(compareExpectedAndActualNumberOfTasks(0, httpTaskManager.getListTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(0, httpTaskManager.getListSubTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(0, httpTaskManager.getListEpicTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(0,
                httpTaskManager.getManagerHistory().getHistoryList().size()));
    }

    @Test
    void serverEmptyHistory() {
        assertTrue(compareExpectedAndActualNumberOfTasks(1, httpTaskManager.getListTask().size()));
        assertEquals(2, httpTaskManager.getListSubTask().size());
        assertTrue(compareExpectedAndActualNumberOfTasks(2, httpTaskManager.getListSubTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(1, httpTaskManager.getListEpicTask().size()));
        assertEquals(0,
                httpTaskManager.getManagerHistory().getHistoryList().size());
    }

    @Test
    void serverEpicTaskWithoutSubtasks() {
        httpTaskManager.cleanRepository();
        httpTaskManager.addTask(new Task("x_x", "*_*", Status.NEW));
        assertTrue(compareExpectedAndActualNumberOfTasks(1,  httpTaskManager.getListTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(0, httpTaskManager.getListSubTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(0, httpTaskManager.getListEpicTask().size()));
        assertTrue(compareExpectedAndActualNumberOfTasks(0,
                httpTaskManager.getManagerHistory().getHistoryList().size()));
    }
}
