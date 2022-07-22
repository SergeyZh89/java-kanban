package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import typeAdapters.DurationAdapter;
import typeAdapters.LocalDateAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter().nullSafe())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter().nullSafe())
            .create();

    @BeforeEach
    void startServer() throws IOException {
        this.kvServer = new KVServer();
        this.kvServer.start();
        this.httpTaskServer = new HttpTaskServer();
        this.httpTaskServer.start();
        this.client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stopServers() {
        this.kvServer.stop();
        this.httpTaskServer.stop();
    }


    @Test
    void allActionWithTask() throws IOException, InterruptedException {
        // Task create
        URI url = URI.create("http://localhost:8080/app/task");
        Task task = new Task("task", "decr", Duration.ofMinutes(20), LocalDateTime.of(2000, 12, 12, 12, 12));
        task.setId(1);
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        assertEquals(200, code, "Задача не создана");
        // Get Task id=1
        url = URI.create("http://localhost:8080/app/task?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);
        assertEquals(task, task1, "Задачи не совпадают");
        // Delete Task id=1
        url = URI.create("http://localhost:8080/app/task?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        code = response.statusCode();
        assertEquals(200, code, "Задача не удалена");
        // Get AllTasks
        url = URI.create("http://localhost:8080/app/task");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        code = response.statusCode();
        List<Task> list = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(200, code, "Список не получен");
        assertEquals(0, list.size(), "Задачи не совпадают");
    }

    @Test
    void allActionWithEpic() throws IOException, InterruptedException {
        // Epic create
        URI url = URI.create("http://localhost:8080/app/epic");
        Epic epic = new Epic("epic", "decr");
        epic.setId(1);
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        assertEquals(200, code, "Задача не создана");
        // Get Epic id=1
        url = URI.create("http://localhost:8080/app/epic?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic, epic1, "Задачи не совпадают");
        // Delete Epic id=1
        url = URI.create("http://localhost:8080/app/epic?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        code = response.statusCode();
        assertEquals(200, code, "Задача не удалена");
        // Get AllEpics
        url = URI.create("http://localhost:8080/app/epic");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        code = response.statusCode();
        List<Task> list = gson.fromJson(response.body(), new TypeToken<List<Epic>>(){}.getType());
        assertEquals(200, code, "Список не получен");
        assertEquals(0, list.size(), "Задачи не совпадают");
    }

    @Test
    void allActionWithSubtasks() throws IOException, InterruptedException {
        // Epic create
        URI url = URI.create("http://localhost:8080/app/epic");
        Epic epic = new Epic("epic", "decr");
        epic.setId(1);
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // SubTask create
        url = URI.create("http://localhost:8080/app/subTask?id=1");
        SubTask subTask = new SubTask("subTask", "decr", epic.getId());
        subTask.setId(2);
        json = gson.toJson(subTask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        assertEquals(200, code, "Задача не создана");
        // Get SubTask id=2
        url = URI.create("http://localhost:8080/app/subTask?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask, subTask1, "Задачи не совпадают");
        // Delete SubTask id=2
        url = URI.create("http://localhost:8080/app/subTask?id=2");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        code = response.statusCode();
        assertEquals(200, code, "Задача не удалена");
        // Get AllSubTasks
        url = URI.create("http://localhost:8080/app/subTask");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        code = response.statusCode();
        List<Task> list = gson.fromJson(response.body(), new TypeToken<List<SubTask>>(){}.getType());
        assertEquals(200, code, "Список не получен");
        assertEquals(0, list.size(), "Задачи не совпадают");
    }
}
