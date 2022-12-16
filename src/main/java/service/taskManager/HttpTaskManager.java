package service.taskManager;

import api.DurationAdapter;
import api.KVTaskClient;
import api.LocalDateTimeAdapter;
import com.google.gson.*;
import model.*;
import service.exception.ManagerException;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager implements TaskManager{
    static KVTaskClient client;
    private static final String LOCAL_DATE_TIME_FORMATTER = "dd--MM--yyyy HH:mm";

    public HttpTaskManager(String urlToServer) {
        super(new File(urlToServer));
        client = new KVTaskClient(urlToServer);
    }

    protected HttpTaskManager(String urlToServer, InMemoryTaskManager bufferKanban) {
        super(urlToServer, bufferKanban);
    }

    private String listOfTasksToJson(List<Task> list) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        return gson.toJson(list);
    }

    private String listOfSubTasksToJson(List<SubTask> list) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        return gson.toJson(list);
    }

    private String listOfEpicTasksToJson(List<EpicTask> list) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        return gson.toJson(list);
    }

    private String listOfIdsToJson(List<Integer> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(list);
    }

    protected void save() throws ManagerException {
        List<Task> tasksList = new ArrayList<>(repository.getTaskMap().values());
        List<SubTask> subTasksList = new ArrayList<>(repository.getSubTaskMap().values());
        List<EpicTask> epicTasksList = new ArrayList<>(repository.getEpicTaskMap().values());
        List<Integer> listOfIdsFromHistory = managerHistory.getHistoryList().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        client.put("task", listOfTasksToJson(tasksList));
        client.put("subtask", listOfSubTasksToJson(subTasksList));
        client.put("epic", listOfEpicTasksToJson(epicTasksList));
        client.put("history", listOfIdsToJson(listOfIdsFromHistory));
    }

    private static List<Task> listOfTasksFromJson(String json) {
        List<Task> listOfTasks = new ArrayList<>();
        if(!json.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(json);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement e : jsonArray) {
                JsonObject jsonObject = e.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                Type type = Type.valueOf(jsonObject.get("type").getAsString());
                Status status = Status.valueOf(jsonObject.get("status").getAsString());
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                        DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMATTER));
                Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
                Task task = new Task(name, description, status, startTime, duration);
                listOfTasks.add(task);
            }
        }
        return listOfTasks;
    }

    private static List<SubTask> listOfSubTasksFromJson(String json) {
        List<SubTask> listOfSubTasks = new ArrayList<>();
        if(!json.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(json);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement e : jsonArray) {
                JsonObject jsonObject = e.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                Type type = Type.valueOf(jsonObject.get("type").getAsString());
                Status status = Status.valueOf(jsonObject.get("status").getAsString());
                int epicTaskId = jsonObject.get("epicTaskId").getAsInt();
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                        DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMATTER));
                Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
                SubTask subTask = new SubTask(name, description, status, startTime, duration, epicTaskId);
                listOfSubTasks.add(subTask);
            }
        }
        return listOfSubTasks;
    }

    private static List<EpicTask> listOfEpicTasksFromJson(String json) {
        List<EpicTask> listOfEpicTasks = new ArrayList<>();
        if(!json.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(json);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement e : jsonArray) {
                JsonObject jsonObject = e.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                Status status = Status.valueOf(jsonObject.get("status").getAsString());
                Type type = Type.valueOf(jsonObject.get("type").getAsString());
                EpicTask epicTask = new EpicTask(name, description, status, type);
                listOfEpicTasks.add(epicTask);
            }
        }
        return listOfEpicTasks;
    }

    private static List<Integer> listOfIdsFromJson(String json) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        if(!json.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(json);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement e : jsonArray) {
                JsonPrimitive jsonPrimitive = e.getAsJsonPrimitive();
                listOfIdsFromHistory.add(jsonPrimitive.getAsInt());
            }
        }
        return listOfIdsFromHistory;
    }

    private static void loadTasksToBuffer(InMemoryTaskManager bufferKanban) {
        String jsonTasksString = client.load("task");
        if(!jsonTasksString.isEmpty()) {
            List<Task> listOfTasks = listOfTasksFromJson(jsonTasksString);
            for (Task task : listOfTasks) {
                bufferKanban.addTask(new Task(task.getNameTask(), task.getTaskDetail(), task.getStatus(), task.getStart(),
                        task.getDurationMinutes()));
            }
        }
        String jsonEpicString = client.load("epic");
        if(!jsonEpicString.isEmpty()) {
            List<EpicTask> listOfEpicTasks = listOfEpicTasksFromJson(jsonEpicString);
            for (EpicTask epic : listOfEpicTasks) {
                bufferKanban.addEpicTask(new EpicTask(epic.getNameTask(), epic.getTaskDetail(), epic.getStatus(),epic.getStart(),epic.getDurationMinutes()));
            }
        }
        String jsonSubtasksString = client.load("subtask");
        if(!jsonSubtasksString.isEmpty()) {
            List<SubTask> listOfSubtasks = listOfSubTasksFromJson(jsonSubtasksString);
            for (SubTask subtask : listOfSubtasks) {
                bufferKanban.addSubTask(subtask.getEpicId(),new SubTask(subtask.getNameTask(), subtask.getNameTask(),
                        subtask.getStatus(), subtask.getStart(), subtask.getDurationMinutes(),subtask.getEpicId()));
            }
        }
    }

    private static void loadHistoryToBuffer(InMemoryTaskManager bufferKanban) {
        String jsonHistoryString = client.load("history");
        List<Integer> listOfIdsFromHistory = listOfIdsFromJson(jsonHistoryString);
        for(Integer id : listOfIdsFromHistory) {
            if (bufferKanban.repository.getTaskMap().containsKey(id)) {
                bufferKanban.getTaskById(id);
            } else if (bufferKanban.repository.getSubTaskMap().containsKey(id)) {
                bufferKanban.getSubTaskById(id);
            } else if (bufferKanban.repository.getEpicTaskMap().containsKey(id)) {
                bufferKanban.getEpicTaskById(id);
            }
        }
    }

    public static HttpTaskManager loadFromServer(String uri) {
        InMemoryTaskManager bufferKanban = new InMemoryTaskManager();
        loadTasksToBuffer(bufferKanban);
        loadHistoryToBuffer(bufferKanban);
        return new HttpTaskManager(uri, bufferKanban);
    }

    private void clearServer() {
        client.clear();
    }

    @Override
    public void cleanRepository() {
        super.cleanRepository();
        clearServer();
    }
}
