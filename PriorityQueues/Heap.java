package PriorityQueues;

public class Heap {
    private final int[] heap;
    private int size;

    public Heap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        heap = new int[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int value) {
        if (size == heap.length) {
            throw new IllegalStateException("Heap is full.");
        }
        int index = size++;
        heap[index] = value;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[parent] <= heap[index]) {
                break;
            }
            swap(parent, index);
            index = parent;
        }
    }

    public int min() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty.");
        }
        return heap[0];
    }

    public int removeMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty.");
        }
        int minimum = heap[0];
        heap[0] = heap[--size];
        int index = 0;
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;
            if (left < size && heap[left] < heap[smallest]) {
                smallest = left;
            }
            if (right < size && heap[right] < heap[smallest]) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
        return minimum;
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    private void swap(int first, int second) {
        int temp = heap[first];
        heap[first] = heap[second];
        heap[second] = temp;
    }
}
