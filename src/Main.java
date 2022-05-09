import Manager.*;
import Model.*;


public class Main {

    public static void main(String[] args) {


    TaskManager manager = Managers.getDefault();


//        Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.

        Task task1 = new Task("task1", "descr1");
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "descr2");
        manager.addNewTask(task2);
        Epic epic1 = new Epic("epic1", "descr1");
        manager.addNewEpic(epic1);
        SubTask subTask1 = new SubTask("subtask1", "descr1", epic1.getId());
        manager.addNewSubTask(subTask1, epic1.getId());
        SubTask subTask2 = new SubTask("subtask2", "descr2", epic1.getId());
        manager.addNewSubTask(subTask2, epic1.getId());
        manager.getEpic(3);
        manager.getSubTask(4);
        manager.getSubTask(5);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);

//        Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)

        printMenu(manager);

//        Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
//                а статус эпика рассчитался по статусам подзадач.

        Task task3 = new Task("task3", "descr3");
        manager.updateTask(task3, 1, Status.DONE);
        Task task4 = new Task("task4", "descr4");
        manager.updateTask(task4, 2, Status.IN_PROGRESS);
        SubTask subTask3 = new SubTask("subtask3", "descr3", epic1.getId());
        manager.updateSubTask(subTask3, 4, Status.DONE);
        SubTask subTask4 = new SubTask("subtask4", "descr4", epic1.getId());
        manager.updateSubTask(subTask4, 5, Status.IN_PROGRESS);

        printMenu(manager);

//        И, наконец, попробуйте удалить одну из задач и один из эпиков.

        manager.deleteTaskById(2);
        manager.deleteEpicById(3);

        printMenu(manager);

        historyMenu(manager);
    }

    static void printMenu(TaskManager manager) {

        for (Task task : manager.getTasks().values()) {
            System.out.println(task);
        }

        for (Epic epic : manager.getEpics().values()) {
            System.out.println(epic);
        }

        for (SubTask subTask : manager.getSubtasks().values()) {
            System.out.println(subTask);
        }
        System.out.println("_____________________________________________________________________");
    }

    static void historyMenu(TaskManager manager ){
        System.out.println("____________________________HISTORY_TASKS____________________________");

        for (int i = 0; i < manager.getHistoryManager().getHistory().size(); i++) {
            System.out.println((i + 1) + " " + manager.getHistoryManager().getHistory().get(i));
        }
    }
}