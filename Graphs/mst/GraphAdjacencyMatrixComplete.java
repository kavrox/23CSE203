import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Stack;
import java.util.ArrayList;

class Vertex implements Comparable<Vertex> {
    private String element;
    private int position;

    Vertex(String element, int position) {
        this.element = element;
        this.position = position;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }

    @Override
    public int compareTo(Vertex other) {
        return this.element.compareTo(other.element);
    }
}

class Edge {
    private String element;
    private Vertex origin;
    private Vertex dest;
    private int weight; // Added to support weighted algorithms

    Edge(String element, Vertex origin, Vertex dest, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setWeight(int w) { this.weight = w; }
    public int getWeight() { return weight; }
}

// Shared Priority Queue Node for Dijkstra and Prim-Jarnik Algorithms
class PQNode implements Comparable<PQNode> {
    Vertex vertex;
    int value; // Represents cost in Dijkstra or distance in Prim-Jarnik

    PQNode(Vertex vertex, int value) {
        this.vertex = vertex;
        this.value = value;
    }

    @Override
    public int compareTo(PQNode other) {
        return Integer.compare(this.value, other.value);
    }
}

class AdjacencyMatrixGraph {
    private Vertex[] vertex; 
    private int vcount;
    private int ecount;
    private Edge[][] adjMat;
    private int maxCapacity;
    
    // Arrays for Goodrich style DFS/BFS traversal labels (using position as index)[cite: 2]
    private String[] vertexLabels;
    private String[][] edgeLabels;

    // Time Complexity: O(V^2) - Instantiating a 2D array of size V x V takes quadratic time relative to max vertices.
    AdjacencyMatrixGraph(int n) {
        this.maxCapacity = n;
        vertex = new Vertex[n];
        adjMat = new Edge[n][n];
        vertexLabels = new String[n];
        edgeLabels = new String[n][n]; // 2D array to track labels mapped perfectly to the Adjacency Matrix structure
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }

    // Time Complexity: O(1) - Returning an array reference takes constant time.
    public Vertex[] vertices() { return vertex; }

    // Time Complexity: O(V^2) - Iterates through the upper triangle of the V x V matrix to collect distinct edges.
    public Edge[] edges() {
        Edge[] e = new Edge[ecount];
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            for (int j = i; j < vcount; j++) { 
                if (adjMat[i][j] != null) {
                    e[c++] = adjMat[i][j];
                }
            }
        }
        return e;
    }

    // Time Complexity: O(1) - Directly accesses the 2D array using the vertices' position metadata.
    public Edge getEdge(Vertex u, Vertex v) {
        int uPos = u.getPosition();
        int vPos = v.getPosition();
        return adjMat[uPos][vPos];
    }

    // Time Complexity: O(1) - Array creation and fetching known object properties is constant time.
    public Vertex[] endVertices(Edge e) {
        Vertex[] v = new Vertex[2];
        v[0] = e.getOrigin();
        v[1] = e.getDest();
        return v;
    }

    // Time Complexity: O(1) - Max two string comparisons and fetching endpoints is constant time.
    public Vertex opposite(Vertex v, Edge e) {
        Vertex[] t = new Vertex[2];
        t[0] = e.getOrigin();
        t[1] = e.getDest();
        if (v.compareTo(t[0]) == 0) return t[1];
        else if (v.compareTo(t[1]) == 0) return t[0];
        else return null;
    }

