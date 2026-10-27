package Graphs.adjacencylist;

import java.util.LinkedList;
import java.util.Queue;

// ===================== 2) ADJACENCY LIST =====================
// every vertex keeps its own list (array) of incident edges

class vertex
{
    private String element;
    private int position;
    private Edge[] adj;        // incident edges of this vertex
    private int deg = 0;       // number of edges in adj

    vertex(String e, int p, int maxEdges)
    {
        element = e;
        position = p;
        adj = new Edge[maxEdges];
    }

    // set and get methods
    public String getElement()      { return element; }
    public int getPosition()        { return position; }
    public void setPosition(int p)  { position = p; }
    public Edge[] getAdj()          { return adj; }
    public int getDeg()             { return deg; }
    public String toString()        { return element; }

    public void addEdge(Edge e1)
    {
        adj[deg++] = e1;
    }

    public void removeEdge(Edge e1)
    {
        for (int i = 0; i < deg; i++)
        {
            if (adj[i] == e1)
            {
                adj[i] = adj[deg - 1];
                adj[deg - 1] = null;
                deg--;
                return;
            }
        }
    }
}

class Edge
{
    private String element;
    private vertex origin;
    private vertex dest;
    private int position;

    Edge(String e, vertex o, vertex d, int p)
    {
        element = e;
        origin = o;
        dest = d;
        position = p;
    }

    // set and get methods
    public String getElement()      { return element; }
    public vertex getOrigin()       { return origin; }
    public vertex getDest()         { return dest; }
    public int getPosition()        { return position; }
    public void setPosition(int p)  { position = p; }
    public String toString()        { return element; }
}

public class AdjacencyListGraph
{
    private vertex[] v;
    private Edge[] e;
    private int vcount = 0;
    private int ecount = 0;

    AdjacencyListGraph(int n, int m)
    {
        v = new vertex[n];
        e = new Edge[m];
    }

    public int numVertices()
    {
        return vcount;
    }

    public int numEdges()
    {
        return ecount;
    }

    public vertex[] vertices()
    {
        vertex[] t = new vertex[vcount];
        for (int i = 0; i < vcount; i++)
            t[i] = v[i];
        return t;
    }

    public Edge[] edges()
    {
        Edge[] t = new Edge[ecount];
        for (int i = 0; i < ecount; i++)
            t[i] = e[i];
        return t;
    }

    // only searches u's own list
    public Edge getEdge(vertex u, vertex v1)
    {
        Edge[] list = u.getAdj();
        for (int i = 0; i < u.getDeg(); i++)
        {
            if (opposite(u, list[i]) == v1)
                return list[i];
        }
        return null;
    }

    public vertex[] endVertices(Edge e1)
    {
        vertex[] t = new vertex[2];
        t[0] = e1.getOrigin();
        t[1] = e1.getDest();
        return t;
    }

    public vertex opposite(vertex v1, Edge e1)
    {
        vertex[] t = new vertex[2];
        t[0] = e1.getOrigin();
        t[1] = e1.getDest();
        if (v1 == t[0])
            return t[1];
        else if (v1 == t[1])
            return t[0];
        return null;
    }

    // for undirected graph, indegree and outdegree are same
    public int outDegree(vertex v1)
    {
        return v1.getDeg();
    }

    public int inDegree(vertex v1)
    {
        return v1.getDeg();
    }

    public Edge[] outgoingEdges(vertex v1)
    {
        Edge[] t = new Edge[v1.getDeg()];
        for (int i = 0; i < v1.getDeg(); i++)
            t[i] = v1.getAdj()[i];
        return t;
    }

    public Edge[] incomingEdges(vertex v1)
    {
        return outgoingEdges(v1);
    }

    public vertex insertVertex(String x)
    {
        if (vcount == v.length)
            return null;
        vertex t = new vertex(x, vcount, e.length);
        v[vcount++] = t;
        return t;
    }

