package ru.ntzw.messageretriever;

interface DataProvider<T> {

    int size();

    T get(int index);
}
