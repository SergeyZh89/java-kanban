import java.util.HashMap;

class Epic {

    private String name;
    private String descriprion;
    private String status;
    private final HashMap<Integer, Subtask> subTasks;

    public Epic(String name, String descriprion, String status) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = status;
        this.subTasks = new HashMap<>();
    }

    public HashMap<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    public String getName() {
        return name;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ЭПИК:      " + name + " --> Описание: '" + descriprion + "'" + " Статус: " + status + ".";
    }
}
