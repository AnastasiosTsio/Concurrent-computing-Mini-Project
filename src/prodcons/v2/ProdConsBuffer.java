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
    public void produce(Message m) throws InterruptedException {
        msgBuffer[writeIndex%bufferSize] = m;
        writeIndex++;
        totmsg++;
    }

    @Override
    public void produce(Message m, int authorIdForFeedBack) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'produce'");
    }

    @Override
    public synchronized Message consume() throws InterruptedException {
        while (this.isEmpty()) {
            this.wait();
        }
        Message m = msgBuffer[readIndex%bufferSize];
        readIndex++;
        notifyAll();
        return m;
    }
    @Override
    public synchronized Message consume(int consumerIdForFeedBack) throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        Message m = msgBuffer[readIndex%bufferSize];
        readIndex++;
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur a consumé" + m);
        notifyAll();
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
