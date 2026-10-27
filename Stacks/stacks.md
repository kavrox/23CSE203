# Stack ADTs

The stack ADT is last-in, first-out (LIFO): insertion and removal happen at the top. `Stacks.StackArray` uses a fixed-capacity array; `Stacks.StackLinkedList` uses one node per element and grows until memory is exhausted.

Let `n` be the current number of elements and `C` the array capacity. Unless stated otherwise, operations take worst-case time.

## Array-backed stack

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `StackArray(capacity)` | Allocate the backing array. | O(C) | O(C) |
| `size()` | Return the number of stored elements. | O(1) | O(1) |
| `isEmpty()` | Check whether the stack is empty. | O(1) | O(1) |
| `isFull()` | Check whether the backing array is full. | O(1) | O(1) |
| `push(value)` | Add at the top; throws if full. | O(1) | O(1) |
| `pop()` | Remove and return the top; throws if empty. | O(1) | O(1) |
| `peek()` | Return the top without removing it; throws if empty. | O(1) | O(1) |

## Linked-list-backed stack

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `StackLinkedList()` | Create an empty stack. | O(1) | O(1) |
| `size()` | Return the stored size. | O(1) | O(1) |
| `isEmpty()` | Check whether the top reference is `null`. | O(1) | O(1) |
| `push(value)` | Allocate a node and link it at the top. | O(1) | O(1) per push |
| `pop()` | Unlink and return the top; throws if empty. | O(1) | O(1) |
| `peek()` | Return the top without removing it; throws if empty. | O(1) | O(1) |

## Employee application stack

`Applications.EMP` includes a separate bounded integer `Stack` used for employee-ID history. Its `size()`, `isEmpty()`, `isFull()`, `setEmpid(...)`, `getEmpid()`, `push(...)`, and `pop()` operations are O(1); `peek()` is O(1); and `display()` is O(n). Construction allocates a capacity-`C` array in O(C) time and space. The auxiliary employee-ID field accessors are O(1). This application-specific class is separate from the reusable stack classes above.

Implementation note: in `Applications.EMP.Stack`, `peek()` indexes `history[size]` rather than the last occupied slot (`history[size - 1]`), so its current result is not the usual stack-top value. Its running time remains O(1).
