package model;

import lombok.Data;

@Data
public class Id {

    protected static int ids;
    protected int id;

    public Id() {
        ids++;
        this.id = ids;
    }

    public Id(int id){
        ids=id;
    }
}
