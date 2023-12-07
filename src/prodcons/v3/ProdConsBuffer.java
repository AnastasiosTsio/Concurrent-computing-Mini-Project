package prodcons.v3;

import java.util.concurrent.Semaphore;

import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    Message msgBuffer[];
    int bufferSize;
    int writeIndex = 0;
    int readIndex = 0;
    int totmsg = 0;
    int producerCount;
    Semaphore emptyness;
    Semaphore fullness;
    Semaphore mutex;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
        producerCount = size;
        emptyness = new Semaphore(0, true);
        fullness = new Semaphore(size, true);
        mutex = new Semaphore(1);
    }

    @Override
    public void put(Message m) throws InterruptedException {

        fullness.acquire();

        mutex.acquire();
        msgBuffer[writeIndex % bufferSize] = m;
        writeIndex++;
        totmsg++;
        mutex.release();

        emptyness.release();
    }

    @Override
    public void put(Message m, long authorIdForFeedBack) throws InterruptedException {

        fullness.acquire();

        mutex.acquire();
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                    + " a produit " + m);
        mutex.release();
        emptyness.release();
    }

    @Override
    public Message get() throws InterruptedException {

        emptyness.acquire();

        Message m;

        mutex.acquire();
            m = msgBuffer[readIndex % bufferSize];
            readIndex++;
        mutex.release();
        fullness.release();

        return m;
    }

    @Override
    public Message get(long consumerIdForFeedBack) throws InterruptedException {

        emptyness.acquire();

        Message m;

        mutex.acquire();
            m = msgBuffer[readIndex % bufferSize];
            readIndex++;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                    + " a consumé " + m);
        mutex.release();
        fullness.release();
        return m;
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
    public Message[] get(int k) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consume'");
    }

    @Override
    public void put(Message m, int n, long authorIdForFeedBack) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public Message[] get(int k, long consumerIdForFeedBack) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consume'");
    }

}
