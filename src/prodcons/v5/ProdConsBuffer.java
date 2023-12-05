package prodcons.v5;


import java.util.concurrent.locks.*;

import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    Message msgBuffer[];
    int bufferSize;
    int writeIndex = 0;
    int readIndex = 0;
    int totmsg = 0;
    int producerCount;
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
        producerCount = size;
    }

    @Override
    public void produce(Message m) throws InterruptedException {

        lock.lock();
        try {
            while (isFull()) {
                notFull.await();
            }
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void produce(Message m, long authorIdForFeedBack) throws InterruptedException {

        lock.lock();
        try {
            while (isFull()) {
                notFull.await();
            }
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                    + " a produit " + m);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message consume() throws InterruptedException {

        lock.lock();
        try {
            while (isEmpty()) {
                notEmpty.await();
            }
            Message m = msgBuffer[readIndex % bufferSize];
            readIndex++;
            notFull.signal();
            return m;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public Message consume(long consumerIdForFeedBack) throws InterruptedException {

        lock.lock();
        try {
            while (isEmpty()) {
                notEmpty.await();
            }
            Message m = msgBuffer[readIndex % bufferSize];
            readIndex++;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                    + " a consumé " + m);
            notFull.signal();
            return m;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public synchronized int nmsg() {
        return writeIndex - readIndex;
    }

    @Override
    public synchronized int totmsg() {
        return totmsg;
    }

    public synchronized boolean isFull() {
        return nmsg() >= bufferSize;
    }

    public synchronized boolean isEmpty() {
        return writeIndex == readIndex;
    }

    @Override
    public void put(Message m, int n) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public Message[] consume(int k) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consume'");
    }

}
