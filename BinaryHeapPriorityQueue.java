package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {

    private int currentSize, maxSize;
    private Wrapper<E>[] storage;
    long sequenceNumber, modCounter;

    public BinaryHeapPriorityQueue(int max) {

        maxSize = max;
        currentSize = 0;
        storage = new Wrapper[max];
        sequenceNumber = 0;

    }

    public BinaryHeapPriorityQueue() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public boolean insert(E object) {

        if (isFull())
            return false;
        Wrapper<E> obj = new Wrapper<E>(object);
        storage[currentSize++] = obj;
        trickleUp();
        return true;
    }

    public E remove() {
        if (isEmpty())
            return null;
        E tmp = storage[0].data;
        //storage[0] = storage[currentSize-1];
        trickleDown();
        currentSize--;
        return tmp;
    }

    public boolean delete(E obj) {

        Wrapper<E>[] temp = storage;
        int tempCS = currentSize;
        clear();
        for (int k = 0; k < tempCS;k++) {

            if(temp[k].data.compareTo(obj) != 0) {

                insert(temp[k].data);

            }
        }
        return true;

    }

    public E peek() {
        return (E) storage[0].data;
    }

    public boolean contains(E obj) {
        return true;
    }

    public int size() {
        return currentSize;
    }

    public void clear() {
        currentSize = 0;
        sequenceNumber = 0;
        modCounter = 0;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public boolean isFull() {
        return currentSize == maxSize;
    }

    public Iterator<E> iterator() {
        return new IteratorHelper();
    }

    public class IteratorHelper implements Iterator<E> {
        int enumIndex;
        long modCheck;
        public IteratorHelper() {
            enumIndex = 0;
            modCheck = modCounter;
        }

        public boolean hasNext() {
            return enumIndex < currentSize;
        }

        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (E) storage[enumIndex++].data;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("hiding")
    protected class Wrapper<E> implements Comparable<Wrapper<E>> {

        long insertNumber;
        E data;

        public Wrapper(E obj) {
            insertNumber = sequenceNumber++;
            data = obj;
        }

        public int compareTo(Wrapper<E> obj) {
            @SuppressWarnings("unchecked")

            int tmp = ((Comparable<E>) data).compareTo(obj.data);
            if (tmp == 0)
                return (int) (insertNumber - obj.insertNumber);
            return tmp;
        }
    }

    private void trickleUp() {
        int newIndex = currentSize - 1;
        int parentIndex = (newIndex - 1) >> 1;
        Wrapper<E> newValue = storage[newIndex];
        while (parentIndex >= 0 && newValue.compareTo(storage[parentIndex]) < 0) {
            storage[newIndex] = storage[parentIndex];
            newIndex = parentIndex;
            parentIndex = (parentIndex - 1) >> 1;// shift right is equal to divide by 2
        } // end while
        storage[newIndex] = newValue;
    }

    public void trickleDown() {
        int current = 0;
        int child = getNextChild(current);
        while (child != -1 && storage[current].compareTo(storage[child]) < 0 && storage[child].compareTo(storage[currentSize-1]) < 0) {
            storage[current] = storage[child];
            current = child;
            child = getNextChild(current);// gets either right or left child
        }
        storage[current] = storage[currentSize-1];
    }

    private int getNextChild(int current) {
        int left = (current << 1) + 1;
        int right = left + 1;
        if (right < currentSize) { // there are two children
            if ((storage[left].compareTo(storage[right]) < 0))
                return left; // the left child is smaller
            return right; // the right chld is smaller
        }
        if (left < currentSize) // there is only one child
            return left;
        return -1; // no children
    }

}

