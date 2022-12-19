package service.taskManager;

import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.Gson;
import model.Status;
import model.SubTask;
import model.Task;
import model.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.Type.*;

public class HttpTaskServerTest {
    HttpClient client;
    KVServer repositoryServer;
    HttpTaskServer taskServer;

    HttpTaskManager httpKanban;


    @BeforeEach
    public void beforeEach() throws IOException {
        repositoryServer = new KVServer();
        repositoryServer.start();
        httpKanban = new HttpTaskManager("http://localhost:8078");
        taskServer = new HttpTaskServer(httpKanban);
        taskServer.start();
        client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        sendPostRequest(TASK, false, true);
        sendPostRequest(TASK, false, false);
        sendPostRequest(EPIC, false, true);
        sendPostRequest(EPIC, false, false);
        sendPostRequest(SUBTASK, false, true);
        sendPostRequest(SUBTASK, false, false);
        sendGetRequest(TASK, false, true, 1, false);
        sendGetRequest(EPIC, false, true, 3, false);
        sendGetRequest(SUBTASK, false, true, 5, false);
    }

    @AfterEach
    public void afterEach() {
        httpKanban.cleanRepository();
        repositoryServer.stop();
        taskServer.stop();
    }

    @Test
    void endpointCreateTask() {
        Assertions.assertEquals(2, httpKanban.getListTask().size());
    }

    //endpoint: POST /tasks/subtask/ Body: {task ...} - without id: createSubTask
    @Test
    void endpointCreateSubtask() {
        Assertions.assertEquals(2, httpKanban.getListSubTask().size());
    }

    //endpoint: POST /tasks/subtask/ Body: {task ...} - without id: createEpicTask
    @Test
    void endpointCreateEpicTask() {
        Assertions.assertEquals(2, httpKanban.getListEpicTask().size());
    }

    //endpoint: GET /tasks
    @Test
    void endpointGetPrioritizedTasks() {
        HttpResponse<String> response = sendGetRequest(null, true, false,
                0, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(5, httpKanban.getPrioritizedTasks().size());
    }

    //endpoint: GET /tasks/task/
    @Test
    void endpointGetAllTasks() {
        HttpResponse<String> response = sendGetRequest(Type.TASK, false, false,
                0, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, httpKanban.getListTask().size());
    }

    //endpoint: GET /tasks/task/?id=
    @Test
    void endpointGetParticularTask() {
        HttpResponse<String> response = sendGetRequest(Type.TASK, false, true,
                1, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, httpKanban.getTaskById(1).getId());
    }

    //endpoint: GET /tasks/subtask/
    @Test
    void endpointGetAllSubtasks() {
        HttpResponse<String> response = sendGetRequest(Type.SUBTASK, false, false,
                0, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, httpKanban.getListSubTask().size());
    }

    //endpoint: GET /tasks/subtask/?id=
    @Test
    void endpointGetParticularSubtask() {
        HttpResponse<String> response = sendGetRequest(Type.SUBTASK, false, true,
                5, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, httpKanban.getListSubTask().size());
    }

    //endpoint: GET /tasks/subtask/epic/?id=
    @Test
    void endpointGetAllSubtasksForParticularEpic() {
        HttpResponse<String> response = sendGetRequest(Type.SUBTASK, false, false,
                3, true);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, httpKanban.getSubListOfEpic(3).size());
    }

    //endpoint: GET /tasks/epic/
    @Test
    void endpointGetAllEpics() {
        HttpResponse<String> response = sendGetRequest(Type.EPIC, false, false,
                0, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, httpKanban.getListEpicTask().size());
    }

