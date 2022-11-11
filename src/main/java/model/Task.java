package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Task extends Id {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(nameTask, task.nameTask) && Objects.equals(taskDetail, task.taskDetail) && status == task.status && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nameTask, taskDetail, status, type);
    }

    protected String nameTask;
    protected String taskDetail;
    protected Status status;
    protected Type type;

    public Task(String nameTask, String taskDetail, Status status) {
        super();
        this.nameTask = nameTask;
        this.taskDetail = taskDetail;
        this.status = status;
    }

    public Task(String nameTask, String taskDetail, Status status, int id) {
        this.id=id;
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
