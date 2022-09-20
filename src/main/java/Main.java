import model.EpicTask;
import model.Status;
import model.Subtask;

import static model.Status.DONE;
import static model.Status.IN_PROGRESS;

public class Main {
    private static final Manager manager = new Manager();

    public static void main(String[] args) {

        EpicTask draiving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        Subtask draiving1 = new Subtask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        Subtask draiving2 = new Subtask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);

        EpicTask birthday = new EpicTask("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        Subtask birthday1 = new Subtask("Посчитать количество гостей", "Сделать рассадку", Status.NEW);


        manager.addEpicDefaultTask(draiving);
        manager.addSubTask(draiving, draiving1);
        manager.addSubTask(draiving, draiving2);
        manager.addEpicDefaultTask(birthday);
        manager.addSubTask(birthday, birthday1);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.changeStatus(1, IN_PROGRESS);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.changeStatus(2, DONE);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.changeStatus(3, DONE);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.remove(5);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.remove(4);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");
    }
}
