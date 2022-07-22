package model;

import enums.Status;
import enums.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtasksId;
    private TaskTypes typeEpic = TaskTypes.EPIC;
    private LocalDateTime endTime;

    public Epic() {
        super.setTypeTask(TaskTypes.EPIC);
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String descriprion) {
        super(name, descriprion);
        super.setTypeTask(TaskTypes.EPIC);
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String descriprion, Status status, int id, TaskTypes typeEpic) {
        super(name, descriprion, status, id);
        super.setTypeTask(TaskTypes.EPIC);
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String descriprion, Status status, int id, TaskTypes typeEpic, Duration duration, LocalDateTime startTime) {
        super(name, descriprion, status, id, duration, startTime);
        super.setTypeTask(TaskTypes.EPIC);
        subtasksId = new ArrayList<>();
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public TaskTypes getTypeEpic() {
        return typeEpic;
    }

    public void setTypeEpic(TaskTypes typeEpic) {
        this.typeEpic = typeEpic;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId) && typeEpic == epic.typeEpic && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtasksId, typeEpic, endTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", decription='" + getdecription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", type=" + getTypeEpic() +
                ", subtasksId=" + getSubtasksId() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
