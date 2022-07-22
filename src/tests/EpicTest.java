
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static enums.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private TaskManager manager;
    private Epic epic;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultFile();
        epic = new Epic("epic", "epic decr");
        manager.addNewEpic(epic);
    }

    @Test
    public void epicWithOutSubTasks() {
        assertEquals(NEW, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusNew() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), NEW);
        manager.addNewSubTask(sub);
        manager.addNewSubTask(sub2);
        assertEquals(NEW, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusDone() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), NEW);
        manager.updateSubTask(sub, sub.getId(), DONE);
        manager.updateSubTask(sub2, sub2.getId(), DONE);
        assertEquals(DONE, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusNewAndDone() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub);
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), NEW);
        manager.updateSubTask(sub2, sub2.getId(), DONE);
        assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void epicWithSubTasksStatusInProgress() {
        SubTask sub = new SubTask("111", "111", epic.getId());
        SubTask sub2 = new SubTask("222", "222", epic.getId());
        manager.addNewSubTask(sub);
        manager.addNewSubTask(sub2);
        manager.updateSubTask(sub, sub.getId(), IN_PROGRESS);
        manager.updateSubTask(sub2, sub2.getId(), IN_PROGRESS);
        assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }
}