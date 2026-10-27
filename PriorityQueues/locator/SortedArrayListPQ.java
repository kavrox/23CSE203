package PriorityQueues.locator;

import java.util.ArrayList;

// The Locator acts as a wrapper for the element.
// It stores the priority key and the back-reference index to the ArrayList.
public class SortedArrayListPQ<E> {
    
    // Using ArrayList<Locator> as specified in the reference notes: 
    // "Sorted Array PQ with ArrayList <Object> (or) ArrayList <locator>"[cite: 1]
    private ArrayList<Locator<E>> list;

    public SortedArrayListPQ() {
        list = new ArrayList<>();
    }

    // Method signature based on the image: "insert (element, int key)"[cite: 1]
    public Locator<E> insert(E element, int key) {
        // Create a new locator with an initially invalid index (-1)
        Locator<E> locator = new Locator<>(element, key, -1);
        
        // Call helper method as shown in the notes: "insertExisting();"[cite: 1]
        insertExisting(locator);
        
        // Return the locator so the user can use it later for O(1) searches
        return locator; 
    }

    // Helper method to insert a locator into its sorted position
    private void insertExisting(Locator<E> locator) {
        int insertPos = 0;
        
        // Find the correct insertion point to maintain ascending sorted order
        while (insertPos < list.size() && list.get(insertPos).key <= locator.key) {
            insertPos++;
        }

        // Insert into the ArrayList
        list.add(insertPos, locator);

        // CRITICAL STEP: Update the back-reference index for the inserted 
        // element and all elements that were shifted to the right.
        for (int i = insertPos; i < list.size(); i++) {
            list.get(i).index = i;
        }
    }

    // Method signature based on the image: "changePriority(....)"[cite: 1]
    public void changePriority(Locator<E> locator, int newKey) {
        // O(1) SEARCH: We don't need a loop! 
        // We use the locator's back-reference index to find it immediately.
        int pos = locator.index;

        // Safety check to ensure the locator is valid and belongs to this PQ
        if (pos < 0 || pos >= list.size() || list.get(pos) != locator) {
            throw new IllegalArgumentException("Invalid locator provided.");
        }

        // Remove the locator from its current position
        list.remove(pos);

        // Update back-reference indices for all elements shifted down by the removal
        for (int i = pos; i < list.size(); i++) {
            list.get(i).index = i;
        }

        // Update the key to the new priority and re-insert into the proper sorted position
        locator.key = newKey;
        insertExisting(locator);
    }

    // Standard PQ method: extract the element with the lowest key (highest priority)
    public E extractMin() {
        if (list.isEmpty()) {
            return null;
        }
        
        // Because the array is sorted, the minimum is always at index 0
        Locator<E> minLocator = list.remove(0);
        minLocator.index = -1; // Invalidate the index so it can't be reused

        // Update back-reference indices for all remaining elements
        for (int i = 0; i < list.size(); i++) {
            list.get(i).index = i;
        }

        return minLocator.element;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}