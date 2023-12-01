package prodcons.v2;

import java.util.Properties;
import java.util.concurrent.Semaphore;

import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    public Semaphore mutex ;
    volatile Message msgBuffer[];
    int bufferSize;
    volatile int writeIndex = 0;
    volatile int readIndex = 0;
    int totmsg = 0;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
        mutex = new Semaphore(1);
    }

    @Override
    public void put(Message m) throws InterruptedException {
        msgBuffer[writeIndex%bufferSize] = m;
        writeIndex++;
        totmsg++;
    }

    @Override
    public Message get() throws InterruptedException {
        Message m = msgBuffer[readIndex%bufferSize];
        readIndex++;
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

}
