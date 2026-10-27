# Binary-search-tree ADTs

`Trees.BinarySearchTree` stores integer keys in an unbalanced binary-search tree with parent links. Smaller keys go left, larger keys go right, and duplicate keys are rejected. Search-path operations cost O(h), where `h` is the tree height; in the worst case `h = n`.

The employee-management example in `Applications.EMP` contains a second BST keyed by employee ID. It is documented separately below because its nodes store employee records rather than integers.

## Integer BST (`Trees.BinarySearchTree`)

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `BinarySearchTree()` | Create an empty tree. | O(1) | O(1) |
| `insert(data)` | Insert a unique key, rejecting duplicates. | O(h), worst O(n) | O(1) auxiliary; O(1) new node |
| `search(key)` | Search for and print whether a key exists. | O(h), worst O(n) | O(1) |
| `delete(key)` | Locate and unlink a key; returns the removed node. | O(h), worst O(n) | O(1) |
| `inorder(node)` | Print keys in ascending order. | O(n) | O(h) recursion |
| `preorder(node)` | Print root-left-right traversal. | O(n) | O(h) recursion |
| `postorder(node)` | Print left-right-root traversal. | O(n) | O(h) recursion |
| `levelOrder()` | Print breadth-first traversal using a fixed-size array queue. | O(n) | O(1) allocated queue (100 node slots) |
| `height(node)` | Compute edge-height (`-1` for an empty subtree). | O(n) | O(h) recursion |
| `depth(key)` | Return the distance from root, or `-1` if missing. | O(h), worst O(n) | O(1) |

The nested `Node` field getters/setters (`getData`, `setData`, `getLeftChild`, `setLeftChild`, `getRightChild`, `setRightChild`, `getParent`, `setParent`) each take O(1) time and O(1) extra space. Recursive traversals and height calculation use call-stack space proportional to tree height.

Implementation notes: `delete` currently dereferences the replacement child when deleting a root leaf, so that case throws rather than completing. `levelOrder` uses a fixed 100-slot queue and cannot safely traverse a tree requiring more than 100 enqueued nodes. The complexity table describes the intended traversal/path work, not these capacity/edge-case failures.

## Employee-ID BST (`Applications.EMP.BinarySearch`)

This application-local tree stores `Employee` objects ordered by `Employee.getId()`.

| Method / operation | Time | Extra space |
|---|---:|---:|
| `insert(employee)` | O(h), worst O(n) | O(1) auxiliary; O(1) new node |
| `delete(id)` | O(h), worst O(n) | O(1) |
| `search(id)` | O(h), worst O(n) | O(1) |
| `display(node)` | O(n) | O(h) recursion |
| nested `Node` getters/setters | O(1) each | O(1) |

Implementation note: the application BST does not update the parent-tracking local while searching in `delete`, and its `search` and `display` methods can dereference a null node on missing/empty paths. The table gives the intended search-path/traversal work.
