package Manager;

import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {


    private HashMap<Integer, Node> customLinkedList = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (customLinkedList.containsKey(task.getId())) {
            removeNode(customLinkedList.get(task.getId()));
        }
        lastLink(task);
    }

    private void lastLink(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        customLinkedList.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        removeNode(customLinkedList.remove(id));
        customLinkedList.remove(id);

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node node) {
        if (head == null) {
            return;
        }
        if (head == tail) {
            head = null;
            tail = null;
            return;
        }
        if (head.data == node.data) {
            head = head.next;
            return;
        }
        Node currentNode = head;
        while (currentNode.next != null) {
            if (currentNode.next.data == node.data) {
                if (tail == currentNode.next) {
                    tail = currentNode;
                }
                currentNode.next = currentNode.next.next;
                return;
            }
            currentNode = currentNode.next;
        }

    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        tasks.add(currentNode.data);
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            tasks.add(currentNode.data);
        }
        return tasks;
    }

}
