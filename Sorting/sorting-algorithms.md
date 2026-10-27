# Sorting algorithms

`Sorting.SortingAlgorithms` sorts an integer array in ascending order in place. The methods are static, use no collection dependencies, and accept empty and single-element arrays.

Let `n` be the array length. Time is worst-case unless best/average cases are shown. Extra space excludes the input array.

| Method | Approach | Best time | Average time | Worst time | Extra space |
|---|---|---:|---:|---:|---:|
| `bubbleSort(values)` | Adjacent swaps; exits early if a pass makes no swap. | O(n) | O(n²) | O(n²) | O(1) |
| `selectionSortMin(values)` | Select the minimum remaining item for each position. | O(n²) | O(n²) | O(n²) | O(1) |
| `selectionSortMinMax(values)` | Select both extremes and place them at both ends per pass. | O(n²) | O(n²) | O(n²) | O(1) |
| `insertionSort(values)` | Insert each next item into the sorted prefix. | O(n) | O(n²) | O(n²) | O(1) |
| `mergeSort(values)` | Recursively sort halves and merge. | O(n log n) | O(n log n) | O(n log n) | O(n) |
| `quickSort(values)` | In-place partition around the middle-selected pivot, then recurse. | O(n log n) typical | O(n log n) | O(n²) | O(log n) typical; O(n) worst recursion |

## Helper methods

| Method | Purpose | Time | Extra space |
|---|---|---:|---:|
| private `mergeSort(values, left, right)` | Recursive range version of merge sort. | O(n log n) over the full sort | O(log n) recursion |
| private `merge(values, left, middle, right)` | Merge two sorted ranges into their original range. | O(k), where `k = right - left + 1` | O(k) |
| private `partition(values, low, high)` | Partition a range around its pivot. | O(k), where `k = high - low + 1` | O(1) |
| private `quickSort(values, low, high)` | Recursive range version of quicksort. | O(n log n) average; O(n²) worst | O(log n) average; O(n) worst recursion |
| private `swap(values, first, second)` | Exchange two array elements. | O(1) | O(1) |
