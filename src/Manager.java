import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Manager {

    private int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public void addTasks(Epic epic, Subtask... subtasks) {
        for (Subtask subtask : subtasks) {
            epic.getSubTasks().put(id, subtask);
            this.id++;
        }
        epics.put(id, epic);
        this.id++;
    }

    public void addTasks(Task task) {
        tasks.put(id, task);
        this.id++;
    }

    public void allTasks() {
        for (Map.Entry<Integer, Epic> pair : epics.entrySet()) {
            System.out.println("Id " + "(" + pair.getKey() + ") " + pair.getValue());
            for (Map.Entry<Integer, Subtask> pair2 : pair.getValue().getSubTasks().entrySet()) {
                System.out.println("Id " + "(" + pair2.getKey() + ") " + pair2.getValue());
            }
        }
        for (Map.Entry<Integer, Task> pair : tasks.entrySet()) {
            System.out.println("Id " + "(" + pair.getKey() + ") " + pair.getValue());
        }
    }

    public void deleteTask(int id) {
        for (Map.Entry<Integer, Epic> pair : epics.entrySet()) {
            if (pair.getKey() == id) {
                epics.remove(id);
            }
            pair.getValue().getSubTasks().remove(id);
        }
        for (Map.Entry<Integer, Task> pair : tasks.entrySet()) {
            if (pair.getKey() == id) {
                tasks.remove(id);
            }
        }
        checkStatus();
    }

    public Object getTask(int id) {
        for (Map.Entry<Integer, Epic> pair : epics.entrySet()) {
            if (pair.getKey() == id) {
                return pair.getValue();
            }
            if (pair.getValue().getSubTasks().containsKey(id)) {
                return pair.getValue().getSubTasks().get(id);
            }

        }
        for (Map.Entry<Integer, Task> pair : tasks.entrySet()) {
            if (pair.getKey() == id) {
                return pair.getValue();
            }
        }
        return null;
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
    }

    public void updateTask(int id, Object task) {
        if (task instanceof Subtask) {
            for (Epic epic : epics.values()) {
                if (epic.getSubTasks().containsKey(id)) {
                    epic.getSubTasks().put(id, (Subtask) task);
                }
            }
        } else {
            tasks.put(id, (Task) task);
        }
        checkStatus();
    }

    public void getSubEpic(int id) {
        for (Map.Entry<Integer, Epic> pair : epics.entrySet()) {
            if (pair.getKey() == id) {
                for (Subtask subtask : pair.getValue().getSubTasks().values()) {
                    System.out.println("Id (" + id +") " + subtask);
                }
            }
        }
    }

    private void checkStatus() {
        for (Epic epic : epics.values()) {
            ArrayList<String> status = new ArrayList<>();
            for (Subtask subtask : epic.getSubTasks().values()) {
                status.add(subtask.getStatus());
            }
            if ((status.contains("NEW") & !status.contains("IN_PROGRESS")
                    & !status.contains("DONE")) | status.isEmpty()) { epic.setStatus("NEW");
            } else if (status.contains("DONE") & !status.contains("IN_PROGRESS") & !status.contains("NEW")) {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }
}