    // Time Complexity: O(V) - Must scan the entire row corresponding to vertex v in the adjacency matrix.
    public int outDegree(Vertex v) {
        int count = 0;
        int pos = v.getPosition();
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                count++;
            }
        }
        return count;
    }

    // Time Complexity: O(V) - Must scan the entire column corresponding to vertex v in the adjacency matrix.
    public int inDegree(Vertex v) {
        int count = 0;
        int pos = v.getPosition();
        for (int i = 0; i < vcount; i++) {
            if (adjMat[i][pos] != null) {
                count++;
            }
        }
        return count;
    }

    // Time Complexity: O(V) - Scans the column for vertex v to populate an array of incoming edges.
    public Edge[] incomingEdge(Vertex v) {
        Edge[] incoming = new Edge[inDegree(v)];
        int pos = v.getPosition();
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[i][pos] != null) {
                incoming[c++] = adjMat[i][pos];
            }
        }
        return incoming;
    }

    // Time Complexity: O(V) - Scans the row for vertex v to populate an array of outgoing edges.
    public Edge[] outgoingEdge(Vertex v) {
        Edge[] outgoing = new Edge[outDegree(v)];
        int pos = v.getPosition();
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                outgoing[c++] = adjMat[pos][i];
            }
        }
        return outgoing;
    }

    // Time Complexity: O(1) - Assigns a reference to a pre-known array index.
    public void insertVertex(String x) {
        if (vcount >= vertex.length) return;
        Vertex v = new Vertex(x, vcount);
        vertex[vcount] = v;
        vertexLabels[vcount] = "UNEXPLORED";
        vcount++;
    }

    // Time Complexity: O(1) - Instantiates an object and assigns it to specific row/col indices in the 2D array.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) {
        Edge e = new Edge(x, u, v, weight);
        int uPos = u.getPosition();
        int vPos = v.getPosition();
        adjMat[uPos][vPos] = e;
        adjMat[vPos][uPos] = e; 
        edgeLabels[uPos][vPos] = "UNEXPLORED";
        edgeLabels[vPos][uPos] = "UNEXPLORED";
        ecount++;
    }

    // Time Complexity: O(1) - Directly accesses the endpoints and nullifies the matrix cells.
    public void removeEdge(Edge e) {
        int uPos = e.getOrigin().getPosition();
        int vPos = e.getDest().getPosition();
        
        if (adjMat[uPos][vPos] != null) {
            adjMat[uPos][vPos] = null;
            adjMat[vPos][uPos] = null; 
            edgeLabels[uPos][vPos] = null;
            edgeLabels[vPos][uPos] = null;
            ecount--;
        }
    }

    // Time Complexity: O(V) - Removes edges in O(V), then copies row/col from the last vertex in O(V), avoiding full matrix shifts.
    public void removeVertex(Vertex v) {
        int pos = v.getPosition();
        if (pos < 0 || pos >= vcount) return;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                removeEdge(adjMat[pos][i]);
            }
        }
        int lastIndex = vcount - 1;
        if (pos != lastIndex) {
            Vertex lastVertex = vertex[lastIndex];
            vertex[pos] = lastVertex;
            lastVertex.setPosition(pos);
            vertexLabels[pos] = vertexLabels[lastIndex];
            
            for (int i = 0; i < vcount; i++) {
                adjMat[pos][i] = adjMat[lastIndex][i];
                adjMat[i][pos] = adjMat[i][lastIndex];
                edgeLabels[pos][i] = edgeLabels[lastIndex][i];
                edgeLabels[i][pos] = edgeLabels[i][lastIndex];
            }
            adjMat[pos][pos] = adjMat[lastIndex][lastIndex]; 
            edgeLabels[pos][pos] = edgeLabels[lastIndex][lastIndex];
        }
        for (int i = 0; i < vcount; i++) {
            adjMat[lastIndex][i] = null;
            adjMat[i][lastIndex] = null;
            edgeLabels[lastIndex][i] = null;
            edgeLabels[i][lastIndex] = null;
        }
        vertex[lastIndex] = null;
        vertexLabels[lastIndex] = null;
        vcount--;
    }
    
    // ==========================================
    // TRAVERSAL LABELS (Goodrich Style)
    // ==========================================
    // Time Complexity: O(1) - Array assignment is constant time.
    public void setLabel(Vertex v, String label) { 
        vertexLabels[v.getPosition()] = label; 
    }
    // Time Complexity: O(1) - Array lookup is constant time.
    public String getLabel(Vertex v) { 
        String label = vertexLabels[v.getPosition()];
        return label != null ? label : "UNEXPLORED"; 
    }
    // Time Complexity: O(1) - 2D Array assignment using endpoint positions is constant time.
    public void setLabel(Edge e, String label) { 
        int uPos = e.getOrigin().getPosition();
        int vPos = e.getDest().getPosition();
        edgeLabels[uPos][vPos] = label;
        edgeLabels[vPos][uPos] = label; // Maintain symmetry for undirected graph
    }
    // Time Complexity: O(1) - 2D Array lookup using endpoint positions is constant time.
    public String getLabel(Edge e) { 
        String label = edgeLabels[e.getOrigin().getPosition()][e.getDest().getPosition()];
        return label != null ? label : "UNEXPLORED";
    }

    // ==========================================
    // DEPTH FIRST SEARCH (DFS) - Iterative
    // ==========================================
    // Time Complexity: O(V^2) - For an Adjacency Matrix, finding neighbors requires scanning the entire row of size V for every vertex.
    public void performDFS() {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        
        Edge[] allEdges = edges();
        for (int i = 0; i < ecount; i++) setLabel(allEdges[i], "UNEXPLORED");
        
        System.out.print("DFS Traversal: ");
        Stack<Vertex> stack = new Stack<>();
        
        for (int i = 0; i < vcount; i++) {
            if (getLabel(vertex[i]).equals("UNEXPLORED")) {
                stack.push(vertex[i]);
                
                while (!stack.isEmpty()) {
                    Vertex v = stack.pop();
                    
                    if (getLabel(v).equals("UNEXPLORED")) {
                        setLabel(v, "VISITED");
                        System.out.print(v.getElement() + " ");
                        
                        Edge[] outgoing = outgoingEdge(v);
                        for (int j = outgoing.length - 1; j >= 0; j--) {
                            Edge e = outgoing[j];
                            if (getLabel(e).equals("UNEXPLORED")) {
                                Vertex w = opposite(v, e);
                                if (getLabel(w).equals("UNEXPLORED")) {
                                    setLabel(e, "DISCOVERY");
                                    stack.push(w);
                                } else {
                                    setLabel(e, "FORWARD_OR_CROSS"); 
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println();
    }

    // ==========================================
    // BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V^2) - For an Adjacency Matrix, scanning neighbors for queued vertices takes O(V) each time.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        
        Queue<Vertex> queue = new LinkedList<>(); 
        queue.add(v1); 
        setLabel(v1, "VISITED"); 
        
        System.out.print("BFS Path Search (" + v1.getElement() + " -> " + v2.getElement() + "): ");
        
        while (!queue.isEmpty()) { 
            Vertex v = queue.poll(); 
            System.out.print(v.getElement() + " ");
            
            if (v.compareTo(v2) == 0) { 
                System.out.println("\n-> A path is found!"); 
                return true;
            }
            
            Edge[] outgoing = outgoingEdge(v);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex n = opposite(v, e);
                if (getLabel(n).equals("UNEXPLORED")) {
                    setLabel(n, "VISITED"); 
                    queue.add(n); 
                }
            }
        }
        System.out.println("\n-> Path is not found."); 
        return false;
    }

    // ==========================================
    // DIJKSTRA'S SHORTEST PATH
    // ==========================================
    // Time Complexity: O(V^2 \log V) - Scanning neighbors takes O(V) per vertex, and adding elements to the priority queue takes O(\log V).
    public void dijkstra(Vertex v1, Vertex v2) {
        // Array based tracking instead of HashMaps
        int[] cost = new int[maxCapacity];
        Vertex[] previous = new Vertex[maxCapacity];
        boolean[] visited = new boolean[maxCapacity];
        PriorityQueue<PQNode> pqueue = new PriorityQueue<>();

        for (int i = 0; i < vcount; i++) {
            Vertex v = vertex[i];
            cost[v.getPosition()] = Integer.MAX_VALUE; 
            previous[v.getPosition()] = null;          
            visited[v.getPosition()] = false;
        }

        cost[v1.getPosition()] = 0; 
        pqueue.add(new PQNode(v1, 0)); 

        while (!pqueue.isEmpty()) { 
            PQNode current = pqueue.poll(); 
            Vertex v = current.vertex; 
            int pos = v.getPosition();

            if (visited[pos]) continue;
            visited[pos] = true; 

            if (v.compareTo(v2) == 0) break;

            Edge[] outgoing = outgoingEdge(v);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex n = opposite(v, e);
                int nPos = n.getPosition();
                
                if (!visited[nPos] && cost[pos] != Integer.MAX_VALUE) {
                    int newCost = cost[pos] + e.getWeight(); 

                    if (newCost < cost[nPos]) { 
                        cost[nPos] = newCost;    
                        previous[nPos] = v;      
                        pqueue.add(new PQNode(n, newCost));
                    }
                }
            }
        }

        ArrayList<Vertex> path = new ArrayList<>();
        Vertex curr = v2;
        
        if (previous[curr.getPosition()] != null || curr.compareTo(v1) == 0) {
            while (curr != null) {
                path.add(curr);
                curr = previous[curr.getPosition()];
            }
        }
        
        Collections.reverse(path);
        
        System.out.print("Dijkstra's Path (" + v1.getElement() + " -> " + v2.getElement() + "): ");
        if (path.isEmpty()) {
            System.out.println("No path exists.");
        } else {
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getElement() + (i == path.size() - 1 ? "" : " -> "));
            }
            System.out.println(" | Total Cost: " + cost[v2.getPosition()]);
        }
    }

    // ==========================================
    // PRIM-JARNIK'S MINIMUM SPANNING TREE
    // ==========================================
    // Time Complexity: O(V^2 \log V) - Extracting vertices and scanning rows for an Adjacency Matrix dominates operations.
    public Edge[] primJarnikMST(Vertex s) {
        PriorityQueue<PQNode> Q = new PriorityQueue<>(); 
        
        // Array based tracking instead of HashMaps
        int[] distance = new int[maxCapacity]; 
        Edge[] parent = new Edge[maxCapacity];      
        boolean[] inCloud = new boolean[maxCapacity];  
        
        for (int i = 0; i < vcount; i++) {
            Vertex v = vertex[i];
            int pos = v.getPosition();
            if (v.compareTo(s) == 0) { 
                distance[pos] = 0;    
            } else { 
                distance[pos] = Integer.MAX_VALUE; 
            }
            parent[pos] = null; 
            inCloud[pos] = false;
            Q.add(new PQNode(v, distance[pos]));
        }

        Edge[] mstEdges = new Edge[vcount - 1];
        int tCount = 0;
        int totalMSTWeight = 0;

        while (!Q.isEmpty() && tCount < vcount - 1) {
            PQNode minNode = Q.poll(); 
            Vertex u = minNode.vertex; 
            int uPos = u.getPosition();

            if (inCloud[uPos]) continue;
            inCloud[uPos] = true; 
            
            if (parent[uPos] != null) {
                mstEdges[tCount++] = parent[uPos];
                totalMSTWeight += parent[uPos].getWeight();
            }

            Edge[] outgoing = outgoingEdge(u);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex z = opposite(u, e); 
                int zPos = z.getPosition();
                
                if (!inCloud[zPos]) {
                    int r = e.getWeight(); 
                    
                    if (r < distance[zPos]) {
                        distance[zPos] = r; 
                        parent[zPos] = e;   
                        Q.add(new PQNode(z, r));
                    }
                }
            }
        }
        
        System.out.println("Prim-Jarnik MST Computed. Total Weight: " + totalMSTWeight);
        return mstEdges;
    }
}

