package Stacks;

public class StackArray {
    private final int[] elements;
    private int size;

    public StackArray(int capacity) {
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

    public void push(int value) {
        if (isFull()) {
            throw new IllegalStateException("Stack is full.");
        }
        elements[size++] = value;
    }

    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        return elements[--size];
    }

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        return elements[size - 1];
    }
}
