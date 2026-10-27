package Graphs.mst;

import java.util.ArrayList;
import java.util.List;

// Union-Find (Disjoint Set) Data Structure for Cycle Detection
class DisjointSet {
    private int[] parent;
    private int[] rank;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        // Initially, every vertex is its own parent (its own separate component)
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    // Find the root of the set in which element 'i' belongs.
    // Uses Path Compression to flatten the tree for optimal O(1) amortized time.
    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]); 
        }
        return parent[i];
    }

    // Unites the sets that include i and j.
    // Uses Union by Rank to attach the smaller tree under the larger tree.
    public void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            if (rank[rootI] < rank[rootJ]) {
                parent[rootI] = rootJ;
            } else if (rank[rootI] > rank[rootJ]) {
                parent[rootJ] = rootI;
            } else {
                parent[rootJ] = rootI;
                rank[rootI]++;
            }
        }
    }
}

class Edge {
    int u, v, weight;

    Edge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }
}

public class MSTCycleDetection {
    private int V;
    private List<Edge> edges;

    public MSTCycleDetection(int vertices) {
        this.V = vertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight) {
        edges.add(new Edge(u, v, weight));
    }

    // Demonstrates cycle detection on the current list of edges
    public boolean containsCycle() {
        DisjointSet dsu = new DisjointSet(V);

        for (Edge edge : edges) {
            int rootU = dsu.find(edge.u);
            int rootV = dsu.find(edge.v);

            // If both vertices share the same root, they are in the same component.
            // Adding this edge completes a cycle.
            if (rootU == rootV) {
                System.out.println("Cycle detected with edge: " + edge.u + " - " + edge.v);
                return true; 
            }

            // Otherwise, combine them into the same component
            dsu.union(rootU, rootV);
        }
        
        System.out.println("No cycle detected in the graph.");
        return false;
    }

    // Example of how this is applied in an MST context (like Kruskal's)
    public void simulateMSTEdgeFiltering() {
        DisjointSet dsu = new DisjointSet(V);
        List<Edge> mstEdges = new ArrayList<>();

        System.out.println("\nBuilding MST...");
        // In actual Kruskal's, edges would be sorted by weight here first.
        
        for (Edge edge : edges) {
            int rootU = dsu.find(edge.u);
            int rootV = dsu.find(edge.v);

            if (rootU != rootV) {
                // No cycle formed, safe to add to MST
                mstEdges.add(edge);
                dsu.union(rootU, rootV);
                System.out.println("Added edge to MST: " + edge.u + " - " + edge.v);
            } else {
                // Cycle detected, discard edge (Applying the Cycle Property)
                System.out.println("Discarded edge (creates cycle): " + edge.u + " - " + edge.v);
            }
        }
    }

    public static void main(String[] args) {
        int V = 4;
        MSTCycleDetection graph = new MSTCycleDetection(V);

        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 15);
        graph.addEdge(2, 3, 4);
        
        // This edge forms a cycle (0-1-2-0)
        graph.addEdge(2, 0, 8); 

        graph.containsCycle();
        graph.simulateMSTEdgeFiltering();
    }
}