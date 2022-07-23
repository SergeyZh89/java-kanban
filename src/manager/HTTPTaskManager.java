package manager;

import client.KVTaskClient;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import service.HTTPhelper;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager {
    private static KVTaskClient kvTaskClient;
    private URI url;

    public HTTPTaskManager(URI url) {
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        kvTaskClient.put("task", HTTPhelper.GSON.toJson(this.tasks));
        kvTaskClient.put("epic", HTTPhelper.GSON.toJson(this.epics));
        kvTaskClient.put("subTasks", HTTPhelper.GSON.toJson(this.subTasks));
        kvTaskClient.put("history", HTTPhelper.GSON.toJson(this.getHistory()));
        kvTaskClient.put("priority", HTTPhelper.GSON.toJson(this.priorityTasks));
        kvTaskClient.put("notPriority", HTTPhelper.GSON.toJson(this.notPriorityTasks));
    }

    public static HTTPTaskManager load() {
        HTTPTaskManager manager = new HTTPTaskManager(URI.create("http://localhost:8078"));
        manager.tasks = HTTPhelper.GSON.fromJson(kvTaskClient.load("task"),
                new TypeToken<HashMap<Integer, Task>>() {
        }.getType());
        manager.epics = HTTPhelper.GSON.fromJson(kvTaskClient.load("epic"),
                new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());
        manager.subTasks = HTTPhelper.GSON.fromJson(kvTaskClient.load("subTasks"),
                new TypeToken<HashMap<Integer, SubTask>>() {
        }.getType());
        List<Task> list = HTTPhelper.GSON.fromJson(kvTaskClient.load("history"),
                new TypeToken<List<Task>>() {
        }.getType());
        for (Task task1 : list) {
            manager.getTask(task1.getId());
        }
        manager.priorityTasks = HTTPhelper.GSON.fromJson(kvTaskClient.load("priority"),
                new TypeToken<Set<Task>>() {
        }.getType());
        manager.notPriorityTasks = HTTPhelper.GSON.fromJson(kvTaskClient.load("notPriority"),
                new TypeToken<List<Task>>() {
        }.getType());
        return manager;
    }
}