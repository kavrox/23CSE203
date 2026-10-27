package PriorityQueues;

public class PQSortedArrayInt {
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

    public PQSortedArrayInt(int initialSize, int capacity) {
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
            System.out.println("Error: Priority Queue is full.");
            return;
        }
        int index = size - 1;
        while (index >= 0 && queue[index].key > key) {
            queue[index + 1] = queue[index];
            index--;
        }
        queue[index + 1] = new Entry(key, value);
        size++;
    }

    public int min() {
        if (isEmpty()) {
            System.err.println("Error: Priority Queue is empty.");
            return -1;
        }
        return queue[0].value;
    }

    public int removeMin() {
        if (isEmpty()) {
            System.err.println("Error: Priority Queue is empty.");
            return -1;
        }
        int minValue = queue[0].value;
        for (int index = 1; index < size; index++) {
            queue[index - 1] = queue[index];
        }
        queue[--size] = null;
        return minValue;
    }

    public static void main(String[] args) {
        PQSortedArrayInt pq = new PQSortedArrayInt(0, 10);
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
