import Manager.*;
import Model.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.util.Calendar.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

//        Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.

        Task task1 = new Task("task1", "descr1", Duration.ofMinutes(30), LocalDateTime.of(2022,JUNE,2,12,00));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "descr2");
        manager.addNewTask(task2);
        Epic epic1 = new Epic("epic1", "descr1");
        manager.addNewEpic(epic1);
        SubTask subTask1 = new SubTask("subtask1", "descr1", epic1.getId());
        manager.addNewSubTask(subTask1, epic1.getId());
        SubTask subTask2 = new SubTask("subtask2", "descr2", epic1.getId());
        manager.addNewSubTask(subTask2, epic1.getId());
        SubTask subTask3 = new SubTask("subtask2", "descr2", epic1.getId());
        manager.addNewSubTask(subTask3, epic1.getId());
        Epic epic2 = new Epic("epic2", "descr2");
        manager.addNewEpic(epic2);
        manager.updateTask(task1,1, Status.DONE);

//         Запросите созданные задачи несколько раз в разном порядке.

        manager.getEpic(3);
        manager.getEpic(3);
        manager.getTask(2);
        manager.getTask(1);
        manager.getSubTask(4);
        manager.getSubTask(6);
        manager.getEpic(3);
        manager.getTask(1);
        manager.getSubTask(5);
        manager.getSubTask(4);
        manager.getTask(1);
        manager.getEpic(7);
        manager.getEpic(3);

        printMenu(manager);

//        После каждого запроса выведите историю и убедитесь, что в ней нет повторов.

        historyMenu(manager);


//        Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        manager.deleteAllSubTasks();

        historyMenu(manager);

    }

    static void printMenu(TaskManager manager) {

        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }

        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }

        for (SubTask subTask : manager.getSubtasks()) {
            System.out.println(subTask);
        }
        System.out.println("_____________________________________________________________________");
    }

    static void historyMenu(TaskManager manager) {
        System.out.println("____________________________HISTORY_TASKS____________________________");

        for (Task task : manager.getHistory()) {
            System.out.println(task.getName());
        }
        System.out.println("________________________________________________________");

    }
}