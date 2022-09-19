package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Subtask extends SetId implements TaskInter {
    private int epicId;
    private String nameTask;
    private String taskDetail;
    private Status status;

    public Subtask(String nameTask, String taskDetail, Status status) {
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", epicId=" + epicId +
                ", nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status +
                '}';
    }
}
