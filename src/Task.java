
class Task {

    private String name;
    private String descriprion;
    private String status;

    public Task(String name, String descriprion, String status) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = status;
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

    @Override
    public String toString() {
        return "Задача:    " + name + " --> Описание: '" + descriprion + "'" + " Статус: " + status + ".";
    }
}
