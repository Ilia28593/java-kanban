package service;

import model.*;
import service.Exception.ManagerException;
import service.taskManager.InMemoryTaskManager;
import service.taskManager.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskManagerTest extends InMemoryTaskManager {

    public boolean checkIdEpicInSubTask(TaskManager managers, EpicTask driving, SubTask driving1, SubTask driving2) {
        managers.addEpicTask(driving);
        managers.addSubTask(driving, driving1);
        managers.addSubTask(driving, driving2);
        return managers.getListSubTask().stream().allMatch(s -> {
            List<Integer> collect = managers.getListEpicTask().stream().map(Id::getId)
                    .collect(Collectors.toList());
            return collect.contains(s.getEpicId());
        });
    }

    public boolean changeStatusInSubTas(TaskManager managers, EpicTask epicTask, SubTask subTask, SubTask subTask1) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask, subTask);
        managers.addSubTask(epicTask, subTask1);
        epicTask.getSubtaskIds().forEach(s -> managers.changeSubtaskStatus(s, Status.DONE));
        return epicTask.getStatus() == Status.DONE;
    }

    public void testGetTaskById(TaskManager managers, Task task) {
        managers.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = managers.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals("Задачи не совпадают.", task, savedTask);

        final List<Task> tasks = managers.getListTask();
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals("Неверное количество задач.", 1, tasks.size());
        assertEquals("Задачи не совпадают.", task, tasks.get(0));
    }

    public void testGetEpicTaskById(TaskManager managers, EpicTask task) {
        managers.addEpicTask(task);
        final int taskId = task.getId();
        final EpicTask savedTask = (EpicTask) managers.getEpicTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals("Задачи не совпадают.", task, savedTask);

        final List<EpicTask> tasks = managers.getListEpicTask();
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals("Неверное количество задач.", 1, tasks.size());
        assertEquals("Задачи не совпадают.", task, tasks.get(0));
    }


    public void testGetSubTaskById(TaskManager managers, SubTask task, EpicTask epicTask) {
        managers.addSubTask(epicTask, task);
        final int taskId = task.getId();
        final SubTask savedTask = (SubTask) managers.getSubTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals("Задачи не совпадают.", task, savedTask);

        final List<SubTask> tasks = managers.getListSubTask();
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals("Неверное количество задач.", 1, tasks.size());
        assertEquals("Задачи не совпадают.", task, tasks.get(0));
    }

    public void testUpdateTask(TaskManager managers, Task task, Task newTask) {
        managers.addTask(task);
        final int taskId = task.getId();
        final Task updateTask = managers.updateTask(task, newTask);
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals("Задачи не совпадают.", managers.getTaskById(taskId), updateTask);
    }

    public void testUpdateEpicTask(TaskManager managers, EpicTask task, EpicTask newTask) {
        managers.addEpicTask(task);
        final int taskId = task.getId();
        final EpicTask updateTask = (EpicTask) managers.updateEpicTask(task, newTask);
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals("Задачи не совпадают.", managers.getEpicTaskById(taskId), updateTask);
    }

    public void testUpdateSubTask(TaskManager managers, EpicTask epicTask, SubTask task, SubTask newTask) {
        managers.addSubTask(epicTask, task);
        final int taskId = task.getId();
        final SubTask updateTask = (SubTask) managers.updateSubtaskTask(task, newTask);
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals("Задачи не совпадают.", managers.getSubTaskById(taskId), updateTask);
    }

    public void testRemoveByID(TaskManager managers, EpicTask epicTask, SubTask subTask, Task task) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask, subTask);
        managers.addTask(task);
        managers.removeByID(subTask.getId());
        final int tasksSizeSubTask = managers.getListSubTask().size();
        assertEquals("Неверное количество задач.", 0, tasksSizeSubTask);
        assertNotNull(managers.getListSubTask(), "Задачи нe возвращаются.");
        managers.removeByID(epicTask.getId());
        final int tasksSizeEpicTask = managers.getListEpicTask().size();
        assertEquals("Неверное количество задач.", 0, tasksSizeEpicTask);
        assertNotNull(managers.getListEpicTask(), "Задачи нe возвращаются.");
        managers.removeByID(task.getId());
        final int tasksSizeTask = managers.getListTask().size();
        assertEquals("Неверное количество задач.", 0, tasksSizeTask);
        assertNotNull(managers.getListTask(), "Задачи нe возвращаются.");
    }

    public void testCleanRepository(TaskManager managers, EpicTask epicTask, SubTask subTask, Task task) {
        managers.addEpicTask(epicTask);
        managers.addSubTask(epicTask, subTask);
        managers.addTask(task);
        assertEquals("Неверное количество задач.", 1, managers.getListTask().size());
        assertEquals("Неверное количество задач.", 1, managers.getListTask().size());
        assertEquals("Неверное количество задач.", 1, managers.getListTask().size());
        managers.cleanRepository();
        assertEquals("Список задач не пуст.", 0, managers.getListTask().size());
        assertEquals("Список задач не пуст.", 0, managers.getListTask().size());
        assertEquals("Список задач не пуст.", 0, managers.getListTask().size());

    }

    public void testSetEpicTaskTimeToCompleted(TaskManager manager, EpicTask epicTask, SubTask subTask, Task task) {
        manager.addTask(task);
        manager.setTaskTime(task.getId(), LocalDateTime.of(2022, 12, 31, 23, 59),25L);
        manager.addEpicTask(epicTask);
        manager.addSubTask(epicTask, subTask);
        LocalDateTime localDateTime = LocalDateTime.of(2022, 11, 2, 13, 0);
        long duaretionOfEpic = 120L;
        manager.setEpicTaskTime(epicTask.getId(), localDateTime, duaretionOfEpic);
        TreeSet<Task> sortedTaskTree = repository.getSortedTaskTree();
        assertEquals(manager.getPrioritizedTasks().size(), 2);
        final ManagerException exception = assertThrows(
                ManagerException.class,
                () -> manager.setSubTaskTime(subTask.getId(), localDateTime, 12L));
        assertEquals("This time use in another Task", exception.getMessage());
    }


}
