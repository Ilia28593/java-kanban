package model;

import lombok.Data;

@Data
public abstract class SetId {
    private static int ids;
    protected int id;

    public SetId() {
        ids++;
        this.id = ids;
    }
}
