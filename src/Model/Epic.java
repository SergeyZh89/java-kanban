package Model;

import Manager.Status;
import Manager.TaskTypes;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;
    private TaskTypes type;

    public Epic(String name, String descriprion) {
        super(name, descriprion);
        subtasksId = new ArrayList<>();
        this.type = TaskTypes.EPIC;
    }

    public Epic(String name, String descriprion, Status status, int id) {
        super(name, descriprion, status, id);
        subtasksId = new ArrayList<>();
        this.type = TaskTypes.EPIC;
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    @Override
    public String toString() {
        return "Эпик: '" + getName() + '\'' +
                ", Описание='" + getDescriprion() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", Подзадачи=" + subtasksId.toString();
    }
}
