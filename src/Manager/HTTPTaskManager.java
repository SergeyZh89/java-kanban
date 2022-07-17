package Manager;

import Client.KVTaskClient;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import TypeAdapters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedTasksManager {

   static KVTaskClient client;
   static URI url;
    static String key;

    public KVTaskClient getClient() {
        return client;
    }

    public HTTPTaskManager(URI url) {
        client = new KVTaskClient(url);
        this.key = client.getKey();
        this.url = url;
    }

    @Override
    public void save() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .create();
        String json = gson.toJson(this);
        try {
            client.put(key, json);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static HTTPTaskManager loadFromFile() throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .create();
        return gson.fromJson(client.load(key), HTTPTaskManager.class);
    }
}
