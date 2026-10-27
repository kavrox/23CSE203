
package Trees;

import java.util.Scanner;

public class BinarySearchTree {

    private class Node {

        private int data;
        private Node left;
        private Node right;
        private Node parent;

        Node(int data) {
            this.data = data;
            left = null;
            right = null;
            parent = null;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getLeftChild() {
            return left;
        }

        public void setLeftChild(Node left) {
            this.left = left;
        }

        public Node getRightChild() {
            return right;
        }

        public void setRightChild(Node right) {
            this.right = right;
        }

        
        public Node getParent() {
            return parent;
        }

        
        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

    private Node root;


    // Time Complexity: O(n)
    public void insert(int data) {

        Node newNode = new Node(data);

        if (root == null) {
            root = newNode;
            root.setParent(null);
            return;
        }

        Node temp = root;

        while (true) {

            if (data < temp.getData()) {

                if (temp.getLeftChild() == null) {
                    temp.setLeftChild(newNode);
                    newNode.setParent(temp);
                    return;
                }

                temp = temp.getLeftChild();
            }

            else if (data > temp.getData()) {

                if (temp.getRightChild() == null) {
                    temp.setRightChild(newNode);
                    newNode.setParent(temp);
                    return;
                }

                temp = temp.getRightChild();
            }

            else {
                System.out.println("Duplicate key");
                return;
            }
        }
    }

   //Time complexity : O(n)
    public Node delete(int key) {


        Node prev = null;
        Node temp = root;

        while (temp != null && temp.getData() != key) {

            prev = temp;

            if (key < temp.getData())
                temp = temp.getLeftChild();
            else
                temp = temp.getRightChild();
        }

        if (temp == null) {
            System.out.println("Key not found");
            return null;
        }

        // Node has two children
        if (temp.getLeftChild() != null &&
            temp.getRightChild() != null) {

            Node prevTemp = temp;
            Node temp2 = temp.getRightChild();

            while (temp2.getLeftChild() != null) {

                prevTemp = temp2;
                temp2 = temp2.getLeftChild();
            }

            // Copy inorder successor's data
            temp.setData(temp2.getData());
            temp = temp2;
            prev = prevTemp;

        }

        // Node has one child
        
            Node child;

            if (temp.getLeftChild() != null)
                child = temp.getLeftChild();
            else
                child = temp.getRightChild();

            if (prev == null) {

                root = child;
                child.setParent(null);

            }
            else if (prev.getLeftChild() == temp) {

                prev.setLeftChild(child);
                child.setParent(prev);

            }
            else {

                prev.setRightChild(child);
                
            }

            temp.setParent(null);
            if(child != null){
                child.setParent(prev);
            }

            return temp;
        
    }


    // Time Complexity: O(n)
    public void search(int key) {

        Node temp = root;

        while (temp != null) {

            if (temp.getData() == key) {
                System.out.println("Key found");
                return;
            }

            if (key < temp.getData())
                temp = temp.getLeftChild();
            else
                temp = temp.getRightChild();
        }

        System.out.println("Key not found");
    }


    public void preorder(Node temp) {

        if (temp == null)
            return;

        System.out.print(temp.getData() + " ");

        preorder(temp.getLeftChild());
        preorder(temp.getRightChild());
    }

    
    public void postorder(Node temp) {

        if (temp == null)
            return;

        postorder(temp.getLeftChild());
        postorder(temp.getRightChild());

        System.out.print(temp.getData() + " ");
    }

   
    // Time Complexity: O(n) 
    public void inorder(Node temp) {

        if (temp == null)
            return;

        inorder(temp.getLeftChild());

        System.out.print(temp.getData() + " ");

        inorder(temp.getRightChild());
    }

    
    // Time Complexity: O(n) 
    public void levelOrder() {

        if (root == null)
            return;

        Node[] queue = new Node[100];

        int front = 0;
        int rear = 0;

        queue[rear] = root;
        rear++;

        while (front < rear) {

            Node temp = queue[front];
            front++;

            System.out.print(temp.getData() + " ");

            if (temp.getLeftChild() != null) {
                queue[rear] = temp.getLeftChild();
                rear++;
            }

            if (temp.getRightChild() != null) {
                queue[rear] = temp.getRightChild();
                rear++;
            }
        }
    }

    
    // Time Complexity: O(n) 
    public int height(Node temp) {

        if (temp == null)
            return -1;

        int leftHeight = height(temp.getLeftChild());
        int rightHeight = height(temp.getRightChild());

        return 1 + Math.max(leftHeight, rightHeight);
    }

    
    // Time Complexity: O(n) 
    public int depth(int key) {

        Node temp = root;
        int depth = 0;

        while (temp != null) {

            if (temp.getData() == key)
                return depth;

            if (key < temp.getData())
                temp = temp.getLeftChild();
            else
                temp = temp.getRightChild();

            depth++;
        }

        return -1;
    }

        public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BinarySearchTree tree = new BinarySearchTree();

        System.out.print("Enter number of nodes: ");
        int n = sc.nextInt();

        System.out.println("Enter elements:");

        for (int i = 0; i < n; i++) {
            tree.insert(sc.nextInt());
        }

        System.out.print("Inorder: ");
        tree.inorder(tree.root);
        System.out.println();

        System.out.print("Preorder: ");
        tree.preorder(tree.root);
        System.out.println();

        System.out.print("Postorder: ");
        tree.postorder(tree.root);
        System.out.println();

        System.out.print("Level Order: ");
        tree.levelOrder();
        System.out.println();

        System.out.print("Enter key to search: ");
        int searchKey = sc.nextInt();
        tree.search(searchKey);

        System.out.print("Enter key to delete: ");
        int deleteKey = sc.nextInt();
        tree.delete(deleteKey);

        System.out.print("Inorder after deletion: ");
        tree.inorder(tree.root);
        System.out.println();

        System.out.println("Height: " + tree.height(tree.root));

        System.out.print("Enter key to find depth: ");
        int depthKey = sc.nextInt();

        int d = tree.depth(depthKey);

        if (d == -1)
            System.out.println("Key not found");
        else
            System.out.println("Depth: " + d);

        sc.close();
    }
}
