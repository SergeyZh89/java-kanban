import java.util.ArrayList;

class Epic extends Task {

    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String descriprion) {
        super(name, descriprion);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }


}
