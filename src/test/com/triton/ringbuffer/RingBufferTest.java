package com.triton.ringbuffer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Hadi on 5/23/2017.
 */
public class RingBufferTest {


    @Test
    public void testAdd(){
        String[] adds = new String[]{"test1","test2","test3"};
        String[] gets = new String[]{"test1","test2"};


        RingBuffer<String> ringBuffer = new RingBuffer<>(10);
        Arrays.stream(adds).forEach(s -> ringBuffer.add(s));
        assertEquals(7,ringBuffer.availableCapacity());

        for(String s : gets)
            assertEquals(s,ringBuffer.get().get());


        assertEquals(9,ringBuffer.availableCapacity());

    }


    @Test
    public void addBatchWithFull(){
        RingBuffer buffer = new RingBuffer(2,BatchMode.FULL);
        Object[] store = new Object[5];
        for(int i = 0; i<store.length;i++){
            store[i] = new Object();
        }
        int inserted = buffer.add(store);
        assertEquals(0,inserted);
        assertEquals(2,buffer.availableCapacity());

        buffer = new RingBuffer(5,BatchMode.FULL);
        store = new Object[5];
        for(int i = 0; i<store.length;i++){
            store[i] = new Object();
        }
        inserted = buffer.add(store);
        assertEquals(5,inserted);



        buffer = new RingBuffer(6,BatchMode.FULL);
        store = new Object[5];
        for(int i = 0; i<store.length;i++){
            store[i] = new Object();
        }
        inserted = buffer.add(store);
        assertEquals(5,inserted);

        assertEquals(1,buffer.availableCapacity());
    }





    @Test
    public void addBatchInPartialMode(){
        RingBuffer buffer = new RingBuffer(2,BatchMode.PARTIAL);
        Object[] store = new Object[5];
        for(int i = 0; i < store.length; i++){
            store[i] = new Object();
        }
        int inserted = buffer.add(store);
        assertEquals(2,inserted);
        assertEquals(0,buffer.availableCapacity());
    }

    @Test
    public void testAddReject0(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(1,true);
        assertEquals(true,ringBuffer.add(1));
        assertEquals(false,ringBuffer.add(1));
        ringBuffer.reset();
    }


    @Test
    public void testAddReject1(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(1);
        assertEquals(true,ringBuffer.add(1));
        ringBuffer.get();
        assertEquals(true,ringBuffer.add(1));
    }



    @Test
    public void testAddReject2(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(1);
        assertEquals(true,ringBuffer.add(1));
        assertEquals(true,ringBuffer.add(1));
    }


    @Test
    public void testGetCircular() {
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(1);
        assertEquals(true,ringBuffer.add(1));
        assertEquals(new Integer(1),ringBuffer.get().get());
        assertEquals(empty(),ringBuffer.get());
        assertEquals(empty(),ringBuffer.get());
        assertEquals(empty(),ringBuffer.get());
        assertEquals(true,ringBuffer.add(2));
        assertEquals(new Integer(2),ringBuffer.get().get());
    }


    @Test
    public void testReset(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(2);
        assertEquals(true,ringBuffer.add(1));
        assertEquals(true,ringBuffer.add(2));
        ringBuffer.reset();
        assertEquals(new Integer(1),ringBuffer.get().get());

    }
}
