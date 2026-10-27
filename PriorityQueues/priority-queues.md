# Priority-queue ADTs and heaps

A min-priority queue stores `(key, value)` entries and returns the value with the smallest key. The package demonstrates several implementations with different insertion/removal tradeoffs. When keys tie, the array and linked-list implementations preserve insertion order among equal keys; the BST sends equal keys to the right. The bounded implementations report/reject insertion when at capacity.

Let `n` be the number of entries, `C` the fixed capacity, and `h` the height of the unbalanced BST (`h` may be `n`).

## Binary min-heap (`PriorityQueues.Heap`)

The heap is a fixed-capacity integer min-heap stored in an array. Parent/child indices maintain the heap-order property.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `Heap(capacity)` | O(C) | O(C) |
| `size()`, `isEmpty()` | O(1) | O(1) |
| `add(value)` | O(log n) | O(1) |
| `min()` | O(1) | O(1) |
| `removeMin()` | O(log n) | O(1) |
| `display()` | O(n) | O(1) |
| private `swap(i, j)` | O(1) | O(1) |

## Sorted array (`PriorityQueues.PQSortedArrayInt`)

Entries remain sorted in ascending key order, so the minimum is at index zero. The backing array is fixed-capacity.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `PQSortedArrayInt(initialSize, capacity)` | O(C) | O(C) |
| `getSize()`, `isEmpty()` | O(1) | O(1) |
| `add(key, value)` | O(n) | O(1) |
| `min()` | O(1) | O(1) |
| `removeMin()` | O(n) | O(1) |

## Unsorted array (`PriorityQueues.PQUnsortedArrayInt`)

Insertion appends to a fixed-capacity array. Finding/removing the minimum requires a linear scan; removal replaces the removed slot with the final entry.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `PQUnsortedArrayInt(initialSize, capacity)` | O(C) | O(C) |
| `getSize()`, `isEmpty()` | O(1) | O(1) |
| `add(key, value)` | O(1) | O(1) |
| `min()` | O(n) | O(1) |
| `removeMin()` | O(n) | O(1) |

## Sorted doubly linked list (`PriorityQueues.PQSortedDoublyLinkedList`)

Nodes are linked in ascending key order; head is the minimum and tail is the maximum. The class limits the number of nodes to its configured capacity.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `PQSortedDoublyLinkedList(initialSize, capacity)` | O(1) | O(1) |
| `getSize()`, `isEmpty()` | O(1) | O(1) |
| `add(key, value)` | O(n) | O(1) per inserted node |
| `min()` | O(1) | O(1) |
| `removeMin()` | O(1) | O(1) |

## Binary-search-tree priority queue (`PriorityQueues.PQBinarySearchTree`)

Entries are stored by key in an unbalanced BST. The minimum is the leftmost node.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `PQBinarySearchTree(initialSize, capacity)` | O(1) | O(1) |
| `getSize()`, `isEmpty()` | O(1) | O(1) |
| `add(key, value)` | O(h), worst O(n) | O(1) per inserted node |
| `min()` | O(h), worst O(n) | O(1) |
| `removeMin()` | O(h), worst O(n) | O(1) |

## Locator-based sorted array (`PriorityQueues.locator.SortedArrayListPQ`)

This generic priority queue stores `Locator<E>` objects in an `ArrayList`, ordered by key. A locator retains its current array index for identity validation; insertion, removal, and priority change still shift list entries.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `SortedArrayListPQ()` | O(1) | O(1) |
| `insert(element, key)` | O(n) | O(1) amortized, excluding backing-array growth |
| `changePriority(locator, newKey)` | O(n) | O(1) |
| `extractMin()` | O(n) | O(1) |
| `isEmpty()` | O(1) | O(1) |
| `Locator(element, key, index)` | O(1) | O(1) |

`Locator` exposes `element`, `key`, and `index` as fields; direct field access/update is O(1). Constructors that accept `initialSize` currently initialize empty structures and do not populate that many entries.
