package Model;

import Manager.Status;
import Manager.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String descriprion;
    private Status status;
    private int id;
    private TaskTypes type;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String descriprion) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = Status.NEW;
        this.type = TaskTypes.TASK;
    }

    public Task(String name, String descriprion, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = Status.NEW;
        this.type = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String descriprion, Status status, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = status;
        this.id = id;
        this.type = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String descriprion, Status status, int id) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = status;
        this.id = id;
        this.type = TaskTypes.TASK;
    }

    public TaskTypes getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEndtime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", descriprion='" + descriprion + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
