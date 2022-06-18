package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Epic extends Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Integer> subtasksId;
    public Epic(String name, String descriprion) {
        super(name, descriprion);
        subtasksId = new ArrayList<>();
    }
    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
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
