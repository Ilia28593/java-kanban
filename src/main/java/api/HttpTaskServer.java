package api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.taskManager.HttpTaskManager;
import service.taskManager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static service.Managers.getHistory;
import static service.taskManager.Constants.*;


public class HttpTaskServer {
    private String url;
    private TaskManager httpKanban;
    private static Gson gson;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        url = URL_TO_KV_SERVER;
        httpKanban = Managers.getDefault(url);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT_HTTP_TASK_SERVER), 0);
        server.createContext("/tasks", new TasksHandler(httpKanban));
    }

    public HttpTaskServer(HttpTaskManager httpKanban) throws IOException {
        url = URL_TO_KV_SERVER;
        this.httpKanban = httpKanban;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT_HTTP_TASK_SERVER), 0);
        server.createContext("/tasks", new TasksHandler(httpKanban));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT_HTTP_TASK_SERVER + " порту!");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT_HTTP_TASK_SERVER + " порту!");
    }

    static class TasksHandler implements HttpHandler {
        TaskManager kanban;

        TasksHandler(TaskManager kanban) {
            this.kanban = kanban;
        }

        private String getCategoryFromPath(String path) {
            String[] pathElements = path.split("/");
            return pathElements[2];
        }

        private int getIdFromQuery(String query) {
            String[] queryElements = query.split("=");
            return Integer.parseInt(queryElements[1]);

        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getRawQuery();
            String path = httpExchange.getRequestURI().getPath();

            if (method.equals("GET") && path.split("/").length == 2) {
                //endpoint: GET /tasks
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    List<Task> listOfAllTasks = kanban.getPrioritizedTasks();
                    os.write(gson.toJson(listOfAllTasks).getBytes());
                }
            } else if (method.equals("DELETE") && path.split("/").length == 2) {
                kanban.cleanRepository();
            } else {
                String taskCategory = getCategoryFromPath(path);
                switch (method) {
                    case "GET":
                        switch (taskCategory) {
                            case "task":
                                //endpoint: GET /tasks/task/
                                httpExchange.sendResponseHeaders(200, 0);
                                if (query == null) {
                                    try (OutputStream os = httpExchange.getResponseBody()) {
                                        List<Task> listOfTasks = kanban.getListTask();
                                        os.write(gson.toJson(listOfTasks).getBytes());
                                    }
                                } else {
                                    //endpoint: GET /tasks/task/?id=
                                    try (OutputStream os = httpExchange.getResponseBody()) {
                                        int taskId = getIdFromQuery(query);
                                        Task task = kanban.getTaskById(taskId);
                                        os.write(gson.toJson(task).getBytes());
                                    }
                                }
                                break;
                            case "subtask":
                                //endpoint: GET /tasks/subtask/
                                if (query == null) {
                                    httpExchange.sendResponseHeaders(200, 0);
                                    try (OutputStream os = httpExchange.getResponseBody()) {
                                        List<SubTask> listOfSubtasks = kanban.getListSubTask();
                                        os.write(gson.toJson(listOfSubtasks).getBytes());
                                    }
                                } else {
                                    if (!path.contains("epic")) {
                                        //endpoint: GET /tasks/subtask/?id=
                                        httpExchange.sendResponseHeaders(200, 0);
                                        try (OutputStream os = httpExchange.getResponseBody()) {
                                            int subtaskId = getIdFromQuery(query);
                                            SubTask subTask = (SubTask) kanban.getSubTaskById(subtaskId);
                                            os.write(gson.toJson(subTask).getBytes());
                                        }
                                    } else {
                                        //endpoint: GET /tasks/subtask/epic/?id=
                                        httpExchange.sendResponseHeaders(200, 0);
                                        try (OutputStream os = httpExchange.getResponseBody()) {
                                            int epicIdForSubtaskList = getIdFromQuery(query);
                                            List<SubTask> listOfSubtasksForEpicTask =
                                                    kanban.getSubListOfEpic(epicIdForSubtaskList);
                                            os.write(gson.toJson(listOfSubtasksForEpicTask).getBytes());
                                        }
                                    }
                                }
                                break;
                            case "epic":
                                //endpoint: GET /tasks/epic/
                                httpExchange.sendResponseHeaders(200, 0);
                                if (query == null) {
                                    try (OutputStream os = httpExchange.getResponseBody()) {
                                        List<EpicTask> listOfEpicTasks = kanban.getListEpicTask();
                                        os.write(gson.toJson(listOfEpicTasks).getBytes());
                                    }
                                } else {
                                    //endpoint: GET /tasks/epic/?id=
                                    try (OutputStream os = httpExchange.getResponseBody()) {
                                        int epicId = getIdFromQuery(query);
                                        EpicTask epicTask = (EpicTask) kanban.getEpicTaskById(epicId);
                                        os.write(gson.toJson(epicTask).getBytes());
                                    }
                                }
                                break;
                            case "history":
                                //endpoint: GET /tasks/history
                                httpExchange.sendResponseHeaders(200, 0);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    List<Task> history =getHistory().getHistoryList();
                                    os.write(gson.toJson(history).getBytes());
                                }
                                break;
                        }
                        break;
                    case "DELETE":
                        switch (taskCategory) {
                            case "task":
                                //endpoint: DELETE /tasks/task/
                                if (query == null) {
                                    kanban.cleanRepository();
                                } else {
                                    //endpoint: DELETE /tasks/task/?id=
                                    int taskId = getIdFromQuery(query);
                                    kanban.removeByID(taskId);
                                }
                                httpExchange.sendResponseHeaders(204, -1);
                                httpExchange.close();
                                break;
                            case "subtask":
                                //endpoint: DELETE /tasks/subtask/
                                if (query == null) {
                                    kanban.getListSubTask().forEach(s ->
                                            kanban.removeByID(s.getId())
                                    );
                                } else {
                                    //endpoint: DELETE /tasks/subtask/?id=
                                    int subtaskId = getIdFromQuery(query);
                                    kanban.removeByID(subtaskId);
                                }
                                httpExchange.sendResponseHeaders(204, -1);
                                httpExchange.close();
                                break;
                            case "epic":
                                //endpoint: DELETE /tasks/epic/
                                if (query == null) {
                                    kanban.getListEpicTask().forEach(e ->
                                            kanban.removeByID(e.getId())
                                    );
                                } else {
                                    //endpoint: DELETE /tasks/epic/?id=
                                    int epicId = getIdFromQuery(query);
                                    kanban.removeByID(epicId);
                                }
                                httpExchange.sendResponseHeaders(204, -1);
                                httpExchange.close();
                                break;
                        }
                        break;
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        JsonElement jsonElement = JsonParser.parseString(body);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        switch (taskCategory) {
                            case "task":
                                //endpoint: POST /tasks/task/ Body: {task ...} - without id: createTask
                                Task task;
                                if (body.contains("startTime")) {
                                    task = new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                                            Status.valueOf(jsonObject.get("status").getAsString()),
                                            LocalDateTime.parse(jsonObject.get("startTime").getAsString(), DateTimeFormatter.ofPattern("dd--MM--yyyy HH:mm")),
                                            Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
                                } else {
                                    task = new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                                            Status.valueOf(jsonObject.get("status").getAsString()));
                                }
                                //Task task = gson.fromJson(body, Task.class);
                                if (!body.contains("id")) {
                                    kanban.addTask(task);
                                } else {
                                    //endpoint: POST /tasks/task/ Body: {task ...} - with id: renewTask
                                    kanban.updateTask(kanban.getTaskById(task.getId()), task);
                                }
                                httpExchange.sendResponseHeaders(201, 0);
                                httpExchange.close();
                                break;
                            case "subtask":
                                //endpoint: POST /tasks/subtask/ Body: {task ...} - without id: createSubTask
                                SubTask subTask;
                                if (body.contains("startTime")) {
                                    subTask = new SubTask(jsonObject.get("name").getAsString(),
                                            jsonObject.get("description").getAsString(),
                                            Status.valueOf(jsonObject.get("status").getAsString()),
                                            LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                                                    DateTimeFormatter.ofPattern("dd--MM--yyyy HH:mm")),
                                            Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
                                } else {
                                    subTask = new SubTask(jsonObject.get("name").getAsString(),
                                            jsonObject.get("description").getAsString(),
                                            Status.valueOf(jsonObject.get("status").getAsString()));
                                }
                                if (!body.contains("id")) {
                                    kanban.addSubTask(jsonObject.get("epicTaskId").getAsInt(), subTask);
                                } else {
                                    //endpoint: POST /tasks/subtask/ Body: {task ...} - with id: renewSubTask
                                    kanban.updateTask(kanban.getSubTaskById(subTask.getId()), subTask);
                                }
                                httpExchange.sendResponseHeaders(201, 0);
                                httpExchange.close();
                                break;
                            case "epic":
                                //endpoint: POST /tasks/subtask/ Body: {task ...} - without id: createEpicTask
                                EpicTask epicTask;
                                if (body.contains("startTime")) {
                                    epicTask = new EpicTask(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                                            Status.valueOf(jsonObject.get("status").getAsString()),
                                            LocalDateTime.parse(jsonObject.get("startTime").getAsString(), DateTimeFormatter.ofPattern("dd--MM--yyyy HH:mm")),
                                            Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
                                } else {
                                    epicTask = new EpicTask(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                                            Status.valueOf(jsonObject.get("status").getAsString()));
                                }
                                kanban.addEpicTask(epicTask);
                                httpExchange.sendResponseHeaders(201, 0);
                                httpExchange.close();
                                break;
                        }
                        break;
                }
            }
        }
    }
}
