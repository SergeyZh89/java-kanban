package Tests;

import Manager.HistoryManager;
import Manager.InMemoryHistoryManager;
import Model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    void beforeEach(){
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        Task task = new Task("task", "task decr");
        historyManager.add(task);
            final List<Task> history = historyManager.getHistory();
            assertNotNull(history, "История не пустая.");
            assertEquals(1, history.size(), "История не пустая.");

    }

    @Test
    void remove() {
        Task task = new Task("task", "task decr");
        historyManager.add(task);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая");
    }

    @Test
    void getHistory() {
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не возвращается.");
    }
}