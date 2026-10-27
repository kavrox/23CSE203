package PriorityQueues;

public class PQUnsortedArrayInt {
    private static class Entry {
        private final int key;
        private final int value;

        private Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Entry[] queue;
    private int size;

    public PQUnsortedArrayInt(int initialSize, int capacity) {
        this.queue = new Entry[capacity];
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int key, int value) {
        if (size == queue.length) {
            System.err.println("Error: Priority Queue is full.");
            return;
        }
        queue[size++] = new Entry(key, value);
    }

    public int min() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority Queue is empty");
        }
        int minIndex = 0;
        for (int index = 1; index < size; index++) {
            if (queue[index].key < queue[minIndex].key) {
                minIndex = index;
            }
        }
        return queue[minIndex].value;
    }

    public int removeMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority Queue is empty");
        }
        int minIndex = 0;
        for (int index = 1; index < size; index++) {
            if (queue[index].key < queue[minIndex].key) {
                minIndex = index;
            }
        }
        int minValue = queue[minIndex].value;
        queue[minIndex] = queue[--size];
        queue[size] = null;
        return minValue;
    }

    public static void main(String[] args) {
        PQUnsortedArrayInt pq = new PQUnsortedArrayInt(0, 10);
        pq.add(5, 100);
        pq.add(1, 200);
        pq.add(3, 300);
        System.out.println("Minimum value: " + pq.min());
        System.out.println("Removed minimum: " + pq.removeMin());
        System.out.println("New minimum: " + pq.min());
    }
}
