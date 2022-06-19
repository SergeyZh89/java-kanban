package Model;

import Manager.Status;
import Manager.TaskTypes;

public class SubTask extends Task {
    private int epicIds;
    private TaskTypes type;

    public SubTask(String name, String descriprion, int epicIds) {
        super(name, descriprion);
        this.epicIds = epicIds;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(String name, String descriprion, Status status, int id, int epicIds) {
        super(name, descriprion, status, id);
        this.epicIds = epicIds;
        this.type = TaskTypes.SUBTASK;
    }

    public int getEpicIds() {
        return epicIds;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setEpicIds(int epicIds) {
        this.epicIds = epicIds;
    }

    @Override
    public String toString() {
        return "Подзадача: '" + getName() + '\'' +
                ", Описание='" + getDescriprion() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", epicID=" + epicIds;

    }
}
