package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int generatorId = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addNewTask(Task task) {
        tasks.put(getGeneratorId(), task);
        task.setId(generatorId);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epics.put(getGeneratorId(), epic);
        epic.setId(generatorId);

    }

    @Override
    public void addNewSubTask(SubTask subTask, int epicId) {
        subTasks.put(getGeneratorId(), subTask);
        subTask.setId(generatorId);
        epics.get(epicId).getSubtasksId().add(generatorId);
    }

    @Override
    public void updateTask(Task task, int id, Status status) {
        tasks.put(id, task);
        task.setId(id);
        task.setStatus(status);
    }

    @Override
    public void updateEpic(Epic epic, int id) {
        ArrayList<Integer> subIds = new ArrayList<>(epics.get(id).getSubtasksId());
        epics.put(id, epic);
        epics.get(id).setSubtasksId(subIds);

    }

    @Override
    public void updateSubTask(SubTask subTask, int id, Status status) {
        subTasks.put(id, subTask);
        subTask.setId(id);
        subTask.setStatus(status);
        setStatusEpic(epics.get(subTasks.get(id).getEpicIds()));
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (Integer subTasksId : epics.get(id).getSubtasksId()) {
            subTasks.remove(subTasksId);
            historyManager.remove(subTasksId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        epics.get(subTasks.get(id).getEpicIds()).getSubtasksId()
                .remove(epics.get(subTasks.get(id).getEpicIds()).getSubtasksId().indexOf(id));
        setStatusEpic(epics.get(subTasks.get(id).getEpicIds()));
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public int getGeneratorId() {
        ++generatorId;
        return generatorId;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(this.subTasks.values());
    }


    public void setStatusEpic(Epic epic) {

        ArrayList<SubTask> subTasksTemp = new ArrayList<>();
        int countNew = 0;
        int countDONE = 0;
        for (Integer id : epic.getSubtasksId()) {
            subTasksTemp.add(subTasks.get(id));
            if (subTasks.get(id).getStatus().equals(Status.NEW)) {
                countNew++;
            } else if (subTasks.get(id).getStatus().equals(Status.DONE)) {
                countDONE++;
            }
        }
        if (countNew == subTasksTemp.size()) {
            epic.setStatus(Status.NEW);
        } else if (countDONE == subTasksTemp.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
