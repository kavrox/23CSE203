package PriorityQueues;

public class PQBalancedBinarySearchTree {
    private static class Entry {
        private final int key;
        private final int value;
        private Entry parent;
        private Entry left;
        private Entry right;
        private int height = 1;

        private Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry root;
    private int size;

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int key, int value) {
        Entry entry = new Entry(key, value);
        if (root == null) {
            root = entry;
            size++;
            return;
        }

        Entry current = root;
        Entry parent;
        do {
            parent = current;
            current = key < current.key ? current.left : current.right;
        } while (current != null);

        entry.parent = parent;
        if (key < parent.key) {
            parent.left = entry;
        } else {
            parent.right = entry;
        }
        size++;
        rebalance(parent);
    }

    public int min() {
        return minimumEntry().value;
    }

    public int removeMin() {
        Entry minimum = minimumEntry();
        Entry parent = minimum.parent;
        Entry child = minimum.right;
        if (parent == null) {
            root = child;
        } else {
            parent.left = child;
        }
        if (child != null) {
            child.parent = parent;
        }

        minimum.parent = null;
        minimum.right = null;
        size--;
        if (parent != null) {
            rebalance(parent);
        }
        return minimum.value;
    }

    private Entry minimumEntry() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty.");
        }
        Entry current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private int height(Entry entry) {
        return entry == null ? 0 : entry.height;
    }

    private void updateHeight(Entry entry) {
        entry.height = 1 + Math.max(height(entry.left), height(entry.right));
    }

    private int balanceFactor(Entry entry) {
        return height(entry.left) - height(entry.right);
    }

    private void rebalance(Entry entry) {
        while (entry != null) {
            updateHeight(entry);
            int balance = balanceFactor(entry);
            Entry parent = entry.parent;

            if (balance > 1) {
                if (balanceFactor(entry.left) < 0) {
                    rotateLeft(entry.left);
                }
                rotateRight(entry);
            } else if (balance < -1) {
                if (balanceFactor(entry.right) > 0) {
                    rotateRight(entry.right);
                }
                rotateLeft(entry);
            }
            entry = parent;
        }
    }

    private void rotateLeft(Entry pivot) {
        Entry replacement = pivot.right;
        Entry middle = replacement.left;
        replaceParentChild(pivot, replacement);
        replacement.left = pivot;
        pivot.parent = replacement;
        pivot.right = middle;
        if (middle != null) {
            middle.parent = pivot;
        }
        updateHeight(pivot);
        updateHeight(replacement);
    }

    private void rotateRight(Entry pivot) {
        Entry replacement = pivot.left;
        Entry middle = replacement.right;
        replaceParentChild(pivot, replacement);
        replacement.right = pivot;
        pivot.parent = replacement;
        pivot.left = middle;
        if (middle != null) {
            middle.parent = pivot;
        }
        updateHeight(pivot);
        updateHeight(replacement);
    }

    private void replaceParentChild(Entry oldChild, Entry newChild) {
        Entry parent = oldChild.parent;
        newChild.parent = parent;
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else {
            parent.right = newChild;
        }
    }
}
