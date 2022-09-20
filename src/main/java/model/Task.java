package model;

import lombok.Data;

@Data
public abstract class Task {
    protected String nameTask;
    protected String taskDetail;
    protected Status status;
    private static int ids;
    protected int id;

    public Task() {
        ids++;
        this.id = ids;
    }
}
