package ru.ntzw.messageretriever;

import java.util.List;

public interface DataBlockProvider<T> {

    List<T> get(int index);
}
