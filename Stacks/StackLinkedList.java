package Stacks;

public class StackLinkedList {
    private static class Node {
        private final int value;
        private Node next;

        private Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node top;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void push(int value) {
        top = new Node(value, top);
        size++;
    }

    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        int value = top.value;
        top = top.next;
        size--;
        return value;
    }

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        return top.value;
    }
}
