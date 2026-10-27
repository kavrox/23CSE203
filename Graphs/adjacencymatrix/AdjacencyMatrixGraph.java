package Graphs.adjacencymatrix;

import java.util.LinkedList;
import java.util.Queue;

// ===================== 3) ADJACENCY MATRIX =====================
// a[i][j] holds the edge between vertex i and vertex j, else null

class vertex
{
    private String element;
    private int position;          // row / column number in the matrix

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

    Edge(String e, vertex o, vertex d)
    {
        element = e;
        origin = o;
        dest = d;
    }

    // set and get methods
    public String getElement()      { return element; }
    public vertex getOrigin()       { return origin; }
    public vertex getDest()         { return dest; }
    public String toString()        { return element; }
}

public class AdjacencyMatrixGraph
{
    private vertex[] v;
    private Edge[][] a;
    private int vcount = 0;
    private int ecount = 0;

    AdjacencyMatrixGraph(int n)
    {
        v = new vertex[n];
        a = new Edge[n][n];
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

    // upper half of the matrix only, so each edge comes once
    public Edge[] edges()
    {
        Edge[] t = new Edge[ecount];
        int k = 0;
        for (int i = 0; i < vcount; i++)
        {
            for (int j = i; j < vcount; j++)
            {
                if (a[i][j] != null)
                    t[k++] = a[i][j];
            }
        }
        return t;
    }

    public Edge getEdge(vertex u, vertex v1)
    {
        return a[u.getPosition()][v1.getPosition()];
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

    // count the edges in v1's row
    public int outDegree(vertex v1)
    {
        int count = 0;
        int i = v1.getPosition();
        for (int j = 0; j < vcount; j++)
        {
            if (a[i][j] != null)
                count++;
        }
        return count;
    }

    // for undirected graph, indegree and outdegree are same
    public int inDegree(vertex v1)
    {
        return outDegree(v1);
    }

    public Edge[] outgoingEdges(vertex v1)
    {
        Edge[] t = new Edge[outDegree(v1)];
        int k = 0;
        int i = v1.getPosition();
        for (int j = 0; j < vcount; j++)
        {
            if (a[i][j] != null)
                t[k++] = a[i][j];
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
        if (getEdge(u, v1) != null)
            return null;
        Edge e1 = new Edge(x, u, v1);
        a[u.getPosition()][v1.getPosition()] = e1;
        a[v1.getPosition()][u.getPosition()] = e1;
        ecount++;
        return e1;
    }

    public void removeEdge(Edge e1)
    {
        int i = e1.getOrigin().getPosition();
        int j = e1.getDest().getPosition();
        a[i][j] = null;
        a[j][i] = null;
        ecount--;
    }

    // remove v1's edges, then move last vertex (row and column) into v1's place
    public void removeVertex(vertex v1)
    {
        Edge[] t = outgoingEdges(v1);
        for (int i = 0; i < t.length; i++)
            removeEdge(t[i]);

        int p = v1.getPosition();
        int last = vcount - 1;
        v[p] = v[last];
        v[p].setPosition(p);
        for (int j = 0; j <= last; j++)
            a[p][j] = a[last][j];
        for (int i = 0; i <= last; i++)
            a[i][p] = a[i][last];
        for (int k = 0; k <= last; k++)
        {
            a[last][k] = null;
            a[k][last] = null;
        }
        v[last] = null;
        vcount--;
    }

    // ==========================================
    // Breadth First Search (BFS) Added Here
    // ==========================================
    public void bfs(vertex start)
    {
        if (start == null) return;

        boolean[] visited = new boolean[vcount];
        Queue<vertex> queue = new LinkedList<>();

        visited[start.getPosition()] = true;
        queue.add(start);

        System.out.print("BFS starting from " + start.getElement() + ": ");
        while (!queue.isEmpty())
        {
            vertex curr = queue.poll();
            System.out.print(curr.getElement() + " ");

            int i = curr.getPosition();
            // Scan the entire row 'i' to find connected neighbors
            for (int j = 0; j < vcount; j++)
            {
                if (a[i][j] != null && !visited[j])
                {
                    visited[j] = true;
                    // v[j] perfectly maps to the vertex at column index j
                    queue.add(v[j]); 
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
        int i = curr.getPosition();
        visited[i] = true;
        System.out.print(curr.getElement() + " ");

        // Recursively visit all unvisited neighbors connected in row 'i'
        for (int j = 0; j < vcount; j++)
        {
            if (a[i][j] != null && !visited[j])
            {
                dfsHelper(v[j], visited);
            }
        }
    }

    public static void main(String[] args)
    {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(10);
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