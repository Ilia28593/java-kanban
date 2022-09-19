package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EpicTask extends SetId implements TaskInter {
    private String nameTask;
    private String taskDetail;
    private Status status;

    private List<Subtask> subtask = new ArrayList<>();

    public EpicTask(String nameTask, String taskDetail, Status status) {
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    public void setSubtask(Subtask subtask) {
        subtask.setEpicId(this.id);
        this.subtask.add(subtask);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status +
                ", subtask=" + subtask.size() +
                '}';
    }
}
