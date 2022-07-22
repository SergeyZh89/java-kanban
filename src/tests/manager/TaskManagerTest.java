package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static enums.Status.*;
import static java.util.Calendar.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;

    public abstract T createTaskManager();


    private KVServer server;
    private HttpTaskServer server1;

    @BeforeEach
    public void updateTaskManager() throws IOException {
        server = new KVServer();
        server.start();
        server1 = new HttpTaskServer();
        server1.start();
        taskManager = createTaskManager();
    }

    @AfterEach
    public void closeServer() {
        server.stop();
        server1.stop();
    }

    @Test
    public void shouldBeGetHystory() {
        assertNotNull(taskManager.getHistory(), "История не возвращается.");
    }


    @Test
    public void shouldBeTestsTask() throws IOException {
        Task task = new Task("task", "task decr");
        taskManager.addNewTask(task);
        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(taskManager.getTasks(), "Задачи не возвращаются.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(task, taskManager.getTasks().get(0), "Задачи не совпадают.");
        taskManager.updateTask(task, task.getId(), DONE);
        assertEquals(DONE, taskManager.getTasks().get(0).getStatus(), "Статус не совпадает");
        taskManager.deleteTaskById(task.getId());
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена.");
        Task task2 = new Task("task2", "task decr");
        taskManager.addNewTask(task2);
        Task task3 = new Task("task3", "task decr");
        taskManager.addNewTask(task3);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasks().size(), "Задачи не удалены.");

    }

    @Test
    public void shouldBeTestsEpic() {
        Epic epic = new Epic("epic", "epic decr");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("subTask", "subTask decr", epic.getId());
        taskManager.addNewSubTask(subTask);
        SubTask subTask2 = new SubTask("subTask2", "subTask decr", epic.getId());
        taskManager.addNewSubTask(subTask2);
        assertEquals(NEW, taskManager.getEpics().get(0).getStatus(), "Статус не совпадает");
        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotNull(taskManager.getEpics(), "Задачи не возвращаются.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(epic, taskManager.getEpics().get(0), "Задачи не совпадают.");
        assertEquals(NEW, taskManager.getEpics().get(0).getStatus(), "Статус не совпадает.");
        taskManager.updateEpic(epic, epic.getId());
        epic.setStatus(IN_PROGRESS);
        taskManager.updateSubTask(subTask, subTask.getId(), IN_PROGRESS);
        assertEquals(IN_PROGRESS, taskManager.getEpics().get(0).getStatus(), "Статус не совпадает");
        taskManager.deleteEpicById(epic.getId());
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удален");
        Epic epic2 = new Epic("epic2", "epic decr");
        taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("epic3", "epic decr");
        taskManager.addNewEpic(epic3);
        SubTask subTask3 = new SubTask("subTask3", "subTask decr", epic3.getId());
        taskManager.addNewSubTask(subTask3);
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getEpics().size(), "Эпики не удалены");
    }

    @Test
    public void shouldBeTestsSubtask() {
        Epic epic = new Epic("epic", "epic decr");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("subTask", "subTask decr", epic.getId());
        taskManager.addNewSubTask(subTask);
        SubTask savedSubTask = taskManager.getSubTask(subTask.getId());
        assertNotNull(subTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");
        assertNotNull(taskManager.getSubtasks(), "Задачи не возвращаются.");
        assertEquals(epic.getId(), taskManager.getSubtasks().get(0).getEpicIds(), "Нет эпика");
        assertEquals(1, taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(subTask, taskManager.getSubtasks().get(0), "Задачи не совпадают.");
        taskManager.updateSubTask(subTask, subTask.getId(), DONE);
        assertEquals(DONE, taskManager.getSubtasks().get(0).getStatus());
        taskManager.deleteSubTaskById(subTask.getId());
        assertEquals(0, taskManager.getSubtasks().size(), "Подзадача не удалена");
        SubTask subTask2 = new SubTask("subTask2", "subTask decr", epic.getId());
        taskManager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subTask3", "subTask decr", epic.getId());
        taskManager.addNewSubTask(subTask3);
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubtasks().size(), "Подзадачи не удалены");
    }

    @Test
    public void shouldBeFileBackupManager() {
        File file = new File("tasks.csv");
        FileBackedTasksManager fbk = Managers.getDefaultFile();
        Task task = new Task("task1", "descr1", Duration.ofMinutes(30), LocalDateTime.of(2022, JUNE, 1, 10, 00));
        fbk.addNewTask(task);
        Epic epic = new Epic("epic1", "descr1");
        fbk.addNewEpic(epic);
        SubTask subTask = new SubTask("subtask2", "descr2", epic.getId(), Duration.ofMinutes(29), LocalDateTime.of(2022, JUNE, 1, 11, 30));
        fbk.addNewSubTask(subTask);
        SubTask subTask2 = new SubTask("subtask2", "descr2", epic.getId());
        fbk.addNewSubTask(subTask2);
        Epic epic2 = new Epic("epic1", "descr1");
        fbk.addNewEpic(epic2);
        Task task2 = new Task("task1", "descr1");
        fbk.addNewTask(task2);
        fbk.getTask(1);
        fbk.getEpic(2);
        fbk.getSubTask(3);
        FileBackedTasksManager fbk2 = new FileBackedTasksManager();
        fbk2.fileFromString(file);
        assertNotNull(fbk2, "Не загружен");
    }

    @Test
    public void shouldBePrioritizedTaskList() {
        ArrayList<Task> list = taskManager.getPrioritizedTasks();
        assertNotNull(list);
    }

    @Test
    public void loadingHttpServer() throws IOException {
        Task task = new Task("task", "task decr");
        taskManager.addNewTask(task);
        Epic epic = new Epic("epic", "epic decr");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("subTask", "subTask decr", epic.getId());
        taskManager.addNewSubTask(subTask);
        taskManager.getTask(1);
        HTTPTaskManager manager = HTTPTaskManager.load();
        assertEquals(taskManager.getTasks().size(), manager.getTasks().size(), "Количество не совпадает");
        assertEquals(taskManager.getEpics().size(), manager.getEpics().size(), "Количество не совпадает");
        assertEquals(taskManager.getSubtasks().size(), manager.getSubtasks().size(), "Количество не совпадает");
        assertEquals(taskManager.getHistory(), manager.getHistory(), "Количество не совпадает");
    }
}