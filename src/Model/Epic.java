package Model;

import Manager.Status;
import Manager.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;
    private final TaskTypes type;
    private LocalDateTime endTime;

    public Epic(String name, String descriprion) {
        super(name, descriprion);
        subtasksId = new ArrayList<>();
        this.type = TaskTypes.EPIC;
    }

    public Epic(String name, String descriprion, Status status, int id, TaskTypes type) {
        super(name, descriprion, status, id);
        subtasksId = new ArrayList<>();
        this.type = TaskTypes.EPIC;
    }

    public Epic(String name, String descriprion, Status status, int id, TaskTypes type, Duration duration, LocalDateTime startTime) {
        super(name, descriprion, status, id, duration, startTime);
        subtasksId = new ArrayList<>();
        this.type = TaskTypes.EPIC;
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
