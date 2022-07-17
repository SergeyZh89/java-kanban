package Tests;

import Manager.HTTPTaskManager;
import Manager.HistoryManager;
import Manager.Managers;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import Server.HttpTaskServer;
import Server.KVServer;
import TypeAdapters.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManagerTest {

    private URI url = URI.create("http://localhost:8078/register");
    private HTTPTaskManager manager;
    private HTTPTaskManager managerFromFile;
    private HttpTaskServer serverTS;
    private Gson gson;
    private Gson gson1;
    private HttpClient client;
    private KVServer serverKV;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        serverKV = new KVServer();
        serverKV.start();
        serverTS = new HttpTaskServer();
        serverTS.start();
        manager = Managers.getDefault(url);
        client = manager.getClient().getClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .create();
    }

    @AfterEach
    public void afterEach() {
        serverKV.stop();
        serverTS.stop();
    }

    @Test
    public void allTestsTask() throws IOException, InterruptedException {
        // Создание таски
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task task = new Task("task", "decr", Duration.ofMinutes(30),
                LocalDateTime.of(2000, 2, 1, 10, 10));
        task.setId(1);
        String jsonTask = gson.toJson(task);
        HttpRequest.BodyPublisher bodyTask = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(bodyTask)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(201, code);
        Assertions.assertEquals(1, managerFromFile.getTasks().size());
        Assertions.assertEquals(task, managerFromFile.getTask(1));
        // Получение по id
        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response2.body(), Task.class);
        Assertions.assertEquals(task, task1);
        // Запрос истории
        URI url5 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request5 = HttpRequest.newBuilder()
                .uri(url5)
                .GET()
                .build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        Type taskHistory = new TypeToken<List<Task>>(){}.getType();
        List<Task> list = gson.fromJson(response5.body(), taskHistory);
        Assertions.assertEquals(managerFromFile.getHistory(), list);
        // Получение всех задач
        URI url3 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url3)
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>(){}.getType();
        List<Task> list2 = gson.fromJson(response3.body(), taskType);
        Assertions.assertEquals(managerFromFile.getTasks(), list2);
        // Удаление задачи
        URI url4 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url4)
                .DELETE()
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        int codeDelete = response4.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(200, codeDelete);
        Assertions.assertEquals(0, managerFromFile.getTasks().size());
        // Создание задачи
        URI url6 = URI.create("http://localhost:8080/tasks/task/");
        Task task2 = new Task("task2", "decr", Duration.ofMinutes(30),
                LocalDateTime.of(2000, 2, 1, 10, 10));
        task2.setId(2);
        String jsonTask2 = gson.toJson(task2);
        HttpRequest.BodyPublisher bodyTask2 = HttpRequest.BodyPublishers.ofString(jsonTask2);
        HttpRequest request6 = HttpRequest.newBuilder()
                .uri(url6)
                .POST(bodyTask2)
                .build();
        client.send(request6, HttpResponse.BodyHandlers.ofString());
        // Удаление всех задач
        URI url7 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request7 = HttpRequest.newBuilder()
                .uri(url7)
                .DELETE()
                .build();
        HttpResponse<String> response7 = client.send(request7, HttpResponse.BodyHandlers.ofString());
        int codeDeleteAll = response7.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(200, codeDeleteAll);
        Assertions.assertEquals(0, managerFromFile.getTasks().size());
    }

    @Test
    public void allTestsEpic() throws IOException, InterruptedException {
        // Создание эпика
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic = new Epic("epic", "decr");
        epic.setId(1);
        String jsonEpic = gson.toJson(epic);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(urlEpic)
                .POST(bodyEpic)
                .build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        int code = responseEpic.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(201, code);
        Assertions.assertEquals(1, managerFromFile.getEpics().size());
        Assertions.assertEquals(epic, managerFromFile.getEpic(1));
        // Получение эпика
        URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest requestEpic2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(requestEpic2, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response2.body(), Epic.class);
        Assertions.assertEquals(epic, epic1);
//         Запрос истории
        URI url3 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url3)
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        List<Task> list = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(response3.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
                JsonObject object = element.getAsJsonObject();
                Epic e = gson.fromJson(object, Epic.class);
                list.add(e);
            }
        Assertions.assertEquals(managerFromFile.getHistory(), list);
        // Получение всех задач
        URI url4 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url4)
                .GET()
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        List<Task> list2 = new ArrayList<>();
        JsonElement jsonElement2 = JsonParser.parseString(response4.body());
        JsonArray jsonArray2 = jsonElement2.getAsJsonArray();
        for (JsonElement element : jsonArray2) {
            JsonObject object = element.getAsJsonObject();
            Epic e = gson.fromJson(object, Epic.class);
            list2.add(e);
        }
        Assertions.assertEquals(managerFromFile.getEpics(), list2);
        // Удаление задачи
        URI url5 = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request5 = HttpRequest.newBuilder()
                .uri(url5)
                .DELETE()
                .build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        int codeDelete = response5.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(200, codeDelete);
        Assertions.assertEquals(0, managerFromFile.getEpics().size());
        // Создание задачи
        URI url6 = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic2 = new Epic("epic2", "decr");
        epic2.setId(2);
        String jsonEpic2 = gson.toJson(epic2);
        HttpRequest.BodyPublisher bodyEpic2 = HttpRequest.BodyPublishers.ofString(jsonEpic2);
        HttpRequest request6 = HttpRequest.newBuilder()
                .uri(url6)
                .POST(bodyEpic2)
                .build();
        client.send(request6, HttpResponse.BodyHandlers.ofString());
        // Удаление всех задач
        URI url7 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request7 = HttpRequest.newBuilder()
                .uri(url7)
                .DELETE()
                .build();
        HttpResponse<String> response7 = client.send(request7, HttpResponse.BodyHandlers.ofString());
        int codeDeleteAll = response7.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(200, codeDeleteAll);
        Assertions.assertEquals(0, managerFromFile.getEpics().size());
    }

    @Test
    public void createSubTask() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic = new Epic("epic", "decr");
        epic.setId(1);
        String jsonEpic = gson.toJson(epic);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(urlEpic)
                .POST(bodyEpic)
                .build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        URI urlSubTask = URI.create("http://localhost:8080/tasks/subTask/");
        SubTask subTask = new SubTask("subTask", "decr", 1,
                Duration.ofMinutes(20),
                LocalDateTime.of(2000, 2, 2, 20, 22));
        subTask.setId(2);
        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest requestSubTask = HttpRequest.newBuilder()
                .uri(urlSubTask)
                .POST(bodySubTask)
                .build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());
        int code = responseSubTask.statusCode();
        managerFromFile = HTTPTaskManager.loadFromFile();
        Assertions.assertEquals(201, code);
        Assertions.assertEquals(1, managerFromFile.getSubtasks().size());
    }
}

