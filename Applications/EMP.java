package Applications;

class Employee{
    private int empid;
    private String Name;
    private String department;
    private int salary;

    Employee(int empid , String Name , String department , int salary){
        this.empid = empid;
        this.Name = Name;
        this.department = department;
        this.salary = salary;
    }

    public void setId(int empid) {
        this.empid = empid;
    }
    public void setName(String name) {
        Name = name;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getId() {
        return empid;
    }
    public String getName() {
        return Name;
    }
    public String getDepartment() {
        return department;
    }
    public int getSalary() {
        return salary;
    }

}

class ServiceManagement{
    private int reqid;
    private int empid;
    private String desc;
    private int priority;

    ServiceManagement(int reqid , int empid , String desc  , int priority){
        this.reqid = reqid;
        this.empid = empid;
        this.desc = desc;
        this.priority = priority;
    }

    public void setReqid(int reqid) {
        this.reqid = reqid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getReqid() {
        return reqid;
    }

    public int getEmpid() {
        return empid;
    }

    public String getDesc() {
        return desc;
    }

    public int getPriority() {
        return priority;
    }   
}
class BinarySearch{
    
    class Node{
        private Employee data;
        private Node left;
        private Node right;
        private Node parent;

        Node(Employee data){
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        public void setData(Employee data) {
            this.data = data;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Employee getData() {
            return data;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public Node getParent() {
            return parent;
        }

    }
    
    private Node root;

    public void insert(Employee data){
        
        Node newNode = new Node(data);

        if(root==null){
            root = newNode;
        }
        else{
            Node temp = root;
            while(temp!=null && temp.getData().getId() != data.getId()){
                if(data.getId() < temp.getData().getId()){
                    if(temp.getLeft() == null){
                        temp.setLeft(newNode);
                        newNode.setParent(temp);
                    }
                    temp = temp.getLeft();
                    return;
                }
                else{
                    if(temp.getRight() == null){
                        temp.setRight(newNode);
                        newNode.setParent(temp);
                    }
                    temp = temp.getRight();
                    return;
                }
            }
            System.out.println("Duplicate ID's not allowed");
        }
    }

    public void delete(int ID){

        Node prev = null;
        Node temp = root;

        if(root == null){
            System.out.println("List Empty");
        }

        else{
            while(temp != null && temp.getData().getId() != ID){
                if(ID < temp.getData().getId()){
                    temp = temp.getLeft();
                }
                else{
                    temp = temp.getRight();
                }
            }
            if(temp == null){
                System.out.println("Id not found");
            }
            else{
                //two child
                if(temp.getLeft()!=null && temp.getRight()!= null){
                    Node prevTemp = temp;
                    Node temp2 = temp.getRight();

                    while (temp2.getLeft() != null) {

                        prevTemp = temp2;
                        temp2 = temp2.getLeft();
                    }

                    // Copy inorder successor's data
                    temp.setData(temp2.getData());
                    temp = temp2;
                    prev = prevTemp;
                }

                //one child

                Node child;

                if (temp.getLeft() != null)
                    child = temp.getLeft();
                else
                    child = temp.getRight();

                if (prev == null) {
                    root = child;
                    if (child != null) {
                        child.setParent(null);
                    }
                }

                else if (prev.getLeft() == temp) {

                    prev.setLeft(child);
                    child.setParent(prev);

                }
                else {

                    prev.setRight(child);
                    
                }

                temp.setParent(null);
                if(child != null){
                    child.setParent(prev);
                }
            }
        }

    }

    public void search(int ID){

        if(root == null){
            System.out.println("Empty list");
        }
        else{
            Node temp = root;

            while(temp != null && temp.getData().getId()!= ID){
                if(ID < temp.getData().getId()){
                    temp = temp.getLeft();
                }
                else{
                    temp = temp.getRight();
                }
                
            }
            if(temp== null){
                    System.out.println("ID not found");
            }

            System.out.println("Employee ID : " + temp.getData().getId() + "\n" +
                                "Employee Name : " + temp.getData().getName() + "\n" +
                                "Department Name : " + temp.getData().getDepartment() + "\n" +
                                "Salary : " + temp.getData().getSalary());
        }
    }

    public void display(Node temp){

        if(root == null){
            System.out.println("List Empty");
        }
        else{
            display(temp.getLeft());
            System.out.print(temp.getData().getId() + " ");
            display(temp.getRight());
        }
    }
    
}

class Stack{
    private int size;
    private int empid;
    private int capacity;

    int[] history;

    Stack(int capacity){
        this.size = 0;
        this.capacity = capacity;

        this.history = new int[capacity];
        
    }

    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public boolean isFull(){
        return size==capacity;
    }

    public void setEmpid(int empid){
        this.empid = empid;
    }
    public int getEmpid(){
        return empid;
    }

    public void push(int empid){
        if(isFull()){
            System.out.println("Stack Overflow Error");
        }
        else{
            history[size] = empid;
            size++;
        }
    }

    public int pop(){
        if(isEmpty()){
            System.out.println("Stack UnderFlow Error");
            return 0;
        }
        else{
            size--;
            return history[size];
        }
    }

    public int peek(){
        if(isEmpty()){
            System.out.println("Stack UnderFlow Error");
            return 0;
        }
        else{
            return history[size];
        }
    }

    public void display(){
        if(isEmpty()){
            System.out.println("Stack UnderFlow Error");
        }

        else{
            for(int i = 0 ; i < size ; i++){
                System.out.println("Employee ID : " + history[i] + "\n");
            }
        }
    }
}
class Queue{
    private int size;
    private int capacity;
    private int front;
    private ServiceManagement service;

    ServiceManagement[] serviceQueue;


    Queue(int capacity){
        this.size = 0;
        this.front = 0;
        this.capacity = capacity;
        this.serviceQueue = new ServiceManagement[capacity];
    }

    public int size(){
        return size;
    }
    public int front(){
        return front;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public boolean isFull(){
        return size==capacity;
    }

    public void setService(ServiceManagement service){
        this.service = service;
    }
    public ServiceManagement getService(){
        return service;
    }

    public void enqueue(ServiceManagement service){
        if(isFull()){
            System.out.println("Queue Overflow Error");
        }
        else{
            serviceQueue[(front+size)%capacity] = service;
            size++;
        }
    }
    public ServiceManagement dequeue(){
        if(isEmpty()){
            System.out.println("Queue UnderFlow Error");
            return null;
        }
        else{
            ServiceManagement temp = serviceQueue[front];
            front = (front+1)%capacity;
            size--;
            return temp;
        }
    } 

    public ServiceManagement first(){
        if(isEmpty()){
            System.out.println("Queue UnderFlow Error");
            return null;
        }
        else{
            ServiceManagement temp = serviceQueue[front];
            return temp;
        }
    }

    public void display(){
        if(isEmpty()){
            System.out.println("Queue Underflow Error");
        }
        else{
            for(int i = 0 ; i <size ; i++){
                int index = (front + i) % capacity;
                ServiceManagement element = serviceQueue[index];
                System.out.println("Request ID : " + element.getReqid() + "\n" +
                                    "Employee ID : " + element.getEmpid() + "\n" +
                                    "Description : " + element.getDesc() + "\n" + 
                                    "Priority : " + element.getPriority() +"\n\n\n");
            }
        }
    }
}

public class EMP {

    public static void main(String args[]){
        
        BinarySearch list = new BinarySearch();
        Stack historyStack = new Stack(50);
        Queue ser = new Queue(50);
        
        Employee emp1 = new Employee(103 , "John" , "CSE" , 20000);
        Employee emp2 = new Employee(101 , "Ann" , "Sales" , 30000);
        Employee emp3 = new Employee(105 , "Brooke" , "Marketing" , 10000);
        Employee emp4 = new Employee(100 , "Ben" , "Manager" , 50000);
        Employee emp5 = new Employee(102 , "Ron" , "CSE" , 90000);
        Employee emp6 = new Employee(104 , "Joel" , "CSE" , 90000);
        Employee emp7 = new Employee(106 , "Mike" , "CSE" , 100000);

        list.insert(emp1);
        list.insert(emp2);
        list.insert(emp3);
        list.insert(emp4);
        list.insert(emp5);
        list.insert(emp6);
        list.insert(emp7);

        ServiceManagement ser1 = new ServiceManagement(1, 100, "Completed", 1);
        ServiceManagement ser2 = new ServiceManagement(2, 101, "NOT Comepleted", 2);
        ServiceManagement ser3 = new ServiceManagement(3, 102, "Completed", 3);
        ServiceManagement ser4 = new ServiceManagement(4, 103, "Completed", 4);
        ServiceManagement ser5 = new ServiceManagement(5, 104, "Completed", 5);
        ServiceManagement ser6 = new ServiceManagement(6, 105, "Completed", 6);
        ServiceManagement ser7 = new ServiceManagement(7, 106, "Completed", 7);

        ser.enqueue(ser1);
        ser.enqueue(ser2);
        ser.enqueue(ser3);
        ser.enqueue(ser4);
        ser.enqueue(ser5);
        ser.enqueue(ser6);
        ser.enqueue(ser7);

        historyStack.push(100);
        historyStack.push(101);
        historyStack.push(102);
        historyStack.push(103);
        historyStack.push(104);
        historyStack.push(105);
        historyStack.push(106);
        historyStack.push(107);

        ser.display();
        System.out.println("\n");
        historyStack.display();
        System.out.println("\n");
        list.search(105);
        System.out.println("\n");
        ser.first();
        System.out.println("\n");
        historyStack.peek();
        System.out.println("\n");

        historyStack.pop();
        System.out.println("\n");
        ser.dequeue();
        System.out.println("\n");
        System.out.println("\n");
    }  
}
