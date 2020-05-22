package ru.ntzw.messageretriever;

import java.util.List;

public class CachedPerElementBlockDataProvider<T> implements DataProvider<T> {

    private final DataBlockProvider<T> source;
    private List<T> cachedBlock;
    private int cachedBlockIndex = -1;
    private int cachedBlockOffset;
    private int cachedBlockLength;
    private int blocksCount;
    private int elementsCount;

    CachedPerElementBlockDataProvider(DataBlockProvider<T> source) {
        this.source = source;
    }

    @Override
    public int size() {
        return elementsCount;
    }

    @Override
    public T get(int elementIndex) {
        if (isElementCached(elementIndex)) {
            T element = getFromCache(elementIndex);
            if(isLastElementInCache(elementIndex)) {
                loadFromSource(elementIndex + 1);
            }
            return element;
        }
        if (loadFromSource(elementIndex)) {
            return getFromCache(elementIndex);
        }
        return null;
    }

    private boolean isElementCached(int elementIndex) {
        return elementIndex >= cachedBlockOffset && elementIndex < cachedBlockOffset + cachedBlockLength;
    }

    private boolean isLastElementInCache(int elementIndex) {
        return elementIndex == cachedBlockOffset + cachedBlockLength - 1;
    }

    private T getFromCache(int elementIndex) {
        return cachedBlock.get(elementIndex - cachedBlockOffset);
    }

    private int getBlockIndexToLoad(int elementIndex) {
        if(elementIndex < cachedBlockOffset) {
            return cachedBlockIndex - 1;
        } else if(elementIndex >= cachedBlockOffset + cachedBlockLength) {
            return cachedBlockIndex + 1;
        }
        throw new IllegalStateException(String.format("Element with index %d already cached", elementIndex));
    }

    private boolean loadFromSource(int elementIndex) {
        while (!isElementCached(elementIndex)) {
            int blockIndexToLoad = getBlockIndexToLoad(elementIndex);
            boolean loaded = loadBlockFromSource(blockIndexToLoad);
            if(!loaded) {
                return false;
            }
        }
        return true;
    }

    private boolean loadBlockFromSource(int blockIndex) {
        List<T> block = source.get(blockIndex);
        if(block == null) {
            return false;
        }
        if(blockIndex >= blocksCount) {
            blocksCount = blockIndex + 1;
            elementsCount += block.size();
        }
        if(blockIndex < cachedBlockIndex) {
            cachedBlockOffset -= block.size();
        } else if(blockIndex > cachedBlockIndex) {
            cachedBlockOffset += cachedBlockLength;
        }
        cachedBlockLength = block.size();
        cachedBlockIndex = blockIndex;
        cachedBlock = block;
        return true;
    }
}
