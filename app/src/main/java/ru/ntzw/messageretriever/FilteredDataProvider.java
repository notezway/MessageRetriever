package ru.ntzw.messageretriever;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilteredDataProvider<T> implements DataProvider<T> {

    private final DataProvider<T> source;
    private List<Region> regions = new ArrayList<>();
    private int elementsRemoved;

    FilteredDataProvider(DataProvider<T> source) {
        this.source = source;
        regions.add(new Region(0, 0, Integer.MAX_VALUE));
    }

    @Override
    public T get(int index) {
        if (elementsRemoved == 0) {
            return source.get(index);
        }
        Region region = getRegionByLocalIndex(index);
        int sourceIndex = region.getRealByLocal(index);
        return source.get(sourceIndex);
    }

    @Override
    public int size() {
        return source.size() - elementsRemoved;
    }

    void removeIndex(int localIndex) {
        int position = getRegionPositionByLocalIndex(localIndex);
        Region region = regions.get(position);
        Region[] parts = region.split(localIndex);
        int sizeChange = insertRegionParts(position, parts[0], parts[1]);
        updateRegions(position, sizeChange);
        elementsRemoved++;
    }

    private int insertRegionParts(int position, Region left, Region right) {
        if(left != null) {
            regions.set(position, left);
            if(right != null) {
                regions.add(position + 1, right);
                return 1;
            } else {
                return 0;
            }
        } else if(right != null) {
            regions.set(position, right);
            return 0;
        } else {
            regions.remove(position);
            return -1;
        }
    }

    private void updateRegions(int position, int sizeChange) {
        for(int i = position + sizeChange + 1; i < regions.size(); i++) {
            Region region = regions.get(i);
            region.localStart--;
        }
    }

    private Region getRegionByLocalIndex(int localIndex) {
        int position = getRegionPositionByLocalIndex(localIndex);
        return regions.get(position);
    }

    private int getRegionPositionByLocalIndex(int localIndex) {
        int position = Collections.binarySearch(regions, localIndex);
        position = position > -1 ? position : -position - 2;
        return position;
    }

    private static class Region implements Comparable<Integer> {

        private int realStart;
        private int localStart;
        private final int length;

        private Region(int realStart, int localStart, int length) {
            this.realStart = realStart;
            this.localStart = localStart;
            this.length = length;
        }

        private int getRealByLocal(int localIndex) {
            int offset = localIndex - localStart;
            return realStart + offset;
        }

        private Region[] split(int localIndex) {
            int offset = localIndex - localStart;
            if (offset < 0 || offset >= length) {
                throw new IndexOutOfBoundsException();
            }
            Region left = null;
            Region right = null;
            if (offset > 0) {
                left = new Region(realStart, localStart, offset);
            }
            if (offset < length - 1) {
                right = new Region(realStart + offset + 1, localStart + offset, length - offset - 1);
            }
            return new Region[]{left, right};
        }

        @Override
        public int compareTo(@NonNull Integer index) {
            if (localStart < index) {
                return -1;
            }
            if (localStart > index) {
                return 1;
            }
            return 0;
        }
    }
}
