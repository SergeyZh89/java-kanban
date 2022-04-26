public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
//      Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        manager.addTasks(new Task("Купить слона", "Прокатиться на слоне", "NEW"));
        manager.addTasks(new Task("Помыть авто", "Перед дождем", "NEW"));
        manager.addTasks(new Epic("Переезд", "В другую страну", "NEW"),
                new Subtask("Выбрать страну", "Зайти на сайт", "NEW"),
                new Subtask("Купить билеты", "Вспомнить пин-код", "NEW"));
        manager.addTasks(new Epic("Шашлыки", "Сбор на праздники", "NEW"),
                new Subtask("Купить алкоголь", "Элитный", "NEW"));
//      Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
        manager.allTasks();
        System.out.println("__________________________________________________________________________________");
//      Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
//      а статус эпика рассчитался по статусам подзадач.
        manager.updateTask(2,new Subtask("Выбрать страну", "Зайти на сайт", "DONE"));
        manager.updateTask(3,new Subtask("Купить билеты", "Вспомнить пин-код", "IN_PROGRESS"));
        manager.updateTask(5,new Subtask("Купить алкоголь", "Элитный", "DONE"));
        manager.updateTask(0,new Task("Купить слона", "Прокатиться на слоне", "DONE"));
        manager.updateTask(1,new Task("Помыть авто", "Перед дождем", "IN_PROGRESS"));
        manager.allTasks();
        System.out.println("__________________________________________________________________________________");
//      И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.deleteTask(3);
        manager.deleteTask(6);
        manager.deleteTask(1);
        manager.allTasks();
        System.out.println("__________________________________________________________________________________");

    }
}