package Manager;

import Model.*;

import java.util.HashMap;

public interface TaskManager {

    void addNewTask(Task task);

    void addNewEpic(Epic epic);

    void addNewSubTask(SubTask subTask, int epicId);

    void updateTask(Task task, int id, Status status);

    void updateEpic(Epic epic, int id);

    void updateSubTask(SubTask subTask, int id, Status status);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    HistoryManager getHistoryManager();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    int getGeneratorId();

    void setStatusEpic(Epic epic);

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubtasks();

}