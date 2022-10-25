package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistoryList() {
        return getTasks();
    }



    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        nodeMap.put(task.getId(), node);
    }
    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        Node node = first;
        while (node != null) {
            tasks.add((Task) node.task);
            node = node.next;
        }
        return tasks;
    }
}


