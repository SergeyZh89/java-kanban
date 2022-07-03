package Tests;

import Manager.FileBackedTasksManager;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager();
    }
}