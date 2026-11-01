# Adjacency-list graph ADT

`Graphs.adjacencylist.AdjacencyListGraph` represents an undirected graph with fixed-capacity arrays of vertices and edges. Each vertex stores its own incident-edge array sized to the graph's edge capacity. The graph avoids parallel edges by checking the source vertex's adjacency list before insertion. It supports vertex/edge inspection, deletion, BFS/DFS traversals and path queries, and Prim's minimum spanning tree algorithm. Edges inserted without a weight default to weight `1`.

Let `V` and `E` be the current vertex and edge counts; `deg(v)` is the degree of vertex `v`. Traversals cover only the connected component reachable from their starting vertex. Space costs below are auxiliary, not storage already owned by the graph.

| Method / operation | Description | Time | Extra space |
|---|---|---:|---:|
| `AdjacencyListGraph(vertexCapacity, edgeCapacity)` | Allocate fixed-capacity vertex/edge arrays. | O(vertexCapacity + edgeCapacity) | O(vertexCapacity + edgeCapacity) |
| `numVertices()`, `numEdges()` | Return counts. | O(1) | O(1) |
| `vertices()` | Copy the current vertex references into a new array. | O(V) | O(V) |
| `edges()` | Copy the current edge references into a new array. | O(E) | O(E) |
| `getEdge(u, v)` | Search `u`'s incident-edge array. | O(deg(u)) | O(1) |
| `endVertices(edge)` | Return its two endpoint references. | O(1) | O(1) |
| `opposite(vertex, edge)` | Return the other endpoint, or `null` if not an endpoint. | O(1) | O(1) |
| `outDegree(vertex)`, `inDegree(vertex)` | Return stored incident-edge count (the representation treats edges as undirected). | O(1) | O(1) |
| `outgoingEdges(vertex)` | Copy its incident edges into a new array. | O(deg(v)) | O(deg(v)) |
| `incomingEdges(vertex)` | Delegate to `outgoingEdges`. | O(deg(v)) | O(deg(v)) |
| `insertVertex(element)` | Allocate that vertex's edge array and append it if capacity remains. | O(edgeCapacity) | O(edgeCapacity) |
| `insertEdge(u, v, element)` | Check for an existing edge, then attach to both endpoints and global edge array. | O(deg(u)) | O(1) |
| `removeEdge(edge)` | Remove from endpoint arrays and swap-delete from global edge array. | O(deg(u) + deg(v)) | O(1) |
| `removeVertex(vertex)` | Repeatedly remove incident edges, then swap-delete the vertex. | O(deg(v) + sum(deg(u)) for its neighbors) | O(1) |
| `bfs(start)` | Breadth-first traversal of the reachable component. | O(V + E) | O(V) |
| `dfs(start)` | Depth-first traversal of the reachable component. | O(V + E) | O(V) recursion/visited state |
| private `dfsHelper(vertex, visited)` | Visit a vertex and recursively traverse its incident edges. | O(V + E) over the traversal | O(V) recursion/visited state |
| `hasPathBfs(start, destination)` | Test whether the destination is reachable from the start. | O(V + E) | O(V) |
| `findPathDfs(start, destination)` | Return one DFS path, or an empty array when no path exists or either vertex is not in this graph. | O(V + E) | O(V) |
| `insertEdge(u, v, element, weight)` | Insert an edge with an explicit integer weight. | O(deg(u)) | O(1) |
| `primMst(start)` | Return a minimum spanning tree for the start vertex's connected component. | O(E log E) | O(E + V) |

The `vertex`/`Edge` constructors and field accessors are O(1), except the `vertex` constructor, which allocates an `edgeCapacity`-sized adjacency array and costs O(edgeCapacity). `vertex.addEdge(edge)` is O(1); `vertex.removeEdge(edge)` searches that endpoint's incident-edge array and is O(deg(v)). Consequently, graph storage is O(vertexCapacity * edgeCapacity + edgeCapacity), even when most vertices have low degree.
