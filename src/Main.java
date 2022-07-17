import Client.KVTaskClient;
import Manager.HTTPTaskManager;
import Manager.HistoryManager;
import Manager.Managers;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import Server.KVServer;
import TypeAdapters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    static URI url = URI.create("http://localhost:8078/register");
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
            .create();

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            new KVServer().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        HTTPTaskManager manager = Managers.getDefault(url);
        Task task1 = new Task("task1", "decr");
        Task task2 = new Task("task2", "decr", Duration.ofMinutes(30), LocalDateTime.of(2022, 11, 1, 11, 00));
//
//        Epic epic3 = new Epic("epic1", "decr");
//        SubTask subTask4 = new SubTask("subTask1", "decr", epic3.getId(), Duration.ofMinutes(30), LocalDateTime.of(2022, 5, 7, 10, 11));
//        manager.addNewTask(task1);
//        manager.addNewTask(task2);
//        manager.addNewEpic(epic3);
//        manager.addNewSubTask(subTask4, epic3.getId());
//        manager.getTask(1);
//        manager.getTask(2);
//        manager.getEpic(3);
//        manager.getSubTask(4);
//        System.out.println("HISTORY " + manager.getHistory());
//
//        System.out.println(manager.getSubtasks());
//        HTTPTaskManager w = manager.loadFromFile();
//        System.out.println("_________________________________________");
//        System.out.println(w.getTasks());
//        System.out.println(w.getEpics());
//        System.out.println(w.getSubtasks());
//        System.out.println(manager.getPrioritizedTasks());
        KVTaskClient client = new KVTaskClient(url);

        try {
            client.put("123",gson.toJson(task1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(client.load("123"));

        try {
            client.put("123",gson.toJson(task2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(client.load("123"));
    }
}