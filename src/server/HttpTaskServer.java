package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.HTTPTaskManager;
import manager.Managers;
import model.Epic;
import model.SubTask;
import model.Task;
import typeAdapters.DurationAdapter;
import typeAdapters.LocalDateAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private HTTPTaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault(URI.create("http://localhost:8078"));
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter().nullSafe())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter().nullSafe())
                .create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/app", this::handler);
    }

    public void start() {
        System.out.println("Server started on port " + PORT);
        System.out.println("http://localhost:" + PORT + "/app");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped on port " + PORT);
    }

    private void handler(HttpExchange h) throws IOException {
        System.out.println("\n/tasks: " + h.getRequestURI());
        final String path = h.getRequestURI().getPath().substring(5);
        switch (path) {
            case "task" -> {
                switch (h.getRequestMethod()) {
                    case "GET" -> {
                        final String query = h.getRequestURI().getQuery();
                        if (query == null) {
                            final List<Task> tasks = taskManager.getTasks();
                            final String response = gson.toJson(tasks, new TypeToken<List<Task>>() {
                            }.getType());
                            sendText(h, response);
                            return;
                        }
                        String idParam = query.substring(3);// ?id=
                        final int id = Integer.parseInt(idParam);
                        final Task task = taskManager.getTask(id);
                        final String response = gson.toJson(task);
                        sendText(h, response);
                    }
                    case "POST" -> {
                        String body = readText(h);
                        if (body.isBlank()) {
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        final Task task = gson.fromJson(body, Task.class);
                        if (taskManager.getTasks().size() != 0 && task.getId() != 0) {
                            for (Task task1 : taskManager.getTasks()) {
                                if (task1.getId() == task.getId()) {
                                    task1.setStatus(task.getStatus());
                                }
                            }
                        } else {
                            h.sendResponseHeaders(200, 0);
                            taskManager.addNewTask(task);
                            h.close();
                        }
                    }
                    case "DELETE" -> {
                        final String query = h.getRequestURI().getQuery();
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        taskManager.deleteTaskById(id);
                        h.sendResponseHeaders(200, 0);
                    }
                    default -> {
                        h.sendResponseHeaders(404, 0);
                    }
                }
            }
            case "epic" -> {
                switch (h.getRequestMethod()) {
                    case "GET" -> {
                        final String query = h.getRequestURI().getQuery();
                        if (query == null) {
                            final List<Epic> epics = taskManager.getEpics();
                            final String response = gson.toJson(epics, new TypeToken<List<Epic>>() {
                            }.getType());
                            sendText(h, response);
                            return;
                        }
                        String idParam = query.substring(3);// ?id=
                        final int id = Integer.parseInt(idParam);
                        final Epic epic = taskManager.getEpic(id);
                        final String response = gson.toJson(epic);
                        sendText(h, response);
                    }
                    case "POST" -> {
                        String body = readText(h);
                        if (body.isBlank()) {
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        h.sendResponseHeaders(200, 0);
                        final Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.addNewEpic(epic);
                    }
                    case "DELETE" -> {
                        final String query = h.getRequestURI().getQuery();
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        h.sendResponseHeaders(200, 0);
                        taskManager.deleteEpicById(id);
                    }
                    default -> {
                        h.sendResponseHeaders(404, 0);
                    }
                }
            }
            case "subTask" -> {
                switch (h.getRequestMethod()) {
                    case "GET" -> {
                        final String query = h.getRequestURI().getQuery();
                        if (query == null) {
                            final List<SubTask> subTasks = taskManager.getSubtasks();
                            final String response = gson.toJson(subTasks, new TypeToken<List<SubTask>>() {
                            }.getType());
                            sendText(h, response);
                            return;
                        }
                        String idParam = query.substring(3);// ?id=
                        final int id = Integer.parseInt(idParam);
                        final SubTask subTask = taskManager.getSubTask(id);
                        final String response = gson.toJson(subTask);
                        sendText(h, response);
                    }
                    case "POST" -> {
                        String body = readText(h);
                        if (body.isBlank()) {
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        final SubTask subTask = gson.fromJson(body, SubTask.class);
                        if (taskManager.getSubtasks().size() != 0 && subTask.getId() != 0) {
                            for (SubTask subTask1 : taskManager.getSubtasks()) {
                                if (subTask1.getId() == subTask.getId()) {
                                    subTask1.setStatus(subTask.getStatus());
                                }
                            }
                        } else {
                            h.sendResponseHeaders(200, 0);
                            taskManager.addNewSubTask(subTask);
                        }
                    }
                    case "DELETE" -> {
                        final String query = h.getRequestURI().getQuery();
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        taskManager.deleteSubTaskById(id);
                        h.sendResponseHeaders(200, 0);
                    }
                    default -> {
                        h.sendResponseHeaders(404, 0);
                    }
                }
            }
            default -> {
                System.out.println("Неизвестный зарос: " + h.getRequestURI());
                h.sendResponseHeaders(404, 0);
            }
        }
        h.close();
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}