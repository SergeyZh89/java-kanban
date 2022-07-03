package Model;

import Manager.Status;
import Manager.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicIds;
    private TaskTypes type;

    public SubTask(String name, String descriprion, int epicIds) {
        super(name, descriprion);
        this.epicIds = epicIds;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(String name, String descriprion, int epicIds, Duration duration, LocalDateTime startTime) {
        super(name, descriprion, duration, startTime);
        this.epicIds = epicIds;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(String name, String descriprion, Status status, int id, int epicIds, TaskTypes type) {
        super(name, descriprion, status, id);
        this.epicIds = epicIds;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(String name, String descriprion, Status status, int id, int epicIds, TaskTypes type, Duration duration, LocalDateTime startTime) {
        super(name, descriprion, status, id, duration, startTime);
        this.epicIds = epicIds;
        this.type = TaskTypes.SUBTASK;
    }

    public int getEpicIds() {
        return epicIds;
    }

    public TaskTypes getType() {
        return type;
    }
}
