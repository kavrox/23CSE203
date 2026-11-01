# Edge-list graph ADT

`Graphs.edgelist.EdgeListGraph` stores vertices and edges in separate fixed-capacity arrays. An edge stores references to its two endpoints and an integer weight; there is no per-vertex adjacency index, so queries for incident edges scan the global edge array. Edges inserted without a weight default to weight `1`.

Let `V` and `E` be the current vertex and edge counts. Traversals cover the connected component reachable from their starting vertex.

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `EdgeListGraph(vertexCapacity, edgeCapacity)` | Allocate fixed-capacity vertex and edge arrays. | O(vertexCapacity + edgeCapacity) | O(vertexCapacity + edgeCapacity) |
| `numVertices()`, `numEdges()` | Return counts. | O(1) | O(1) |
| `vertices()` | Copy current vertex references into a new array. | O(V) | O(V) |
| `edges()` | Copy current edge references into a new array. | O(E) | O(E) |
| `getEdge(u, v)` | Scan edges for either endpoint order. | O(E) | O(1) |
| `endVertices(edge)` | Return its two endpoint references. | O(1) | O(1) |
| `opposite(vertex, edge)` | Return the other endpoint, or `null` if not an endpoint. | O(1) | O(1) |
| `outDegree(vertex)`, `inDegree(vertex)` | Count incident edges by scanning the edge array. | O(E) | O(1) |
| `outgoingEdges(vertex)` | Scan and copy all incident edges. | O(E) | O(d(v)) |
| `incomingEdges(vertex)` | Delegate to `outgoingEdges`. | O(E) | O(d(v)) |
| `insertVertex(element)` | Append a vertex if capacity remains. | O(1) | O(1) |
| `insertEdge(u, v, element)` | Check capacity and scan for duplicates, then append. | O(E) | O(1) |
| `insertEdge(u, v, element, weight)` | Insert an edge with an explicit integer weight. | O(E) | O(1) |
| `removeEdge(edge)` | Swap-delete using the edge's stored position. | O(1) | O(1) |
| `removeVertex(vertex)` | Scan all edges to remove incident edges, then swap-delete the vertex. | O(E) | O(1) |
| `bfs(start)` | Traverse with a queue; each reached vertex scans all edges to find neighbors. | O(VE) | O(V) |
| `dfs(start)` | Traverse recursively; each reached vertex scans all edges to find neighbors. | O(VE) | O(V) recursion/visited state |
| private `dfsHelper(vertex, visited)` | Visit a vertex and scan the global edge array recursively. | O(VE) over the traversal | O(V) recursion/visited state |
| `kruskalMst()` | Return a minimum spanning tree, or a minimum spanning forest if the graph is disconnected. | O(E log E) | O(E + V) |
| `boruvkaMst()` | Return a minimum spanning tree, or a minimum spanning forest if the graph is disconnected. | O(E log V) over successive component-merging rounds | O(V) |

The `vertex`/`Edge` constructors and field accessors are O(1). The `toString()` methods return the stored element and are O(1). The traversal costs are higher than adjacency lists because `outgoingEdges` scans the entire edge array on every visited vertex.
