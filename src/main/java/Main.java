import model.EpicTask;
import model.Status;
import model.SubTask;
import service.Managers;
import service.taskManager.FileBackedTasksManager;
import service.taskManager.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.Status.DONE;

public class Main {

    public static void main(String[] args) {
        write();
        read();
    }

    public static void write() {
        File f = new File("back-up file.csv");
        TaskManager manager = Managers.getFileBacked(f);

        EpicTask driving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW,
                LocalDateTime.of(2022,11,15,13,17), Duration.ofMinutes(22));
        SubTask driving1 = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW,
                LocalDateTime.of(2022,11,15,18,17), Duration.ofMinutes(59));
        SubTask driving2 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);
        EpicTask birthday = new EpicTask("Празднование ДР", "Организовать вкусную еду", Status.NEW,
                LocalDateTime.of(2022,11,19,13,17), Duration.ofMinutes(3));
        SubTask birthday1 = new SubTask("Посчитать количество гостей", "Сделать рассадку", Status.NEW,
                LocalDateTime.of(2022,12,15,13,17), Duration.ofMinutes(48));

        manager.addEpicTask(driving);
        manager.addSubTask(driving.getId(), driving1);
        manager.addSubTask(driving.getId(), driving2);
        manager.addEpicTask(birthday);
        manager.addSubTask(birthday.getId(), birthday1);

        System.out.println("Были добавлены следующие задачи");
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
        System.out.println("Проверка history");
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");
        System.out.println("Была запрошена таска с id1, проверка history");
        manager.getEpicTaskById(1);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");
        System.out.println("Запрос на изменение SubTask c id5, должно также вызваться изменение id4, так как это единственная его сабтаска");
        manager.changeSubtaskStatus(5, DONE);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");
        System.out.println("Запрос отсортированного списка");
        manager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }

    public static void read() {
        File f = new File("back-up file.csv");
        TaskManager manager = FileBackedTasksManager.loadFromFile(f);
        System.out.println("Загрузка с файла, были восстановлены следующие задачи");
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
        System.out.println("Загруженная history ");
        manager.printHistoryElement();
    }
}
