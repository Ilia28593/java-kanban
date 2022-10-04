import model.EpicTask;
import model.Status;
import model.SubTask;
import repository.Repository;
import service.Managers;

import static model.Status.DONE;
import static model.Status.IN_PROGRESS;

public class Main {
    private static final Manager manager = new Manager();

    public static void main(String[] args) {

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
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.changeEpicStatus(1, IN_PROGRESS);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.changeSubTaskStatus(2, DONE);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.changeSubTaskStatus(3, DONE);
        manager.printHistoryElement();
        System.out.println("-----------------------------------------------");

        manager.removeById(5);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.removeById(4);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        SubTask draiving3 = new SubTask("Собрать коробкиdfd", "Разложить вещи по коробкамdfd", Status.NEW);
        manager.addSubTask(draiving, draiving3);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
    }
}
