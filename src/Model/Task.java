package Model;

import Manager.Status;
import Manager.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String decription;
    private Status status;
    private int id;
    private TaskTypes typeTask;
    private Duration duration;
    private LocalDateTime startTime;

    public Task() {
        this.status = Status.NEW;
        this.typeTask = TaskTypes.TASK;
    }

    public Task(String name, String decription) {
        this.name = name;
        this.decription = decription;
        this.status = Status.NEW;
        this.typeTask = TaskTypes.TASK;
    }

    public Task(String name, String decription, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.decription = decription;
        this.status = Status.NEW;
        this.typeTask = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String decription, Status status, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.decription = decription;
        this.status = status;
        this.id = id;
        this.typeTask = TaskTypes.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public void setTypeTask(TaskTypes typeTask) {
        this.typeTask = typeTask;
    }

    public Task(String name, String decription, Status status, int id) {
        this.name = name;
        this.decription = decription;
        this.status = status;
        this.id = id;
        this.typeTask = TaskTypes.TASK;
    }

    public TaskTypes getTypeTask() {
        return typeTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdecription() {
        return decription;
    }

    public void setdecription(String decription) {
        this.decription = decription;
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
                ", decription='" + decription + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + typeTask +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(decription, task.decription) && status == task.status && typeTask == task.typeTask && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, decription, status, id, typeTask, duration, startTime);
    }
}
