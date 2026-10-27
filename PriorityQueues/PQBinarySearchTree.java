package PriorityQueues;

public class PQBinarySearchTree {
    private static class Node {
        private final int key;
        private final int value;
        private Node left;
        private Node right;

        private Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;
    private final int capacity;

    public PQBinarySearchTree(int initialSize, int capacity) {
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
        if (root == null) {
            root = newNode;
        } else {
            Node current = root;
            while (true) {
                if (key < current.key) {
                    if (current.left == null) {
                        current.left = newNode;
                        break;
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = newNode;
                        break;
                    }
                    current = current.right;
                }
            }
        }
        size++;
    }

    public int min() {
        if (isEmpty()) {
            System.err.println("Error: Priority Queue is empty.");
            return -1;
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    public int removeMin() {
        if (isEmpty()) {
            System.err.println("Error: Priority Queue is empty.");
            return -1;
        }

        Node parent = null;
        Node current = root;
        while (current.left != null) {
            parent = current;
            current = current.left;
        }
        int minValue = current.value;
        if (parent == null) {
            root = current.right;
        } else {
            parent.left = current.right;
        }
        size--;
        return minValue;
    }

    public static void main(String[] args) {
        PQBinarySearchTree pq = new PQBinarySearchTree(0, 10);
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
