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
    protected transient Set<Task> priorityTasks = new TreeSet<>(new ComparatorTasks());
    protected transient ArrayList<Task> notPriorityTasks = new ArrayList<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

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

    private void setStatusEpic(Epic epic) {
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

    private void epicTime(Epic epic) {
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;
        for (Integer integer : epics.get(epic.getId()).getSubtasksId()) {
            if (subTasks.get(integer).getDuration() != null) {
                duration = duration.plus(subTasks.get(integer).getDuration());
            }
            if (subTasks.get(integer).getStartTime() != null) {
                if (start.isAfter(subTasks.get(integer).getStartTime())) {
                    start = subTasks.get(integer).getStartTime();
                }
                if (end.isBefore(subTasks.get(integer).getEndtime())) {
                    end = subTasks.get(integer).getEndtime();
                }
            }
            epic.setDuration(duration);
            epic.setStartTime(start);
            epic.setEndTime(end);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addNewTask(Task task) {
        task.setId(getGeneratorId());
        tasks.put(generatorId, task);
        if (isDataTime(task)) {
            try {
                validatorTimeTasks(task);
                priorityTasks.add(task);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
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
    public void addNewSubTask(SubTask subTask) {
        if (isDataTime(subTask)) {
            try {
                validatorTimeTasks(subTask);
                priorityTasks.add(subTask);
                subTasks.put(getGeneratorId(), subTask);
                subTask.setId(generatorId);
                epics.get(subTask.getEpicIds()).getSubtasksId().add(generatorId);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            subTasks.put(getGeneratorId(), subTask);
            subTask.setId(generatorId);
            epics.get(subTask.getEpicIds()).getSubtasksId().add(generatorId);
            notPriorityTasks.add(subTask);
        }
        if (subTask.getStartTime() != null) {
            epicTime(epics.get(subTask.getEpicIds()));
        }
        setStatusEpic(epics.get(subTask.getEpicIds()));
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
        epicTime(epics.get(subTasks.get(id).getEpicIds()));
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
    public SubTask getSubTask(int id){
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
        priorityTasks.removeIf(priorityTask -> priorityTask.getId() == id);
    }

    @Override
    public void deleteEpicById(int id) {
        ArrayList<Task> sub = new ArrayList<>();
        for (Integer subTasksId : epics.get(id).getSubtasksId()) {
            sub.add(subTasks.get(subTasksId));
            subTasks.remove(subTasksId);
            historyManager.remove(subTasksId);
        }
        for (Task task : sub) {
            for (Task priorityTask : priorityTasks) {
                if (priorityTask.getId() == task.getId()) {
                    priorityTasks.remove(task);
                    break;
                } else {
                    notPriorityTasks.remove(task);
                }
            }
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        epics.get(subTasks.get(id).getEpicIds()).getSubtasksId()
                .remove((Integer) id);
        setStatusEpic(epics.get(subTasks.get(id).getEpicIds()));
        epicTime(epics.get(subTasks.get(id).getEpicIds()));
        priorityTasks.removeIf(priorityTask -> priorityTask.getId() == subTasks.get(id).getId());
        notPriorityTasks.removeIf(priorityTask -> priorityTask.getId() == subTasks.get(id).getId());
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks(){
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        for (Task value : tasks.values()) {
            for (Task priorityTask : priorityTasks) {
                if (priorityTask.getId() == value.getId()) {
                    priorityTasks.remove(value);
                    break;
                } else {
                    notPriorityTasks.remove(value);
                }
            }
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics(){
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            for (Task priorityTask : priorityTasks) {
                if (priorityTask.getId() == id) {
                    priorityTasks.remove(subTasks.get(id));
                    break;
                } else {
                    notPriorityTasks.remove(subTasks.get(id));
                }
            }
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        for (Task value : subTasks.values()) {
            for (Task priorityTask : priorityTasks) {
                if (priorityTask.getId() == value.getId()) {
                    priorityTasks.remove(value);
                    break;
                } else {
                    notPriorityTasks.remove(value);
                }
            }
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus(Status.NEW);
            epic.setDuration(Duration.ZERO);
        }
    }

    private int getGeneratorId() {
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
}
