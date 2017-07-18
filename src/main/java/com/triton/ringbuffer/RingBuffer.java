package com.triton.ringbuffer;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Hadi on 5/23/2017.
 */
public final class RingBuffer<T>{

    private final Object[] store;
    private final BatchMode batchMode;
    private final int partition;
    private int readPos;
    private int writePos;
    private final boolean rejectOnFull;
    private final int defaultPartitionSize = 2;
    private final int defaultCapacitySize = 2;


    /**
     *
     */
    public RingBuffer(){
        this(1024);
    }


    /**
     *
     * @param capacity
     */
    public RingBuffer(int capacity){
        this(capacity,false);
    }





    /**
     *
     * @param capacity
     * @param rejectOnFull
     */
    public RingBuffer(int capacity, boolean rejectOnFull){
        this(capacity,rejectOnFull,BatchMode.FULL);
    }


    /**
     *
     * @param capacity
     * @param batchMode
     */
    public RingBuffer(int capacity, BatchMode batchMode){
        this(capacity,false,batchMode);
    }


    /**
     *
     * @param capacity
     * @param rejectOnFull
     * @param batchMode
     */
    public RingBuffer(int capacity, boolean rejectOnFull, BatchMode batchMode){
       this(capacity,rejectOnFull,batchMode,-1);
    }


    /**
     *
     * @param capacity
     * @param rejectOnFull
     * @param batchMode
     * @param partition
     */
    public RingBuffer(int capacity, boolean rejectOnFull, BatchMode batchMode, int partition){
        store = new Object[capacity <= 0 ?  defaultCapacitySize : capacity];
        this.rejectOnFull = rejectOnFull;
        this.batchMode = batchMode;
        this.partition = partition <= 0 ?  defaultPartitionSize : partition;
    }


    /**
     *
     * @param entry
     * @return
     */
    public boolean add(T entry) {
        if(availableCapacity() == 0 && rejectOnFull){
            return false;
        }else{
            if(writePos == store.length){
                writePos = 0;
            }
            store[writePos++] = entry;
            return true;
        }
    }


    /**
     *
     * @param entries
     * trying to insert the tries into free slots with the specified order as of their order in @param entries
     * if their is not available capacity for all items, the operation will be suspended if the waitForFreeSpace is enabled
     * else if the batch mode is configured to be partial batch insert, then the items equal to the free space will be inserted
     * else if the batch mode is configured to be full batch insert and no free capacity for all items the whole batch will be ignored
     * and the return value is equals to the batch size
     * @return the number of inserted items in the buffer
     */
    public int add(T[] entries){
        int count = 0;
        int capacity = availableCapacity();
        T[] temp;
        if(batchMode == BatchMode.PARTIAL){
            temp = (T[]) new Object[capacity];
            System.arraycopy(entries,0,temp,0,capacity);
        }else{
            temp = entries;
        }
        if(temp.length <= capacity){
            for(T t : temp){
                if(t != null){
                    add(t);
                    count++;
                }
            }
        }
        return count;
    }


    /**
     *
     * @return
     */
    public Optional<T> get() {
            if(readPos == store.length){
                readPos = 0;
            }
            T obj = (T)store[readPos];
            store[readPos] = null;
            readPos++;
            if(Objects.isNull(obj))
                return Optional.empty();
            return Optional.of(obj);
    }


    /**
     *
     * @param batchSize
     * @return
     */
    public Optional<T[]> get(int batchSize){
        return Optional.empty();
    }


    /**
     * @return the free slots for insertion
     */
    public int availableCapacity() {
        return store.length - Math.abs(writePos - readPos);
    }


    /**
     * Resets the read and write pointers
     */
    public void reset() {
        readPos = 0;
        writePos = 0;
    }

}
