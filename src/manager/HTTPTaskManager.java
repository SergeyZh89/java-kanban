package manager;

import client.KVTaskClient;
import model.Epic;
import model.SubTask;
import model.Task;
import service.HTTPhelper;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager {

    private static KVTaskClient kvTaskClient;
    private URI url;

    public HTTPTaskManager(URI url) {
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        String tasks = HTTPhelper.GSON.toJson(this.tasks);
        String epics = HTTPhelper.GSON.toJson(this.epics);
        String subTasks = HTTPhelper.GSON.toJson(this.subTasks);
        String history = HTTPhelper.GSON.toJson(this.getHistory());

        kvTaskClient.put("tasks/task", tasks);
        kvTaskClient.put("tasks/epic", epics);
        kvTaskClient.put("tasks/subTasks", subTasks);
        kvTaskClient.put("history", history);
    }


    public static HTTPTaskManager load() throws IOException {
        HTTPTaskManager manager = new HTTPTaskManager(URI.create("http://localhost:8078"));
        String task = kvTaskClient.load("tasks/task");
        String epic = kvTaskClient.load("tasks/epic");
        String subTask = kvTaskClient.load("tasks/subTasks");
        String history = kvTaskClient.load("history");
        manager.tasks = HTTPhelper.GSON.fromJson(task, new TypeToken<HashMap<Integer, Task>>() {
        }.getType());
        manager.epics = HTTPhelper.GSON.fromJson(epic, new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());
        manager.subTasks = HTTPhelper.GSON.fromJson(subTask, new TypeToken<HashMap<Integer, SubTask>>() {
        }.getType());
        List<Task> list = HTTPhelper.GSON.fromJson(history, new TypeToken<List<Task>>(){}.getType());
        for (Task task1 : list) {
            manager.getTask(task1.getId());
        }
        return manager;
    }

}