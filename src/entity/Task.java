package entity;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private static int ids;

    private final int id;
    private int parentId;
    private String nameTask;
    private String taskDetail;
    private Status status;
    private Type type;
    private final List<Task> tasksPart;


    {
        tasksPart = new ArrayList<>();
    }

    public Task() {
        ids++;
        this.id = ids;
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "id=" + id;
        if (!(parentId == 0)) {
            result += ", parentId=" + parentId;
        }
        result += ", nameTask='" + nameTask + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", status=" + status.name() +
                ", type=" + type.name() +
                '}';
        return result;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Task> getTasksPart() {
        return tasksPart;
    }

    public void setTasksPart(Task task) {
        tasksPart.add(task);
    }
}
