package PriorityQueues.locator;

public class Locator<E> {
    public E element;
    public int key;
    public int index;

    public Locator(E element, int key, int index) {
        this.element = element;
        this.key = key;
        this.index = index;
    }
}
