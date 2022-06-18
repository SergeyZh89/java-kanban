package Manager;

import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements Serializable {
    public static void main(String[] args) {

        File file = new File("tasks.bin");
        FileBackedTasksManager fbt = new FileBackedTasksManager();
        Task task1 = new Task("task1", "descr1");
        fbt.addNewTask(task1);
        Task task2 = new Task("task2", "descr2");
        fbt.addNewTask(task2);
        Epic epic1 = new Epic("epic1", "descr1");
        fbt.addNewEpic(epic1);
        SubTask subTask1 = new SubTask("subtask1", "descr1", epic1.getId());
        fbt.addNewSubTask(subTask1, epic1.getId());
        SubTask subTask2 = new SubTask("subtask2", "descr2", epic1.getId());
        fbt.addNewSubTask(subTask2, epic1.getId());
        SubTask subTask3 = new SubTask("subtask2", "descr2", epic1.getId());
        fbt.addNewSubTask(subTask3, epic1.getId());
        Epic epic2 = new Epic("epic2", "descr2");
        fbt.addNewEpic(epic2);

        fbt.getTask(1);
        fbt.getEpic(3);
        fbt.getSubTask(5);

        FileBackedTasksManager newTaskManager = loadFromFile(file);
        System.out.println(newTaskManager.getTasks());
        System.out.println(newTaskManager.getSubtasks());
        System.out.println(newTaskManager.getEpics());
        System.out.println("_________HYSTORY__________");
        System.out.println(newTaskManager.getHistory());
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.bin"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Ошибка записи!");
        }
    }

    static FileBackedTasksManager loadFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (FileBackedTasksManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask, int epicId) {
        super.addNewSubTask(subTask, epicId);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task tempTask = super.getTask(id);
        this.save();
        return tempTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic tempEpic = super.getEpic(id);
        this.save();
        return tempEpic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask tempSubtask = super.getSubTask(id);
        this.save();
        return tempSubtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }
}
