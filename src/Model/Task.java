package Model;

import Manager.*;

public class Task {

    private String name;
    private String descriprion;
    private Status status;
    private int id;

    public Task(String name, String descriprion) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = Status.NEW;
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

    @Override
    public String toString() {
        return "Задача: '" + name + '\'' +
                ", Описание='" + descriprion + '\'' +
                ", status=" + status +
                ", id=" + id;
    }
}