public class GraphAdjacencyMatrixComplete {
    public static void main(String[] args) {
        // Initialize an AdjacencyMatrixGraph large enough to hold a dense Regional Railway Grid
        AdjacencyMatrixGraph railways = new AdjacencyMatrixGraph(20);
        
        // --- 1. INSERTION OF NODES (16 Major Stations in Northern/Eastern Grid) ---
        railways.insertVertex("Delhi");      // 0
        railways.insertVertex("Gurgaon");    // 1
        railways.insertVertex("Noida");      // 2
        railways.insertVertex("Faridabad");  // 3
        railways.insertVertex("Ghaziabad");  // 4
        railways.insertVertex("Meerut");     // 5
        railways.insertVertex("Agra");       // 6
        railways.insertVertex("Mathura");    // 7
        railways.insertVertex("Aligarh");    // 8
        railways.insertVertex("Kanpur");     // 9
        railways.insertVertex("Lucknow");    // 10
        railways.insertVertex("Prayagraj");  // 11
        railways.insertVertex("Varanasi");   // 12
        railways.insertVertex("Patna");      // 13
        railways.insertVertex("Gaya");       // 14
        railways.insertVertex("Ranchi");     // 15
        
        Vertex[] stn = railways.vertices();
        Vertex Delhi = stn[0], Gurgaon = stn[1], Noida = stn[2], Faridabad = stn[3];
        Vertex Ghaziabad = stn[4], Meerut = stn[5], Agra = stn[6], Mathura = stn[7];
        Vertex Aligarh = stn[8], Kanpur = stn[9], Lucknow = stn[10], Prayagraj = stn[11];
        Vertex Varanasi = stn[12], Patna = stn[13], Gaya = stn[14], Ranchi = stn[15];

        // --- 2. INSERTION OF EDGES (Dense Mesh with approximate distances in KM as weights) ---
        // Delhi Hub heavily interconnected (Dense star-like core)
        railways.insertEdge(Delhi, Gurgaon, "DL-GGN", 30);
        railways.insertEdge(Delhi, Noida, "DL-NOI", 25);
        railways.insertEdge(Delhi, Faridabad, "DL-FBD", 35);
        railways.insertEdge(Delhi, Ghaziabad, "DL-GZB", 20);
        railways.insertEdge(Delhi, Meerut, "DL-MRT", 70);
        railways.insertEdge(Delhi, Agra, "DL-AGC", 200);
        railways.insertEdge(Delhi, Mathura, "DL-MTJ", 150);
        railways.insertEdge(Delhi, Aligarh, "DL-ALJN", 130);
        
        // NCR cross-connections (Making it a dense regional mesh)
        railways.insertEdge(Gurgaon, Faridabad, "GGN-FBD", 40);
        railways.insertEdge(Noida, Ghaziabad, "NOI-GZB", 15);
        railways.insertEdge(Ghaziabad, Meerut, "GZB-MRT", 50);
        railways.insertEdge(Faridabad, Mathura, "FBD-MTJ", 120);
        
        // UP/Central dense interconnected grid
        railways.insertEdge(Mathura, Agra, "MTJ-AGC", 55);
        railways.insertEdge(Agra, Aligarh, "AGC-ALJN", 85);
        railways.insertEdge(Aligarh, Kanpur, "ALJN-CNB", 260);
        railways.insertEdge(Agra, Kanpur, "AGC-CNB", 280);
        railways.insertEdge(Meerut, Aligarh, "MRT-ALJN", 130);
        
        railways.insertEdge(Kanpur, Lucknow, "CNB-LKO", 90);
        railways.insertEdge(Kanpur, Prayagraj, "CNB-PRYJ", 190);
        railways.insertEdge(Lucknow, Prayagraj, "LKO-PRYJ", 200);
        railways.insertEdge(Lucknow, Varanasi, "LKO-BSB", 280);
        railways.insertEdge(Prayagraj, Varanasi, "PRYJ-BSB", 120);
        
        // Eastern Grid Interconnections
        railways.insertEdge(Varanasi, Patna, "BSB-PNBE", 230);
        railways.insertEdge(Varanasi, Gaya, "BSB-GAYA", 210);
        railways.insertEdge(Prayagraj, Patna, "PRYJ-PNBE", 360);
        railways.insertEdge(Lucknow, Patna, "LKO-PNBE", 500);
        railways.insertEdge(Patna, Gaya, "PNBE-GAYA", 100);
        railways.insertEdge(Gaya, Ranchi, "GAYA-RNC", 200);
        railways.insertEdge(Patna, Ranchi, "PNBE-RNC", 300);

        System.out.println("--- INDIAN RAILWAYS DENSE GRID DEMONSTRATION ---");
        System.out.println("Stations (Vertices): " + railways.numVertices());
        System.out.println("Routes (Edges): " + railways.numEdges());
        System.out.println("------------------------------------------------\n");
        
        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        railways.performDFS();
        System.out.println();
        
        System.out.println("-> Finding Route by Fewest Hops (Delhi to Ranchi)");
        railways.bfs(Delhi, Ranchi);
        System.out.println();
        
        // --- 4. SHORTEST PATH FINDING (Dijkstra) ---
        System.out.println("-> Calculating Shortest Distance Route (Dijkstra)");
        railways.dijkstra(Delhi, Ranchi);
        System.out.println();
        
        // --- 5. MINIMUM SPANNING TREE (Prim-Jarnik) ---
        System.out.println("-> Calculating Minimum Spanning Tree (Prim-Jarnik's Backbone)");
        Edge[] primMST = railways.primJarnikMST(Delhi);
        System.out.print("Backbone Tracks: ");
        for (int i = 0; i < primMST.length; i++) {
            if (primMST[i] != null) System.out.print(primMST[i].getElement() + " ");
        }
        System.out.println("\n");
        
        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> STATION CLOSURE: Removing Kanpur Junction from the Network");
        railways.removeVertex(Kanpur);
        System.out.println("Stations (Vertices) Post-Deletion: " + railways.numVertices());
        System.out.println("Routes (Edges) Post-Deletion: " + railways.numEdges());
        System.out.println();
        
        System.out.println("-> Recalculating Shortest Distance Route (Delhi to Ranchi) without Kanpur");
        railways.dijkstra(Delhi, Ranchi);
        System.out.println();
    }
}


