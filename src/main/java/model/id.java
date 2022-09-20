package model;

import lombok.Data;

@Data
public abstract class id {

    private static int ids;
    protected int id;

    public id() {
        ids++;
        this.id = ids;
    }
}
