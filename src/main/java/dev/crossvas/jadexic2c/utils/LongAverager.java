package dev.crossvas.jadexic2c.utils;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongPriorityQueue;

public class LongAverager {
    LongPriorityQueue queue = new LongArrayFIFOQueue();
    long total;
    int limit;
    int index = 0;
    long cachedLong = 0L;
    double cachedDouble = 0.0;

    public LongAverager(int limit) {
        this.limit = limit;
    }

    public void clear() {
        this.queue.clear();
        this.total = 0L;
        this.cachedDouble = 0.0;
        this.cachedLong = 0L;
        this.index = 0;
    }

    public void extract(long[] array) {
        int i = 0;

        for(int m = this.queue.size(); i < m; ++i) {
            long key = this.queue.dequeueLong();
            this.queue.enqueue(key);
            if (i < array.length) {
                array[i] += key;
            }
        }

    }

    public void addEntry(long entry) {
        this.total += entry;
        this.queue.enqueue(entry);
        if (this.queue.size() > this.limit) {
            this.total -= this.queue.dequeueLong();
        }

        this.index = (this.index + 1) % (this.limit > 16 ? this.limit / 16 : this.limit);
        if (this.index == 0) {
            this.buildCache();
        }

    }

    public void buildCache() {
        if (this.total == 0L) {
            this.cachedDouble = 0.0;
            this.cachedLong = 0L;
        } else {
            this.cachedDouble = (double)this.total / (double)this.queue.size();
            this.cachedLong = this.total / (long)this.queue.size();
        }
    }

    public long getCachedAverage() {
        return this.cachedLong;
    }

    public double getCachedDoubleAverage() {
        return this.cachedDouble;
    }

    public long getAverage() {
        return this.total == 0L ? 0L : this.total / (long)this.queue.size();
    }

    public double getPreciseAverage() {
        return this.total == 0L ? 0.0 : (double)this.total / (double)this.queue.size();
    }
}
