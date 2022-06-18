package Manager;

import Model.*;

import java.io.Serializable;
import java.util.List;

public interface HistoryManager extends Serializable {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
