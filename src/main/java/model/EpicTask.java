package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EpicTask extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    public EpicTask(String nameTask, String taskDetail, Status status) {
        super(nameTask, taskDetail, status);
    }
    public EpicTask(String nameTask, String taskDetail, Status status, int id) {
        super(nameTask, taskDetail, status,id);
    }

    public void addSubtask(SubTask subtaskIds) {
        subtaskIds.setEpicId(this.id);
        this.subtaskIds.add(subtaskIds.id);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status +
                ", subtask=" + subtaskIds +
                '}';
    }
}
