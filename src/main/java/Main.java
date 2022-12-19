import api.HttpTaskServer;
import api.KVServer;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import service.taskManager.HttpTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {

        {
            //Requests to HttpTaskManager via HttpTaskServer using Insomnia
            KVServer repoServer = null;
            try {
                repoServer = new KVServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            repoServer.start();
            HttpTaskServer taskServer = null;
            try {
                taskServer = new HttpTaskServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert taskServer != null;
            taskServer.start();
            //NOTE: To test in Insomnia - please comment the following stop-lines.
            repoServer.stop();
            taskServer.stop();
        }

        {
            KVServer server = null;
            try {
                server = new KVServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert server != null;
            server.start();
            //Serialization using the server
            HttpTaskManager kanban = new HttpTaskManager("http://localhost:8078");

            EpicTask driving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW,
                    LocalDateTime.of(2022, 11, 15, 13, 17), Duration.ofMinutes(22));
            SubTask driving1 = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW,
                    LocalDateTime.of(2022, 11, 15, 18, 17), Duration.ofMinutes(59));
            SubTask driving2 = new SubTask("Найти компанию по перевозки грузов"
                    , "Заказать машину на определенный день", Status.NEW);
            EpicTask birthday = new EpicTask("Празднование ДР", "Организовать вкусную еду", Status.NEW,
                    LocalDateTime.of(2022, 11, 19, 13, 17), Duration.ofMinutes(3));
            Task birthday1 = new Task("Посчитать количество гостей", "Сделать рассадку", Status.NEW,
                    LocalDateTime.of(2022, 12, 15, 13, 17), Duration.ofMinutes(48));

            kanban.addEpicTask(driving);
            kanban.addSubTask(driving.getId(), driving1);
            kanban.addSubTask(driving.getId(), driving2);
            kanban.addEpicTask(birthday);
            kanban.addTask(birthday1);

            kanban.getTaskById(5);
            kanban.getSubTaskById(3);
            kanban.getEpicTaskById(1);
            //Deserialization using the same server
            System.out.println("List of tasks:");
            kanban.getListTask().forEach(System.out::println);
            System.out.println("List of epics:");
            kanban.getListEpicTask().forEach(System.out::println);
            System.out.println("List of subtasks:");
            kanban.getListSubTask().forEach(System.out::println);
            System.out.println("History:");
            kanban.getManagerHistory().getHistoryList().forEach(System.out::println);

            server.stop();
        }
    }
}