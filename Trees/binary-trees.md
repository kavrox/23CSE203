# Binary-tree ADT

`Trees.BinaryTree` stores integer values in a general binary tree. `insertNode` fills the tree level by level, while `deleteLeaf` removes the first matching leaf in level-order traversal. The class also supports explicit child insertion/removal, attaching two trees, deepest/rightmost deletion, tree metrics, reverse traversals, and structural checks. Unlike a binary-search tree, the values are not ordered. Height uses the edge convention: an empty tree has height `-1` and a leaf has height `0`.

Let `n` be the number of nodes and `h` the tree height.

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `BinaryTree()` | Create an empty tree. | O(1) | O(1) |
| `getRoot()`, `setRoot(node)` | Read or replace the root reference. | O(1) | O(1) |
| `getSize()`, `setSize(size)` | Read or replace the stored node count. | O(1) | O(1) |
| `isEmpty()`, `isRoot(node)`, `hasLeft(node)`, `hasRight(node)`, `isInternal(node)`, `isExternal(node)` | Inspect tree/node state. | O(1) | O(1) |
| `replace(node, value)` | Replace a node's value and return the previous value. | O(1) | O(1) |
| `children(node)` | Return existing children in left-to-right order. | O(1) | O(1) |
| `depth(node)` | Count parent links from the root, or return `-1` for `null`. | O(h) | O(h) recursion |
| `depth(node, value)` | Return the distance to the first matching descendant, or `-1` if absent. | O(n) | O(h) recursion |
| `preOrder(node)` | Print root-left-right traversal. | O(n) | O(h) recursion |
| `inOrder(node)` | Print left-root-right traversal. | O(n) | O(h) recursion |
| `postOrder(node)` | Print left-right-root traversal. | O(n) | O(h) recursion |
| `reversePreOrder(node)`, `reverseInOrder(node)`, `reversePostOrder(node)` | Print the corresponding traversal with left and right exchanged. | O(n) | O(h) recursion |
| `levelOrder(node)` | Print breadth-first traversal. | O(n) | O(n) queue |
| `height(node)` | Compute the edge-height of a subtree. | O(n) | O(h) recursion |
| `countNodes(node)`, `countExternalNodes(node)`, `countInternalNodes(node)`, `sumOfAll(node)` | Count nodes/leaves/internal nodes or sum subtree values. | O(n) | O(h) recursion |
| `addRoot(value)` | Add the root to an empty tree. | O(1) | O(1) |
| `insertLeft(node, value)`, `insertRight(node, value)` | Add a child to an attached node; update parent links and stored size. | O(h) | O(1) per new node |
| `remove(node)` | Remove an attached node with at most one child, return its value, and reconnect its child. | O(h) | O(1) |
| `attach(node, leftTree, rightTree)` | Adopt the roots of two trees at an attached leaf; the source trees become empty. | O(h) | O(1) |
| `findNode(node, value)`, `search(node, value)` | Find/check the first matching value in preorder. | O(n) | O(h) recursion |
| `insertNode(data)` | Insert at the first open position in level order. | O(n) | O(n) queue |
| `deleteLeaf(data)` | Remove the first matching leaf in level order; return whether one was found. | O(n) | O(n) queue |
| `delete()` | Remove the deepest, rightmost node. | O(n) | O(n) queue |
| `delete(value)` | Replace the last matching node in level order with the deepest, rightmost value, then remove that deepest node. | O(n) | O(n) queue |
| `isBST(node)`, `isBalanced(node)` | Check strict BST ordering or height balance. | O(n) | O(h) recursion |
| `lowestCommonAncestor(node, first, second)` | Return the lowest common ancestor, or `null` if either value is absent. | O(n) | O(h) recursion |
| `diameter(node)` | Return the longest path in edges. | O(n²) | O(h) recursion |
| `mirrorTree(node)` | Swap left/right children throughout a subtree. | O(n) | O(h) recursion |

Each `Node` getter or setter takes O(1) time and O(1) extra space. Insertion and deletion methods update the tree's size. The root and size setters directly replace their fields and do not recalculate or validate the tree. The diameter implementation favors clarity and recomputes subtree heights.
