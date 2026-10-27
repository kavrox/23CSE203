package Sorting;

public class SortingAlgorithms {
    public static void bubbleSort(int[] values) {
        for (int end = values.length - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    swap(values, i, i + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                return;
            }
        }
    }

    public static void selectionSortMin(int[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < values.length; j++) {
                if (values[j] < values[min]) {
                    min = j;
                }
            }
            swap(values, i, min);
        }
    }

    public static void selectionSortMinMax(int[] values) {
        for (int i = 0; i < values.length / 2; i++) {
            int min = i;
            int max = i;
            for (int j = i; j < values.length - i; j++) {
                if (values[j] < values[min]) {
                    min = j;
                }
                if (values[j] > values[max]) {
                    max = j;
                }
            }
            swap(values, i, min);
            if (max == i) {
                max = min;
            }
            swap(values, values.length - 1 - i, max);
        }
    }

    public static void insertionSort(int[] values) {
        for (int i = 1; i < values.length; i++) {
            int key = values[i];
            int j = i;
            while (j > 0 && values[j - 1] > key) {
                values[j] = values[j - 1];
                j--;
            }
            values[j] = key;
        }
    }

    public static void mergeSort(int[] values) {
        mergeSort(values, 0, values.length - 1);
    }

    private static void mergeSort(int[] values, int left, int right) {
        if (left >= right) {
            return;
        }
        int middle = left + (right - left) / 2;
        mergeSort(values, left, middle);
        mergeSort(values, middle + 1, right);
        merge(values, left, middle, right);
    }

    private static void merge(int[] values, int left, int middle, int right) {
        int[] leftPart = new int[middle - left + 1];
        int[] rightPart = new int[right - middle];
        System.arraycopy(values, left, leftPart, 0, leftPart.length);
        System.arraycopy(values, middle + 1, rightPart, 0, rightPart.length);

        int i = 0;
        int j = 0;
        int destination = left;
        while (i < leftPart.length && j < rightPart.length) {
            if (leftPart[i] <= rightPart[j]) {
                values[destination++] = leftPart[i++];
            } else {
                values[destination++] = rightPart[j++];
            }
        }
        while (i < leftPart.length) {
            values[destination++] = leftPart[i++];
        }
        while (j < rightPart.length) {
            values[destination++] = rightPart[j++];
        }
    }

    public static void quickSort(int[] values) {
        quickSort(values, 0, values.length - 1);
    }

    private static void quickSort(int[] values, int low, int high) {
        if (low < high) {
            int pivot = partition(values, low, high);
            quickSort(values, low, pivot - 1);
            quickSort(values, pivot + 1, high);
        }
    }

    private static int partition(int[] values, int low, int high) {
        int middle = low + (high - low) / 2;
        swap(values, middle, high);
        int pivot = values[high];
        int boundary = low - 1;
        for (int i = low; i < high; i++) {
            if (values[i] < pivot) {
                swap(values, ++boundary, i);
            }
        }
        swap(values, boundary + 1, high);
        return boundary + 1;
    }

    private static void swap(int[] values, int first, int second) {
        int temp = values[first];
        values[first] = values[second];
        values[second] = temp;
    }
}
