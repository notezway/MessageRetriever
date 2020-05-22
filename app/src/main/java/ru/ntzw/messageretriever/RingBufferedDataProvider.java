package ru.ntzw.messageretriever;

public class RingBufferedDataProvider<T> implements DataProvider<T> {

    private final DataProvider<T> source;
    private final RingBuffer<T> buffer;
    private int offset;

    RingBufferedDataProvider(DataProvider<T> source, int bufferSize) {
        this.source = source;
        this.buffer = new RingBuffer<>(bufferSize);
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public T get(int index) {
        if (index < 0) throw new IndexOutOfBoundsException();
        if (index < offset || index >= offset + buffer.getSize()) {
            readFromSource(index);
        }
        if (index >= offset + buffer.getSize()) {
            return null;
        }
        return buffer.get(index - offset);
    }

    private void readFromSource(int index) {
        if(index < offset) {
            for (int i = offset; i >= index; i--) {
                buffer.addFirst(source.get(i));
                offset--;
            }
        } else if(index >= offset + buffer.getSize()) {
            for (int i = offset; i <= index; i++) {
                if(buffer.addLast(source.get(i))) {
                    offset++;
                }
            }
        }
    }
}
