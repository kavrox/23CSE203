import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Stack;

class Vertex implements Comparable<Vertex> {
    private String element;
    private int position;
    private ArrayList<Edge> edge = new ArrayList<>();

    Vertex(String element, int position) {
        this.element = element;
        this.position = position;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }
    public void addEdge(Edge e) { edge.add(e); }
    public ArrayList<Edge> getInEdge() { return edge; }

    @Override 
    public int compareTo(Vertex other) {
        return this.element.compareTo(other.element);
    }
}

class Edge {
    private String element;
    private Vertex origin;
    private Vertex dest;
    private int oPos;
    private int dPos;
    private int weight; 

    Edge(String element, Vertex origin, Vertex dest, int oPos, int dPos, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.oPos = oPos;
        this.dPos = dPos;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setOpos(int opos) { this.oPos = opos; }
    public int getOpos() { return oPos; }
    public void setDpos(int dpos) { this.dPos = dpos; }
    public int getDpos() { return dPos; }
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

class AdjacencyGraphList {
    private ArrayList<Vertex> vertex;
    private int vcount;
    private int ecount;
    
    // Arrays for Goodrich style DFS/BFS traversal labels (using position as index)
    private String[] vertexLabels;
    private String[] edgeLabels;
    private int maxCapacity;

    // Time Complexity: O(1) - Instantiating the ArrayList, Arrays, and primitive variables takes constant time.
    AdjacencyGraphList(int capacity) {
        this.maxCapacity = capacity;
        vertex = new ArrayList<>(capacity);
        vertexLabels = new String[capacity];
        edgeLabels = new String[capacity * 4]; 
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Vertex> vertices() { return vertex; }

    // Time Complexity: O(V + E) - Iterates through all vertices and their edges, strictly adding unique edges.
    public ArrayList<Edge> edges() {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Vertex v : vertex) {
            for(Edge e : v.getInEdge()){
                if (e.getOrigin().compareTo(v) == 0) { 
                    edges.add(e); 
                }
            }
        }
        return edges;
    }

    // Time Complexity: O(deg(u)) - Iterates only through the incident edges of vertex u.
    public Edge getEdge(Vertex u, Vertex v) {
        for (Edge e : u.getInEdge()) {
            if ((e.getOrigin().compareTo(v) == 0) || (e.getDest().compareTo(v) == 0)) {
                return e;
            }
        }
        return null;
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

    // Time Complexity: O(1) - Returning the size of an ArrayList takes constant time.
    public int outDegree(Vertex v) { return v.getInEdge().size(); }

    // Time Complexity: O(1) - Directly returns the result of outDegree in constant time.
    public int inDegree(Vertex v) { return outDegree(v); }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Edge> incomingEdge(Vertex v) { return v.getInEdge(); }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Edge> outgoingEdge(Vertex v) { return incomingEdge(v); }

    // Time Complexity: O(1) Amortized - Creates an object and appends it to the end of the ArrayList.
    public void insertVertex(String x) {
        Vertex v = new Vertex(x, vcount);
        vertex.add(v);
        vcount++;
    }

    // Time Complexity: O(1) Amortized - Creates an object and appends it to the incidence lists.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) {
        int opos = u.getInEdge().size(); 
        int dpos = v.getInEdge().size(); 
        Edge e = new Edge(x, u, v, opos, dpos, weight);
        u.getInEdge().add(e);
        v.getInEdge().add(e);
        ecount++;
    }

    // Time Complexity: O(1) - Uses position attributes to overwrite elements with the last element in the incident lists.
    public void removeEdge(Edge e) {
        Vertex u = e.getOrigin();
        Vertex v = e.getDest();
        
        int opos = e.getOpos();
        ArrayList<Edge> uEdges = u.getInEdge();
        if (opos >= 0 && opos < uEdges.size()) {
            if (opos != uEdges.size() - 1) {
                Edge lastEdge = uEdges.get(uEdges.size() - 1);
                uEdges.set(opos, lastEdge);
                if (lastEdge.getOrigin().compareTo(u) == 0) lastEdge.setOpos(opos);
                else lastEdge.setDpos(opos);
            }
            uEdges.remove(uEdges.size() - 1);
        }
        
        int dpos = e.getDpos();
        ArrayList<Edge> vEdges = v.getInEdge();
        if (dpos >= 0 && dpos < vEdges.size()) {
            if (dpos != vEdges.size() - 1) {
                Edge lastEdge = vEdges.get(vEdges.size() - 1);
                vEdges.set(dpos, lastEdge);
                if (lastEdge.getOrigin().compareTo(v) == 0) lastEdge.setOpos(dpos);
                else lastEdge.setDpos(dpos);
            }
            vEdges.remove(vEdges.size() - 1);
        }
        ecount--;
    }

    // Time Complexity: O(deg(v)) - Removes all incident edges in O(deg(v)) time, then removes the vertex in O(1) time using swap.
    public void removeVertex(Vertex v) {
        ArrayList<Edge> incidentEdges = v.getInEdge();
        for (int i = incidentEdges.size() - 1; i >= 0; i--) {
            removeEdge(incidentEdges.get(i));
        }
        
        int pos = v.getPosition();
        int lastIndex = vertex.size() - 1;
        if (pos >= 0 && pos <= lastIndex) {
            if (pos != lastIndex) {
                Vertex lastVertex = vertex.get(lastIndex);
                vertex.set(pos, lastVertex);
                lastVertex.setPosition(pos);
            }
            vertex.remove(lastIndex);
            vcount--;
        }
    }

    // Time Complexity: O(1) - Array assignment is constant time.
    public void setLabel(Vertex v, String label) { vertexLabels[v.getPosition()] = label; }
    // Time Complexity: O(1) - Array lookup is constant time.
    public String getLabel(Vertex v) { return vertexLabels[v.getPosition()] != null ? vertexLabels[v.getPosition()] : "UNEXPLORED"; }
    // Time Complexity: O(1) - Array assignment is constant time. Uses a hash code since Edges don't have a global linear index here.
    public void setLabel(Edge e, String label) { edgeLabels[Math.abs(e.hashCode() % edgeLabels.length)] = label; }
    // Time Complexity: O(1) - Array lookup is constant time.
    public String getLabel(Edge e) { return edgeLabels[Math.abs(e.hashCode() % edgeLabels.length)] != null ? edgeLabels[Math.abs(e.hashCode() % edgeLabels.length)] : "UNEXPLORED"; }

    // ==========================================
    // 1. DEPTH FIRST SEARCH (DFS) - Iterative
    // ==========================================
    // Time Complexity: O(V + E) - Visits every vertex and explores each incident edge exactly twice (once per endpoint).
    public void performDFS() {
        for (Vertex u : vertices()) setLabel(u, "UNEXPLORED");
        for (Edge e : edges()) setLabel(e, "UNEXPLORED");
        
        System.out.print("DFS Traversal: ");
        Stack<Vertex> stack = new Stack<>();
        
        for (Vertex v : vertices()) {
            if (getLabel(v).equals("UNEXPLORED")) {
                stack.push(v);
                
                while (!stack.isEmpty()) {
                    Vertex current = stack.pop();
                    
                    if (getLabel(current).equals("UNEXPLORED")) {
                        setLabel(current, "VISITED");
                        System.out.print(current.getElement() + " ");
                        
                        ArrayList<Edge> outgoing = outgoingEdge(current);
                        // Iterating backwards to mimic the left-to-right exploration of a recursive stack
                        for (int i = outgoing.size() - 1; i >= 0; i--) {
                            Edge e = outgoing.get(i);
                            if (getLabel(e).equals("UNEXPLORED")) {
                                Vertex w = opposite(current, e);
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
    // 2. BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V + E) - Enqueues reachable vertices once and explores their incident edges exactly once.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (Vertex u : vertices()) setLabel(u, "UNEXPLORED");
        
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
            
            for (Edge e : outgoingEdge(v)) { 
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
    // 3. DIJKSTRA'S SHORTEST PATH
    // ==========================================
    // Time Complexity: O((V + E) log V) - Priority Queue operations take logarithmic time per vertex extraction and edge relaxation.
    public void dijkstra(Vertex v1, Vertex v2) {
        // Array based tracking instead of HashMaps
        int[] cost = new int[maxCapacity];
        Vertex[] previous = new Vertex[maxCapacity];
        boolean[] visited = new boolean[maxCapacity];
        PriorityQueue<PQNode> pqueue = new PriorityQueue<>();

        for (Vertex v : vertices()) {
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

            for (Edge e : outgoingEdge(v)) { 
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
    // 4. PRIM-JARNIK'S MINIMUM SPANNING TREE
    // ==========================================
    // Time Complexity: O((V + E) log V) - Extracts vertices from priority queue and updates their keys in logarithmic time.
    public ArrayList<Edge> primJarnikMST(Vertex s) {
        PriorityQueue<PQNode> Q = new PriorityQueue<>(); 
        
        // Array based tracking instead of HashMaps
        int[] distance = new int[maxCapacity]; 
        Edge[] parent = new Edge[maxCapacity];      
        boolean[] inCloud = new boolean[maxCapacity];  
        
        for (Vertex v : vertices()) {
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

        ArrayList<Edge> mstEdges = new ArrayList<>();
        int totalMSTWeight = 0;

        while (!Q.isEmpty()) {
            PQNode minNode = Q.poll(); 
            Vertex u = minNode.vertex; 
            int uPos = u.getPosition();

            if (inCloud[uPos]) continue;
            inCloud[uPos] = true; 
            
            if (parent[uPos] != null) {
                mstEdges.add(parent[uPos]);
                totalMSTWeight += parent[uPos].getWeight();
            }

            for (Edge e : u.getInEdge()) {
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

public class GraphAdjacencyListComplete {
    public static void main(String[] args) {
        AdjacencyGraphList airtelNetwork = new AdjacencyGraphList(20);

        // --- 1. INSERTION OF NODES (16 Major Telecom Hubs) ---
        airtelNetwork.insertVertex("Delhi");      // 0
        airtelNetwork.insertVertex("Mumbai");     // 1
        airtelNetwork.insertVertex("Chennai");    // 2
        airtelNetwork.insertVertex("Kolkata");    // 3
        airtelNetwork.insertVertex("Bangalore");  // 4
        airtelNetwork.insertVertex("Hyderabad");  // 5
        airtelNetwork.insertVertex("Pune");       // 6
        airtelNetwork.insertVertex("Ahmedabad");  // 7
        airtelNetwork.insertVertex("Jaipur");     // 8
        airtelNetwork.insertVertex("Lucknow");    // 9
        airtelNetwork.insertVertex("Bhopal");     // 10
        airtelNetwork.insertVertex("Nagpur");     // 11
        airtelNetwork.insertVertex("Patna");      // 12
        airtelNetwork.insertVertex("Bhubaneswar");// 13
        airtelNetwork.insertVertex("Guwahati");   // 14
        airtelNetwork.insertVertex("Kochi");      // 15

        ArrayList<Vertex> hubs = airtelNetwork.vertices();
        Vertex Delhi = hubs.get(0), Mumbai = hubs.get(1), Chennai = hubs.get(2), Kolkata = hubs.get(3);
        Vertex Bangalore = hubs.get(4), Hyderabad = hubs.get(5), Pune = hubs.get(6), Ahmedabad = hubs.get(7);
        Vertex Jaipur = hubs.get(8), Lucknow = hubs.get(9), Bhopal = hubs.get(10), Nagpur = hubs.get(11);
        Vertex Patna = hubs.get(12), Bhubaneswar = hubs.get(13), Guwahati = hubs.get(14), Kochi = hubs.get(15);

        // --- 2. INSERTION OF EDGES (Fiber links with Latency in ms as weights) ---
        airtelNetwork.insertEdge(Delhi, Jaipur, "DL-JP", 10);
        airtelNetwork.insertEdge(Delhi, Lucknow, "DL-LKO", 15);
        airtelNetwork.insertEdge(Delhi, Bhopal, "DL-BPL", 20);
        airtelNetwork.insertEdge(Jaipur, Ahmedabad, "JP-ADI", 18);
        airtelNetwork.insertEdge(Ahmedabad, Mumbai, "ADI-BOM", 12);
        airtelNetwork.insertEdge(Mumbai, Pune, "BOM-PNQ", 5);
        airtelNetwork.insertEdge(Pune, Bangalore, "PNQ-BLR", 25);
        airtelNetwork.insertEdge(Bangalore, Chennai, "BLR-MAA", 10);
        airtelNetwork.insertEdge(Bangalore, Kochi, "BLR-COK", 14);
        airtelNetwork.insertEdge(Chennai, Hyderabad, "MAA-HYD", 15);
        airtelNetwork.insertEdge(Hyderabad, Nagpur, "HYD-NAG", 12);
        airtelNetwork.insertEdge(Nagpur, Bhopal, "NAG-BPL", 10);
        airtelNetwork.insertEdge(Nagpur, Bhubaneswar, "NAG-BBI", 22);
        airtelNetwork.insertEdge(Bhubaneswar, Kolkata, "BBI-CCU", 14);
        airtelNetwork.insertEdge(Kolkata, Patna, "CCU-PAT", 16);
        airtelNetwork.insertEdge(Patna, Lucknow, "PAT-LKO", 12);
        airtelNetwork.insertEdge(Kolkata, Guwahati, "CCU-GAU", 30);
        airtelNetwork.insertEdge(Mumbai, Nagpur, "BOM-NAG", 24);
        airtelNetwork.insertEdge(Chennai, Kochi, "MAA-COK", 20);

        System.out.println("--- AIRTEL FIBER NETWORK DEMONSTRATION ---");
        System.out.println("Hubs (Vertices): " + airtelNetwork.numVertices());
        System.out.println("Fiber Links (Edges): " + airtelNetwork.numEdges());
        System.out.println("----------------------------------------------\n");

        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        airtelNetwork.performDFS();
        System.out.println();

        System.out.println("-> Finding Shortest Route by Hops (Delhi to Kochi)");
        airtelNetwork.bfs(Delhi, Kochi);
        System.out.println();

        // --- 4. SHORTEST PATH (Dijkstra) ---
        System.out.println("-> Calculating Lowest Latency Route (Dijkstra)");
        airtelNetwork.dijkstra(Delhi, Kochi);
        System.out.println();

        // --- 5. MINIMUM SPANNING TREE ---
        System.out.println("-> Calculating Minimum Spanning Tree (Prim-Jarnik)");
        ArrayList<Edge> mst = airtelNetwork.primJarnikMST(Delhi);
        System.out.print("Backbone Links (MST Edges): ");
        for (Edge e : mst) {
            System.out.print(e.getElement() + " ");
        }
        System.out.println("\n");

        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> HUB FAILURE: Removing Jaipur Hub from the Network");
        airtelNetwork.removeVertex(Jaipur);
        System.out.println("Hubs (Vertices) Post-Deletion: " + airtelNetwork.numVertices());
        System.out.println("Fiber Links (Edges) Post-Deletion: " + airtelNetwork.numEdges());
        System.out.println();

        System.out.println("-> Recalculating Lowest Latency Route (Delhi to Ahmedabad) without Jaipur");
        airtelNetwork.dijkstra(Delhi, Ahmedabad);
        System.out.println();
    }
}


/* import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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
    private int position;
    private int weight; 

    Edge(String element, Vertex origin, Vertex dest, int position, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.position = position;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }
    public void setWeight(int w) { this.weight = w; }
    public int getWeight() { return weight; }
}

// ==========================================
// ARRAY-BASED DISJOINT SET UNION (DSU)
// ==========================================
class DSU {
    private Vertex[] parent;
    private int[] rank;
    private int maxCapacity;

    // Time Complexity: O(1) - Instantiating arrays takes constant time relative to given capacity.
    public DSU(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        parent = new Vertex[maxCapacity];
        rank = new int[maxCapacity];
    }

    // Time Complexity: O(1) - Array assignment is O(1).
    public void makeSet(Vertex v) {
        int pos = v.getPosition();
        parent[pos] = v; 
        rank[pos] = 0;
    }

    // Time Complexity: O(α(V)) - Nearly constant time using path compression.
    public Vertex find(Vertex v) {
        int pos = v.getPosition();
        if (parent[pos] != v) {
            parent[pos] = find(parent[pos]); 
        }
        return parent[pos];
    }

    // Time Complexity: O(α(V)) - Nearly constant time using union by rank and path compression.
    public void union(Vertex u, Vertex v) {
        Vertex rootU = find(u);
        Vertex rootV = find(v);

        if (rootU != rootV) {
            int posU = rootU.getPosition();
            int posV = rootV.getPosition();
            
            int rankU = rank[posU];
            int rankV = rank[posV];

            if (rankU > rankV) {
                parent[posV] = rootU;
            } else if (rankU < rankV) {
                parent[posU] = rootV;
            } else {
                parent[posV] = rootU;
                rank[posU] = rankU + 1;
            }
        }
    }
}

// ==========================================
// CUSTOM MINIMUM HEAP FOR EDGES
// ==========================================
class MinHeap {
    private Edge[] heap;
    private int size;
    private int capacity;
    
    // Time Complexity: O(1) - Array initialization takes constant time relative to given capacity.
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Edge[capacity];
    }
    
    // Time Complexity: O(log E) - Inserts at the end and bubbles up the height of the heap.
    public void insert(Edge e) {
        if (size == capacity) return;
        heap[size] = e;
        bubbleUp(size);
        size++;
    }
    
    // Time Complexity: O(log E) - Removes root, replaces with last element, and bubbles down.
    public Edge removeMin() {
        if (size == 0) return null;
        Edge min = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        bubbleDown(0);
        return min;
    }
    
    // Time Complexity: O(1) - Primitive comparison.
    public boolean isEmpty() { return size == 0; }
    
    // Time Complexity: O(log E) - Ascends the tree path comparing parents.
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap[index].getWeight() < heap[parentIndex].getWeight()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }
    
    // Time Complexity: O(log E) - Descends the tree path comparing children.
    private void bubbleDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;
            
            if (right < size && heap[right].getWeight() < heap[left].getWeight()) {
                smallest = right;
            }
            if (heap[index].getWeight() > heap[smallest].getWeight()) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }
    
    // Time Complexity: O(1) - Swapping elements in an array is constant time.
    private void swap(int i, int j) {
        Edge temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}

// ==========================================
// EDGE LIST GRAPH
// ==========================================
class EdgeListGraph {
    private Vertex[] vertex;
    private Edge[] edge;
    private int vcount;
    private int ecount;
    private int maxVertices;
    private int maxEdges;
    
    // Array-based maps for Goodrich style DFS/BFS traversal labels (using position as index)
    private String[] vertexLabels;
    private String[] edgeLabels;

    // Time Complexity: O(1) - Instantiating arrays and primitive variables is constant time.
    EdgeListGraph(int n, int m) { 
        this.maxVertices = n;
        this.maxEdges = m;
        vertex = new Vertex[n];
        edge = new Edge[m];
        vertexLabels = new String[n];
        edgeLabels = new String[m];
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }
    
    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }
    
    // Time Complexity: O(1) - Returning an array reference takes constant time.
    public Vertex[] vertices() { return vertex; }
    
    // Time Complexity: O(1) - Returning an array reference takes constant time.
    public Edge[] edges() { return edge; }

    // Time Complexity: O(E) - In the worst-case, it iterates through the entire edge array.
    public Edge getEdge(Vertex u, Vertex v) { 
        for (int i = 0; i < ecount; i++) {
            Vertex o = edge[i].getOrigin();
            Vertex d = edge[i].getDest();
            if ((o.compareTo(u) == 0 && d.compareTo(v) == 0) || 
                (d.compareTo(u) == 0 && o.compareTo(v) == 0)) {
                return edge[i];
            }
        }
        return null;
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

    // Time Complexity: O(E) - Scans the edge list twice: once to count, once to populate the array.
    public Edge[] outgoingEdge(Vertex v) {
        int count = 0;
        for (int i = 0; i < ecount; i++) {
            if (edge[i].getOrigin().compareTo(v) == 0 || edge[i].getDest().compareTo(v) == 0) {
                count++;
            }
        }
        Edge[] t = new Edge[count];
        int c = 0;
        for (int i = 0; i < ecount; i++) {
            if (edge[i].getOrigin().compareTo(v) == 0 || edge[i].getDest().compareTo(v) == 0) {
                t[c++] = edge[i];
            }
        }
        return t;
    }

    // Time Complexity: O(1) - Creates an object and assigns it to a pre-known array index.
    public void insertVertex(String x) { 
        if (vcount >= maxVertices) return;
        Vertex v = new Vertex(x, vcount);
        vertex[vcount] = v;
        vertexLabels[vcount] = "UNEXPLORED";
        vcount++;
    }

    // Time Complexity: O(1) - Creates an object and assigns it to a pre-known array index.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) { 
        if (ecount >= maxEdges) return;
        Edge e = new Edge(x, u, v, ecount, weight);
        edge[ecount] = e;
        edgeLabels[ecount] = "UNEXPLORED";
        ecount++;
    }

    // Time Complexity: O(1) - Direct array access by index.
    public void setLabel(Vertex v, String label) { 
        vertexLabels[v.getPosition()] = label; 
    }
    
    // Time Complexity: O(1) - Direct array access by index.
    public String getLabel(Vertex v) { 
        String label = vertexLabels[v.getPosition()];
        return label != null ? label : "UNEXPLORED";
    }
    
    // Time Complexity: O(1) - Direct array access by index.
    public void setLabel(Edge e, String label) { 
        edgeLabels[e.getPosition()] = label; 
    }
    
    // Time Complexity: O(1) - Direct array access by index.
    public String getLabel(Edge e) { 
        String label = edgeLabels[e.getPosition()];
        return label != null ? label : "UNEXPLORED";
    }

    // ==========================================
    // 1. DEPTH FIRST SEARCH (DFS)
    // ==========================================
    // Time Complexity: O(V * E) - For an Edge List representation, finding neighbors takes O(E).
    public void performDFS() {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        for (int i = 0; i < ecount; i++) setLabel(edge[i], "UNEXPLORED");
        
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
                        // Iterating backwards maintains exact left-to-right processing order typical of recursive DFS
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
    // 2. BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V * E) - Iterates reachable vertices and for each, scans all E edges.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (int i = 0; i < vcount; i++) setLabel(vertex[i], "UNEXPLORED");
        for (int i = 0; i < ecount; i++) setLabel(edge[i], "UNEXPLORED");
        
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
    // 3. KRUSKAL'S ALGORITHM
    // ==========================================
    // Time Complexity: O(E log E) - Sorting edges with priority queue dominates the nearly constant DSU operations.
    public Edge[] kruskalMST() {
        DSU dsu = new DSU(maxVertices);
        for (int i = 0; i < vcount; i++) {
            dsu.makeSet(vertex[i]); 
        }
        
        MinHeap Q = new MinHeap(ecount); 
        for (int i = 0; i < ecount; i++) { 
            Q.insert(edge[i]);
        }
        
        Edge[] T = new Edge[vcount - 1]; 
        int tCount = 0;
        int totalMSTWeight = 0;
        
        while (tCount < vcount - 1 && !Q.isEmpty()) { 
            Edge e = Q.removeMin(); 
            
            Vertex[] endpoints = endVertices(e); 
            Vertex u = endpoints[0];
            Vertex v = endpoints[1];
            
            if (dsu.find(v) != dsu.find(u)) { 
                T[tCount++] = e; 
                totalMSTWeight += e.getWeight();
                dsu.union(v, u); 
            }
        }
        
        System.out.println("Kruskal's MST Computed. Total Weight: " + totalMSTWeight);
        return T; 
    }

    // ==========================================
    // 4. BARUVKA'S ALGORITHM
    // ==========================================
    // Time Complexity: O(E log V) - Each while iteration takes O(E) to scan all edges, and reduces components by half.
    public Edge[] baruvkaMST() {
        DSU dsu = new DSU(maxVertices);
        for (int i = 0; i < vcount; i++) { 
            dsu.makeSet(vertex[i]);
        }
        
        Edge[] T = new Edge[vcount - 1]; 
        int tCount = 0;
        int totalMSTWeight = 0;
        int numComponents = vcount;

        while (tCount < vcount - 1 && numComponents > 1) { 
            // Using array for cheapest outgoing edges (indexed by root vertex position)
            Edge[] cheapest = new Edge[maxVertices];

            for (int i = 0; i < ecount; i++) {
                Edge e = edge[i];
                Vertex u = e.getOrigin();
                Vertex v = e.getDest();
                
                Vertex rootU = dsu.find(u);
                Vertex rootV = dsu.find(v);
                
                if (rootU != rootV) {
                    int posU = rootU.getPosition();
                    int posV = rootV.getPosition();
                    
                    if (cheapest[posU] == null || e.getWeight() < cheapest[posU].getWeight()) {
                        cheapest[posU] = e;
                    }
                    if (cheapest[posV] == null || e.getWeight() < cheapest[posV].getWeight()) {
                        cheapest[posV] = e;
                    }
                }
            }

            for (int i = 0; i < vcount; i++) { 
                // Only process roots
                if (dsu.find(vertex[i]) == vertex[i] && cheapest[i] != null) {
                    Edge e = cheapest[i]; 
                    
                    Vertex rootU = dsu.find(e.getOrigin());
                    Vertex rootV = dsu.find(e.getDest());

                    if (rootU != rootV) { 
                        T[tCount++] = e; 
                        totalMSTWeight += e.getWeight();
                        
                        dsu.union(rootU, rootV);
                        numComponents--;
                    }
                }
            }
        }
        
        System.out.println("Baruvka's MST Computed. Total Weight: " + totalMSTWeight);
        return T; 
    }

    // Time Complexity: O(1) - Uses the position attribute to directly overwrite it with the last element.
    public void removeEdge(Edge e) { 
        int pos = e.getPosition();
        if (pos < 0 || pos >= ecount) return;
        
        if (pos != ecount - 1) {          
            edge[pos] = edge[ecount - 1]; 
            edge[pos].setPosition(pos); 
            // Also need to swap the label arrays to match
            edgeLabels[pos] = edgeLabels[ecount - 1];
        }
        edge[ecount - 1] = null;
        edgeLabels[ecount - 1] = null;
        ecount--;
    }

    // Time Complexity: O(E) - Removing incident edges takes O(E) to scan, removing the vertex itself is O(1).
    public void removeVertex(Vertex v) { 
        for (int i = ecount - 1; i >= 0; i--) { 
            if (edge[i].getOrigin().compareTo(v) == 0 || edge[i].getDest().compareTo(v) == 0) {
                removeEdge(edge[i]);
            }
        }
        
        int pos = v.getPosition();
        if (pos < 0 || pos >= vcount) return;
        
        if (pos != vcount - 1) {              
            vertex[pos] = vertex[vcount - 1]; 
            vertex[pos].setPosition(pos);     
            // Swap labels to maintain parallel structure
            vertexLabels[pos] = vertexLabels[vcount - 1];
        }
        vertex[vcount - 1] = null;
        vertexLabels[vcount - 1] = null;
        vcount--;
    }
}

public class GraphEdgeListComplete {
    public static void main(String[] args) {
        // Initialize an EdgeListGraph large enough to hold Indian Railways Network subset
        EdgeListGraph railways = new EdgeListGraph(20, 40);
        
        // --- 1. INSERTION OF NODES (16 Major Stations) ---
        railways.insertVertex("Delhi");      // 0
        railways.insertVertex("Mumbai");     // 1
        railways.insertVertex("Chennai");    // 2
        railways.insertVertex("Kolkata");    // 3
        railways.insertVertex("Bangalore");  // 4
        railways.insertVertex("Hyderabad");  // 5
        railways.insertVertex("Pune");       // 6
        railways.insertVertex("Ahmedabad");  // 7
        railways.insertVertex("Jaipur");     // 8
        railways.insertVertex("Lucknow");    // 9
        railways.insertVertex("Bhopal");     // 10
        railways.insertVertex("Nagpur");     // 11
        railways.insertVertex("Patna");      // 12
        railways.insertVertex("Bhubaneswar");// 13
        railways.insertVertex("Guwahati");   // 14
        railways.insertVertex("Kochi");      // 15
        
        Vertex[] stn = railways.vertices();
        Vertex Delhi = stn[0], Mumbai = stn[1], Chennai = stn[2], Kolkata = stn[3];
        Vertex Bangalore = stn[4], Hyderabad = stn[5], Pune = stn[6], Ahmedabad = stn[7];
        Vertex Jaipur = stn[8], Lucknow = stn[9], Bhopal = stn[10], Nagpur = stn[11];
        Vertex Patna = stn[12], Bhubaneswar = stn[13], Guwahati = stn[14], Kochi = stn[15];

        // --- 2. INSERTION OF EDGES (Approx distances in KM as weights) ---
        railways.insertEdge(Delhi, Jaipur, "DL-JP", 300);
        railways.insertEdge(Delhi, Lucknow, "DL-LKO", 500);
        railways.insertEdge(Delhi, Bhopal, "DL-BPL", 700);
        railways.insertEdge(Jaipur, Ahmedabad, "JP-ADI", 600);
        railways.insertEdge(Ahmedabad, Mumbai, "ADI-BCT", 500);
        railways.insertEdge(Mumbai, Pune, "BCT-PUNE", 150);
        railways.insertEdge(Pune, Bangalore, "PUNE-SBC", 800);
        railways.insertEdge(Bangalore, Chennai, "SBC-MAS", 350);
        railways.insertEdge(Bangalore, Kochi, "SBC-ERS", 500);
        railways.insertEdge(Chennai, Hyderabad, "MAS-SC", 600);
        railways.insertEdge(Hyderabad, Nagpur, "SC-NGP", 500);
        railways.insertEdge(Nagpur, Bhopal, "NGP-BPL", 400);
        railways.insertEdge(Nagpur, Bhubaneswar, "NGP-BBS", 800);
        railways.insertEdge(Bhubaneswar, Kolkata, "BBS-HWH", 450);
        railways.insertEdge(Kolkata, Patna, "HWH-PNBE", 500);
        railways.insertEdge(Patna, Lucknow, "PNBE-LKO", 550);
        railways.insertEdge(Kolkata, Guwahati, "HWH-GHY", 1000);
        railways.insertEdge(Mumbai, Nagpur, "BCT-NGP", 800);
        railways.insertEdge(Chennai, Kochi, "MAS-ERS", 700);
        
        System.out.println("--- INDIAN RAILWAYS NETWORK DEMONSTRATION ---");
        System.out.println("Stations (Vertices): " + railways.numVertices());
        System.out.println("Routes (Edges): " + railways.numEdges());
        System.out.println("----------------------------------------------\n");
        
        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        railways.performDFS();
        System.out.println();
        
        // --- 4. SHORTEST PATH FINDING (Unweighted / Least Hops via BFS) ---
        System.out.println("-> Finding Shortest Route by Hops (Delhi to Kochi)");
        railways.bfs(Delhi, Kochi);
        System.out.println();
        
        // --- 5. MINIMUM SPANNING TREES ---
        System.out.println("-> Calculating Minimum Spanning Tree (Kruskal's)");
        Edge[] kruskalMST = railways.kruskalMST();
        System.out.print("Tracks in MST: ");
        for (int i = 0; i < kruskalMST.length; i++) {
            if (kruskalMST[i] != null) System.out.print(kruskalMST[i].getElement() + " ");
        }
        System.out.println("\n");
        
        System.out.println("-> Calculating Minimum Spanning Tree (Baruvka's)");
        Edge[] baruvkaMST = railways.baruvkaMST();
        System.out.print("Tracks in MST: ");
        for (int i = 0; i < baruvkaMST.length; i++) {
            if (baruvkaMST[i] != null) System.out.print(baruvkaMST[i].getElement() + " ");
        }
        System.out.println("\n");
        
        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> STATION CLOSURE: Removing Jaipur Station from the Network");
        railways.removeVertex(Jaipur);
        System.out.println("Stations (Vertices) Post-Deletion: " + railways.numVertices());
        System.out.println("Routes (Edges) Post-Deletion: " + railways.numEdges());
        System.out.println();
        
        System.out.println("-> Searching Alternative Route by Hops (Delhi to Ahmedabad) without Jaipur");
        railways.bfs(Delhi, Ahmedabad);
        System.out.println();
    }
} */


/* import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;

class Vertex implements Comparable<Vertex> {
    private String element;
    private int position;
    private ArrayList<Edge> edge = new ArrayList<>();

    Vertex(String element, int position) {
        this.element = element;
        this.position = position;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }
    public void addEdge(Edge e) { edge.add(e); }
    public ArrayList<Edge> getInEdge() { return edge; }

    @Override 
    public int compareTo(Vertex other) {
        return this.element.compareTo(other.element);
    }
}

class Edge {
    private String element;
    private Vertex origin;
    private Vertex dest;
    private int oPos;
    private int dPos;
    private int weight; 

    Edge(String element, Vertex origin, Vertex dest, int oPos, int dPos, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.oPos = oPos;
        this.dPos = dPos;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setOpos(int opos) { this.oPos = opos; }
    public int getOpos() { return oPos; }
    public void setDpos(int dpos) { this.dPos = dpos; }
    public int getDpos() { return dPos; }
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

class AdjacencyGraphList {
    private ArrayList<Vertex> vertex;
    private int vcount;
    private int ecount;
    
    // Arrays for Goodrich style DFS/BFS traversal labels (using position as index)[cite: 2]
    private String[] vertexLabels;
    private String[] edgeLabels;
    private int maxCapacity;

    // Time Complexity: O(1) - Instantiating the ArrayList, Arrays, and primitive variables takes constant time.
    AdjacencyGraphList(int capacity) {
        this.maxCapacity = capacity;
        vertex = new ArrayList<>(capacity);
        // Assuming edge capacity is roughly 2x vertex capacity for label arrays
        vertexLabels = new String[capacity];
        edgeLabels = new String[capacity * 4]; 
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Vertex> vertices() { return vertex; }

    // Time Complexity: O(V + E) - Iterates through all vertices and their edges, strictly adding unique edges.
    public ArrayList<Edge> edges() {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Vertex v : vertex) {
            for(Edge e : v.getInEdge()){
                if (e.getOrigin().compareTo(v) == 0) { 
                    edges.add(e); 
                }
            }
        }
        return edges;
    }

    // Time Complexity: O(deg(u)) - Iterates only through the incident edges of vertex u.
    public Edge getEdge(Vertex u, Vertex v) {
        for (Edge e : u.getInEdge()) {
            if ((e.getOrigin().compareTo(v) == 0) || (e.getDest().compareTo(v) == 0)) {
                return e;
            }
        }
        return null;
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

    // Time Complexity: O(1) - Returning the size of an ArrayList takes constant time.
    public int outDegree(Vertex v) { return v.getInEdge().size(); }

    // Time Complexity: O(1) - Directly returns the result of outDegree in constant time.
    public int inDegree(Vertex v) { return outDegree(v); }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Edge> incomingEdge(Vertex v) { return v.getInEdge(); }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Edge> outgoingEdge(Vertex v) { return incomingEdge(v); }

    // Time Complexity: O(1) Amortized - Creates an object and appends it to the end of the ArrayList.
    public void insertVertex(String x) {
        Vertex v = new Vertex(x, vcount);
        vertex.add(v);
        vcount++;
    }

    // Time Complexity: O(1) Amortized - Creates an object and appends it to the incidence lists.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) {
        int opos = u.getInEdge().size(); 
        int dpos = v.getInEdge().size(); 
        Edge e = new Edge(x, u, v, opos, dpos, weight);
        u.getInEdge().add(e);
        v.getInEdge().add(e);
        ecount++;
    }

    // Time Complexity: O(1) - Uses position attributes to overwrite elements with the last element in the incident lists.
    public void removeEdge(Edge e) {
        Vertex u = e.getOrigin();
        Vertex v = e.getDest();
        
        int opos = e.getOpos();
        ArrayList<Edge> uEdges = u.getInEdge();
        if (opos >= 0 && opos < uEdges.size()) {
            if (opos != uEdges.size() - 1) {
                Edge lastEdge = uEdges.get(uEdges.size() - 1);
                uEdges.set(opos, lastEdge);
                if (lastEdge.getOrigin().compareTo(u) == 0) lastEdge.setOpos(opos);
                else lastEdge.setDpos(opos);
            }
            uEdges.remove(uEdges.size() - 1);
        }
        
        int dpos = e.getDpos();
        ArrayList<Edge> vEdges = v.getInEdge();
        if (dpos >= 0 && dpos < vEdges.size()) {
            if (dpos != vEdges.size() - 1) {
                Edge lastEdge = vEdges.get(vEdges.size() - 1);
                vEdges.set(dpos, lastEdge);
                if (lastEdge.getOrigin().compareTo(v) == 0) lastEdge.setOpos(dpos);
                else lastEdge.setDpos(dpos);
            }
            vEdges.remove(vEdges.size() - 1);
        }
        ecount--;
    }

    // Time Complexity: O(deg(v)) - Removes all incident edges in O(deg(v)) time, then removes the vertex in O(1) time using swap.
    public void removeVertex(Vertex v) {
        ArrayList<Edge> incidentEdges = v.getInEdge();
        for (int i = incidentEdges.size() - 1; i >= 0; i--) {
            removeEdge(incidentEdges.get(i));
        }
        
        int pos = v.getPosition();
        int lastIndex = vertex.size() - 1;
        if (pos >= 0 && pos <= lastIndex) {
            if (pos != lastIndex) {
                Vertex lastVertex = vertex.get(lastIndex);
                vertex.set(pos, lastVertex);
                lastVertex.setPosition(pos);
            }
            vertex.remove(lastIndex);
            vcount--;
        }
    }

    // Time Complexity: O(1) - Array assignment is constant time.
    public void setLabel(Vertex v, String label) { vertexLabels[v.getPosition()] = label; }
    // Time Complexity: O(1) - Array lookup is constant time.
    public String getLabel(Vertex v) { return vertexLabels[v.getPosition()] != null ? vertexLabels[v.getPosition()] : "UNEXPLORED"; }
    // Time Complexity: O(1) - Array assignment is constant time. Uses a hash code since Edges don't have a global linear index here.
    public void setLabel(Edge e, String label) { edgeLabels[Math.abs(e.hashCode() % edgeLabels.length)] = label; }
    // Time Complexity: O(1) - Array lookup is constant time.
    public String getLabel(Edge e) { return edgeLabels[Math.abs(e.hashCode() % edgeLabels.length)] != null ? edgeLabels[Math.abs(e.hashCode() % edgeLabels.length)] : "UNEXPLORED"; }

    // ==========================================
    // 1. DEPTH FIRST SEARCH (DFS)
    // ==========================================
    // Time Complexity: O(V + E) - Visits every vertex and explores each incident edge exactly twice (once per endpoint).
    public void performDFS() {
        for (Vertex u : vertices()) setLabel(u, "UNEXPLORED");
        for (Edge e : edges()) setLabel(e, "UNEXPLORED");
        
        System.out.print("DFS Traversal: ");
        for (Vertex v : vertices()) {
            if (getLabel(v).equals("UNEXPLORED")) {
                dfs(this, v);
            }
        }
        System.out.println();
    }

    // Time Complexity: O(deg(v)) per recursive call - Explores only the incident edges of the current vertex.
    private void dfs(AdjacencyGraphList G, Vertex v) {
        setLabel(v, "VISITED");
        System.out.print(v.getElement() + " ");
        
        for (Edge e : G.outgoingEdge(v)) {
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
    // 2. BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V + E) - Enqueues reachable vertices once and explores their incident edges exactly once.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (Vertex u : vertices()) setLabel(u, "UNEXPLORED");
        
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
            
            for (Edge e : outgoingEdge(v)) { 
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
    // 3. DIJKSTRA'S SHORTEST PATH
    // ==========================================
    // Time Complexity: O((V + E) log V) - Priority Queue operations take logarithmic time per vertex extraction and edge relaxation.
    public void dijkstra(Vertex v1, Vertex v2) {
        // Array based tracking instead of HashMaps
        int[] cost = new int[maxCapacity];
        Vertex[] previous = new Vertex[maxCapacity];
        boolean[] visited = new boolean[maxCapacity];
        PriorityQueue<PQNode> pqueue = new PriorityQueue<>();

        for (Vertex v : vertices()) {
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

            for (Edge e : outgoingEdge(v)) { 
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
    // 4. PRIM-JARNIK'S MINIMUM SPANNING TREE
    // ==========================================
    // Time Complexity: O((V + E) log V) - Extracts vertices from priority queue and updates their keys in logarithmic time.
    public ArrayList<Edge> primJarnikMST(Vertex s) {
        PriorityQueue<PQNode> Q = new PriorityQueue<>(); 
        
        // Array based tracking instead of HashMaps
        int[] distance = new int[maxCapacity]; 
        Edge[] parent = new Edge[maxCapacity];      
        boolean[] inCloud = new boolean[maxCapacity];  
        
        for (Vertex v : vertices()) {
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

        ArrayList<Edge> mstEdges = new ArrayList<>();
        int totalMSTWeight = 0;

        while (!Q.isEmpty()) {
            PQNode minNode = Q.poll(); 
            Vertex u = minNode.vertex; 
            int uPos = u.getPosition();

            if (inCloud[uPos]) continue;
            inCloud[uPos] = true; 
            
            if (parent[uPos] != null) {
                mstEdges.add(parent[uPos]);
                totalMSTWeight += parent[uPos].getWeight();
            }

            for (Edge e : u.getInEdge()) {
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

public class GraphAdjacencyListComplete {
    public static void main(String[] args) {
        AdjacencyGraphList airtelNetwork = new AdjacencyGraphList(20);

        // --- 1. INSERTION OF NODES (16 Major Telecom Hubs) ---
        airtelNetwork.insertVertex("Delhi");      // 0
        airtelNetwork.insertVertex("Mumbai");     // 1
        airtelNetwork.insertVertex("Chennai");    // 2
        airtelNetwork.insertVertex("Kolkata");    // 3
        airtelNetwork.insertVertex("Bangalore");  // 4
        airtelNetwork.insertVertex("Hyderabad");  // 5
        airtelNetwork.insertVertex("Pune");       // 6
        airtelNetwork.insertVertex("Ahmedabad");  // 7
        airtelNetwork.insertVertex("Jaipur");     // 8
        airtelNetwork.insertVertex("Lucknow");    // 9
        airtelNetwork.insertVertex("Bhopal");     // 10
        airtelNetwork.insertVertex("Nagpur");     // 11
        airtelNetwork.insertVertex("Patna");      // 12
        airtelNetwork.insertVertex("Bhubaneswar");// 13
        airtelNetwork.insertVertex("Guwahati");   // 14
        airtelNetwork.insertVertex("Kochi");      // 15

        ArrayList<Vertex> hubs = airtelNetwork.vertices();
        Vertex Delhi = hubs.get(0), Mumbai = hubs.get(1), Chennai = hubs.get(2), Kolkata = hubs.get(3);
        Vertex Bangalore = hubs.get(4), Hyderabad = hubs.get(5), Pune = hubs.get(6), Ahmedabad = hubs.get(7);
        Vertex Jaipur = hubs.get(8), Lucknow = hubs.get(9), Bhopal = hubs.get(10), Nagpur = hubs.get(11);
        Vertex Patna = hubs.get(12), Bhubaneswar = hubs.get(13), Guwahati = hubs.get(14), Kochi = hubs.get(15);

        // --- 2. INSERTION OF EDGES (Fiber links with Latency in ms as weights) ---
        airtelNetwork.insertEdge(Delhi, Jaipur, "DL-JP", 10);
        airtelNetwork.insertEdge(Delhi, Lucknow, "DL-LKO", 15);
        airtelNetwork.insertEdge(Delhi, Bhopal, "DL-BPL", 20);
        airtelNetwork.insertEdge(Jaipur, Ahmedabad, "JP-ADI", 18);
        airtelNetwork.insertEdge(Ahmedabad, Mumbai, "ADI-BOM", 12);
        airtelNetwork.insertEdge(Mumbai, Pune, "BOM-PNQ", 5);
        airtelNetwork.insertEdge(Pune, Bangalore, "PNQ-BLR", 25);
        airtelNetwork.insertEdge(Bangalore, Chennai, "BLR-MAA", 10);
        airtelNetwork.insertEdge(Bangalore, Kochi, "BLR-COK", 14);
        airtelNetwork.insertEdge(Chennai, Hyderabad, "MAA-HYD", 15);
        airtelNetwork.insertEdge(Hyderabad, Nagpur, "HYD-NAG", 12);
        airtelNetwork.insertEdge(Nagpur, Bhopal, "NAG-BPL", 10);
        airtelNetwork.insertEdge(Nagpur, Bhubaneswar, "NAG-BBI", 22);
        airtelNetwork.insertEdge(Bhubaneswar, Kolkata, "BBI-CCU", 14);
        airtelNetwork.insertEdge(Kolkata, Patna, "CCU-PAT", 16);
        airtelNetwork.insertEdge(Patna, Lucknow, "PAT-LKO", 12);
        airtelNetwork.insertEdge(Kolkata, Guwahati, "CCU-GAU", 30);
        airtelNetwork.insertEdge(Mumbai, Nagpur, "BOM-NAG", 24);
        airtelNetwork.insertEdge(Chennai, Kochi, "MAA-COK", 20);

        System.out.println("--- AIRTEL FIBER NETWORK DEMONSTRATION ---");
        System.out.println("Hubs (Vertices): " + airtelNetwork.numVertices());
        System.out.println("Fiber Links (Edges): " + airtelNetwork.numEdges());
        System.out.println("----------------------------------------------\n");

        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        airtelNetwork.performDFS();
        System.out.println();

        System.out.println("-> Finding Shortest Route by Hops (Delhi to Kochi)");
        airtelNetwork.bfs(Delhi, Kochi);
        System.out.println();

        // --- 4. SHORTEST PATH (Dijkstra) ---
        System.out.println("-> Calculating Lowest Latency Route (Dijkstra)");
        airtelNetwork.dijkstra(Delhi, Kochi);
        System.out.println();

        // --- 5. MINIMUM SPANNING TREE ---
        System.out.println("-> Calculating Minimum Spanning Tree (Prim-Jarnik)");
        ArrayList<Edge> mst = airtelNetwork.primJarnikMST(Delhi);
        System.out.print("Backbone Links (MST Edges): ");
        for (Edge e : mst) {
            System.out.print(e.getElement() + " ");
        }
        System.out.println("\n");

        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> HUB FAILURE: Removing Jaipur Hub from the Network");
        airtelNetwork.removeVertex(Jaipur);
        System.out.println("Hubs (Vertices) Post-Deletion: " + airtelNetwork.numVertices());
        System.out.println("Fiber Links (Edges) Post-Deletion: " + airtelNetwork.numEdges());
        System.out.println();

        System.out.println("-> Recalculating Lowest Latency Route (Delhi to Ahmedabad) without Jaipur");
        airtelNetwork.dijkstra(Delhi, Ahmedabad);
        System.out.println();
    }
} */


/* import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;

class Vertex implements Comparable<Vertex> {
    private String element;
    private int position;
    private ArrayList<Edge> edge = new ArrayList<>();

    Vertex(String element, int position) {
        this.element = element;
        this.position = position;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setPosition(int pos) { this.position = pos; }
    public int getPosition() { return position; }
    public void addEdge(Edge e) { edge.add(e); }
    public ArrayList<Edge> getInEdge() { return edge; }

    @Override 
    public int compareTo(Vertex other) {
        return this.element.compareTo(other.element);
    }
}

class Edge {
    private String element;
    private Vertex origin;
    private Vertex dest;
    private int oPos;
    private int dPos;
    private int weight; 

    Edge(String element, Vertex origin, Vertex dest, int oPos, int dPos, int weight) {
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.oPos = oPos;
        this.dPos = dPos;
        this.weight = weight;
    }

    public void setElement(String element) { this.element = element; }
    public String getElement() { return element; }
    public void setOrigin(Vertex o) { this.origin = o; }
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex d) { this.dest = d; }
    public Vertex getDest() { return dest; }
    public void setOpos(int opos) { this.oPos = opos; }
    public int getOpos() { return oPos; }
    public void setDpos(int dpos) { this.dPos = dpos; }
    public int getDpos() { return dPos; }
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

class AdjacencyGraphList {
    private ArrayList<Vertex> vertex;
    private int vcount;
    private int ecount;
    
    private HashMap<Vertex, String> vertexLabels;
    private HashMap<Edge, String> edgeLabels;

    // Time Complexity: O(1) - Instantiating the ArrayList, HashMaps, and primitive variables takes constant time.
    AdjacencyGraphList() {
        vertex = new ArrayList<>();
        vertexLabels = new HashMap<>();
        edgeLabels = new HashMap<>();
        vcount = ecount = 0;
    }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numVertices() { return vcount; }

    // Time Complexity: O(1) - Returning a primitive variable takes constant time.
    public int numEdges() { return ecount; }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Vertex> vertices() { return vertex; }

    // Time Complexity: O(V + E) - Iterates through all vertices and their edges, strictly adding unique edges.
    public ArrayList<Edge> edges() {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Vertex v : vertex) {
            for(Edge e : v.getInEdge()){
                if (e.getOrigin().compareTo(v) == 0) { 
                    edges.add(e); 
                }
            }
        }
        return edges;
    }

    // Time Complexity: O(deg(u)) - Iterates only through the incident edges of vertex u.
    public Edge getEdge(Vertex u, Vertex v) {
        for (Edge e : u.getInEdge()) {
            if ((e.getOrigin().compareTo(v) == 0) || (e.getDest().compareTo(v) == 0)) {
                return e;
            }
        }
        return null;
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

    // Time Complexity: O(1) - Returning the size of an ArrayList takes constant time.
    public int outDegree(Vertex v) { return v.getInEdge().size(); }

    // Time Complexity: O(1) - Directly returns the result of outDegree in constant time.
    public int inDegree(Vertex v) { return outDegree(v); }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Edge> incomingEdge(Vertex v) { return v.getInEdge(); }

    // Time Complexity: O(1) - Returning an ArrayList reference takes constant time.
    public ArrayList<Edge> outgoingEdge(Vertex v) { return incomingEdge(v); }

    // Time Complexity: O(1) Amortized - Creates an object and appends it to the end of the ArrayList.
    public void insertVertex(String x) {
        Vertex v = new Vertex(x, vcount);
        vertex.add(v);
        vcount++;
    }

    // Time Complexity: O(1) Amortized - Creates an object and appends it to the incidence lists.
    public void insertEdge(Vertex u, Vertex v, String x, int weight) {
        int opos = u.getInEdge().size(); 
        int dpos = v.getInEdge().size(); 
        Edge e = new Edge(x, u, v, opos, dpos, weight);
        u.getInEdge().add(e);
        v.getInEdge().add(e);
        ecount++;
    }

    // Time Complexity: O(1) - Uses position attributes to overwrite elements with the last element in the incident lists.
    public void removeEdge(Edge e) {
        Vertex u = e.getOrigin();
        Vertex v = e.getDest();
        
        int opos = e.getOpos();
        ArrayList<Edge> uEdges = u.getInEdge();
        if (opos >= 0 && opos < uEdges.size()) {
            if (opos != uEdges.size() - 1) {
                Edge lastEdge = uEdges.get(uEdges.size() - 1);
                uEdges.set(opos, lastEdge);
                if (lastEdge.getOrigin().compareTo(u) == 0) lastEdge.setOpos(opos);
                else lastEdge.setDpos(opos);
            }
            uEdges.remove(uEdges.size() - 1);
        }
        
        int dpos = e.getDpos();
        ArrayList<Edge> vEdges = v.getInEdge();
        if (dpos >= 0 && dpos < vEdges.size()) {
            if (dpos != vEdges.size() - 1) {
                Edge lastEdge = vEdges.get(vEdges.size() - 1);
                vEdges.set(dpos, lastEdge);
                if (lastEdge.getOrigin().compareTo(v) == 0) lastEdge.setOpos(dpos);
                else lastEdge.setDpos(dpos);
            }
            vEdges.remove(vEdges.size() - 1);
        }
        ecount--;
    }

    // Time Complexity: O(deg(v)) - Removes all incident edges in O(deg(v)) time, then removes the vertex in O(1) time using swap.
    public void removeVertex(Vertex v) {
        ArrayList<Edge> incidentEdges = v.getInEdge();
        for (int i = incidentEdges.size() - 1; i >= 0; i--) {
            removeEdge(incidentEdges.get(i));
        }
        
        int pos = v.getPosition();
        int lastIndex = vertex.size() - 1;
        if (pos >= 0 && pos <= lastIndex) {
            if (pos != lastIndex) {
                Vertex lastVertex = vertex.get(lastIndex);
                vertex.set(pos, lastVertex);
                lastVertex.setPosition(pos);
            }
            vertex.remove(lastIndex);
            vcount--;
        }
    }

    // Time Complexity: O(1) Expected - Putting into a HashMap takes expected constant time.
    public void setLabel(Vertex v, String label) { vertexLabels.put(v, label); }
    // Time Complexity: O(1) Expected - Getting from a HashMap takes expected constant time.
    public String getLabel(Vertex v) { return vertexLabels.getOrDefault(v, "UNEXPLORED"); }
    // Time Complexity: O(1) Expected - Putting into a HashMap takes expected constant time.
    public void setLabel(Edge e, String label) { edgeLabels.put(e, label); }
    // Time Complexity: O(1) Expected - Getting from a HashMap takes expected constant time.
    public String getLabel(Edge e) { return edgeLabels.getOrDefault(e, "UNEXPLORED"); }

    // ==========================================
    // 1. DEPTH FIRST SEARCH (DFS)
    // ==========================================
    // Time Complexity: O(V + E) - Visits every vertex and explores each incident edge exactly twice (once per endpoint).
    public void performDFS() {
        for (Vertex u : vertices()) setLabel(u, "UNEXPLORED");
        for (Edge e : edges()) setLabel(e, "UNEXPLORED");
        
        System.out.print("DFS Traversal: ");
        for (Vertex v : vertices()) {
            if (getLabel(v).equals("UNEXPLORED")) {
                dfs(this, v);
            }
        }
        System.out.println();
    }

    // Time Complexity: O(deg(v)) per recursive call - Explores only the incident edges of the current vertex.
    private void dfs(AdjacencyGraphList G, Vertex v) {
        setLabel(v, "VISITED");
        System.out.print(v.getElement() + " ");
        
        for (Edge e : G.outgoingEdge(v)) {
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
    // 2. BREADTH FIRST SEARCH (BFS)
    // ==========================================
    // Time Complexity: O(V + E) - Enqueues reachable vertices once and explores their incident edges exactly once.
    public boolean bfs(Vertex v1, Vertex v2) {
        for (Vertex u : vertices()) setLabel(u, "UNEXPLORED");
        
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
            
            for (Edge e : outgoingEdge(v)) { 
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
    // 3. DIJKSTRA'S SHORTEST PATH
    // ==========================================
    // Time Complexity: O((V + E) log V) - Priority Queue operations take logarithmic time per vertex extraction and edge relaxation.
    public void dijkstra(Vertex v1, Vertex v2) {
        HashMap<Vertex, Integer> cost = new HashMap<>();
        HashMap<Vertex, Vertex> previous = new HashMap<>();
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        PriorityQueue<PQNode> pqueue = new PriorityQueue<>();

        for (Vertex v : vertices()) {
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

            for (Edge e : outgoingEdge(v)) { 
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
    // 4. PRIM-JARNIK'S MINIMUM SPANNING TREE
    // ==========================================
    // Time Complexity: O((V + E) log V) - Extracts vertices from priority queue and updates their keys in logarithmic time.
    public ArrayList<Edge> primJarnikMST(Vertex s) {
        PriorityQueue<PQNode> Q = new PriorityQueue<>(); 
        HashMap<Vertex, Integer> distance = new HashMap<>(); 
        HashMap<Vertex, Edge> parent = new HashMap<>();      
        HashMap<Vertex, Boolean> inCloud = new HashMap<>();  
        
        for (Vertex v : vertices()) {
            if (v.compareTo(s) == 0) { 
                distance.put(v, 0);    
            } else { 
                distance.put(v, Integer.MAX_VALUE); 
            }
            parent.put(v, null); 
            inCloud.put(v, false);
            Q.add(new PQNode(v, distance.get(v)));
        }

        ArrayList<Edge> mstEdges = new ArrayList<>();
        int totalMSTWeight = 0;

        while (!Q.isEmpty()) {
            PQNode minNode = Q.poll(); 
            Vertex u = minNode.vertex; 

            if (inCloud.get(u)) continue;
            inCloud.put(u, true); 
            
            if (parent.get(u) != null) {
                mstEdges.add(parent.get(u));
                totalMSTWeight += parent.get(u).getWeight();
            }

            for (Edge e : u.getInEdge()) {
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

public class GraphAdjacencyListComplete {
    public static void main(String[] args) {
        AdjacencyGraphList airtelNetwork = new AdjacencyGraphList();

        // --- 1. INSERTION OF NODES (16 Major Telecom Hubs) ---
        airtelNetwork.insertVertex("Delhi");      // 0
        airtelNetwork.insertVertex("Mumbai");     // 1
        airtelNetwork.insertVertex("Chennai");    // 2
        airtelNetwork.insertVertex("Kolkata");    // 3
        airtelNetwork.insertVertex("Bangalore");  // 4
        airtelNetwork.insertVertex("Hyderabad");  // 5
        airtelNetwork.insertVertex("Pune");       // 6
        airtelNetwork.insertVertex("Ahmedabad");  // 7
        airtelNetwork.insertVertex("Jaipur");     // 8
        airtelNetwork.insertVertex("Lucknow");    // 9
        airtelNetwork.insertVertex("Bhopal");     // 10
        airtelNetwork.insertVertex("Nagpur");     // 11
        airtelNetwork.insertVertex("Patna");      // 12
        airtelNetwork.insertVertex("Bhubaneswar");// 13
        airtelNetwork.insertVertex("Guwahati");   // 14
        airtelNetwork.insertVertex("Kochi");      // 15

        ArrayList<Vertex> hubs = airtelNetwork.vertices();
        Vertex Delhi = hubs.get(0), Mumbai = hubs.get(1), Chennai = hubs.get(2), Kolkata = hubs.get(3);
        Vertex Bangalore = hubs.get(4), Hyderabad = hubs.get(5), Pune = hubs.get(6), Ahmedabad = hubs.get(7);
        Vertex Jaipur = hubs.get(8), Lucknow = hubs.get(9), Bhopal = hubs.get(10), Nagpur = hubs.get(11);
        Vertex Patna = hubs.get(12), Bhubaneswar = hubs.get(13), Guwahati = hubs.get(14), Kochi = hubs.get(15);

        // --- 2. INSERTION OF EDGES (Fiber links with Latency in ms as weights) ---
        airtelNetwork.insertEdge(Delhi, Jaipur, "DL-JP", 10);
        airtelNetwork.insertEdge(Delhi, Lucknow, "DL-LKO", 15);
        airtelNetwork.insertEdge(Delhi, Bhopal, "DL-BPL", 20);
        airtelNetwork.insertEdge(Jaipur, Ahmedabad, "JP-ADI", 18);
        airtelNetwork.insertEdge(Ahmedabad, Mumbai, "ADI-BOM", 12);
        airtelNetwork.insertEdge(Mumbai, Pune, "BOM-PNQ", 5);
        airtelNetwork.insertEdge(Pune, Bangalore, "PNQ-BLR", 25);
        airtelNetwork.insertEdge(Bangalore, Chennai, "BLR-MAA", 10);
        airtelNetwork.insertEdge(Bangalore, Kochi, "BLR-COK", 14);
        airtelNetwork.insertEdge(Chennai, Hyderabad, "MAA-HYD", 15);
        airtelNetwork.insertEdge(Hyderabad, Nagpur, "HYD-NAG", 12);
        airtelNetwork.insertEdge(Nagpur, Bhopal, "NAG-BPL", 10);
        airtelNetwork.insertEdge(Nagpur, Bhubaneswar, "NAG-BBI", 22);
        airtelNetwork.insertEdge(Bhubaneswar, Kolkata, "BBI-CCU", 14);
        airtelNetwork.insertEdge(Kolkata, Patna, "CCU-PAT", 16);
        airtelNetwork.insertEdge(Patna, Lucknow, "PAT-LKO", 12);
        airtelNetwork.insertEdge(Kolkata, Guwahati, "CCU-GAU", 30);
        airtelNetwork.insertEdge(Mumbai, Nagpur, "BOM-NAG", 24);
        airtelNetwork.insertEdge(Chennai, Kochi, "MAA-COK", 20);

        System.out.println("--- AIRTEL FIBER NETWORK DEMONSTRATION ---");
        System.out.println("Hubs (Vertices): " + airtelNetwork.numVertices());
        System.out.println("Fiber Links (Edges): " + airtelNetwork.numEdges());
        System.out.println("----------------------------------------------\n");

        // --- 3. TRAVERSALS ---
        System.out.println("-> Executing DFS Traversal (Network Mapping)");
        airtelNetwork.performDFS();
        System.out.println();

        System.out.println("-> Finding Shortest Route by Hops (Delhi to Kochi)");
        airtelNetwork.bfs(Delhi, Kochi);
        System.out.println();

        // --- 4. SHORTEST PATH (Dijkstra) ---
        System.out.println("-> Calculating Lowest Latency Route (Dijkstra)");
        airtelNetwork.dijkstra(Delhi, Kochi);
        System.out.println();

        // --- 5. MINIMUM SPANNING TREE ---
        System.out.println("-> Calculating Minimum Spanning Tree (Prim-Jarnik)");
        ArrayList<Edge> mst = airtelNetwork.primJarnikMST(Delhi);
        System.out.print("Backbone Links (MST Edges): ");
        for (Edge e : mst) {
            System.out.print(e.getElement() + " ");
        }
        System.out.println("\n");

        // --- 6. DELETION DEMONSTRATION ---
        System.out.println("-> HUB FAILURE: Removing Jaipur Hub from the Network");
        airtelNetwork.removeVertex(Jaipur);
        System.out.println("Hubs (Vertices) Post-Deletion: " + airtelNetwork.numVertices());
        System.out.println("Fiber Links (Edges) Post-Deletion: " + airtelNetwork.numEdges());
        System.out.println();

        System.out.println("-> Recalculating Lowest Latency Route (Delhi to Ahmedabad) without Jaipur");
        airtelNetwork.dijkstra(Delhi, Ahmedabad);
        System.out.println();
    }
} */