package PriorityQueues;

public class PQHeap {
    private static final int DEFAULT_CAPACITY = 50;

    public static final class Entry {
        private final int key;
        private final int value;

        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "[Priority Key: " + key + ", Value: " + value + "]";
        }
    }

    private final Entry[] heap;
    private int size;

    public PQHeap() {
        this(DEFAULT_CAPACITY);
    }

    public PQHeap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        heap = new Entry[capacity];
    }

    public PQHeap(Entry[] entries) {
        if (entries == null) {
            throw new IllegalArgumentException("Entries cannot be null.");
        }
        heap = new Entry[Math.max(DEFAULT_CAPACITY, entries.length)];
        for (Entry entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Entries cannot contain null.");
            }
        }
        System.arraycopy(entries, 0, heap, 0, entries.length);
        size = entries.length;
        buildHeap();
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == heap.length;
    }

    public void add(int key, int value) {
        if (isFull()) {
            throw new IllegalStateException("Priority queue is full.");
        }
        int index = size++;
        heap[index] = new Entry(key, value);
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[parent].key <= heap[index].key) {
                break;
            }
            swap(parent, index);
            index = parent;
        }
    }

    public Entry min() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty.");
        }
        return heap[0];
    }

    public Entry removeMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty.");
        }
        Entry minimum = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        if (!isEmpty()) {
            heapifyDown(0);
        }
        return minimum;
    }

    private void buildHeap() {
        for (int index = size / 2 - 1; index >= 0; index--) {
            heapifyDown(index);
        }
    }

    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;
            if (left < size && heap[left].key < heap[smallest].key) {
                smallest = left;
            }
            if (right < size && heap[right].key < heap[smallest].key) {
                smallest = right;
            }
            if (smallest == index) {
                return;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int first, int second) {
        Entry entry = heap[first];
        heap[first] = heap[second];
        heap[second] = entry;
    }
}
