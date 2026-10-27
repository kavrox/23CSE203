# Queue ADTs

The queue ADT is first-in, first-out (FIFO): enqueue at the rear and dequeue at the front. `Queues.QueueArray` is a fixed-capacity circular buffer; `Queues.QueueLinkedList` stores front and rear node references. The employee application also has a fixed-capacity circular-array queue.

Let `n` be the current number of elements and `C` the array capacity.

## Circular-array queue

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `QueueArray(capacity)` | Allocate the backing array. | O(C) | O(C) |
| `size()` | Return the number of stored elements. | O(1) | O(1) |
| `isEmpty()` | Check whether the queue is empty. | O(1) | O(1) |
| `isFull()` | Check whether all array slots are occupied. | O(1) | O(1) |
| `front()` | Read the first element; throws if empty. | O(1) | O(1) |
| `enqueue(value)` | Add at the circular rear; throws if full. | O(1) | O(1) |
| `dequeue()` | Remove and return the front; throws if empty. | O(1) | O(1) |

## Linked-list queue

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `QueueLinkedList()` | Create an empty queue. | O(1) | O(1) |
| `size()` | Return the stored size. | O(1) | O(1) |
| `isEmpty()` | Check whether the front reference is `null`. | O(1) | O(1) |
| `front()` | Read the front element; throws if empty. | O(1) | O(1) |
| `enqueue(value)` | Append a newly allocated node at the rear. | O(1) | O(1) per enqueue |
| `dequeue()` | Remove and return the front node; throws if empty. | O(1) | O(1) |

## Employee application queue

`Applications.EMP` includes a separate bounded circular-array `Queue` of service requests. `size()`, `front()`, `isEmpty()`, `isFull()`, `setService(...)`, `getService()`, `enqueue(...)`, `dequeue()`, and `first()` are O(1); `display()` is O(n). The `front()` accessor returns the stored front index, while `first()` returns the front request. Construction allocates a capacity-`C` array in O(C) time and space.
