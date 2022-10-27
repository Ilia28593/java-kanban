import model.EpicTask;
import model.Status;
import model.SubTask;
import service.Managers;
import service.taskManager.FileBackedTasksManager;
import service.taskManager.TaskManager;

import java.io.File;

import static model.Status.DONE;
import static model.Status.IN_PROGRESS;

public class test {
    private final static Managers managers = new Managers();

    public static void test1_5() {
        final TaskManager manager = managers.getDefaultTaskManager();

        EpicTask draiving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        SubTask draiving1 = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        SubTask draiving2 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);


        EpicTask birthday = new EpicTask("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        SubTask birthday1 = new SubTask("Посчитать количество гостей", "Сделать рассадку", Status.NEW);


        manager.addEpicTask(draiving);
        manager.addSubTask(draiving, draiving1);
        manager.addSubTask(draiving, draiving2);
        manager.addEpicTask(birthday);
        manager.addSubTask(birthday, birthday1);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");
        manager.changeEpicStatus(1, IN_PROGRESS);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.changeSubtaskStatus(2, DONE);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.changeSubtaskStatus(5, DONE);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.updateEpicTask(draiving, birthday);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.removeByID(4);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
        manager.printHistoryElement();

    }

    public static void write() {
        File f = new File("back-up file.csv");
        TaskManager manager = managers.getFileBacked(f);

        EpicTask driving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        SubTask draiving1 = new SubTask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        SubTask draiving2 = new SubTask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);
        EpicTask birthday = new EpicTask("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        SubTask birthday1 = new SubTask("Посчитать количество гостей", "Сделать рассадку", Status.NEW);

        manager.addEpicTask(driving);
        manager.addSubTask(driving, draiving1);
        manager.addSubTask(driving, draiving2);
        manager.addEpicTask(birthday);
        manager.addSubTask(birthday, birthday1);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");
        manager.getEpicTaskById(1);
        System.out.println("-----------------------------------------------");
        manager.changeSubtaskStatus(5, DONE);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");
    }

    public static void read() {
        File f = new File("back-up file.csv");
        TaskManager manager = FileBackedTasksManager.loadFromFile(f);
        System.out.println("*********************");
        manager.printAllElement();
        System.out.println("*********************");
        manager.printHistoryElement();
    }
}