    public Edge insertEdge(vertex u, vertex v1, String x)
    {
        if (ecount == e.length || getEdge(u, v1) != null)
            return null;
        Edge e1 = new Edge(x, u, v1, ecount);
        e[ecount++] = e1;
        u.addEdge(e1);
        v1.addEdge(e1);
        return e1;
    }

    public void removeEdge(Edge e1)
    {
        e1.getOrigin().removeEdge(e1);
        e1.getDest().removeEdge(e1);
        int i = e1.getPosition();
        e[i] = e[ecount - 1];
        e[i].setPosition(i);
        e[ecount - 1] = null;
        ecount--;
    }

    public void removeVertex(vertex v1)
    {
        while (v1.getDeg() > 0)
            removeEdge(v1.getAdj()[0]);
        int i = v1.getPosition();
        v[i] = v[vcount - 1];
        v[i].setPosition(i);
        v[vcount - 1] = null;
        vcount--;
    }

    // ==========================================
    // Breadth First Search (BFS) Added Here
    // ==========================================
    public void bfs(vertex start)
    {
        if (start == null) return;
        
        // Track visited status using the dynamic vertex position
        boolean[] visited = new boolean[vcount];
        Queue<vertex> queue = new LinkedList<>();

        visited[start.getPosition()] = true;
        queue.add(start);

        System.out.print("BFS starting from " + start.getElement() + ": ");
        while (!queue.isEmpty())
        {
            vertex curr = queue.poll();
            System.out.print(curr.getElement() + " ");

            // Adjacency list optimization: we only iterate over connected edges directly
            Edge[] outEdges = outgoingEdges(curr);
            for (Edge edge : outEdges)
            {
                vertex neighbor = opposite(curr, edge);
                if (neighbor != null && !visited[neighbor.getPosition()])
                {
                    visited[neighbor.getPosition()] = true;
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }

    // ==========================================
    // Depth First Search (DFS) Added Here
    // ==========================================
    public void dfs(vertex start)
    {
        if (start == null) return;
        
        boolean[] visited = new boolean[vcount];
        System.out.print("DFS starting from " + start.getElement() + ": ");
        dfsHelper(start, visited);
        System.out.println();
    }

    private void dfsHelper(vertex curr, boolean[] visited)
    {
        visited[curr.getPosition()] = true;
        System.out.print(curr.getElement() + " ");

        Edge[] outEdges = outgoingEdges(curr);
        for (Edge edge : outEdges)
        {
            vertex neighbor = opposite(curr, edge);
            if (neighbor != null && !visited[neighbor.getPosition()])
            {
                dfsHelper(neighbor, visited);
            }
        }
    }

    public static void main(String[] args)
    {
        AdjacencyListGraph g = new AdjacencyListGraph(10, 10);
        vertex u = g.insertVertex("u");
        vertex v = g.insertVertex("v");
        vertex w = g.insertVertex("w");
        vertex z = g.insertVertex("z");
        
        Edge a = g.insertEdge(u, v, "a");
        Edge b = g.insertEdge(v, w, "b");
        Edge c = g.insertEdge(u, w, "c");
        Edge d = g.insertEdge(w, z, "d");

        System.out.println("numVertices = " + g.numVertices());
        System.out.println("numEdges = " + g.numEdges());
        System.out.println("getEdge(w,u) = " + g.getEdge(w, u));
        System.out.println("opposite(w,b) = " + g.opposite(w, b));
        System.out.println("outDegree(w) = " + g.outDegree(w));
        System.out.println("outgoingEdges(w) = " + java.util.Arrays.toString(g.outgoingEdges(w)));

        // Calling BFS and DFS on the graph before vertex removal
        System.out.println("\n--- Graph Traversals ---");
        g.bfs(u);
        g.dfs(u);
        System.out.println("------------------------\n");

        g.removeVertex(w);
        System.out.println("after removeVertex(w): " + java.util.Arrays.toString(g.vertices())
                           + " edges " + java.util.Arrays.toString(g.edges()));
    }
}