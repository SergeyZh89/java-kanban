package Manager;

import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected HashMap<Integer, Node> customLinkedList = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void remove(int id) {
        removeNode(customLinkedList.remove(id));
        customLinkedList.remove(id);
    }

    @Override
    public void add(Task task) {
        if (task != null && customLinkedList.containsKey(task.getId())) {
            removeNode(customLinkedList.get(task.getId()));
        }
        lastLink(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node node) {
        if (node != null) {
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
    }

    private void lastLink(Task task) {
        if (task != null) {
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
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        if (currentNode != null) {
            tasks.add(currentNode.data);
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                tasks.add(currentNode.data);
            }
        }
        return tasks;
    }
}