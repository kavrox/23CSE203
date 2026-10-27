package LinkedLists;

public class DoublyLL {
	private class Node{
		private Node prev;
		private int data;
		private Node next;
		
		public Node(Node prev, int data, Node next) {
			this.prev = prev;
			this.data = data;
			this.next = next;
		}

		public Node getPrev() {
			return prev;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

		public int getData() {
			return data;
		}

		public void setData(int data) {
			this.data = data;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}
	}
	private Node head;
	private Node tail;
	private int size;
	
	public DoublyLL() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public int getSize() {
		return size;
	}
	
	public void insAtFirst(int data) {
		Node nn = new Node(null, data, head);
		if (head == null) {
			head = nn;
			tail = nn;
		} else {
			head.setPrev(nn);
			head = nn;
		}
		size++;
	}
	
	public void insAtLast(int data) {
		Node nn = new Node(tail, data, null);
		if (head == null) {
			head = nn;
			tail = nn;
		} else {
			tail.setNext(nn);
			tail = nn;
		}
		size++;
	}
	
	public void insAtPos(int data, int pos) {
		if (pos <= 0 || pos > size + 1) {
			System.out.println("Invalid insertion pos");
		} else if (pos == 1) {
			insAtFirst(data);
		} else if (pos == (size + 1)) {
			insAtLast(data);
		} else { //pos 2 to size
			int index = 1;
			Node temp = head;
			while (index < pos) {
				temp = temp.getNext();
				index++;
			}
			//now temp node is the node at give pos.
			Node nn = new Node(temp.getPrev(),data,temp);
			temp.getPrev().setNext(nn);
			temp.setPrev(nn);
			size++;
		}
	}
	
	public Node delFirst() {
		if (head == null) {
			return null;
		}
		Node delNode = head;
		
		if (size == 1) {
			head = null;
			tail = null;
		} else {
			head.getNext().setPrev(null);
			head = head.getNext();
			delNode.setNext(null);
		}
		size--;
		return delNode;
	}
	
	public Node delLast() {
		if (head == null) {
			return null;
		} 
		Node delNode = tail;
		
		if (size == 1) {
			head = null;
			tail = null;
		} else {
			tail.getPrev().setNext(null);
			tail = tail.getPrev();
			delNode.setPrev(null);
		}
		size--;
		return delNode;
	}
	
	public Node delNode(int data) {
		if (head == null) {
			return head;
		}
		
		if (head.getData() == data) {
			return delFirst();
		}
		
		if (tail.getData() == data) {
			return delLast();
		}
		
		Node temp = head.getNext();
		while (temp != null && temp.getData() != data) {
			temp = temp.getNext();
		}
		if (temp != null) {
			temp.getPrev().setNext(temp.getNext());
			temp.getNext().setPrev(temp.getPrev());
			temp.setPrev(null);
			temp.setNext(null);
			size--;
		}
		return temp;
	}
	
	public void displayLL() {
		if (head == null) {
			System.out.println("Linked List is empty");
		} else {
			System.out.print("HEAD -> ");
			Node temp = head;
			while(temp != null) {
				System.out.print(temp.getData() + " <==> ");
				temp = temp.getNext();
			}
			System.out.print("\b\b\b\b\b<- TAIL\n");
		}
	}
	
	public boolean search(int data) {
		if (head == null) {
			return false;
		}
		
		if (head.getData() == data) {
			return true;
		}
		Node temp = head.getNext();
		while (temp != null) {
			if (temp.getData() == data) {
				return true;
			}
			temp = temp.getNext();
		}
		return false;
	}
	
	public void reverseLL() {
		if (head == null) {
			System.out.println("Empty LL");
		}
		
		else if (size == 1) {
			System.out.println("Only 1 node present in LL");
		}
		
		else {
			Node prev = null;
			Node temp = head;
			
			while (temp != null) {
				Node next = temp.getNext();
				temp.setPrev(next);
				temp.setNext(prev);
				prev = temp;
				temp = next;
			}
			
			tail = head;
			head = prev;
		}
	}
	
	public void update(int oldD, int newD) {
		if (head == null) {
			System.out.println("Empty LL");
		} else {
			Node temp = head;
			while (temp != null) {
				if (temp.getData() == oldD) {
					temp.setData(newD);
					return;
				}
				temp = temp.getNext();
			}
		}
		System.out.println("Data not found");
	}
	
	public boolean isEmpty() {
		return (size == 0);
	}
	
	public void clear() {
		head = null;
		tail = null;
		size = 0;
	}
	
	
}
