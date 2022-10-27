package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SubTask extends Task {
    private int epicId;

    public SubTask(String nameTask, String taskDetail, Status status) {
        super(nameTask, taskDetail, status);
    }
    public SubTask(String nameTask, String taskDetail, Status status, int id) {
        super(nameTask, taskDetail, status,id);
        this.id=id;
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
