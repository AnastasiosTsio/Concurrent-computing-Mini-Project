package prodcons.v2;


import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    volatile Message msgBuffer[];
    int bufferSize;
    volatile int writeIndex = 0;
    volatile int readIndex = 0;
    int totmsg = 0;
    int producerCount ;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
        producerCount = size;
    }

    @Override
    public synchronized void produce(Message m) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        msgBuffer[writeIndex % bufferSize] = m;
        writeIndex++;
        totmsg++;

        notifyAll();
    }

    @Override
    public synchronized void produce(Message m, long authorIdForFeedBack) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        msgBuffer[writeIndex % bufferSize] = m;
        writeIndex++;
        totmsg++;
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                + " a produit " + m);
        notifyAll();
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
    public synchronized Message consume(long consumerIdForFeedBack) throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        Message m = msgBuffer[readIndex%bufferSize];
        readIndex++;
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                + " a consumé " + m);
        notifyAll();
        return m;
    }

    public synchronized void quitProduction() {
        producerCount--;
        if (producerCount == 0) {
            notifyAll();
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

}
