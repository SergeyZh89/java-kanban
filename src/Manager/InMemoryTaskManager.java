package Manager;

import Model.Epic;
import Model.SubTask;
import Model.Task;
import Service.ComparatorTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int generatorId = 0;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();

    protected Set<Task> priorityTasks = new TreeSet<>(new ComparatorTasks());
    protected ArrayList<Task> notPriorityTasks = new ArrayList<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private boolean isDataTime(Task task) {
        return task.getStartTime() != null;
    }

    private void validatorTimeTasks(Task task) {
        for (Task priorityTask : priorityTasks) {
            if (!task.getStartTime().isBefore(priorityTask.getStartTime()) && !task.getStartTime().isAfter(priorityTask.getEndtime())) {
                throw new IllegalArgumentException("Даты пересекаются у задач с номерами: " + priorityTask.getId() + " и " + task.getId());
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addNewTask(Task task) {
        tasks.put(getGeneratorId(), task);
        task.setId(generatorId);
        if (isDataTime(task)) {
            try {
                validatorTimeTasks(task);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } finally {
                priorityTasks.add(task);
            }
        } else {
            notPriorityTasks.add(task);
        }
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
        if (subTask.getStartTime() != null) {
            epicTime(epics.get(epicId), subTask.getStartTime(), subTask.getDuration());
        }
        if (isDataTime(subTask)) {
            try {
                validatorTimeTasks(subTask);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } finally {
            priorityTasks.add(subTask);
        }
        } else {
            notPriorityTasks.add(subTask);
        }
    }

    private void epicTime(Epic epic, LocalDateTime startTime, Duration duration) {
        if (epics.size() == 0 || epic.getStartTime() == null || epic.getStartTime().isAfter(startTime)) {
            epic.setStartTime(startTime);
            if (epics.size() == 1) {
                epic.setEndTime(startTime.plus(duration));
                epic.setDuration(duration);
            }
        }
        if (epic.getEndTime().isBefore(startTime.plus(duration))) {
            epic.setEndTime(startTime.plus(duration));
        }
        Duration duration1 = Duration.ZERO;
        for (SubTask value : subTasks.values()) {
            if (value.getEpicIds() == epic.getId() && value.getDuration() != null) {
                duration1 = duration1.plus(value.getDuration());
            }
        }
        epic.setDuration(duration1);
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
                .remove((Integer) id);
        setStatusEpic(epics.get(subTasks.get(id).getEpicIds()));
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
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

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        ArrayList<Task> allprioritizedTasks = new ArrayList<>(this.priorityTasks);
        allprioritizedTasks.addAll(notPriorityTasks);
        return allprioritizedTasks;
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
