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
    protected static final LocalDateTime DEFAULT_START_TIME =
            LocalDateTime.of(1993, 11, 2, 11, 30);
    protected String nameTask;
    protected String taskDetail;
    protected Status status;
    protected Type type;
    protected Duration durationMinutes = Duration.ofMinutes(1L);
    protected LocalDateTime start = DEFAULT_START_TIME;
    protected LocalDateTime finish = start.plusMinutes(durationMinutes.toMinutes());

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
                ", type=" + type +
                ", startTime=" + start +
                ", duration=" + durationMinutes.toMinutes() +
                '}';
    }
}
