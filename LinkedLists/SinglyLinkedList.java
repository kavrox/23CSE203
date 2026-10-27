package LinkedLists;

public class SinglyLinkedList {

    private class Node {
        private int val;
        private Node next;

        public Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public SinglyLinkedList() {
        head = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node last() {
        if (head == null) {
            return null;
        }

        Node temp = head;

        while (temp.getNext() != null) {
            temp = temp.getNext();
        }

        return temp;
    }

    public void insertAtFirst(int val) {
        head = new Node(val, head);
        size++;
    }

    public void insertAtLast(int val) {
        Node newNode = new Node(val, null);

        if (head == null) {
            head = newNode;
        } else {
            last().setNext(newNode);
        }

        size++;
    }

    // Positions are 1-indexed
    public void insertAtPos(int val, int pos) {
        if (pos < 1 || pos > size + 1) {
            System.err.println("Invalid insertion position");
            return;
        }

        if (pos == 1) {
            insertAtFirst(val);
            return;
        }

        if (pos == size + 1) {
            insertAtLast(val);
            return;
        }

        Node temp = head;

        for (int i = 1; i < pos - 1; i++) {
            temp = temp.getNext();
        }

        Node newNode = new Node(val, temp.getNext());
        temp.setNext(newNode);
        size++;
    }

    public Node delFirst() {
        if (head == null) {
            return null;
        }

        Node deleted = head;
        head = head.getNext();
        deleted.setNext(null);
        size--;

        return deleted;
    }

    public Node deleteLast() {
        if (head == null) {
            return null;
        }

        if (size == 1) {
            return delFirst();
        }

        Node temp = head;

        while (temp.getNext().getNext() != null) {
            temp = temp.getNext();
        }

        Node deleted = temp.getNext();
        temp.setNext(null);
        deleted.setNext(null);
        size--;

        return deleted;
    }

    public Node deleteNode(int val) {
        if (head == null) {
            return null;
        }

        if (head.getVal() == val) {
            return delFirst();
        }

        Node prev = head;
        Node temp = head.getNext();

        while (temp != null) {
            if (temp.getVal() == val) {
                prev.setNext(temp.getNext());
                temp.setNext(null);
                size--;

                return temp;
            }

            prev = temp;
            temp = temp.getNext();
        }

        return null;
    }

    public Node search(int val) {
        Node temp = head;

        while (temp != null) {
            if (temp.getVal() == val) {
                return temp;
            }

            temp = temp.getNext();
        }

        return null;
    }

    public boolean update(int oldVal, int newVal) {
        Node temp = search(oldVal);

        if (temp == null) {
            return false;
        }

        temp.setVal(newVal);
        return true;
    }

    public void reverseLinkedList() {
        Node prev = null;
        Node curr = head;

        while (curr != null) {
            Node next = curr.getNext();
            curr.setNext(prev);
            prev = curr;
            curr = next;
        }

        head = prev;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Head --> ");

        Node temp = head;

        while (temp != null) {
            sb.append(temp.getVal()).append(" --> ");
            temp = temp.getNext();
        }

        sb.append("NULL");
        return sb.toString();
    }
}
