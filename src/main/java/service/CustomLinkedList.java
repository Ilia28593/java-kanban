package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomLinkedList {
    public final static Map<Integer, Node<Task>> nodeMap = new HashMap<>();
    private static Node<Task> first;
    private static Node<Task> last;

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
        final Node<Task> node = new Node<>(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        nodeMap.put(task.getId(), node);
    }

    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Task task = nodeMap.get(id).task;
            if (first == null) {
                return;
            }
            if (first.task == task) {
                first = first.next;
                return;
            }
            Node<Task> currentNode = first;
            while (currentNode.next != null) {
                if (currentNode.next.task == task) {
                    currentNode.next = currentNode.next.next;
                    return;
                }
                currentNode = currentNode.next;
            }
            nodeMap.remove(id);
        }
    }
}

class Node<Task> {
    final Task task;
    Node<Task> next;
    Node<Task> before;

    public Node(Task task, Node<Task> before, Node<Task> next) {
        this.task = task;
        this.next = next;
        this.before = before;
    }
}
