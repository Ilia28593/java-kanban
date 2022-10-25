package service;

public class Node<Task> {

    final Task task;
    Node<Task> next;
    Node<Task> last;

    public Node(Task task, Node<Task> last, Node<Task> next) {
        this.task = task;
        this.next = next;
        this.last = last;
    }

    @Override
    public String toString() {
        return "Node{" +
                "task=" + task +
                ", next=" + next +
                ", last=" + last +
                '}';
    }
}
