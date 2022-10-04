package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Task extends Id {
    protected String nameTask;
    protected String taskDetail;
    protected Status status;

    public Task(String nameTask, String taskDetail, Status status) {
        super();
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    @Override
    public String toString() {
        return "DefaultTask{" +
                "nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