/* import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.ArrayList;

class Vertex implements Comparable<Vertex> {
    private String element;
    private int position;

    Vertex(String element, int position) {
        this.element = element;
        this.position = position;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }

    @Override
    public int compareTo(Vertex other) {
        return this.element.compareTo(other.element);
    }
}

class Edge {
    private String element;
    private Vertex origin;
    private Vertex dest;
    private int weight; // Added to support weighted algorithms

    Edge(String element, Vertex origin, Vertex dest, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setWeight(int w) { this.weight = w; }
    public int getWeight() { return weight; }
}

// Shared Priority Queue Node for Dijkstra and Prim-Jarnik Algorithms
class PQNode implements Comparable<PQNode> {
    Vertex vertex;
    int value; // Represents cost in Dijkstra or distance in Prim-Jarnik

    PQNode(Vertex vertex, int value) {
        this.vertex = vertex;
        this.value = value;
    }

    @Override
    public int compareTo(PQNode other) {
        return Integer.compare(this.value, other.value);
    }
}

class AdjacencyMatrixGraph {
    private Vertex[] vertex; 
    private int vcount;
    private int ecount;
    private Edge[][] adjMat;
    private int maxCapacity;
    
    // Arrays for Goodrich style DFS/BFS traversal labels (using position as index)[cite: 2]
    private String[] vertexLabels;
    private String[][] edgeLabels;

    // Time Complexity: O(V^2) - Instantiating a 2D array of size V x V takes quadratic time relative to max vertices.
    AdjacencyMatrixGraph(int n) {
        this.maxCapacity = n;
        vertex = new Vertex[n];
        adjMat = new Edge[n][n];
        vertexLabels = new String[n];
        edgeLabels = new String[n][n]; // 2D array to track labels mapped perfectly to the Adjacency Matrix structure
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }

    // Time Complexity: O(1) - Returning an array reference takes constant time.
    public Vertex[] vertices() { return vertex; }

    // Time Complexity: O(V^2) - Iterates through the upper triangle of the V x V matrix to collect distinct edges.
    public Edge[] edges() {
        Edge[] e = new Edge[ecount];
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            for (int j = i; j < vcount; j++) { 
                if (adjMat[i][j] != null) {
                    e[c++] = adjMat[i][j];
                }
            }
        }
        return e;
    }

    // Time Complexity: O(1) - Directly accesses the 2D array using the vertices' position metadata.
    public Edge getEdge(Vertex u, Vertex v) {
        int uPos = u.getPosition();
        int vPos = v.getPosition();
        return adjMat[uPos][vPos];
    }

    // Time Complexity: O(1) - Array creation and fetching known object properties is constant time.
    public Vertex[] endVertices(Edge e) {
        Vertex[] v = new Vertex[2];
        v[0] = e.getOrigin();
        v[1] = e.getDest();
        return v;
    }

    // Time Complexity: O(1) - Max two string comparisons and fetching endpoints is constant time.
    public Vertex opposite(Vertex v, Edge e) {
        Vertex[] t = new Vertex[2];
        t[0] = e.getOrigin();
        t[1] = e.getDest();
        if (v.compareTo(t[0]) == 0) return t[1];
        else if (v.compareTo(t[1]) == 0) return t[0];
        else return null;
    }

    // Time Complexity: O(V) - Must scan the entire row corresponding to vertex v in the adjacency matrix.
    public int outDegree(Vertex v) {
        int count = 0;
        int pos = v.getPosition();
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                count++;
            }
        }
        return count;
    }

    // Time Complexity: O(V) - Must scan the entire column corresponding to vertex v in the adjacency matrix.
    public int inDegree(Vertex v) {
        int count = 0;
        int pos = v.getPosition();
        for (int i = 0; i < vcount; i++) {
            if (adjMat[i][pos] != null) {
                count++;
            }
        }
        return count;
    }

    // Time Complexity: O(V) - Scans the column for vertex v to populate an array of incoming edges.
    public Edge[] incomingEdge(Vertex v) {
        Edge[] incoming = new Edge[inDegree(v)];
        int pos = v.getPosition();
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[i][pos] != null) {
                incoming[c++] = adjMat[i][pos];
            }
        }
        return incoming;
    }

    // Time Complexity: O(V) - Scans the row for vertex v to populate an array of outgoing edges.
    public Edge[] outgoingEdge(Vertex v) {
        Edge[] outgoing = new Edge[outDegree(v)];
        int pos = v.getPosition();
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                outgoing[c++] = adjMat[pos][i];
            }
        }
        return outgoing;
    }

    // Time Complexity: O(1) - Assigns a reference to a pre-known array index.
    public void insertVertex(String x) {
        if (vcount >= vertex.length) return;
        Vertex v = new Vertex(x, vcount);
        vertex[vcount] = v;
        vertexLabels[vcount] = "UNEXPLORED";
        vcount++;
    }

    // Time Complexity: O(1) - Instantiates an object and assigns it to specific row/col indices in the 2D array.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) {
        Edge e = new Edge(x, u, v, weight);
        int uPos = u.getPosition();
        int vPos = v.getPosition();
        adjMat[uPos][vPos] = e;
        adjMat[vPos][uPos] = e; 
        edgeLabels[uPos][vPos] = "UNEXPLORED";
        edgeLabels[vPos][uPos] = "UNEXPLORED";
        ecount++;
    }

    // Time Complexity: O(1) - Directly accesses the endpoints and nullifies the matrix cells.
    public void removeEdge(Edge e) {
        int uPos = e.getOrigin().getPosition();
        int vPos = e.getDest().getPosition();
        
        if (adjMat[uPos][vPos] != null) {
            adjMat[uPos][vPos] = null;
            adjMat[vPos][uPos] = null; 
            edgeLabels[uPos][vPos] = null;
            edgeLabels[vPos][uPos] = null;
            ecount--;
        }
    }

    // Time Complexity: O(V) - Removes edges in O(V), then copies row/col from the last vertex in O(V), avoiding full matrix shifts.
    public void removeVertex(Vertex v) {
        int pos = v.getPosition();
        if (pos < 0 || pos >= vcount) return;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                removeEdge(adjMat[pos][i]);
            }
        }
        int lastIndex = vcount - 1;
        if (pos != lastIndex) {
            Vertex lastVertex = vertex[lastIndex];
            vertex[pos] = lastVertex;
            lastVertex.setPosition(pos);
            vertexLabels[pos] = vertexLabels[lastIndex];
            
            for (int i = 0; i < vcount; i++) {
                adjMat[pos][i] = adjMat[lastIndex][i];
                adjMat[i][pos] = adjMat[i][lastIndex];
                edgeLabels[pos][i] = edgeLabels[lastIndex][i];
                edgeLabels[i][pos] = edgeLabels[i][lastIndex];
            }
            adjMat[pos][pos] = adjMat[lastIndex][lastIndex]; 
            edgeLabels[pos][pos] = edgeLabels[lastIndex][lastIndex];
        }
        for (int i = 0; i < vcount; i++) {
            adjMat[lastIndex][i] = null;
            adjMat[i][lastIndex] = null;
            edgeLabels[lastIndex][i] = null;
            edgeLabels[i][lastIndex] = null;
        }
        vertex[lastIndex] = null;
        vertexLabels[lastIndex] = null;
        vcount--;
    }
    
    // ==========================================
    // TRAVERSAL LABELS (Goodrich Style)
    // ==========================================
    // Time Complexity: O(1) - Array assignment is constant time.
    public void setLabel(Vertex v, String label) { 
        vertexLabels[v.getPosition()] = label; 
    }
    // Time Complexity: O(1) - Array lookup is constant time.
    public String getLabel(Vertex v) { 
        String label = vertexLabels[v.getPosition()];
        return label != null ? label : "UNEXPLORED"; 
    }
    // Time Complexity: O(1) - 2D Array assignment using endpoint positions is constant time.
    public void setLabel(Edge e, String label) { 
        int uPos = e.getOrigin().getPosition();
        int vPos = e.getDest().getPosition();
        edgeLabels[uPos][vPos] = label;
        edgeLabels[vPos][uPos] = label; // Maintain symmetry for undirected graph
    }
    // Time Complexity: O(1) - 2D Array lookup using endpoint positions is constant time.
    public String getLabel(Edge e) { 
        String label = edgeLabels[e.getOrigin().getPosition()][e.getDest().getPosition()];
        return label != null ? label : "UNEXPLORED";
    }

    // ==========================================
    // DEPTH FIRST SEARCH (DFS)
    // ==========================================
    // Time Complexity: O(V^2) - For an Adjacency Matrix, finding neighbors requires scanning the entire row of size V for every vertex.
    public void performDFS() {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        
        Edge[] allEdges = edges();
        for (int i = 0; i < ecount; i++) setLabel(allEdges[i], "UNEXPLORED");
        
        System.out.print("DFS Traversal: ");
        for (int i = 0; i < vcount; i++) {
            if (getLabel(vertex[i]).equals("UNEXPLORED")) {
                dfs(this, vertex[i]);
            }
        }
        System.out.println();
    }

    // Time Complexity: O(V) per call - Must scan the entire row for outgoing edges.
    private void dfs(AdjacencyMatrixGraph G, Vertex v) {
        setLabel(v, "VISITED");
        System.out.print(v.getElement() + " ");
        
        Edge[] outgoing = G.outgoingEdge(v);
        for (int i = 0; i < outgoing.length; i++) {
            Edge e = outgoing[i];
            if (getLabel(e).equals("UNEXPLORED")) {
                Vertex w = G.opposite(v, e);
                if (getLabel(w).equals("UNEXPLORED")) {
                    setLabel(e, "DISCOVERY");
                    dfs(G, w);
                } else {
                    setLabel(e, "FORWARD_OR_CROSS"); 
                }
            }
        }
    }

    // ==========================================
    // BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V^2) - For an Adjacency Matrix, scanning neighbors for queued vertices takes O(V) each time.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        
        Queue<Vertex> queue = new LinkedList<>(); 
        queue.add(v1); 
        setLabel(v1, "VISITED"); 
        
        System.out.print("BFS Path Search (" + v1.getElement() + " -> " + v2.getElement() + "): ");
        
        while (!queue.isEmpty()) { 
            Vertex v = queue.poll(); 
            System.out.print(v.getElement() + " ");
            
            if (v.compareTo(v2) == 0) { 
                System.out.println("\n-> A path is found!"); 
                return true;
            }
            
            Edge[] outgoing = outgoingEdge(v);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex n = opposite(v, e);
                if (getLabel(n).equals("UNEXPLORED")) {
                    setLabel(n, "VISITED"); 
                    queue.add(n); 
                }
            }
        }
        System.out.println("\n-> Path is not found."); 
        return false;
    }

    // ==========================================
    // DIJKSTRA'S SHORTEST PATH
    // ==========================================
    // Time Complexity: O(V^2 \log V) - Scanning neighbors takes O(V) per vertex, and adding elements to the priority queue takes O(\log V).
    public void dijkstra(Vertex v1, Vertex v2) {
        // Array based tracking instead of HashMaps
        int[] cost = new int[maxCapacity];
        Vertex[] previous = new Vertex[maxCapacity];
        boolean[] visited = new boolean[maxCapacity];
        PriorityQueue<PQNode> pqueue = new PriorityQueue<>();

        for (int i = 0; i < vcount; i++) {
            Vertex v = vertex[i];
            cost[v.getPosition()] = Integer.MAX_VALUE; 
            previous[v.getPosition()] = null;          
            visited[v.getPosition()] = false;
        }

        cost[v1.getPosition()] = 0; 
        pqueue.add(new PQNode(v1, 0)); 

        while (!pqueue.isEmpty()) { 
            PQNode current = pqueue.poll(); 
            Vertex v = current.vertex; 
            int pos = v.getPosition();

            if (visited[pos]) continue;
            visited[pos] = true; 

            if (v.compareTo(v2) == 0) break;

            Edge[] outgoing = outgoingEdge(v);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex n = opposite(v, e);
                int nPos = n.getPosition();
                
                if (!visited[nPos] && cost[pos] != Integer.MAX_VALUE) {
                    int newCost = cost[pos] + e.getWeight(); 

                    if (newCost < cost[nPos]) { 
                        cost[nPos] = newCost;    
                        previous[nPos] = v;      
                        pqueue.add(new PQNode(n, newCost));
                    }
                }
            }
        }

        ArrayList<Vertex> path = new ArrayList<>();
        Vertex curr = v2;
        
        if (previous[curr.getPosition()] != null || curr.compareTo(v1) == 0) {
            while (curr != null) {
                path.add(curr);
                curr = previous[curr.getPosition()];
            }
        }
        
        Collections.reverse(path);
        
        System.out.print("Dijkstra's Path (" + v1.getElement() + " -> " + v2.getElement() + "): ");
        if (path.isEmpty()) {
            System.out.println("No path exists.");
        } else {
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getElement() + (i == path.size() - 1 ? "" : " -> "));
            }
            System.out.println(" | Total Cost: " + cost[v2.getPosition()]);
        }
    }

    // ==========================================
    // PRIM-JARNIK'S MINIMUM SPANNING TREE
    // ==========================================
    // Time Complexity: O(V^2 \log V) - Extracting vertices and scanning rows for an Adjacency Matrix dominates operations.
    public Edge[] primJarnikMST(Vertex s) {
        PriorityQueue<PQNode> Q = new PriorityQueue<>(); 
        
        // Array based tracking instead of HashMaps
        int[] distance = new int[maxCapacity]; 
        Edge[] parent = new Edge[maxCapacity];      
        boolean[] inCloud = new boolean[maxCapacity];  
        
        for (int i = 0; i < vcount; i++) {
            Vertex v = vertex[i];
            int pos = v.getPosition();
            if (v.compareTo(s) == 0) { 
                distance[pos] = 0;    
            } else { 
                distance[pos] = Integer.MAX_VALUE; 
            }
            parent[pos] = null; 
            inCloud[pos] = false;
            Q.add(new PQNode(v, distance[pos]));
        }

        Edge[] mstEdges = new Edge[vcount - 1];
        int tCount = 0;
        int totalMSTWeight = 0;

        while (!Q.isEmpty() && tCount < vcount - 1) {
            PQNode minNode = Q.poll(); 
            Vertex u = minNode.vertex; 
            int uPos = u.getPosition();

            if (inCloud[uPos]) continue;
            inCloud[uPos] = true; 
            
            if (parent[uPos] != null) {
                mstEdges[tCount++] = parent[uPos];
                totalMSTWeight += parent[uPos].getWeight();
            }

            Edge[] outgoing = outgoingEdge(u);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex z = opposite(u, e); 
                int zPos = z.getPosition();
                
                if (!inCloud[zPos]) {
                    int r = e.getWeight(); 
                    
                    if (r < distance[zPos]) {
                        distance[zPos] = r; 
                        parent[zPos] = e;   
                        Q.add(new PQNode(z, r));
                    }
                }
            }
        }
        
        System.out.println("Prim-Jarnik MST Computed. Total Weight: " + totalMSTWeight);
        return mstEdges;
    }
}

public class GraphAdjacencyMatrixComplete {
    public static void main(String[] args) {
        // Initialize an AdjacencyMatrixGraph large enough to hold a dense Regional Railway Grid
        AdjacencyMatrixGraph railways = new AdjacencyMatrixGraph(20);
        
        // --- 1. INSERTION OF NODES (16 Major Stations in Northern/Eastern Grid) ---
        railways.insertVertex("Delhi");      // 0
        railways.insertVertex("Gurgaon");    // 1
        railways.insertVertex("Noida");      // 2
        railways.insertVertex("Faridabad");  // 3
        railways.insertVertex("Ghaziabad");  // 4
        railways.insertVertex("Meerut");     // 5
        railways.insertVertex("Agra");       // 6
        railways.insertVertex("Mathura");    // 7
        railways.insertVertex("Aligarh");    // 8
        railways.insertVertex("Kanpur");     // 9
        railways.insertVertex("Lucknow");    // 10
        railways.insertVertex("Prayagraj");  // 11
        railways.insertVertex("Varanasi");   // 12
        railways.insertVertex("Patna");      // 13
        railways.insertVertex("Gaya");       // 14
        railways.insertVertex("Ranchi");     // 15
        
        Vertex[] stn = railways.vertices();
        Vertex Delhi = stn[0], Gurgaon = stn[1], Noida = stn[2], Faridabad = stn[3];
        Vertex Ghaziabad = stn[4], Meerut = stn[5], Agra = stn[6], Mathura = stn[7];
        Vertex Aligarh = stn[8], Kanpur = stn[9], Lucknow = stn[10], Prayagraj = stn[11];
        Vertex Varanasi = stn[12], Patna = stn[13], Gaya = stn[14], Ranchi = stn[15];

        // --- 2. INSERTION OF EDGES (Dense Mesh with approximate distances in KM as weights) ---
        // Delhi Hub heavily interconnected (Dense star-like core)
        railways.insertEdge(Delhi, Gurgaon, "DL-GGN", 30);
        railways.insertEdge(Delhi, Noida, "DL-NOI", 25);
        railways.insertEdge(Delhi, Faridabad, "DL-FBD", 35);
        railways.insertEdge(Delhi, Ghaziabad, "DL-GZB", 20);
        railways.insertEdge(Delhi, Meerut, "DL-MRT", 70);
        railways.insertEdge(Delhi, Agra, "DL-AGC", 200);
        railways.insertEdge(Delhi, Mathura, "DL-MTJ", 150);
        railways.insertEdge(Delhi, Aligarh, "DL-ALJN", 130);
        
        // NCR cross-connections (Making it a dense regional mesh)
        railways.insertEdge(Gurgaon, Faridabad, "GGN-FBD", 40);
        railways.insertEdge(Noida, Ghaziabad, "NOI-GZB", 15);
        railways.insertEdge(Ghaziabad, Meerut, "GZB-MRT", 50);
        railways.insertEdge(Faridabad, Mathura, "FBD-MTJ", 120);
        
        // UP/Central dense interconnected grid
        railways.insertEdge(Mathura, Agra, "MTJ-AGC", 55);
        railways.insertEdge(Agra, Aligarh, "AGC-ALJN", 85);
        railways.insertEdge(Aligarh, Kanpur, "ALJN-CNB", 260);
        railways.insertEdge(Agra, Kanpur, "AGC-CNB", 280);
        railways.insertEdge(Meerut, Aligarh, "MRT-ALJN", 130);
        
        railways.insertEdge(Kanpur, Lucknow, "CNB-LKO", 90);
        railways.insertEdge(Kanpur, Prayagraj, "CNB-PRYJ", 190);
        railways.insertEdge(Lucknow, Prayagraj, "LKO-PRYJ", 200);
        railways.insertEdge(Lucknow, Varanasi, "LKO-BSB", 280);
        railways.insertEdge(Prayagraj, Varanasi, "PRYJ-BSB", 120);
        
        // Eastern Grid Interconnections
        railways.insertEdge(Varanasi, Patna, "BSB-PNBE", 230);
        railways.insertEdge(Varanasi, Gaya, "BSB-GAYA", 210);
        railways.insertEdge(Prayagraj, Patna, "PRYJ-PNBE", 360);
        railways.insertEdge(Lucknow, Patna, "LKO-PNBE", 500);
        railways.insertEdge(Patna, Gaya, "PNBE-GAYA", 100);
        railways.insertEdge(Gaya, Ranchi, "GAYA-RNC", 200);
        railways.insertEdge(Patna, Ranchi, "PNBE-RNC", 300);

        System.out.println("--- INDIAN RAILWAYS DENSE GRID DEMONSTRATION ---");
        System.out.println("Stations (Vertices): " + railways.numVertices());
        System.out.println("Routes (Edges): " + railways.numEdges());
        System.out.println("------------------------------------------------\n");
        
        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        railways.performDFS();
        System.out.println();
        
        System.out.println("-> Finding Route by Fewest Hops (Delhi to Ranchi)");
        railways.bfs(Delhi, Ranchi);
        System.out.println();
        
        // --- 4. SHORTEST PATH FINDING (Dijkstra) ---
        System.out.println("-> Calculating Shortest Distance Route (Dijkstra)");
        railways.dijkstra(Delhi, Ranchi);
        System.out.println();
        
        // --- 5. MINIMUM SPANNING TREE (Prim-Jarnik) ---
        System.out.println("-> Calculating Minimum Spanning Tree (Prim-Jarnik's Backbone)");
        Edge[] primMST = railways.primJarnikMST(Delhi);
        System.out.print("Backbone Tracks: ");
        for (int i = 0; i < primMST.length; i++) {
            if (primMST[i] != null) System.out.print(primMST[i].getElement() + " ");
        }
        System.out.println("\n");
        
        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> STATION CLOSURE: Removing Kanpur Junction from the Network");
        railways.removeVertex(Kanpur);
        System.out.println("Stations (Vertices) Post-Deletion: " + railways.numVertices());
        System.out.println("Routes (Edges) Post-Deletion: " + railways.numEdges());
        System.out.println();
        
        System.out.println("-> Recalculating Shortest Distance Route (Delhi to Ranchi) without Kanpur");
        railways.dijkstra(Delhi, Ranchi);
        System.out.println();
    }
} */


