package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public SubTask(String nameTask, String taskDetail, Status status, Type type) {
        super(nameTask, taskDetail, status, type);
    }

    public SubTask(String nameTask, String taskDetail, Status status, Type type, LocalDateTime startTime, Duration duration) {
        super(nameTask, taskDetail, status, type, startTime, duration);
    }

    public SubTask(String nameTask, String taskDetail, Status status, Type type, LocalDateTime startTime, Duration duration, int id) {
        super(nameTask, taskDetail, status, type, startTime, duration, id);
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
