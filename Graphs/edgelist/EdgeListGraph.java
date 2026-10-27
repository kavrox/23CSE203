package Graphs.edgelist;

import java.util.LinkedList;
import java.util.Queue;

class vertex
{
    private String element;
    private int position;

    vertex(String e, int p)
    {
        element = e;
        position = p;
    }

    // set and get methods
    public String getElement()      { return element; }
    public int getPosition()        { return position; }
    public void setPosition(int p)  { position = p; }
    public String toString()        { return element; }
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

public class EdgeListGraph
{
    private vertex[] v;
    private Edge[] e;
    private int vcount = 0;
    private int ecount = 0;

    EdgeListGraph(int n, int m)
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

    public Edge getEdge(vertex u, vertex v1)
    {
        for (int i = 0; i < ecount; i++)
        {
            if ((e[i].getOrigin() == u && e[i].getDest() == v1) ||
                (e[i].getOrigin() == v1 && e[i].getDest() == u))
                return e[i];
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
        int count = 0;
        for (int i = 0; i < ecount; i++)
        {
            if (e[i].getOrigin() == v1 || e[i].getDest() == v1)
                count++;
        }
        return count;
    }

    public int inDegree(vertex v1)
    {
        return outDegree(v1);
    }

    public Edge[] outgoingEdges(vertex v1)
    {
        Edge[] t = new Edge[outDegree(v1)];
        int k = 0;
        for (int i = 0; i < ecount; i++)
        {
            if (e[i].getOrigin() == v1 || e[i].getDest() == v1)
                t[k++] = e[i];
        }
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
        vertex t = new vertex(x, vcount);
        v[vcount++] = t;
        return t;
    }

    public Edge insertEdge(vertex u, vertex v1, String x)
    {
        if (ecount == e.length || getEdge(u, v1) != null)
            return null;
        Edge e1 = new Edge(x, u, v1, ecount);
        e[ecount++] = e1;
        return e1;
    }

    // last edge is moved into the removed edge's place
    public void removeEdge(Edge e1)
    {
        int i = e1.getPosition();
        e[i] = e[ecount - 1];
        e[i].setPosition(i);
        e[ecount - 1] = null;
        ecount--;
    }

    // remove all edges of v1, then move last vertex into v1's place
    public void removeVertex(vertex v1)
    {
        for (int i = ecount - 1; i >= 0; i--)
        {
            if (e[i].getOrigin() == v1 || e[i].getDest() == v1)
                removeEdge(e[i]);
        }
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
        
        // We track visited vertices using their dynamic positions in the array
        boolean[] visited = new boolean[vcount];
        Queue<vertex> queue = new LinkedList<>();

        visited[start.getPosition()] = true;
        queue.add(start);

        System.out.print("BFS starting from " + start.getElement() + ": ");
        while (!queue.isEmpty())
        {
            vertex curr = queue.poll();
            System.out.print(curr.getElement() + " ");

            // Get all edges connected to the current vertex
            Edge[] outEdges = outgoingEdges(curr);
            for (Edge edge : outEdges)
            {
                // Find the adjacent vertex on the other side of the edge
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
        EdgeListGraph g = new EdgeListGraph(10, 10);
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