/* import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.ArrayList;

class Vertex implements Comparable<Vertex> {
    private String element;
    private int position;

    Vertex(String element, int position) {
        this.element = element;
        this.position = position;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }

    @Override
    public int compareTo(Vertex other) {
        return this.element.compareTo(other.element);
    }
}

class Edge {
    private String element;
    private Vertex origin;
    private Vertex dest;
    private int weight; // Added to support weighted algorithms

    Edge(String element, Vertex origin, Vertex dest, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setWeight(int w) { this.weight = w; }
    public int getWeight() { return weight; }
}

// Shared Priority Queue Node for Dijkstra and Prim-Jarnik Algorithms
class PQNode implements Comparable<PQNode> {
    Vertex vertex;
    int value; // Represents cost in Dijkstra or distance in Prim-Jarnik

    PQNode(Vertex vertex, int value) {
        this.vertex = vertex;
        this.value = value;
    }

    @Override
    public int compareTo(PQNode other) {
        return Integer.compare(this.value, other.value);
    }
}

class AdjacencyMatrixGraph {
    private Vertex[] vertex; 
    private int vcount;
    private int ecount;
    private Edge[][] adjMat;
    
    // HashMaps for Goodrich style DFS/BFS traversal labels
    private HashMap<Vertex, String> vertexLabels;
    private HashMap<Edge, String> edgeLabels;

    // Time Complexity: O(V^2) - Instantiating a 2D array of size V x V takes quadratic time relative to max vertices.
    AdjacencyMatrixGraph(int n) {
        vertex = new Vertex[n];
        adjMat = new Edge[n][n];
        vertexLabels = new HashMap<>();
        edgeLabels = new HashMap<>();
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }

    // Time Complexity: O(1) - Returning an array reference takes constant time.
    public Vertex[] vertices() { return vertex; }

    // Time Complexity: O(V^2) - Iterates through the upper triangle of the V x V matrix to collect distinct edges.
    public Edge[] edges() {
        Edge[] e = new Edge[ecount];
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            for (int j = i; j < vcount; j++) { 
                if (adjMat[i][j] != null) {
                    e[c++] = adjMat[i][j];
                }
            }
        }
        return e;
    }

    // Time Complexity: O(1) - Directly accesses the 2D array using the vertices' position metadata.
    public Edge getEdge(Vertex u, Vertex v) {
        int uPos = u.getPosition();
        int vPos = v.getPosition();
        return adjMat[uPos][vPos];
    }

    // Time Complexity: O(1) - Array creation and fetching known object properties is constant time.
    public Vertex[] endVertices(Edge e) {
        Vertex v[] = new Vertex[2];
        v[0] = e.getOrigin();
        v[1] = e.getDest();
        return v;
    }

    // Time Complexity: O(1) - Max two string comparisons and fetching endpoints is constant time.
    public Vertex opposite(Vertex v, Edge e) {
        Vertex[] t = new Vertex[2];
        t[0] = e.getOrigin();
        t[1] = e.getDest();
        if (v.compareTo(t[0]) == 0) return t[1];
        else if (v.compareTo(t[1]) == 0) return t[0];
        else return null;
    }

    // Time Complexity: O(V) - Must scan the entire row corresponding to vertex v in the adjacency matrix.
    public int outDegree(Vertex v) {
        int count = 0;
        int pos = v.getPosition();
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                count++;
            }
        }
        return count;
    }

    // Time Complexity: O(V) - Must scan the entire column corresponding to vertex v in the adjacency matrix.
    public int inDegree(Vertex v) {
        int count = 0;
        int pos = v.getPosition();
        for (int i = 0; i < vcount; i++) {
            if (adjMat[i][pos] != null) {
                count++;
            }
        }
        return count;
    }

    // Time Complexity: O(V) - Scans the column for vertex v to populate an array of incoming edges.
    public Edge[] incomingEdge(Vertex v) {
        Edge[] incoming = new Edge[inDegree(v)];
        int pos = v.getPosition();
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[i][pos] != null) {
                incoming[c++] = adjMat[i][pos];
            }
        }
        return incoming;
    }

    // Time Complexity: O(V) - Scans the row for vertex v to populate an array of outgoing edges.
    public Edge[] outgoingEdge(Vertex v) {
        Edge[] outgoing = new Edge[outDegree(v)];
        int pos = v.getPosition();
        int c = 0;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                outgoing[c++] = adjMat[pos][i];
            }
        }
        return outgoing;
    }

    // Time Complexity: O(1) - Assigns a reference to a pre-known array index.
    public void insertVertex(String x) {
        if (vcount >= vertex.length) return;
        Vertex v = new Vertex(x, vcount);
        vertex[vcount++] = v;
    }

    // Time Complexity: O(1) - Instantiates an object and assigns it to specific row/col indices in the 2D array.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) {
        Edge e = new Edge(x, u, v, weight);
        int uPos = u.getPosition();
        int vPos = v.getPosition();
        adjMat[uPos][vPos] = e;
        adjMat[vPos][uPos] = e; 
        ecount++;
    }

    // Time Complexity: O(1) - Directly accesses the endpoints and nullifies the matrix cells.
    public void removeEdge(Edge e) {
        int uPos = e.getOrigin().getPosition();
        int vPos = e.getDest().getPosition();
        
        if (adjMat[uPos][vPos] != null) {
            adjMat[uPos][vPos] = null;
            adjMat[vPos][uPos] = null; 
            ecount--;
        }
    }

    // Time Complexity: O(V) - Removes edges in O(V), then copies row/col from the last vertex in O(V), avoiding full matrix shifts.
    public void removeVertex(Vertex v) {
        int pos = v.getPosition();
        if (pos < 0 || pos >= vcount) return;
        for (int i = 0; i < vcount; i++) {
            if (adjMat[pos][i] != null) {
                removeEdge(adjMat[pos][i]);
            }
        }
        int lastIndex = vcount - 1;
        if (pos != lastIndex) {
            Vertex lastVertex = vertex[lastIndex];
            vertex[pos] = lastVertex;
            lastVertex.setPosition(pos);
            for (int i = 0; i < vcount; i++) {
                adjMat[pos][i] = adjMat[lastIndex][i];
                adjMat[i][pos] = adjMat[i][lastIndex];
            }
            adjMat[pos][pos] = adjMat[lastIndex][lastIndex]; 
        }
        for (int i = 0; i < vcount; i++) {
            adjMat[lastIndex][i] = null;
            adjMat[i][lastIndex] = null;
        }
        vertex[lastIndex] = null;
        vcount--;
    }
    
    // ==========================================
    // TRAVERSAL LABELS (Goodrich Style)
    // ==========================================
    // Time Complexity: O(1) Expected - Putting into a HashMap takes expected constant time.
    public void setLabel(Vertex v, String label) { vertexLabels.put(v, label); }
    // Time Complexity: O(1) Expected - Getting from a HashMap takes expected constant time.
    public String getLabel(Vertex v) { return vertexLabels.getOrDefault(v, "UNEXPLORED"); }
    // Time Complexity: O(1) Expected - Putting into a HashMap takes expected constant time.
    public void setLabel(Edge e, String label) { edgeLabels.put(e, label); }
    // Time Complexity: O(1) Expected - Getting from a HashMap takes expected constant time.
    public String getLabel(Edge e) { return edgeLabels.getOrDefault(e, "UNEXPLORED"); }

    // ==========================================
    // DEPTH FIRST SEARCH (DFS)
    // ==========================================
    // Time Complexity: O(V^2) - For an Adjacency Matrix, finding neighbors requires scanning the entire row of size V for every vertex.
    public void performDFS() {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        
        Edge[] allEdges = edges();
        for (int i = 0; i < ecount; i++) setLabel(allEdges[i], "UNEXPLORED");
        
        System.out.print("DFS Traversal: ");
        for (int i = 0; i < vcount; i++) {
            if (getLabel(vertex[i]).equals("UNEXPLORED")) {
                dfs(this, vertex[i]);
            }
        }
        System.out.println();
    }

    // Time Complexity: O(V) per call - Must scan the entire row for outgoing edges.
    private void dfs(AdjacencyMatrixGraph G, Vertex v) {
        setLabel(v, "VISITED");
        System.out.print(v.getElement() + " ");
        
        Edge[] outgoing = G.outgoingEdge(v);
        for (int i = 0; i < outgoing.length; i++) {
            Edge e = outgoing[i];
            if (getLabel(e).equals("UNEXPLORED")) {
                Vertex w = G.opposite(v, e);
                if (getLabel(w).equals("UNEXPLORED")) {
                    setLabel(e, "DISCOVERY");
                    dfs(G, w);
                } else {
                    setLabel(e, "FORWARD_OR_CROSS"); 
                }
            }
        }
    }

    // ==========================================
    // BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V^2) - For an Adjacency Matrix, scanning neighbors for queued vertices takes O(V) each time.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        
        Queue<Vertex> queue = new LinkedList<>(); 
        queue.add(v1); 
        setLabel(v1, "VISITED"); 
        
        System.out.print("BFS Path Search (" + v1.getElement() + " -> " + v2.getElement() + "): ");
        
        while (!queue.isEmpty()) { 
            Vertex v = queue.poll(); 
            System.out.print(v.getElement() + " ");
            
            if (v.compareTo(v2) == 0) { 
                System.out.println("\n-> A path is found!"); 
                return true;
            }
            
            Edge[] outgoing = outgoingEdge(v);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex n = opposite(v, e);
                if (getLabel(n).equals("UNEXPLORED")) {
                    setLabel(n, "VISITED"); 
                    queue.add(n); 
                }
            }
        }
        System.out.println("\n-> Path is not found."); 
        return false;
    }

    // ==========================================
    // DIJKSTRA'S SHORTEST PATH
    // ==========================================
    // Time Complexity: O(V^2 \log V) - Scanning neighbors takes O(V) per vertex, and adding elements to the priority queue takes O(\log V).
    public void dijkstra(Vertex v1, Vertex v2) {
        HashMap<Vertex, Integer> cost = new HashMap<>();
        HashMap<Vertex, Vertex> previous = new HashMap<>();
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        PriorityQueue<PQNode> pqueue = new PriorityQueue<>();

        for (int i = 0; i < vcount; i++) {
            Vertex v = vertex[i];
            cost.put(v, Integer.MAX_VALUE); 
            previous.put(v, null);          
            visited.put(v, false);
        }

        cost.put(v1, 0); 
        pqueue.add(new PQNode(v1, 0)); 

        while (!pqueue.isEmpty()) { 
            PQNode current = pqueue.poll(); 
            Vertex v = current.vertex; 

            if (visited.get(v)) continue;
            visited.put(v, true); 

            if (v.compareTo(v2) == 0) break;

            Edge[] outgoing = outgoingEdge(v);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex n = opposite(v, e);
                
                if (!visited.get(n) && cost.get(v) != Integer.MAX_VALUE) {
                    int newCost = cost.get(v) + e.getWeight(); 

                    if (newCost < cost.get(n)) { 
                        cost.put(n, newCost);    
                        previous.put(n, v);      
                        pqueue.add(new PQNode(n, newCost));
                    }
                }
            }
        }

        ArrayList<Vertex> path = new ArrayList<>();
        Vertex curr = v2;
        
        if (previous.get(curr) != null || curr.compareTo(v1) == 0) {
            while (curr != null) {
                path.add(curr);
                curr = previous.get(curr);
            }
        }
        
        Collections.reverse(path);
        
        System.out.print("Dijkstra's Path (" + v1.getElement() + " -> " + v2.getElement() + "): ");
        if (path.isEmpty()) {
            System.out.println("No path exists.");
        } else {
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getElement() + (i == path.size() - 1 ? "" : " -> "));
            }
            System.out.println(" | Total Cost: " + cost.get(v2));
        }
    }

    // ==========================================
    // PRIM-JARNIK'S MINIMUM SPANNING TREE
    // ==========================================
    // Time Complexity: O(V^2 \log V) - Extracting vertices and scanning rows for an Adjacency Matrix dominates operations.
    public Edge[] primJarnikMST(Vertex s) {
        PriorityQueue<PQNode> Q = new PriorityQueue<>(); 
        HashMap<Vertex, Integer> distance = new HashMap<>(); 
        HashMap<Vertex, Edge> parent = new HashMap<>();      
        HashMap<Vertex, Boolean> inCloud = new HashMap<>();  
        
        for (int i = 0; i < vcount; i++) {
            Vertex v = vertex[i];
            if (v.compareTo(s) == 0) { 
                distance.put(v, 0);    
            } else { 
                distance.put(v, Integer.MAX_VALUE); 
            }
            parent.put(v, null); 
            inCloud.put(v, false);
            Q.add(new PQNode(v, distance.get(v)));
        }

        Edge[] mstEdges = new Edge[vcount - 1];
        int tCount = 0;
        int totalMSTWeight = 0;

        while (!Q.isEmpty() && tCount < vcount - 1) {
            PQNode minNode = Q.poll(); 
            Vertex u = minNode.vertex; 

            if (inCloud.get(u)) continue;
            inCloud.put(u, true); 
            
            if (parent.get(u) != null) {
                mstEdges[tCount++] = parent.get(u);
                totalMSTWeight += parent.get(u).getWeight();
            }

            Edge[] outgoing = outgoingEdge(u);
            for (int i = 0; i < outgoing.length; i++) { 
                Edge e = outgoing[i];
                Vertex z = opposite(u, e); 
                
                if (!inCloud.get(z)) {
                    int r = e.getWeight(); 
                    
                    if (r < distance.get(z)) {
                        distance.put(z, r); 
                        parent.put(z, e);   
                        Q.add(new PQNode(z, r));
                    }
                }
            }
        }
        
        System.out.println("Prim-Jarnik MST Computed. Total Weight: " + totalMSTWeight);
        return mstEdges;
    }
}

public class GraphAdjacencyMatrixComplete {
    public static void main(String[] args) {
        // Initialize an AdjacencyMatrixGraph large enough to hold a dense Regional Railway Grid
        AdjacencyMatrixGraph railways = new AdjacencyMatrixGraph(20);
        
        // --- 1. INSERTION OF NODES (16 Major Stations in Northern/Eastern Grid) ---
        railways.insertVertex("Delhi");      // 0
        railways.insertVertex("Gurgaon");    // 1
        railways.insertVertex("Noida");      // 2
        railways.insertVertex("Faridabad");  // 3
        railways.insertVertex("Ghaziabad");  // 4
        railways.insertVertex("Meerut");     // 5
        railways.insertVertex("Agra");       // 6
        railways.insertVertex("Mathura");    // 7
        railways.insertVertex("Aligarh");    // 8
        railways.insertVertex("Kanpur");     // 9
        railways.insertVertex("Lucknow");    // 10
        railways.insertVertex("Prayagraj");  // 11
        railways.insertVertex("Varanasi");   // 12
        railways.insertVertex("Patna");      // 13
        railways.insertVertex("Gaya");       // 14
        railways.insertVertex("Ranchi");     // 15
        
        Vertex[] stn = railways.vertices();
        Vertex Delhi = stn[0], Gurgaon = stn[1], Noida = stn[2], Faridabad = stn[3];
        Vertex Ghaziabad = stn[4], Meerut = stn[5], Agra = stn[6], Mathura = stn[7];
        Vertex Aligarh = stn[8], Kanpur = stn[9], Lucknow = stn[10], Prayagraj = stn[11];
        Vertex Varanasi = stn[12], Patna = stn[13], Gaya = stn[14], Ranchi = stn[15];

        // --- 2. INSERTION OF EDGES (Dense Mesh with approximate distances in KM as weights) ---
        // Delhi Hub heavily interconnected (Dense star-like core)
        railways.insertEdge(Delhi, Gurgaon, "DL-GGN", 30);
        railways.insertEdge(Delhi, Noida, "DL-NOI", 25);
        railways.insertEdge(Delhi, Faridabad, "DL-FBD", 35);
        railways.insertEdge(Delhi, Ghaziabad, "DL-GZB", 20);
        railways.insertEdge(Delhi, Meerut, "DL-MRT", 70);
        railways.insertEdge(Delhi, Agra, "DL-AGC", 200);
        railways.insertEdge(Delhi, Mathura, "DL-MTJ", 150);
        railways.insertEdge(Delhi, Aligarh, "DL-ALJN", 130);
        
        // NCR cross-connections (Making it a dense regional mesh)
        railways.insertEdge(Gurgaon, Faridabad, "GGN-FBD", 40);
        railways.insertEdge(Noida, Ghaziabad, "NOI-GZB", 15);
        railways.insertEdge(Ghaziabad, Meerut, "GZB-MRT", 50);
        railways.insertEdge(Faridabad, Mathura, "FBD-MTJ", 120);
        
        // UP/Central dense interconnected grid
        railways.insertEdge(Mathura, Agra, "MTJ-AGC", 55);
        railways.insertEdge(Agra, Aligarh, "AGC-ALJN", 85);
        railways.insertEdge(Aligarh, Kanpur, "ALJN-CNB", 260);
        railways.insertEdge(Agra, Kanpur, "AGC-CNB", 280);
        railways.insertEdge(Meerut, Aligarh, "MRT-ALJN", 130);
        
        railways.insertEdge(Kanpur, Lucknow, "CNB-LKO", 90);
        railways.insertEdge(Kanpur, Prayagraj, "CNB-PRYJ", 190);
        railways.insertEdge(Lucknow, Prayagraj, "LKO-PRYJ", 200);
        railways.insertEdge(Lucknow, Varanasi, "LKO-BSB", 280);
        railways.insertEdge(Prayagraj, Varanasi, "PRYJ-BSB", 120);
        
        // Eastern Grid Interconnections
        railways.insertEdge(Varanasi, Patna, "BSB-PNBE", 230);
        railways.insertEdge(Varanasi, Gaya, "BSB-GAYA", 210);
        railways.insertEdge(Prayagraj, Patna, "PRYJ-PNBE", 360);
        railways.insertEdge(Lucknow, Patna, "LKO-PNBE", 500);
        railways.insertEdge(Patna, Gaya, "PNBE-GAYA", 100);
        railways.insertEdge(Gaya, Ranchi, "GAYA-RNC", 200);
        railways.insertEdge(Patna, Ranchi, "PNBE-RNC", 300);

        System.out.println("--- INDIAN RAILWAYS DENSE GRID DEMONSTRATION ---");
        System.out.println("Stations (Vertices): " + railways.numVertices());
        System.out.println("Routes (Edges): " + railways.numEdges());
        System.out.println("------------------------------------------------\n");
        
        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        railways.performDFS();
        System.out.println();
        
        System.out.println("-> Finding Route by Fewest Hops (Delhi to Ranchi)");
        railways.bfs(Delhi, Ranchi);
        System.out.println();
        
        // --- 4. SHORTEST PATH FINDING (Dijkstra) ---
        System.out.println("-> Calculating Shortest Distance Route (Dijkstra)");
        railways.dijkstra(Delhi, Ranchi);
        System.out.println();
        
        // --- 5. MINIMUM SPANNING TREE (Prim-Jarnik) ---
        System.out.println("-> Calculating Minimum Spanning Tree (Prim-Jarnik's Backbone)");
        Edge[] primMST = railways.primJarnikMST(Delhi);
        System.out.print("Backbone Tracks: ");
        for (int i = 0; i < primMST.length; i++) {
            if (primMST[i] != null) System.out.print(primMST[i].getElement() + " ");
        }
        System.out.println("\n");
        
        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> STATION CLOSURE: Removing Kanpur Junction from the Network");
        railways.removeVertex(Kanpur);
        System.out.println("Stations (Vertices) Post-Deletion: " + railways.numVertices());
        System.out.println("Routes (Edges) Post-Deletion: " + railways.numEdges());
        System.out.println();
        
        System.out.println("-> Recalculating Shortest Distance Route (Delhi to Ranchi) without Kanpur");
        railways.dijkstra(Delhi, Ranchi);
        System.out.println();
    }
} */