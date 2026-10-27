# Adjacency-matrix graph ADT

`Graphs.adjacencymatrix.AdjacencyMatrixGraph` represents an undirected graph with a fixed-capacity vertex array and an `n × n` matrix of edge references. Both symmetric matrix cells refer to an inserted edge. This makes edge lookup constant-time, at the cost of quadratic storage and scanning.

Let `V` be the current vertex count and `d(v)` the degree. Traversals visit only vertices reachable from the start vertex.

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `AdjacencyMatrixGraph(capacity)` | Allocate a vertex array and square edge matrix. | O(capacity²) | O(capacity²) |
| `numVertices()`, `numEdges()` | Return counts. | O(1) | O(1) |
| `vertices()` | Copy the current vertex references. | O(V) | O(V) |
| `edges()` | Scan the matrix's upper triangle and collect edges. | O(V²) | O(E) |
| `getEdge(u, v)` | Read the matrix cell for the endpoints. | O(1) | O(1) |
| `endVertices(edge)` | Return its two endpoint references. | O(1) | O(1) |
| `opposite(vertex, edge)` | Return the other endpoint, or `null` if not an endpoint. | O(1) | O(1) |
| `outDegree(vertex)`, `inDegree(vertex)` | Scan the vertex's matrix row. | O(V) | O(1) |
| `outgoingEdges(vertex)` | Scan its row and collect non-null edges. | O(V) | O(d(v)) |
| `incomingEdges(vertex)` | Delegate to `outgoingEdges`. | O(V) | O(d(v)) |
| `insertVertex(element)` | Append a vertex if capacity remains. | O(1) | O(1) |
| `insertEdge(u, v, element)` | Check a matrix cell, then write both symmetric cells. | O(1) | O(1) |
| `removeEdge(edge)` | Clear both endpoint cells. | O(1) | O(1) |
| `removeVertex(vertex)` | Remove incident edges, move the final row/column into its position, and compact arrays. | O(V) | O(V) for the edge snapshot |
| `bfs(start)` | Breadth-first traversal, scanning each reached row. | O(V²) | O(V) |
| `dfs(start)` | Depth-first traversal, scanning each reached row. | O(V²) | O(V) recursion/visited state |
| private `dfsHelper(vertex, visited)` | Visit a vertex and scan its row recursively. | O(V²) over the traversal | O(V) recursion/visited state |

The `vertex`/`Edge` constructors and field accessors are O(1). For undirected graphs, `inDegree` and `outDegree` are equal in this implementation.
