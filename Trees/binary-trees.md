# Binary-tree ADT

`Trees.BinaryTree` stores integer values in a general binary tree. `insertNode` fills the tree level by level, while `deleteLeaf` removes the first matching leaf in level-order traversal. Unlike a binary-search tree, the values are not ordered. Height uses the edge convention: an empty tree has height `-1` and a leaf has height `0`.

Let `n` be the number of nodes and `h` the tree height.

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `BinaryTree()` | Create an empty tree. | O(1) | O(1) |
| `getRoot()`, `setRoot(node)` | Read or replace the root reference. | O(1) | O(1) |
| `getSize()`, `setSize(size)` | Read or replace the stored node count. | O(1) | O(1) |
| `depth(node)` | Count parent links from the root, or return `-1` for `null`. | O(h) | O(h) recursion |
| `depth(node, value)` | Return the distance to the first matching descendant, or `-1` if absent. | O(n) | O(h) recursion |
| `preOrder(node)` | Print root-left-right traversal. | O(n) | O(h) recursion |
| `inOrder(node)` | Print left-root-right traversal. | O(n) | O(h) recursion |
| `postOrder(node)` | Print left-right-root traversal. | O(n) | O(h) recursion |
| `levelOrder(node)` | Print breadth-first traversal. | O(n) | O(n) queue |
| `height(node)` | Compute the edge-height of a subtree. | O(n) | O(h) recursion |
| `insertNode(data)` | Insert at the first open position in level order. | O(n) | O(n) queue |
| `deleteLeaf(data)` | Remove the first matching leaf in level order; return whether one was found. | O(n) | O(n) queue |

Each `Node` getter or setter takes O(1) time and O(1) extra space. `insertNode` and `deleteLeaf` update the tree's size. The root and size setters directly replace their fields and do not recalculate or validate the tree.
