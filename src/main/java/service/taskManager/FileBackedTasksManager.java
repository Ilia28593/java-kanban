package service.taskManager;


import model.*;
import service.exception.ManagerException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static service.taskManager.Constants.DATE_TIME_FORMAT_FILE_BACKED_TASK_MANAGER;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final String fileName;

    public FileBackedTasksManager(File file) {
        this.fileName = file.getName();
    }

    public FileBackedTasksManager(String fileName, InMemoryTaskManager inMemoryTaskManager) {
        this.fileName = fileName;
        this.repository = inMemoryTaskManager.repository;
        this.managerHistory = inMemoryTaskManager.managerHistory;
    }

    private void save() throws ManagerException {
        try (FileWriter fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8, false)) {
            if (repository.listElement().size() != 0) {
                fileWriter.write("id,type,name,status,description,startTime,duration,endTime,epic\n");
                writeAllElementOfList(fileWriter, repository.listElement());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerException("Сбой при сохранение файлов +_+");
        }
    }

    private void writeAllElementOfList(FileWriter fileWriter, List<Task> list) throws IOException {
        for (Task task : list) {
            fileWriter.write(taskToString(task));
        }
        fileWriter.write("\n");
        fileWriter.write(historyToString(managerHistory.getHistoryList()));
    }

    private static String historyToString(List<Task> list) {
        String[] historyIdStrings = new String[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            historyIdStrings[i] = Integer.toString(list.get(i).getId());
        }
        return String.join(",", historyIdStrings);
    }

    private String taskToString(Task task) {
        if (task.getType().equals(Type.SUBTASK)) {
            return task.getStart() != null ? transformSubTaskString(task) : transformDefoltSubTaskString(task);
        } else {
            return task.getStart() != null ? transformTaskAndEpicTaskToString(task) : transformDefoltTaskAndEpicTaskToString(task);
        }
    }

    private String transformSubTaskString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%d,%s%n", task.getId(), task.getType(), task.getNameTask(),
                task.getStatus(), task.getTaskDetail(), ((SubTask) task).getEpicId(), dateTimeToString(task.getStart()),
                task.getDurationMinutes().toMinutes(), dateTimeToString(task.getFinish()));
    }

    private String transformDefoltSubTaskString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%d%n", task.getId(), task.getType(), task.getNameTask(),
                task.getStatus(), task.getTaskDetail(), ((SubTask) task).getEpicId());
    }

    private String transformDefoltTaskAndEpicTaskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s%n", task.getId(), task.getType(), task.getNameTask(),
                task.getStatus(), task.getTaskDetail());
    }


    private String transformTaskAndEpicTaskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%d,%s%n", task.getId(), task.getType(), task.getNameTask(),
                task.getStatus(), task.getTaskDetail(), dateTimeToString(task.getStart()),
                task.getDurationMinutes().toMinutes(), dateTimeToString(task.getFinish()));
    }

    private String dateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FILE_BACKED_TASK_MANAGER);
        return dateTime.format(formatter);
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        InMemoryTaskManager bufferKanban = new InMemoryTaskManager();
        String content = checkFileContents(file.getAbsolutePath());
        String[] splitContent = new String[0];
        if (checkFileContents(file.getAbsolutePath()) != null) {
            splitContent = content.split("\r?\n");
        }
        if (splitContent.length != 0) {
            readContentFile(bufferKanban, splitContent);
        }
        return new FileBackedTasksManager(file.getName(), bufferKanban);
    }

    private static LocalDateTime stringDateTime(String string) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FILE_BACKED_TASK_MANAGER));
    }

    private static void readContentFile(InMemoryTaskManager bufferKanban, String[] splitContent) {
        for (int i = 1; i < splitContent.length; i = i + 1) {
            if (i < splitContent.length - 2) {
                stringToTask(splitContent[i], bufferKanban);
            } else if (i == splitContent.length - 1) {
                addTaskInHistory(historyStringToList(splitContent[i]), bufferKanban);
            }
        }
    }

    private static String checkFileContents(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addTaskInHistory(List<Integer> listOfTaskIds, InMemoryTaskManager bakUp) {
        listOfTaskIds.forEach(t -> {
            if (bakUp.repository.getEpicTaskMap().containsKey(t)) {
                bakUp.managerHistory.add(bakUp.repository.getEpicTaskMap().get(t));
            } else if (bakUp.repository.getSubTaskMap().containsKey(t)) {
                bakUp.managerHistory.add(bakUp.repository.getSubTaskMap().get(t));
            } else {
                bakUp.managerHistory.add(bakUp.repository.getTaskMap().get(t));
            }
        });
    }

    private static List<Integer> historyStringToList(String value) {
        List<Integer> listHistory = new ArrayList<>();
        for (String s : value.split(",")) {
            listHistory.add(Integer.parseInt(s));
        }
        return listHistory;
    }

    private static void stringToTask(String string, InMemoryTaskManager bufferKanban) {
        String[] values = string.split(",");
        switch (Type.valueOf(values[1])) {
            case TASK:
                Task task = new Task(values[2], values[4], Status.valueOf(values[3]), Integer.parseInt(values[0]));
                task.setType(Type.TASK);
                task.setStart(stringDateTime(values[5]));
                task.setDurationMinutes(Duration.ofMinutes(Long.parseLong(values[6])));
                bufferKanban.repository.addTaskMap(task);
                break;
            case SUBTASK:
                SubTask subTask = new SubTask(values[2], values[4], Status.valueOf(values[3]), Integer.parseInt(values[0]));
                subTask.setType(Type.SUBTASK);
                bufferKanban.repository.getEpicTaskMap().get(Integer.parseInt(values[5])).addSubtask(subTask);
                if(values.length>6) {
                    subTask.setStart(stringDateTime(values[6]));
                    subTask.setDurationMinutes(Duration.ofMinutes(Long.parseLong(values[7])));
                }
                bufferKanban.repository.addSubtaskMap(subTask);
                break;
            case EPIC:
                EpicTask epicTask = new EpicTask(values[2], values[4], Status.valueOf(values[3]), Integer.parseInt(values[0]));
                epicTask.setType(Type.EPIC);
                epicTask.setStart(stringDateTime(values[5]));
                epicTask.setDurationMinutes(Duration.ofMinutes(Long.parseLong(values[6])));
                bufferKanban.repository.addEpicTaskMap(epicTask);
                break;
        }
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = (EpicTask) super.getEpicTaskById(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = (SubTask) super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        super.addEpicTask(epicTask);
        save();
    }

    @Override
    public void addSubTask(int id, SubTask subtask) {
        super.addSubTask(id, subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Task updateTask(Task task, Task newTask) {
        Task tasks = super.updateTask(task, newTask);
        save();
        return tasks;

    }

    @Override
    public Task updateEpicTask(EpicTask task, EpicTask newTask) {
        EpicTask epicTask = (EpicTask) super.updateEpicTask(task, newTask);
        save();
        return epicTask;
    }

    @Override
    public Task updateSubtaskTask(SubTask task, SubTask newSubTask) {
        SubTask subTask = (SubTask) super.updateSubtaskTask(task, newSubTask);
        save();
        return subTask;
    }

    @Override
    public void changeSubtaskStatus(int id, Status status) {
        super.changeSubtaskStatus(id, status);
        save();
    }

    @Override
    public void changeEpicStatus(Integer id, Status status) {
        super.changeEpicStatus(id, status);
        save();
    }

    @Override
    public void changeTaskStatus(Integer id, Status status) {
        super.changeTaskStatus(id, status);
        save();
    }

    @Override
    public void removeByID(Integer id) {
        super.removeByID(id);
        save();
    }

    @Override
    public void cleanRepository() {
        super.cleanRepository();
        save();
    }
}
