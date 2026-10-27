# Disjoint-set ADT and cycle detection

`Graphs.mst.MSTCycleDetection` uses a disjoint-set union (DSU, or union-find) structure. Each vertex starts in its own set; `find` uses path compression and `union` uses rank. The graph stores an insertion-ordered list of undirected edges and uses the DSU to detect cycles or to filter edges that do not create a cycle.

Let `V` be the number of vertices and `E` the number of edges processed. `α(V)` is the inverse Ackermann function, which is effectively constant for practical inputs. The DSU operations have O(α(V)) amortized cost with path compression plus union by rank; an individual operation is O(log V) worst-case under the rank invariant.

## Disjoint set

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `DisjointSet(n)` | Initialize `parent` and `rank` arrays so every element is its own set. | O(V) | O(V) |
| `find(i)` | Return the set representative and compress the search path. | O(α(V)) amortized; O(log V) worst-case | O(log V) recursion worst-case |
| `union(i, j)` | Merge the two sets by rank; performs two finds. | O(α(V)) amortized; O(log V) worst-case | O(1) |

## Cycle-detection graph

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `MSTCycleDetection(vertices)` | Store vertex count and create an empty edge list. | O(1) | O(1) |
| `addEdge(u, v, weight)` | Append an edge to the list. | O(1) amortized | O(1) per edge |
| `containsCycle()` | Process edges with DSU; return on the first edge whose endpoints already share a set. | O(E α(V)) amortized; early exit may reduce work | O(V) |
| `simulateMSTEdgeFiltering()` | Scan input edges and retain those connecting distinct DSU sets. | O(E α(V)) amortized | O(V + E) including retained-edge list |

The filtering demonstration does not sort the edges by weight; it illustrates cycle filtering in input order, not a complete Kruskal minimum-spanning-tree algorithm. The `Edge` constructor and field access are O(1).
