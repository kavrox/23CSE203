import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class Vertex implements Comparable<Vertex>{
    String element;
    int pos;
    
    Vertex(String element , int pos ){
        this.element = element;
        this.pos = pos;
    }
    public void setElement(String element) {this.element = element;}
    public String getElement() {return element;}
    public void setPos(int pos) { this.pos = pos;}
    public int getPos() { return pos;}
    @Override
    public int compareTo(Vertex o) {
        return this.element.compareTo(o.element);
    }
}

class Edge {
    String element;
    Vertex origin,dest;
    int pos , weight;
    
    Edge(String element , Vertex origin , Vertex dest , int pos , int weight){
        this.element = element;
        this.origin = origin;
        this.dest = dest;
        this.pos = pos;
        this.weight = weight;
    }
    public void setElement(String element) {this.element = element;}
    public String getElement() { return element; }
    public void setOrigin(Vertex origin) {this.origin = origin;}
    public Vertex getOrigin() { return origin; }
    public void setDest(Vertex dest) {this.dest = dest;}
    public Vertex getDest() { return dest; }
    public int getPosition() { return pos; }
    public void setPosition(int pos) { this.pos = pos; }
    public void setWeight(int weight) {this.weight = weight;} // Fixed to int weight
    public int getWeight() { return weight; }
}

class DSU {
    private Vertex[] parent;
    private int[] rank;
    
    DSU (int maxCapacity){
        parent = new Vertex[maxCapacity];
        rank = new int[maxCapacity];
    }
    
    public void makeSet(Vertex v) {
        int pos = v.getPos();
        parent[pos] = v;
        rank[pos] = 0;
    }
    
    public Vertex find(Vertex v) {
        int pos = v.getPos();
        if (parent[pos] != v) {
            parent[pos] = find(parent[pos]);
        }
        return parent[pos];
    }
    
    public void union(Vertex u,Vertex v) {
        Vertex parentU = find(u);
        Vertex parentV = find(v);
        
        if (parentU != parentV) {
            int posU = parentU.getPos();
            int posV = parentV.getPos();
            
            if (rank[posU]>rank[posV]) parent[posV] = parentU;
            else if (rank[posV]>rank[posU]) parent[posU] = parentV;
            else {
                parent[posV] = parentU;
                rank[posU]++;
            }
        }
    }
}

class minHeap {
    private Edge[] heap;
    int size;
    int capacity;
    
    minHeap(int capacity){
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Edge[capacity];
    }
    
    public void insert(Edge e) {
        if (size == capacity) return;
        else {
            heap[size] = e;
            int curr = size;
            size++;
            
            while (curr>0) {
                int parent = (curr - 1) / 2;
                if (heap[curr].getWeight() < heap[parent].getWeight()) {
                    Edge temp = heap[curr];
                    heap[curr] = heap[parent];
                    heap[parent] = temp;
                    curr = parent;
                } else break;
            }
        }
    }
    
    public Edge removeMin() {
        if (size == 0) return null;
        else {
            Edge e = heap[0];
            heap[0] = heap[--size];
            
            int curr = 0;
            while (true) {
                int left = (2 * curr) + 1;
                int right = (2 * curr) + 2;
                int smallest = curr;
                
                if (left < size && heap[left].getWeight() < heap[smallest].getWeight()) smallest = left;
                if (right <size && heap[right].getWeight() < heap[smallest].getWeight()) smallest = right;
                
                if (smallest != curr) {
                    Edge temp = heap[curr];
                    heap[curr] = heap[smallest];
                    heap[smallest] = temp;
                    curr = smallest;
                }else break;
            }
            return e;
        }
    }
    
    public boolean isEmpty() { return size == 0; }
}

public class EdgeListGraph {
    private Vertex[] vertex;
    private Edge[] edge;
    private int vcount;
    private int ecount;
    private int maxVertices;
    private int maxEdges;
    
    private String[] vertexLabels;
    private String[] edgeLabels;

    public EdgeListGraph(int n, int m) { 
        this.maxVertices = n;
        this.maxEdges = m;
        vertex = new Vertex[n];
        edge = new Edge[m];
        vertexLabels = new String[n];
        edgeLabels = new String[m];
        vcount = ecount = 0;
    }

    public int numVertices() { return vcount; }
    public int numEdges() { return ecount; }
    public Vertex[] vertices() { return vertex; }
    public Edge[] edges() { return edge; }

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

    public Vertex[] endVertices(Edge e) { 
        Vertex v[] = new Vertex[2];
        v[0] = e.getOrigin();
        v[1] = e.getDest();
        return v;
    }

