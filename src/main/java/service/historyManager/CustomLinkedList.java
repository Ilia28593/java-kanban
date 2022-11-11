package service.historyManager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomLinkedList {
    public final Map<Integer, Node<Task>> nodeMap = new HashMap<>();
    private static Node<Task> first;
    private static Node<Task> last;
    private int historySize;

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        Node<Task> node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    public void linkLast(Task task) {
        if(nodeMap.containsKey(task.getId())){
            nodeMap.get(task.getId()).task=task;
        } else {
            final Node<Task> node = new Node<>(task, last, null);
            checkSize();
            if (first == null) {
                first = node;
            } else {
                last.next = node;
            }
            last = node;
            nodeMap.put(task.getId(), node);
            historySize++;
        }
    }

    private void checkSize() {
        if (historySize >= 10) {
            remove(first.task.getId());
        }
    }

    public void remove(int id) {
        if (nodeMap.containsKey(id) && nodeMap.size()>0) {
            Task task = nodeMap.get(id).task;
            if (checkInFirstAndSetNext(task)) return;
            Node<Task> currentNode = first;
            if (updateNextInNew(task, currentNode)) return;
            nodeMap.remove(id);
            historySize--;
        }
    }

    private boolean checkInFirstAndSetNext(Task task) {
        if (first == null) {
            return true;
        }
        if (first.task.equals(task)) {
            first = first.next;
            return true;
        }
        return false;
    }

    private boolean updateNextInNew(Task task, Node<Task> currentNode) {
        while (currentNode.next != null) {
            if (currentNode.next.task.equals(task)) {
                currentNode.next = currentNode.next.next;
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }
}

class Node<Task> {
    Task task;
    Node<Task> next;
    Node<Task> before;

    public Node(Task task, Node<Task> before, Node<Task> next) {
        this.task = task;
        this.next = next;
        this.before = before;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(task, node.task) && Objects.equals(next, node.next) && Objects.equals(before, node.before);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, next, before);
    }
}