    //endpoint: GET /tasks/epic/?id=
    @Test
    void endpointGetParticularEpic() {
        HttpResponse<String> response = sendGetRequest(Type.EPIC, false, true,
                3, false);
        assert response != null;
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, httpKanban.getEpicTaskById(3).getId());
    }

    //endpoint: GET /tasks/history
    @Test
    void endpointGetHistory() {
        Assertions.assertEquals(3, httpKanban.managerHistory.getHistoryList().size());
    }

    //endpoint: DELETE /tasks/task/
    @Test
    void endpointClearAllTasks() {
        HttpResponse<String> response = sendDeleteRequest(Type.TASK, false, 0);
        assert response != null;
        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals(0, httpKanban.getListTask().size());
    }

    //endpoint: DELETE /tasks/task/?id=
    @Test
    void endpointRemoveParticularTask() {
        HttpResponse<String> response = sendDeleteRequest(Type.TASK, true, 1);
        assert response != null;
        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals(1, httpKanban.getListTask().size());
    }

    //endpoint: DELETE /tasks/subtask/?id=
    @Test
    void endpointRemoveParticularSubtask() {
        HttpResponse<String> response = sendDeleteRequest(Type.SUBTASK, true, 5);
        assert response != null;
        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals(1, httpKanban.getListSubTask().size());
    }

    //endpoint: DELETE /tasks/epic/
    @Test
    void endpointClearAllEpics() {
        HttpResponse<String> response = sendDeleteRequest(Type.EPIC, false, 0);
        assert response != null;
        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals(0, httpKanban.getListEpicTask().size());
    }

    //endpoint: DELETE /tasks/epic/?id=
    @Test
    void endpointRemoveParticularEpic() {
        HttpResponse<String> response = sendDeleteRequest(Type.EPIC, true, 3);
        assert response != null;
        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals(1, httpKanban.getListEpicTask().size());
    }

    private HttpResponse<String> sendPostRequest(Type type, boolean toBeRenewed, boolean isFirst) {
        String typeForUrl = type.toString().toLowerCase();
        URI url = URI.create("http://localhost:8082/tasks/" + typeForUrl);
        String jsonTask = getJsonPostRequestBody(type, toBeRenewed, isFirst);
        HttpRequest request = null;
        if (jsonTask != null) {
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                    .build();
        }
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private HttpResponse<String> sendGetRequest(Type type, boolean toGetPrioritizedList,
                                                boolean toGetParticularTask, int id, boolean toGetSubtasksForEpic) {
        URI url;
        if (toGetPrioritizedList) {
            url = URI.create("http://localhost:8082/tasks");
        } else {
            String typeForUrl = type.toString().toLowerCase();
            if (!toGetParticularTask) {
                url = URI.create(String.format("http://localhost:8082/tasks/%s", typeForUrl));
            } else {
                url = URI.create(String.format("http://localhost:8082/tasks/%s/?id=%d", typeForUrl, id));
            }
            if (toGetSubtasksForEpic) {
                url = URI.create(String.format("http://localhost:8082/tasks/subtask/epic/?id=%d", id));
            }
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-type", "application/json")
                .GET()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private HttpResponse<String> sendDeleteRequest(Type type, boolean deleteParticularTask, int id) {
        URI url;
        String typeForUrl = type.toString().toLowerCase();
        if (!deleteParticularTask) {
            url = URI.create(String.format("http://localhost:8082/tasks/%s", typeForUrl));
        } else {
            url = URI.create(String.format("http://localhost:8082/tasks/%s?id=%d", typeForUrl, id));
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-type", "application/json")
                .DELETE()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //NOTE: Request bodies were got from Insomnia
    private String getJsonPostRequestBody(Type type, boolean toBeRenewed, boolean isFirst) {
        if (!toBeRenewed) {
            switch (type) {
                case TASK:
                    if (isFirst) {
                        return "{\n" +
                                "\t\"name\": \"T1\",\n" +
                                "\t\"description\": \"TT1\",\n" +
                                "\t\"status\": \"NEW\",\n" +
                                "\t\"startTime\": \"04--11--2022 18:37\",\n" +
                                "\t\"duration\": 120\n" +
                                "}";
                    } else {
                        return "{\n" +
                                "\t\"name\": \"T2\",\n" +
                                "\t\"description\": \"TT2\",\n" +
                                "\t\"status\": \"NEW\",\n" +
                                "\t\"startTime\": \"04--12--2022 18:37\",\n" +
                                "\t\"duration\": 120\n" +
                                "}";
                    }
                case SUBTASK:
                    if (isFirst) {
                        return "{\n" +
                                "\t\"name\": \"S1\",\n" +
                                "\t\"description\": \"SS1\",\n" +
                                "\t\"status\": \"NEW\",\n" +
                                "\t\"epicTaskId\": 3,\n" +
                                "\t\"startTime\": \"05--11--2022 18:37\",\n" +
                                "\t\"duration\": 120\n" +
                                "}";
                    } else {
                        return "{\n" +
                                "\t\"name\": \"S2\",\n" +
                                "\t\"description\": \"SS2\",\n" +
                                "\t\"status\": \"NEW\",\n" +
                                "\t\"epicTaskId\": 4,\n" +
                                "\t\"startTime\": \"03--12--2022 18:37\",\n" +
                                "\t\"duration\": 120\n" +
                                "}";
                    }
                case EPIC:
                    if (isFirst) {
                        return "{\n" +
                                "\t\"name\": \"E1\",\n" +
                                "\t\"description\": \"EE1\",\n" +
                                "\t\"status\": \"NEW\"\n" +
                                "}";
                    } else {
                        return "{\n" +
                                "\t\"name\": \"E2\",\n" +
                                "\t\"description\": \"EE2\",\n" +
                                "\t\"status\": \"NEW\"\n" +
                                "}";
                    }
            }
        } else {
            switch (type) {
                case TASK:
                    return new Gson().toJson(new Task("Режим салатики ^_^",
                            "Оливье и селедка под шубой", Status.NEW,
                            LocalDateTime.of(2022, 12, 31, 18, 0),
                            Duration.ofMinutes(45)));
                case SUBTASK:
                    return new Gson().toJson(new SubTask("O_o", ":)", Status.NEW,
                            LocalDateTime.of(2022, 12, 13, 18, 0),
                            Duration.ofMinutes(30), 2));
            }
        }
        return null;
    }
}
