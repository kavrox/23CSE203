package Queues;

public class QueueLinkedList {
    private static class Node {
        private final int value;
        private Node next;

        private Node(int value) {
            this.value = value;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        return front.value;
    }

    public void enqueue(int value) {
        Node newNode = new Node(value);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        int value = front.value;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return value;
    }
}
