package manager;

import java.net.URI;

public class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    public URI url = URI.create("http://localhost:8078");

    @Override
    public HTTPTaskManager createTaskManager() {
        return new HTTPTaskManager(url);
    }
}
