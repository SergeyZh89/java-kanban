package manager;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    public FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager();
    }
}