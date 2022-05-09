package Model;

public class SubTask extends Task {

    private int epicIds;


    public SubTask(String name, String descriprion, int epicIds) {
        super(name, descriprion);
        this.epicIds = epicIds;
    }

    public int getEpicIds() {
        return epicIds;
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
