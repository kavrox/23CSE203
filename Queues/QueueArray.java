package Queues;

public class QueueArray {
    private final int[] elements;
    private int front;
    private int size;

    public QueueArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        elements = new int[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    public int front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        return elements[front];
    }

    public void enqueue(int value) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full.");
        }
        elements[(front + size) % elements.length] = value;
        size++;
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        int value = elements[front];
        front = (front + 1) % elements.length;
        size--;
        return value;
    }
}
