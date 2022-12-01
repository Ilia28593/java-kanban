package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Task extends Id {
    protected String nameTask;
    protected String taskDetail;
    protected Status status;
    protected Type type;
    protected Duration durationMinutes ;
    protected LocalDateTime start;
    protected LocalDateTime finish ;

    public Task(String nameTask, String taskDetail, Status status) {
        super();
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    public Task(int ids){
        super(ids);
    }

    public Task(String nameTask, String taskDetail, Status status, int id) {
        this.id = id;
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    public Task(String nameTask, String taskDetail, Status status, Type type) {
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
        this.type = type;
    }

    public Task(String nameTask, String taskDetail, Status status, LocalDateTime startTime,
                Duration duration) {
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
        this.start = startTime;
        this.durationMinutes = duration;
    }

    public Task(String nameTask, String taskDetail, Status status, LocalDateTime startTime,
                Duration duration, int id) {
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
        this.start = startTime;
        this.durationMinutes = duration;
        this.id = id;
    }

    @Override
    public String toString() {
        return "DefaultTask{" +
                "nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + type+
                ", startTime=" + start +
                ", duration=" + durationMinutes +
                '}';
    }
}
