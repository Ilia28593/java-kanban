package model;

import lombok.Data;

@Data
public abstract class Id {

    private static int ids;
    protected int id;

    public Id() {
        ids++;
        this.id = ids;
    }
}
