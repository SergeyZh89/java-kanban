package Tests;

import Manager.HistoryManager;
import Manager.Managers;
import Manager.TaskManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import TypeAdapters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static Manager.Status.*;
import static java.util.Calendar.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private TaskManager manager;
    private Epic epic;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultFile();
        epic = new Epic("epic", "epic decr");
        manager.addNewEpic(epic);
    }

    @Test
    public void epicWithOutSubTasks() {
        assertEquals(NEW, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusNew() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), NEW);
        manager.addNewSubTask(sub);
        manager.addNewSubTask(sub2);
        assertEquals(NEW, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusDone() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), NEW);
        manager.updateSubTask(sub, sub.getId(), DONE);
        manager.updateSubTask(sub2, sub2.getId(), DONE);
        assertEquals(DONE, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusNewAndDone() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub);
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), NEW);
        manager.updateSubTask(sub2, sub2.getId(), DONE);
        assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusInProgress() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub);
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), IN_PROGRESS);
        manager.updateSubTask(sub2, sub2.getId(), IN_PROGRESS);
        assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSub() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .create();
        Epic epic3 = new Epic("epic1", "descr1");
        manager.addNewEpic(epic3);
        SubTask subTask1 = new SubTask("subtask2", "descr2", epic3.getId(),
                Duration.ofMinutes(29), LocalDateTime.of(2022, JUNE,
                1, 11, 30));
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subtask2", "descr2", epic3.getId(),
                Duration.ofMinutes(29), LocalDateTime.of(2022, JUNE,
                2, 11, 30));
        manager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subtask2", "descr2", epic3.getId(),
                Duration.ofMinutes(29), LocalDateTime.of(2022, JUNE,
                3, 11, 30));
        manager.addNewSubTask(subTask3);

        String serEpic = gson.toJson(epic3);
        Epic epic111 = gson.fromJson(serEpic, Epic.class);
        Assertions.assertEquals(epic3, epic111);
    }

    @Test
    public void epicWithOutSub() {
        Gson gson2 = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
        Epic epic3 = new Epic("epic1", "descr1");
        manager.addNewEpic(epic3);
        SubTask subTask1 = new SubTask("subtask2", "descr2", epic3.getId(),
                Duration.ofMinutes(29), LocalDateTime.of(2022, JUNE,
                1, 11, 30));
        manager.addNewSubTask(subTask1);
        Gson gson = new Gson();
        String serEpic = gson2.toJson(epic3);
        Epic epic111 = gson2.fromJson(serEpic, Epic.class);
        Assertions.assertEquals(epic3, epic111);
        System.out.println(serEpic);
    }

}