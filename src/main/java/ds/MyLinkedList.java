package ds;
import metrics.Metrics;

public class MyLinkedList implements IntList {
    private static class Node {
        int val;
        Node next;
        Node(int val) { this.val = val; }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics;

    public MyLinkedList(Metrics metrics) {
        this.metrics = metrics;
        this.size = 0;
    }

    @Override
    public void add(int x) {
        Node newNode = new Node(x);
        if (head == null) {
            head = tail = newNode;
            metrics.moves++;
        } else {
            tail.next = newNode;
            tail = newNode;
            metrics.moves += 2;
        }
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index");
        Node newNode = new Node(x);

        if (index == 0) {
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = head;
            metrics.moves += 2;
        } else {
            Node curr = head;
            metrics.steps++;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
                metrics.steps++;
            }
            newNode.next = curr.next;
            curr.next = newNode;
            if (newNode.next == null) tail = newNode;
            metrics.moves += 2;
        }
        size++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");

        if (index == 0) {
            head = head.next;
            if (head == null) tail = null;
            metrics.moves++;
        } else {
            Node curr = head;
            metrics.steps++;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
                metrics.steps++;
            }
            curr.next = curr.next.next;
            if (curr.next == null) tail = curr;
            metrics.moves++;
        }
        size--;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");
        Node curr = head;
        metrics.steps++;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
            metrics.steps++;
        }
        return curr.val;
    }

    @Override
    public boolean contains(int x) {
        Node curr = head;
        if (curr != null) metrics.steps++;
        while (curr != null) {
            metrics.comparisons++;
            if (curr.val == x) return true;

            curr = curr.next;
            if (curr != null) metrics.steps++;
        }
        return false;
    }
}