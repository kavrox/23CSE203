package PriorityQueues;

public class PQUnsortedDoublyLinkedList {
    private static class Entry {
        private final int key;
        private final int value;
        private Entry previous;
        private Entry next;

        private Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry head;
    private Entry tail;
    private int size;

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int key, int value) {
        Entry entry = new Entry(key, value);
        if (tail == null) {
            head = entry;
        } else {
            tail.next = entry;
            entry.previous = tail;
        }
        tail = entry;
        size++;
    }

    public int min() {
        return minimumEntry().value;
    }

    public int removeMin() {
        Entry minimum = minimumEntry();
        if (minimum.previous == null) {
            head = minimum.next;
        } else {
            minimum.previous.next = minimum.next;
        }
        if (minimum.next == null) {
            tail = minimum.previous;
        } else {
            minimum.next.previous = minimum.previous;
        }
        size--;
        return minimum.value;
    }

    private Entry minimumEntry() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty.");
        }
        Entry minimum = head;
        for (Entry current = head.next; current != null; current = current.next) {
            if (current.key < minimum.key) {
                minimum = current;
            }
        }
        return minimum;
    }
}
