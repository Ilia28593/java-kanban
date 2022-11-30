package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import service.exception.IncorrectIdException;
import service.taskManager.InMemoryTaskManager;
import service.taskManager.TaskManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskManagerTest extends InMemoryTaskManager {

    public boolean checkIdEpicInSubTask(TaskManager managers, EpicTask driving, SubTask driving1, SubTask driving2) {
        managers.addEpicTask(driving);
        managers.addSubTask(driving.getId(), driving1);
        managers.addSubTask(driving.getId(), driving2);
        return managers.getListSubTask().stream().allMatch(s -> {
            List<Integer> collect = managers.getListEpicTask().stream().map(Task::getId)
                    .collect(Collectors.toList());
            return collect.contains(s.getEpicId());
        });
    }

    public void testGetTaskById(TaskManager managers, Task task) {
        managers.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = managers.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = managers.getListTask();
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    public void testGetEpicTaskById(TaskManager managers, EpicTask task) {
        managers.addEpicTask(task);
        final int taskId = task.getId();
        final EpicTask savedTask = (EpicTask) managers.getEpicTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<EpicTask> tasks = managers.getListEpicTask();
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }


    public void testGetSubTaskById(TaskManager managers, SubTask task, EpicTask epicTask) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask.getId(), task);
        final int taskId = task.getId();
        final SubTask savedTask = (SubTask) managers.getSubTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<SubTask> tasks = managers.getListSubTask();
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    public void testUpdateTask(TaskManager managers, Task task, Task newTask) throws IncorrectIdException {
        managers.addTask(task);
        final int taskId = task.getId();
        final Task updateTask = managers.updateTask(task, newTask);
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals(managers.getTaskById(taskId), updateTask, "Задачи не совпадают.");
    }

    public void testUpdateEpicTask(TaskManager managers, EpicTask task, EpicTask newTask) {
        managers.addEpicTask(task);
        final int taskId = task.getId();
        final EpicTask updateTask = (EpicTask) managers.updateEpicTask(task, newTask);
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals(managers.getEpicTaskById(taskId), updateTask, "Задачи не совпадают.");
    }

    public void testUpdateSubTask(TaskManager managers, EpicTask epicTask, SubTask task, SubTask newTask) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask.getId(), task);
        final int taskId = task.getId();
        final SubTask updateTask = (SubTask) managers.updateSubtaskTask(task, newTask);
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals(managers.getSubTaskById(taskId), updateTask, "Задачи не совпадают.");
    }

    public void testRemoveByID(TaskManager managers, EpicTask epicTask, SubTask subTask, Task task) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask.getId(), subTask);
        managers.addTask(task);
        managers.removeByID(subTask.getId());
        final int tasksSizeSubTask = managers.getListSubTask().size();
        assertEquals(0, tasksSizeSubTask, "Неверное количество задач.");
        assertNotNull(managers.getListSubTask(), "Задачи нe возвращаются.");
        managers.removeByID(epicTask.getId());
        final int tasksSizeEpicTask = managers.getListEpicTask().size();
        assertEquals(0, tasksSizeEpicTask, "Неверное количество задач.");
        assertNotNull(managers.getListEpicTask(), "Задачи нe возвращаются.");
        managers.removeByID(task.getId());
        final int tasksSizeTask = managers.getListTask().size();
        assertEquals(0, tasksSizeTask, "Неверное количество задач.");
        assertNotNull(managers.getListTask(), "Задачи нe возвращаются.");
    }

    public void testCleanRepository(TaskManager managers, EpicTask epicTask, SubTask subTask, Task task) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask.getId(), subTask);
        managers.addTask(task);
        assertEquals(1, managers.getListTask().size(), "Неверное количество задач.");
        assertEquals(1, managers.getListTask().size(), "Неверное количество задач.");
        assertEquals(1, managers.getListTask().size(), "Неверное количество задач.");
        managers.cleanRepository();
        assertEquals(0, managers.getListTask().size(), "Список задач не пуст.");
        assertEquals(0, managers.getListTask().size(), "Список задач не пуст.");
        assertEquals(0, managers.getListTask().size(), "Список задач не пуст.");

    }

    public void testSetEpicTaskTimeToCompleted(TaskManager manager, EpicTask epicTask, SubTask subTask, Task task) {
        manager.addTask(task);
        manager.setTaskTime(task.getId(), LocalDateTime.of(2022, 12, 31, 23, 59), 25L);
        manager.addEpicTask(epicTask);
        manager.addSubTask(epicTask.getId(), subTask);
        LocalDateTime localDateTime = LocalDateTime.of(2022, 11, 2, 13, 0);
        long duaretionOfEpic = 120L;
        manager.setEpicTaskTime(epicTask.getId(), localDateTime, duaretionOfEpic);
        assertEquals(manager.getPrioritizedTasks().size(), 2);
    }


}
