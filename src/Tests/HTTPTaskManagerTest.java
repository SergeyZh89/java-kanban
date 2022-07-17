package Tests;

import Manager.HTTPTaskManager;
import Manager.Managers;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import Server.HttpTaskServer;
import Server.KVServer;
import TypeAdapters.EpicAdapter;
import TypeAdapters.LocalDateAdapter;
import TypeAdapters.SubTaskAdapter;
import TypeAdapters.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskManagerTest {
    private static URI url = URI.create("http://localhost:8078/register");
    private static HTTPTaskManager manager;
    private static HttpTaskServer serverTS;
    private static Gson gson;
    private static HttpClient client;
    private static KVServer serverKV;


    @BeforeEach
     public void beforeEach() throws IOException {
        System.out.println("start test");
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
                .create();
    }

    @AfterEach
    public void afterEach() {
        serverKV.stop();
        serverTS.stop();
    }



    @Test
    public void createTask() throws IOException, InterruptedException {
        System.out.println("first test");
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task task = new Task("task", "decr", Duration.ofMinutes(30), LocalDateTime.of(2000, 2, 1, 10, 10));
        String jsonTask = gson.toJson(task);
        HttpRequest.BodyPublisher bodyTask = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(bodyTask).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        Assertions.assertEquals(201, code);
        Assertions.assertNotNull(manager.getTasks().size());
        HTTPTaskManager manager2 = HTTPTaskManager.loadFromFile();
        Assertions.assertNotNull(manager2.getTasks().size());
        Assertions.assertEquals(task,manager2.getTask(1));
    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        System.out.println("double test");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        URI urlSubTask = URI.create("http://localhost:8080/tasks/subTask/");
        Epic epic = new Epic("epic", "decr");
        manager.addNewEpic(epic);
        String jsonEpic = gson.toJson(epic);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        int code = responseEpic.statusCode();
        Assertions.assertEquals(201, code);
        Assertions.assertNotNull(manager.getEpics().size());
        HTTPTaskManager manager2 = HTTPTaskManager.loadFromFile();
        Assertions.assertNotNull(manager2.getEpics().size());

    }

    @Test
    public void createSubTask() throws IOException, InterruptedException {
        URI urlSubTask = URI.create("http://localhost:8080/tasks/subTask/");
        SubTask subTask = new SubTask("subTask", "decr", 1);
        manager.addNewSubTask(subTask);
        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest requestSubTask = HttpRequest.newBuilder().uri(urlSubTask).POST(bodySubTask).build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());
        int code = responseSubTask.statusCode();
        Assertions.assertEquals(201, code);
        Assertions.assertNotNull(manager.getSubtasks().size());
        HTTPTaskManager manager2 = HTTPTaskManager.loadFromFile();
        Assertions.assertNotNull(manager2.getSubtasks().size());
    }
}

