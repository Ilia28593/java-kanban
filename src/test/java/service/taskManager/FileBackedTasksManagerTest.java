package service.taskManager;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    private File d;
    private Path f;
    private TaskManager fileBackedTasksManager;
    private EpicTask epicTask;
    private Task task;
    private SubTask subTask;
    private SubTask subTask1;

    @BeforeEach
    public void beforeEach() {
        d = new File("back-up Test.csv");
        f = Path.of((d).getAbsolutePath());
        fileBackedTasksManager = Managers.getFileBacked(d);
        task = new Task("Режим салатики ^_^", "Оливье и селедка под шубой", Status.NEW,
                LocalDateTime.of(2022, 12, 31, 18, 0), Duration.ofMinutes(45));
        epicTask = new EpicTask("-_-", "=)", Status.NEW);
        subTask = new SubTask("O_o", ":)", Status.NEW,
                LocalDateTime.of(2022, 12, 13, 18, 0), Duration.ofMinutes(30), 2);
        subTask1 = new SubTask("+_+", "|_|", Status.NEW,
                LocalDateTime.of(2022, 11, 24, 14, 0), Duration.ofMinutes(37), 2);

    }

    @AfterEach
    void clear() {
        fileBackedTasksManager.cleanRepository();
    }

    @Test
    void saveWithTasksAndHistory() {
        fileBackedTasksManager.cleanRepository();
        fileBackedTasksManager.addTask(task);
        fileBackedTasksManager.addEpicTask(epicTask);
        fileBackedTasksManager.addSubTask(2, subTask);
        fileBackedTasksManager.addSubTask(2, subTask1);
        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getEpicTaskById(2);
        fileBackedTasksManager.getSubTaskById(3);
        try {
            String fileString = Files.readString(f);
            String[] lines = fileString.split("\r?\n");
            String line = "id,type,name,status,description,startTime,duration,endTime,epic";
            String lineCheck = lines[0];
            assertEquals(line, lineCheck);
            String line1 = "1,TASK,Режим салатики ^_^,NEW,Оливье и селедка под шубой,31.12.2022/18:00,45,31.12.2022/18:45";
            String lineCheck1 = lines[1];
            assertEquals(line1, lineCheck1);
            String line2 = "2,EPIC,-_-,NEW,=)";
            String lineCheck2 = lines[2];
            assertEquals(line2, lineCheck2);
            String line3 = "3,SUBTASK,O_o,NEW,:),2,13.12.2022/18:00,30,13.12.2022/18:30";
            String lineCheck3 = lines[3];
            assertEquals(line3, lineCheck3);
            String line4 = "4,SUBTASK,+_+,NEW,|_|,2,24.11.2022/14:00,37,24.11.2022/14:37";
            String lineCheck4 = lines[4];
            assertEquals(line4, lineCheck4);
            String line5 = "";
            String lineCheck5 = lines[5];
            assertEquals(line5, lineCheck5);
            String line6 = "1,2,3";
            String lineCheck6 = lines[6];
            assertEquals(line6, lineCheck6);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileBackedTasksManager.cleanRepository();
    }

    @Test
    void checkCleanSave() {
        fileBackedTasksManager.cleanRepository();
        try {
            String expectedFileString = "";
            String resultFileString = Files.readString(f);
            assertEquals(expectedFileString, resultFileString);
        } catch (IOException e) {
            System.out.println("���������� ��������� ����.");
        }

    }

    @Test
    void saveEmptyHistory() {
        fileBackedTasksManager.addTask(task);
        fileBackedTasksManager.addEpicTask(epicTask);
        fileBackedTasksManager.addSubTask(2, subTask);
        fileBackedTasksManager.addSubTask(2, subTask1);
        try {
            String fileString = Files.readString(f);
            String[] lines = fileString.split("\r?\n");
            String line = "id,type,name,status,description,startTime,duration,endTime,epic";
            String lineCheck = lines[0];
            assertEquals(line, lineCheck);
            String line1 = "1,TASK,Режим салатики ^_^,NEW,Оливье и селедка под шубой,31.12.2022/18:00,45,31.12.2022/18:45";
            String lineCheck1 = lines[1];
            assertEquals(line1, lineCheck1);
            String line2 = "2,EPIC,-_-,NEW,=)";
            String lineCheck2 = lines[2];
            assertEquals(line2, lineCheck2);
            String line3 = "3,SUBTASK,O_o,NEW,:),2,13.12.2022/18:00,30,13.12.2022/18:30";
            String lineCheck3 = lines[3];
            assertEquals(line3, lineCheck3);
            String line4 = "4,SUBTASK,+_+,NEW,|_|,2,24.11.2022/14:00,37,24.11.2022/14:37";
            String lineCheck4 = lines[4];
            assertEquals(line4, lineCheck4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileBackedTasksManager.cleanRepository();
    }

    @Test
    void saveEpicTaskWithoutSubtasks() {
        fileBackedTasksManager.cleanRepository();
        fileBackedTasksManager.addEpicTask(new EpicTask("+_+", "|_|", Status.NEW));
        fileBackedTasksManager.getEpicTaskById(1);
        try {
            String fileString = Files.readString(f);
            String[] lines = fileString.split("\r?\n");
            String expectedLine1 = "id,type,name,status,description,startTime,duration,endTime,epic";
            String resultLine1 = lines[0];
            assertEquals(expectedLine1, resultLine1);
            String expectedLine2 = "1,EPIC,+_+,NEW,|_|";
            String resultLine2 = lines[1];
            assertEquals(expectedLine2, resultLine2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileBackedTasksManager.cleanRepository();
    }

    @Test
    void loadFromFileWithTasks() {
        fileBackedTasksManager.addTask(task);
        fileBackedTasksManager.addEpicTask(epicTask);
        fileBackedTasksManager.addSubTask(2, subTask);
        fileBackedTasksManager.addSubTask(2, subTask1);
        assertEquals(fileBackedTasksManager.getListTask().size(), 1);
        assertEquals(fileBackedTasksManager.getListSubTask().size(), 2);
        assertEquals(fileBackedTasksManager.getListEpicTask().size(), 1);
        fileBackedTasksManager.cleanRepository();
    }

    @Test
    void loadFromFileNoTasksCreated() {
        fileBackedTasksManager.cleanRepository();
        assertEquals(fileBackedTasksManager.getListTask().size(), 0);
        assertEquals(fileBackedTasksManager.getListSubTask().size(), 0);
        assertEquals(fileBackedTasksManager.getListEpicTask().size(), 0);
    }
}