package manager;

import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.net.URI;

public class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    public URI url = URI.create("http://localhost:8078");

    @Override
    public HTTPTaskManager createTaskManager() {
        return new HTTPTaskManager(url);
    }
}
