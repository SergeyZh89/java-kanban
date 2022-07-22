package model;

import enums.Status;
import enums.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicIds;
    private TaskTypes typeSubTask = TaskTypes.SUBTASK;

    public SubTask() {
        super.setTypeTask(TaskTypes.SUBTASK);
    }

    public SubTask(String name, String descriprion, int epicIds) {
        super(name, descriprion);
        super.setTypeTask(TaskTypes.SUBTASK);
        this.epicIds = epicIds;
    }

    public SubTask(String name, String descriprion, int epicIds, Duration duration, LocalDateTime startTime) {
        super(name, descriprion, duration, startTime);
        super.setTypeTask(TaskTypes.SUBTASK);
        this.epicIds = epicIds;
    }

    public SubTask(String name, String descriprion, Status status, int id, int epicIds, TaskTypes typeSubTask) {
        super(name, descriprion, status, id);
        super.setTypeTask(TaskTypes.SUBTASK);
        this.epicIds = epicIds;
    }

    public SubTask(String name, String descriprion, Status status, int id, int epicIds, TaskTypes typeSubTask, Duration duration, LocalDateTime startTime) {
        super(name, descriprion, status, id, duration, startTime);
        this.epicIds = epicIds;
    }

    public int getEpicIds() {
        return epicIds;
    }

    public TaskTypes getTypeSubTask() {
        return typeSubTask;
    }

    public void setEpicIds(int epicIds) {
        this.epicIds = epicIds;
    }

    public void setTypeSubTask(TaskTypes typeSubTask) {
        this.typeSubTask = typeSubTask;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + getName() + '\'' +
                ", decription='" + getdecription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", type=" + getTypeSubTask() +
                ", epicIds=" + getEpicIds() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}
