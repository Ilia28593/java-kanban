package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EpicTask extends Task implements TaskInter {

    private List<Integer> idSubtasks = new ArrayList<>();

    public EpicTask(String nameTask, String taskDetail, Status status) {
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    public void setIdSubtasks(Subtask idSubtasks) {
        idSubtasks.setEpicId(this.id);
        this.idSubtasks.add(idSubtasks.id);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status +
                ", subtask=" + idSubtasks +
                '}';
    }
}
