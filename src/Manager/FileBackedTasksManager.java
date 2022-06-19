package Manager;

import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager load = new FileBackedTasksManager();
        load.fileFromString(file);
        return load;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fbt = Managers.getDefaultFile();
        File file = new File("tasks.csv");

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

        FileBackedTasksManager load = FileBackedTasksManager.loadFromFile(file);

        System.out.println("____HYSTORY____");
        System.out.println(load.getHistory());
        System.out.println("_______________");
        System.out.println(load.getTasks());
        System.out.println(load.getEpics());
        System.out.println(load.getSubtasks());
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
            fr.write("id,type,name,status,description,epic\n");
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
            System.out.println("Ошибка записи");
        }
    }

    public String toString(Task task) {
        if (task instanceof SubTask) {
            return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescriprion(), ((SubTask) task).getEpicIds());
        } else {
            return String.format("%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescriprion());
        }
    }

    public void fileFromString(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                String[] lines = line.split(",");
                if (!line.isBlank()) {
                    if (lines[1].equals("TASK")) {
                        Task task = new Task(lines[2], lines[4], Status.valueOf(lines[3]),
                                Integer.parseInt(lines[0]));
                        tasks.put(Integer.parseInt(lines[0]), task);
                    } else if (lines[1].equals("EPIC")) {
                        Epic epic = new Epic(lines[2], lines[4], Status.valueOf(lines[3]),
                                Integer.parseInt(lines[0]));
                        epics.put(Integer.parseInt(lines[0]), epic);
                    } else if (lines[1].equals("SUBTASK")) {
                        SubTask subTask = new SubTask(lines[2], lines[4], Status.valueOf(lines[3]),
                                Integer.parseInt(lines[0]), Integer.parseInt(lines[5]));
                        subTasks.put(Integer.parseInt(lines[0]), subTask);
                        epics.get(Integer.parseInt(lines[5])).getSubtasksId().add(Integer.parseInt(lines[0]));
                    } else {
                        for (String s : lines) {
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
    public SubTask getSubTask(int id) {
        SubTask tempSubTask = super.getSubTask(id);
        save();
        return tempSubTask;
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
