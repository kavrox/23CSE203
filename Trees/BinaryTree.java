package Trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
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

    public boolean isEmpty() {
        return root == null;
    }

    public boolean isRoot(Node node) {
        return node != null && node == root;
    }

    public boolean hasLeft(Node node) {
        return node != null && node.getLeft() != null;
    }

    public boolean hasRight(Node node) {
        return node != null && node.getRight() != null;
    }

    public boolean isInternal(Node node) {
        return hasLeft(node) || hasRight(node);
    }

    public boolean isExternal(Node node) {
        return node != null && !isInternal(node);
    }

    public int replace(Node node, int value) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }
        int previousValue = node.getVal();
        node.setVal(value);
        return previousValue;
    }

    public List<Node> children(Node node) {
        List<Node> result = new ArrayList<>(2);
        if (hasLeft(node)) {
            result.add(node.getLeft());
        }
        if (hasRight(node)) {
            result.add(node.getRight());
        }
        return result;
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

    public void reversePreOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.getVal() + " ");
        reversePreOrder(node.getRight());
        reversePreOrder(node.getLeft());
    }

    public void reverseInOrder(Node node) {
        if (node == null) {
            return;
        }
        reverseInOrder(node.getRight());
        System.out.print(node.getVal() + " ");
        reverseInOrder(node.getLeft());
    }

    public void reversePostOrder(Node node) {
        if (node == null) {
            return;
        }
        reversePostOrder(node.getRight());
        reversePostOrder(node.getLeft());
        System.out.print(node.getVal() + " ");
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

    public int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }

    public int countExternalNodes(Node node) {
        if (node == null) {
            return 0;
        }
        if (isExternal(node)) {
            return 1;
        }
        return countExternalNodes(node.getLeft()) + countExternalNodes(node.getRight());
    }

    public int countInternalNodes(Node node) {
        if (node == null || !isInternal(node)) {
            return 0;
        }
        return 1 + countInternalNodes(node.getLeft()) + countInternalNodes(node.getRight());
    }

    public int sumOfAll(Node node) {
        if (node == null) {
            return 0;
        }
        return node.getVal() + sumOfAll(node.getLeft()) + sumOfAll(node.getRight());
    }

    public Node addRoot(int value) {
        if (root != null) {
            throw new IllegalStateException("Tree is not empty.");
        }
        root = new Node(null, value, null, null);
        size = 1;
        return root;
    }

    public Node insertLeft(Node node, int value) {
        requireMember(node);
        if (hasLeft(node)) {
            throw new IllegalArgumentException("Node already has a left child.");
        }
        Node child = new Node(node, value, null, null);
        node.setLeft(child);
        size++;
        return child;
    }

    public Node insertRight(Node node, int value) {
        requireMember(node);
        if (hasRight(node)) {
            throw new IllegalArgumentException("Node already has a right child.");
        }
        Node child = new Node(node, value, null, null);
        node.setRight(child);
        size++;
        return child;
    }

    public int remove(Node node) {
        requireMember(node);
        if (hasLeft(node) && hasRight(node)) {
            throw new IllegalArgumentException("Cannot remove a node with two children.");
        }

        Node parent = node.getParent();
        Node child = hasLeft(node) ? node.getLeft() : node.getRight();
        if (node == root) {
            root = child;
        } else if (parent == null
                || (parent.getLeft() != node && parent.getRight() != node)) {
            throw new IllegalArgumentException("Node is not attached to this tree.");
        } else if (parent.getLeft() == node) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }

        if (child != null) {
            child.setParent(parent);
        }
        int removedValue = node.getVal();
        node.setParent(null);
        node.setLeft(null);
        node.setRight(null);
        size--;
        return removedValue;
    }

    public void attach(Node node, BinaryTree leftTree, BinaryTree rightTree) {
        if (node == null || leftTree == null || rightTree == null) {
            throw new IllegalArgumentException("Node and trees cannot be null.");
        }
        if (leftTree == this || rightTree == this || leftTree == rightTree) {
            throw new IllegalArgumentException("Attached trees must be distinct from each other and this tree.");
        }
        requireMember(node);
        if (isInternal(node)) {
            throw new IllegalArgumentException("Cannot attach to a node with children.");
        }

        size += leftTree.size + rightTree.size;
        if (leftTree.root != null) {
            leftTree.root.setParent(node);
            node.setLeft(leftTree.root);
            leftTree.root = null;
            leftTree.size = 0;
        }
        if (rightTree.root != null) {
            rightTree.root.setParent(node);
            node.setRight(rightTree.root);
            rightTree.root = null;
            rightTree.size = 0;
        }
    }

    private void requireMember(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }
        Node current = node;
        while (current != root) {
            Node parent = current.getParent();
            if (parent == null || (parent.getLeft() != current && parent.getRight() != current)) {
                throw new IllegalArgumentException("Node is not attached to this tree.");
            }
            current = parent;
        }
        if (root == null) {
            throw new IllegalArgumentException("Node is not attached to this tree.");
        }
    }

    public Node findNode(Node node, int value) {
        if (node == null || node.getVal() == value) {
            return node;
        }
        Node leftResult = findNode(node.getLeft(), value);
        return leftResult != null ? leftResult : findNode(node.getRight(), value);
    }

    public boolean search(Node node, int value) {
        return findNode(node, value) != null;
    }

    public void delete() {
        Node deepest = deepestRightmostNode();
        if (deepest != null) {
            remove(deepest);
        }
    }

    public boolean delete(int value) {
        if (root == null) {
            return false;
        }

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        Node target = null;
        Node deepest = null;
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            if (current.getVal() == value) {
                target = current;
            }
            deepest = current;
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }

        if (target == null) {
            return false;
        }
        if (target != deepest) {
            target.setVal(deepest.getVal());
        }
        remove(deepest);
        return true;
    }

    private Node deepestRightmostNode() {
        if (root == null) {
            return null;
        }
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        Node deepest = null;
        while (!queue.isEmpty()) {
            deepest = queue.remove();
            if (deepest.getLeft() != null) {
                queue.add(deepest.getLeft());
            }
            if (deepest.getRight() != null) {
                queue.add(deepest.getRight());
            }
        }
        return deepest;
    }

    public boolean isBST(Node node) {
        return isBST(node, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isBST(Node node, long minimum, long maximum) {
        if (node == null) {
            return true;
        }
        int value = node.getVal();
        return value > minimum && value < maximum
                && isBST(node.getLeft(), minimum, value)
                && isBST(node.getRight(), value, maximum);
    }

    public boolean isBalanced(Node node) {
        return balancedHeight(node) >= 0;
    }

    private int balancedHeight(Node node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = balancedHeight(node.getLeft());
        if (leftHeight < 0) {
            return -1;
        }
        int rightHeight = balancedHeight(node.getRight());
        if (rightHeight < 0 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }
        return 1 + Math.max(leftHeight, rightHeight);
    }

    public Node lowestCommonAncestor(Node node, int firstValue, int secondValue) {
        if (!search(node, firstValue) || !search(node, secondValue)) {
            return null;
        }
        return lowestCommonAncestorNode(node, firstValue, secondValue);
    }

    private Node lowestCommonAncestorNode(Node node, int firstValue, int secondValue) {
        if (node == null || node.getVal() == firstValue || node.getVal() == secondValue) {
            return node;
        }
        Node leftAncestor = lowestCommonAncestorNode(node.getLeft(), firstValue, secondValue);
        Node rightAncestor = lowestCommonAncestorNode(node.getRight(), firstValue, secondValue);
        if (leftAncestor != null && rightAncestor != null) {
            return node;
        }
        return leftAncestor != null ? leftAncestor : rightAncestor;
    }

    public int diameter(Node node) {
        if (node == null) {
            return 0;
        }
        int throughNode = height(node.getLeft()) + height(node.getRight()) + 2;
        return Math.max(throughNode,
                Math.max(diameter(node.getLeft()), diameter(node.getRight())));
    }

    public void mirrorTree(Node node) {
        if (node == null) {
            return;
        }
        mirrorTree(node.getLeft());
        mirrorTree(node.getRight());
        Node left = node.getLeft();
        node.setLeft(node.getRight());
        node.setRight(left);
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
