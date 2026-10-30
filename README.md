# Data Structures and Algorithms

This folder is the consolidated Java collection. Package names match the topic folders. From this folder, compile all sources with:

```powershell
$javaFiles = Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName
javac -d out $javaFiles
```

## Data structures

- `Graphs/adjacencylist`, `Graphs/adjacencymatrix`, and `Graphs/edgelist`: one graph representation per storage model.
- `Graphs/mst`: disjoint-set union and cycle detection.
- `LinkedLists`: singly and doubly linked lists.
- `Stacks`: array-backed and linked-list-backed stacks.
- `Queues`: circular-array and linked-list-backed queues.
- `PriorityQueues`: binary min-heaps and priority queues backed by sorted/unsorted arrays, sorted/unsorted doubly linked lists, and unbalanced/balanced binary search trees. `locator` contains the locator-based sorted-array priority queue.
- `Trees`: general binary tree (including tree operations, metrics, and structural checks) and binary search tree.

## Algorithms

- `Sorting/SortingAlgorithms.java`: bubble sort, minimum-only selection sort, min/max selection sort, insertion sort, merge sort, and quicksort.

## ADT documentation

Each data structure's operation descriptions and per-method time/space analyses are stored in its own topic folder. Complexity tables cover constructors, ADT operations, and algorithm helpers; demonstration `main` methods are not ADT operations.

- [Linked lists](./LinkedLists/linked-lists.md)
- [Stacks](./Stacks/stacks.md)
- [Queues](./Queues/queues.md)
- [Priority queues and heaps](./PriorityQueues/priority-queues.md)
- [Binary trees](./Trees/binary-trees.md)
- [Binary search trees](./Trees/binary-search-trees.md)
- [Adjacency-list graphs](./Graphs/adjacencylist/adjacency-list-graphs.md)
- [Adjacency-matrix graphs](./Graphs/adjacencymatrix/adjacency-matrix-graphs.md)
- [Edge-list graphs](./Graphs/edgelist/edge-list-graphs.md)
- [Disjoint sets and cycle detection](./Graphs/mst/disjoint-sets.md)
- [Sorting algorithms](./Sorting/sorting-algorithms.md)

## Application

- `Applications/EMP.java`: the existing employee/service-management example.

The airport runway program remains in the sibling `Aircraft Runway Scheduling System` project and is not part of this Java collection. Duplicate graph and sorting examples, empty files, compiled artifacts, and text files that contained Java source were consolidated or removed.
