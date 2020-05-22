package ru.ntzw.messageretriever;

class RingBuffer<T> {

    private final T[] array;
    private int head = -1;
    private int tail;

    @SuppressWarnings("unchecked")
    RingBuffer(int length) {
        this.array = (T[]) new Object[length];
    }

    int getCapacity() {
        return array.length;
    }

    int getSize() {
        if(head == -1) return 0;
        return mod(tail - head, array.length) + 1;
    }

    T get(int index) {
        if(index < 0 || index >= getSize()) throw new IndexOutOfBoundsException();
        return array[mod(head + index, array.length)];
    }

    boolean addLast(T element) {
        if(head == -1) {
            array[++head] = element;
            return false;
        }
        tail = mod(tail + 1, array.length);
        array[tail] = element;
        if(head == tail) {
            head = mod(head + 1, array.length);
            return true;
        }
        return false;
    }

    boolean addFirst(T element) {
        if(head == -1) {
            array[++head] = element;
            return false;
        }
        head = mod(head - 1, array.length);
        array[head] = element;
        if(head == tail) {
            tail = mod(tail - 1, array.length);
            return true;
        }
        return false;
    }

    private static int mod(int a, int b) {
        return (a % b + b) % b;
    }
}
