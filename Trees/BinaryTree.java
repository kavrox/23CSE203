package Trees;

import java.util.ArrayDeque;
import java.util.Queue;

public class BinaryTree {
    public class Node {
        private Node parent;
        private int val;
        private Node left;
        private Node right;

        public Node(Node parent, int val, Node left, Node right) {
            this.parent = parent;
            this.val = val;
            this.left = left;
            this.right = right;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    private Node root;
    private int size;

    public BinaryTree() {
        root = null;
        size = 0;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int depth(Node node) {
        if (node == null) {
            return -1;
        }
        return 1 + depth(node.getParent());
    }

    public int depth(Node node, int value) {
        if (node == null) {
            return -1;
        }

        int distance = -1;
        if (node.getVal() == value
                || (distance = depth(node.getLeft(), value)) >= 0
                || (distance = depth(node.getRight(), value)) >= 0) {
            return distance + 1;
        }
        return -1;
    }

    public void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.getVal());
        preOrder(node.getLeft());
        preOrder(node.getRight());
    }

    public void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft());
        System.out.print(node.getVal());
        inOrder(node.getRight());
    }

    public void postOrder(Node node) {
        if (node == null) {
            return;
        }
        postOrder(node.getLeft());
        postOrder(node.getRight());
        System.out.print(node.getVal());
    }

    public void levelOrder(Node node) {
        if (node == null) {
            return;
        }

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            System.out.print(current.getVal() + " ");
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }
    }

    public int height(Node node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
    }

    public void insertNode(int data) {
        if (root == null) {
            root = new Node(null, data, null, null);
            size++;
            return;
        }

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            if (current.getLeft() == null) {
                current.setLeft(new Node(current, data, null, null));
                size++;
                return;
            }
            queue.add(current.getLeft());

            if (current.getRight() == null) {
                current.setRight(new Node(current, data, null, null));
                size++;
                return;
            }
            queue.add(current.getRight());
        }
    }

    public boolean deleteLeaf(int data) {
        if (root == null) {
            return false;
        }

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            if (current.getLeft() == null && current.getRight() == null) {
                if (current.getVal() == data) {
                    if (current == root) {
                        root = null;
                    } else if (current.getParent().getLeft() == current) {
                        current.getParent().setLeft(null);
                    } else {
                        current.getParent().setRight(null);
                    }
                    size--;
                    return true;
                }
            } else {
                if (current.getLeft() != null) {
                    queue.add(current.getLeft());
                }
                if (current.getRight() != null) {
                    queue.add(current.getRight());
                }
            }
        }
        return false;
    }
}
