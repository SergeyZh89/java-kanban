package Server;

import Manager.FileBackedTasksManager;
import Manager.HTTPTaskManager;
import Manager.HistoryManager;
import Manager.Managers;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import TypeAdapters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private HttpServer server;
    private Gson gson;
    private HTTPTaskManager httpTaskManager;

    public void start() throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .create();
        httpTaskManager = Managers.getDefault(URI.create("http://localhost:8078/register"));
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/", new TaskHandler());
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/task/?id=", new TaskHandler());
        server.createContext("/tasks/subtask", new TaskHandler());
        server.createContext("/tasks/subtask/?id=", new TaskHandler());
        server.createContext("/tasks/subtask/epic?id=", new TaskHandler());
        server.createContext("/tasks/epic", new TaskHandler());
        server.createContext("/tasks/epic/?id=", new TaskHandler());
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (method) {
                case "GET":
                    if (path.endsWith("/tasks/task")) {
                        exchange.sendResponseHeaders(200, 0);
                        String jsonTask = gson.toJson(httpTaskManager.getTasks());
                        os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/task/")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        String jsonTask = gson.toJson( httpTaskManager.getTask(Integer.parseInt(query)));
                        os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/subTask")) {
                        exchange.sendResponseHeaders(200, 0);
                        String jsonSubTask = gson.toJson(httpTaskManager.getSubtasks());
                        os.write(jsonSubTask.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/subTask/")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        String jsonSubTask = gson.toJson( httpTaskManager.getSubTask(Integer.parseInt(query)));
                        os.write(jsonSubTask.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/subTask/epic")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        String json = gson.toJson( httpTaskManager.getEpic(Integer.parseInt(query)));
                        os.write(json.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/epic")) {
                        exchange.sendResponseHeaders(200, 0);
                        String tasksGson = gson.toJson(httpTaskManager.getEpics());
                        os.write(tasksGson.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/epic/")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        String json = gson.toJson( httpTaskManager.getEpic(Integer.parseInt(query)));
                        os.write(json.getBytes(DEFAULT_CHARSET));
                    } else if (path.endsWith("/tasks/history")) {
                        exchange.sendResponseHeaders(200, 0);
                        String historyJson = gson.toJson(httpTaskManager.getHistory());
                        os.write(historyJson.getBytes(DEFAULT_CHARSET));
                    }
                    break;
                case "POST":
                    if (path.endsWith("/tasks/task/")) {
                        exchange.sendResponseHeaders(201, 0);
                        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(body, Task.class);
                        if (httpTaskManager.getTasks().size() != 0 && task.getId() != 0) {
                            for (Task task1 : httpTaskManager.getTasks()) {
                                if (task1.getId() == task.getId()) {
                                    task1.setStatus(task.getStatus());
                                }
                            }
                        } else {
                            httpTaskManager.addNewTask(task);
                        }
                    } else if (path.endsWith("/tasks/subTask/")) {
                        exchange.sendResponseHeaders(201, 0);
                        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        if (httpTaskManager.getSubtasks().size() != 0 && subTask.getId() != 0) {
                            for (SubTask subTask1 : httpTaskManager.getSubtasks()) {
                                if (subTask1.getId() == subTask.getId()) {
                                    subTask1.setStatus(subTask.getStatus());
                                }
                            }
                        } else {
                            httpTaskManager.addNewSubTask(subTask);
                        }

                    } else if (path.endsWith("/tasks/epic/")) {
                        exchange.sendResponseHeaders(201, 0);
                        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (httpTaskManager.getEpics().size() != 0 && epic.getId() != 0) {
                            for (Epic epic1 : httpTaskManager.getEpics()) {
                                if (epic1.getId() == epic.getId()) {
                                    epic1.setStatus(epic.getStatus());
                                }
                            }
                        } else {
                            httpTaskManager.addNewEpic(epic);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/tasks/task/")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        httpTaskManager.deleteTaskById(Integer.parseInt(query));
                    } else if (path.endsWith("/tasks/task")) {
                        exchange.sendResponseHeaders(200, 0);
                        httpTaskManager.deleteAllTasks();
                    } else if (path.endsWith("/tasks/subTask/")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        httpTaskManager.deleteSubTaskById(Integer.parseInt(query));
                    } else if (path.endsWith("/tasks/subTask")) {
                        exchange.sendResponseHeaders(200, 0);
                        httpTaskManager.deleteAllSubTasks();
                    } else if (path.endsWith("/tasks/epic/")) {
                        exchange.sendResponseHeaders(200, 0);
                        String query = exchange.getRequestURI().getQuery().split("=")[1];
                        httpTaskManager.deleteEpicById(Integer.parseInt(query));
                    } else if (path.endsWith("/tasks/epic")) {
                        exchange.sendResponseHeaders(200, 0);
                        httpTaskManager.deleteAllEpics();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + method);
            }
            os.close();
            is.close();
        }
    }
}