    public Vertex opposite(Vertex v, Edge e) { 
        Vertex[] t = new Vertex[2];
        t[0] = e.getOrigin();
        t[1] = e.getDest();
        if (v.compareTo(t[0]) == 0) return t[1];
        else if (v.compareTo(t[1]) == 0) return t[0];
        else return null;
    }

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

    public void insertVertex(String x) { 
        if (vcount >= maxVertices) return;
        Vertex v = new Vertex(x, vcount);
        vertex[vcount] = v;
        vertexLabels[vcount] = "UNEXPLORED";
        vcount++;
    }

    public void insertEdge(Vertex u, Vertex v, String x, int weight) { 
        if (ecount >= maxEdges) return;
        Edge e = new Edge(x, u, v, ecount, weight);
        edge[ecount] = e;
        edgeLabels[ecount] = "UNEXPLORED";
        ecount++;
    }

    public void setLabel(Vertex v, String label) { vertexLabels[v.getPos()] = label; }
    public String getLabel(Vertex v) { 
        String label = vertexLabels[v.getPos()];
        return label != null ? label : "UNEXPLORED";
    }
    
    public void setLabel(Edge e, String label) { edgeLabels[e.getPosition()] = label; }
    public String getLabel(Edge e) { 
        String label = edgeLabels[e.getPosition()];
        return label != null ? label : "UNEXPLORED";
    }

    public void removeEdge(Edge e) { 
        int pos = e.getPosition();
        if (pos < 0 || pos >= ecount) return;
        
        if (pos != ecount - 1) {          
            edge[pos] = edge[ecount - 1]; 
            edge[pos].setPosition(pos); 
            edgeLabels[pos] = edgeLabels[ecount - 1];
        }
        edge[ecount - 1] = null;
        edgeLabels[ecount - 1] = null;
        ecount--;
    }

    public void removeVertex(Vertex v) { 
        for (int i = ecount - 1; i >= 0; i--) { 
            if (edge[i].getOrigin().compareTo(v) == 0 || edge[i].getDest().compareTo(v) == 0) {
                removeEdge(edge[i]);
            }
        }
        
        int pos = v.getPos();
        if (pos < 0 || pos >= vcount) return;
        
        if (pos != vcount - 1) {              
            vertex[pos] = vertex[vcount - 1]; 
            vertex[pos].setPos(pos);     
            vertexLabels[pos] = vertexLabels[vcount - 1];
        }
        vertex[vcount - 1] = null;
        vertexLabels[vcount - 1] = null;
        vcount--;
    }

    // ==========================================
    // DFS (ITERATIVE - NO HELPER METHOD)
    // ==========================================

    public void performDFS(Vertex start) {

    for (int i = 0; i < vcount; i++)
        setLabel(vertex[i], "UNEXPLORED");

    for (int i = 0; i < ecount; i++)
        setLabel(edge[i], "UNEXPLORED");

    Stack<Vertex> stack = new Stack<>();

    stack.push(start);

    System.out.print("DFS Traversal: ");

    while (!stack.isEmpty()) {

        Vertex v = stack.pop();

        if (getLabel(v).equals("UNEXPLORED")) {

            setLabel(v, "VISITED");
            System.out.print(v.getElement() + " ");

            Edge[] outgoing = outgoingEdge(v);

            for (int i = outgoing.length - 1; i >= 0; i--) {

                Edge e = outgoing[i];
                Vertex w = opposite(v, e);

                if (getLabel(w).equals("UNEXPLORED")) {
                    stack.push(w);
                }
            }
        }
    }

    System.out.println();
}
    

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

    public Edge[] kruskalMST() {
        DSU dsu = new DSU(maxVertices);
        for (int i = 0; i < vcount; i++) {
            dsu.makeSet(vertex[i]); 
        }
        
        minHeap Q = new minHeap(ecount); 
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
            Edge[] cheapest = new Edge[maxVertices];

            for (int i = 0; i < ecount; i++) {
                Edge e = edge[i];
                Vertex u = e.getOrigin();
                Vertex v = e.getDest();
                
                Vertex rootU = dsu.find(u);
                Vertex rootV = dsu.find(v);
                
                if (rootU != rootV) {
                    int posU = rootU.getPos();
                    int posV = rootV.getPos();
                    
                    if (cheapest[posU] == null || e.getWeight() < cheapest[posU].getWeight()) {
                        cheapest[posU] = e;
                    }
                    if (cheapest[posV] == null || e.getWeight() < cheapest[posV].getWeight()) {
                        cheapest[posV] = e;
                    }
                }
            }

            for (int i = 0; i < vcount; i++) { 
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

    public static void main(String[] args) {
        // Main logic goes here
    }
}