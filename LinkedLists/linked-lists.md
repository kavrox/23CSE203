# Linked-list ADTs

The linked-list package contains a singly linked list (`LinkedLists.SinglyLinkedList`) and a doubly linked list (`LinkedLists.DoublyLL`). Both store integers and maintain a size counter. The singly linked list stores a head reference; the doubly linked list stores head and tail references, with both `next` and `prev` links. Positions are 1-based in the positional insertion methods.

Let `n` be the number of elements. The tables describe worst-case running time unless noted. Returning a detached node does not include any later caller work.

## Singly linked list

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `SinglyLinkedList()` | Create an empty list. | O(1) | O(1) |
| `getSize()` | Return the stored size. | O(1) | O(1) |
| `isEmpty()` | Test whether size is zero. | O(1) | O(1) |
| `last()` | Find and return the final node. | O(n) | O(1) |
| `insertAtFirst(val)` | Link a new node before the head. | O(1) | O(1) |
| `insertAtLast(val)` | Find the tail with `last()` and append. | O(n) | O(1) |
| `insertAtPos(val, pos)` | Validate and insert at a 1-based position. | O(n) | O(1) |
| `delFirst()` | Unlink and return the head; return `null` if empty. | O(1) | O(1) |
| `deleteLast()` | Find the predecessor of the tail and unlink it. | O(n) | O(1) |
| `deleteNode(val)` | Find and unlink the first matching value. | O(n) | O(1) |
| `search(val)` | Return the first matching node or `null`. | O(n) | O(1) |
| `update(oldVal, newVal)` | Search for the first match and replace its value. | O(n) | O(1) |
| `reverseLinkedList()` | Reverse all `next` links in place. | O(n) | O(1) |
| `clear()` | Drop the head reference and reset size. | O(1) | O(1) |
| `toString()` | Traverse and construct a string for the list. | O(n) | O(n) |

## Doubly linked list

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `DoublyLL()` | Create an empty list. | O(1) | O(1) |
| `getHead()`, `setHead(node)` | Read or replace the head reference. | O(1) | O(1) |
| `getTail()`, `setTail(node)` | Read or replace the tail reference. | O(1) | O(1) |
| `getSize()` | Return the stored size. | O(1) | O(1) |
| `isEmpty()` | Test whether size is zero. | O(1) | O(1) |
| `insAtFirst(data)` | Insert before the head, updating both links. | O(1) | O(1) |
| `insAtLast(data)` | Insert after the tail, updating both links. | O(1) | O(1) |
| `insAtPos(data, pos)` | Find and insert at a 1-based position. | O(n) | O(1) |
| `delFirst()` | Unlink and return the head; return `null` if empty. | O(1) | O(1) |
| `delLast()` | Unlink and return the tail. | O(1) | O(1) |
| `delNode(data)` | Find and unlink the first matching value. | O(n) | O(1) |
| `search(data)` | Test whether a value occurs in the list. | O(n) | O(1) |
| `reverseLL()` | Reverse all links and exchange head and tail. | O(n) | O(1) |
| `update(oldD, newD)` | Find and replace the first matching value. | O(n) | O(1) |
| `clear()` | Drop head/tail references and reset size. | O(1) | O(1) |
| `displayLL()` | Print all nodes from head to tail. | O(n) | O(1) |

`Node` getter/setter methods in both implementations take O(1) time and O(1) extra space. They expose or update one node field.
