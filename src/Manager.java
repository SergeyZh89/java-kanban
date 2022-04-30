import java.util.ArrayList;
import java.util.HashMap;

class Manager {

    private int generatorId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addNewTask(Task task) {
        tasks.put(getGeneratorId(), task);
        task.setId(generatorId);
    }

    public void addNewEpic(Epic epic) {
        epics.put(getGeneratorId(), epic);
        epic.setId(generatorId);

    }

    public void addNewSubTask(SubTask subTask, int epicId) {
        subTasks.put(getGeneratorId(), subTask);
        subTask.setId(generatorId);
        epics.get(epicId).getSubtasksId().add(generatorId);
    }


    public void updateTask(Task task, int id, Status status) {
        tasks.put(id, task);
        task.setId(id);
        task.setStatus(status);
    }

    public void updateEpic(Epic epic, int id) {
        ArrayList<Integer> subIds = new ArrayList<>(epics.get(id).getSubtasksId());
        epics.put(id, epic);
        epics.get(id).setSubtasksId(subIds);

    }

    public void updateSubTask(SubTask subTask, int id, Status status) {
        subTasks.put(id, subTask);
        subTask.setId(id);
        subTask.setStatus(status);
        setStatusEpic(epics.get(subTasks.get(id).getEpicIds()));
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        for (Integer subTasksId : epics.get(id).getSubtasksId()) {
            subTasks.remove(subTasksId);
        }
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        epics.get(subTasks.get(id).getEpicIds()).getSubtasksId().remove(id);
        subTasks.remove(id);
        setStatusEpic(epics.get(subTasks.get(id).getEpicIds()));
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus(Status.NEW);
        }
    }

    public int getGeneratorId() {
        ++generatorId;
        return generatorId;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubtasks() {
        return subTasks;
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