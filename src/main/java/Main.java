import model.*;

import java.util.ArrayList;
import java.util.List;

import static model.Status.DONE;
import static model.Status.InPROGRESS;

public class Main {
    private static Manager manager = new Manager();

    public static void main(String[] args) {

        EpicTask draiving = new EpicTask("Переезд", "Продумать план переезда", Status.NEW);
        Subtask draiving1 = new Subtask("Собрать коробки", "Разложить вещи по коробкам", Status.NEW);
        Subtask draiving2 = new Subtask("Найти компанию по перевозки грузов"
                , "Заказать машину на определенный день", Status.NEW);

        EpicTask birthday = new EpicTask("Празднование ДР", "Организовать вкусную еду", Status.NEW);
        Subtask birthday1 = new Subtask("Посчитать количество гостей", "Сделать рассадку", Status.NEW);

        List<TaskInter> tasks = new ArrayList<>() {{
            add(draiving);
            add(birthday);
        }};


        manager.addEpicDefaultTask(tasks);
        manager.addSubTask(draiving, draiving1);
        manager.addSubTask(draiving, draiving2);
        manager.addSubTask(birthday, birthday1);
        manager.printAllElement();
        System.out.println("-----------------------------------------------");

        manager.changeStatus(1, InPROGRESS);
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
