package manager;

import java.net.URI;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HTTPTaskManager getDefault(URI url) {
        return new HTTPTaskManager(url);
    }

    public static FileBackedTasksManager getDefaultFile() {
        return new FileBackedTasksManager();
    }
}
