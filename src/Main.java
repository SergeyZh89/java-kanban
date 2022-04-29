import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

//        Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.

        Task task1 = new Task("task1", "decr1");
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "decr2");
        manager.addNewTask(task2);
        Epic epic1 = new Epic("epic1", "decr1");
        manager.addNewEpic(epic1);
        SubTask subTask1 = new SubTask("subtask1", "decr1", epic1.getId());
        manager.addNewSubTask(subTask1, epic1.getId());
        SubTask subTask2 = new SubTask("subtask2", "decr2", epic1.getId());
        manager.addNewSubTask(subTask2, epic1.getId());

//        Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)

        printMenu(manager);

//        Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
//                а статус эпика рассчитался по статусам подзадач.

        Task task3 = new Task("task3", "decr3");
        manager.updateTask(task3, 1, Status.DONE);
        Task task4 = new Task("task4", "decr4");
        manager.updateTask(task4, 2, Status.IN_PROGRESS);
        SubTask subTask3 = new SubTask("subtask3", "decr3", epic1.getId());
        manager.updateSubTask(subTask3, 4, Status.DONE);
        SubTask subTask4 = new SubTask("subtask4", "decr4", epic1.getId());
        manager.updateSubTask(subTask4, 5, Status.IN_PROGRESS);

        printMenu(manager);

//        И, наконец, попробуйте удалить одну из задач и один из эпиков.

        manager.deleteTaskById(2);
        manager.deleteEpicById(3);

        printMenu(manager);
    }

    static void printMenu(Manager manager) {

        for (Map.Entry<Integer, Task> pair : manager.getTasks().entrySet()) {
            System.out.println(pair.getKey() + " Задача " + " #" + pair.getValue().getId() + " "
                    + pair.getValue().getName() + " Описание: '" + pair.getValue().getDescriprion() + "' "
                    + pair.getValue().getStatus());
        }
        for (Map.Entry<Integer, Epic> pair : manager.getEpics().entrySet()) {
            System.out.println(pair.getKey() + " ЭПИК " + " #" + pair.getValue().getId() + " "
                    + pair.getValue().getName() + " Описание: '" + pair.getValue().getDescriprion() + "' "
                    + pair.getValue().getStatus() + " Подзадачи: " + pair.getValue().getSubtasksId().toString());
        }
        for (Map.Entry<Integer, SubTask> pair : manager.getSubtasks().entrySet()) {
            System.out.println(pair.getKey() + " Подзадача " + " #" + pair.getValue().getId() + " "
                    + pair.getValue().getName() + " Описание: '" + pair.getValue().getStatus() + "' "
                    + pair.getValue().getEpicIds());
        }
        System.out.println("_______________________________________________________________________________________");
    }
}