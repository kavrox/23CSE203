package PriorityQueues;

public class PQSortedDoublyLinkedList {
    private static class Node {
        private final int key;
        private final int value;
        private Node prev;
        private Node next;

        private Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final int capacity;

    public PQSortedDoublyLinkedList(int initialSize, int capacity) {
        this.capacity = capacity;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int key, int value) {
        if (size == capacity) {
            System.err.println("Error: Priority Queue is full.");
            return;
        }
        Node newNode = new Node(key, value);
        if (isEmpty()) {
            head = tail = newNode;
        } else if (key < head.key) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            Node current = head;
            while (current != null && current.key <= key) {
                current = current.next;
            }
            if (current == null) {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            } else {
                Node previous = current.prev;
                previous.next = newNode;
                newNode.prev = previous;
                newNode.next = current;
                current.prev = newNode;
            }
        }
        size++;
    }

    public int min() {
        if (isEmpty()) {
            System.err.println("Error: Priority Queue is empty.");
            return -1;
        }
        return head.value;
    }

    public int removeMin() {
        if (isEmpty()) {
            System.err.println("Error: Priority Queue is empty.");
            return -1;
        }
        int minValue = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return minValue;
    }

    public static void main(String[] args) {
        PQSortedDoublyLinkedList pq = new PQSortedDoublyLinkedList(0, 10);
        pq.add(5, 100);
        pq.add(1, 200);
        pq.add(3, 300);
        System.out.println("Minimum value: " + pq.min());
        System.out.println("Removed minimum: " + pq.removeMin());
        System.out.println("New minimum: " + pq.min());
        pq.removeMin();
        pq.removeMin();
        System.out.println("Attempting to remove from empty queue: " + pq.removeMin());
    }
}
