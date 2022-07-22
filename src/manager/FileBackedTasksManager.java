package manager;

import enums.Status;
import enums.TaskTypes;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager load = new FileBackedTasksManager();
        load.fileFromString(file);
        return load;
    }

    public String toStringHistory() {
        StringBuilder sb = new StringBuilder();
        List<Task> taskList;
        taskList = getHistory();
        for (Task task : taskList) {
            sb.append(task.getId());
            sb.append(",");
        }
        return String.valueOf(sb);
    }

    public void save() {
        try (FileWriter fr = new FileWriter("tasks.csv")) {
            fr.write("id,type,name,status,description,epic,duration,startTime\n");
            for (Task task : getTasks()) {
                fr.write(toString(task));
            }
            for (Epic task : getEpics()) {
                fr.write(toString(task));
            }
            for (SubTask task : getSubtasks()) {
                fr.write(toString(task));
            }
            fr.write("\n");
            fr.write(toStringHistory());
        } catch (IOException | NullPointerException e) {
            System.out.println("Ошибка записи :" + e.getMessage());
        }
    }

    public String toString(Task task) {
        if (task.getTypeTask().equals(TaskTypes.TASK)) {
            if (task.getDuration() != null) {
                return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getTypeTask(), task.getName(),
                        task.getStatus(), task.getdecription(), "null", task.getDuration(), task.getStartTime());
            } else {
                return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getTypeTask(), task.getName(),
                        task.getStatus(), task.getdecription(), "null", "null", "null");
            }
        }
        if (task.getTypeTask().equals(TaskTypes.EPIC)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getTypeTask(), task.getName(),
                    task.getStatus(), task.getdecription(), "null", task.getDuration(), task.getStartTime());
        }
        if (task.getTypeTask().equals(TaskTypes.SUBTASK)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getTypeTask(), task.getName(),
                    task.getStatus(), task.getdecription(), ((SubTask) task).getEpicIds(), task.getDuration(),
                    task.getStartTime());
        }
        return null;
    }

    public void fileFromString(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                String[] lines = line.split(",");
                if (line.trim().length() != 0) {
                    if (lines[1].equals("TASK")) {
                        Task task;
                        if (!lines[6].equals("null")) {
                            task = new Task(lines[2], lines[4], Status.valueOf(lines[3]),
                                    Integer.parseInt(lines[0]), Duration.parse(lines[6]),
                                    LocalDateTime.parse(lines[7]));
                        } else {
                            task = new Task(lines[2], lines[4], Status.valueOf(lines[3]),
                                    Integer.parseInt(lines[0]));
                        }
                        tasks.put(Integer.parseInt(lines[0]), task);

                    } else if (lines[1].equals("EPIC")) {
                        Epic epic;
                        if (!lines[6].equals("null") && !lines[7].equals("null")) {
                            epic = new Epic(lines[2], lines[4], Status.valueOf(lines[3]),
                                    Integer.parseInt(lines[0]), TaskTypes.EPIC,
                                    Duration.parse(lines[6]), LocalDateTime.parse(lines[7]));
                        } else {
                            epic = new Epic(lines[2], lines[4], Status.valueOf(lines[3]),
                                    Integer.parseInt(lines[0]), TaskTypes.EPIC);
                        }
                        epics.put(Integer.parseInt(lines[0]), epic);

                    } else if (lines[1].equals("SUBTASK")) {
                        SubTask subTask;
                        if (!lines[6].equals("null") && !lines[7].equals("null")) {
                            subTask = new SubTask(lines[2], lines[4],
                                    Status.valueOf(lines[3]), Integer.parseInt(lines[0]),
                                    Integer.parseInt(lines[5]), TaskTypes.SUBTASK,
                                    Duration.parse(lines[6]), LocalDateTime.parse(lines[7]));
                        } else {
                            subTask = new SubTask(lines[2], lines[4],
                                    Status.valueOf(lines[3]), Integer.parseInt(lines[0]),
                                    Integer.parseInt(lines[5]), TaskTypes.SUBTASK);
                        }
                        subTasks.put(Integer.parseInt(lines[0]), subTask);
                        epics.get(Integer.parseInt(lines[5])).getSubtasksId().add(Integer.parseInt(lines[0]));
                    }
                } else {
                    String lne = br.readLine();
                    if (lne != null) {
                        String[] lll = lne.split(",");
                        for (String s : lll) {
                            if (tasks.containsKey(Integer.parseInt(s))) {
                                getTask(Integer.parseInt(s));
                            } else if (epics.containsKey(Integer.parseInt(s))) {
                                getEpic(Integer.parseInt(s));
                            } else if (subTasks.containsKey(Integer.parseInt(s))) {
                                getSubTask(Integer.parseInt(s));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addNewTask(Task task){
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic){
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask){
        super.addNewSubTask(subTask);
        save();
    }


    @Override
    public Task getTask(int id) {
        Task tempTask = super.getTask(id);
        save();
        return tempTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic tempEpic = super.getEpic(id);
        save();
        return tempEpic;
    }

    @Override
    public SubTask getSubTask(int id){
        SubTask tempSubTask = super.getSubTask(id);
        save();
        return tempSubTask;
    }

    @Override
    public void deleteAllTasks(){
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics(){
        super.deleteAllEpics();
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }
}
