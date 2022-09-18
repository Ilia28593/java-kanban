import entity.Status;
import entity.Task;
import entity.Type;

import java.util.ArrayList;
import java.util.List;

import static entity.Status.DONE;

public class Main {
    private static Manager manager = new Manager();

    public static void main(String[] args) {

        Task driving = new Task();
        driving.setNameTask("Переезд");
        driving.setTaskDetail("Продумать план переезда");
        driving.setStatus(Status.NEW);
        driving.setType(Type.EPIC);

        Task drivingPart1 = new Task();
        drivingPart1.setParentId(driving.getId());
        drivingPart1.setNameTask("Собрать коробки");
        drivingPart1.setTaskDetail("Разложить вещи по коробкам");
        drivingPart1.setStatus(Status.NEW);
        drivingPart1.setType(Type.SUBTASK);

        Task drivingPart2 = new Task();
        drivingPart2.setParentId(driving.getId());
        drivingPart2.setNameTask("Найти компанию по перевозки грузов");
        drivingPart2.setTaskDetail("Заказать машину на определенный день");
        drivingPart2.setStatus(Status.NEW);
        drivingPart2.setType(Type.SUBTASK);


        Task birthday = new Task();
        birthday.setNameTask("Найти место празднования");
        birthday.setTaskDetail("Организовать вкусную еду");
        birthday.setStatus(Status.NEW);
        birthday.setType(Type.EPIC);

        Task birthdayPart1 = new Task();
        birthdayPart1.setParentId(birthday.getId());
        birthdayPart1.setNameTask("Посчитать количество гостей");
        birthdayPart1.setTaskDetail("Сделать рассадку");
        birthdayPart1.setStatus(Status.NEW);
        birthdayPart1.setType(Type.SUBTASK);

        List<Task> tasks = new ArrayList<>() {{
            add(driving);
            add(drivingPart1);
            add(drivingPart2);
            add(birthday);
            add(birthdayPart1);
        }};

        manager.addAllTask(tasks);
//        manager.printAllElement();
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
