package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
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
        super(nameTask, taskDetail, status, id);
    }

    public EpicTask(String nameTask, String taskDetail, Status status, Type type) {
        super(nameTask, taskDetail, status, type);
    }

    public EpicTask(String nameTask, String taskDetail, Status status, Type type, LocalDateTime startTime, Duration duration) {
        super(nameTask, taskDetail, status, type, startTime, duration);
    }

    public EpicTask(String nameTask, String taskDetail, Status status, Type type, LocalDateTime startTime, Duration duration, int id) {
        super(nameTask, taskDetail, status, type, startTime, duration, id);
    }

    public void addSubtask(SubTask subtaskIds) {
        subtaskIds.setEpicId(this.id);
        this.subtaskIds.add(subtaskIds.id);
    }

    public void setFinish(Duration sumSubTaskDuration) {
        this.finish = start.plus(durationMinutes.plus(sumSubTaskDuration));
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
