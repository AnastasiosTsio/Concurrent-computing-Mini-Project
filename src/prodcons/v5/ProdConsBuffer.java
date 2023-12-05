package prodcons.v5;


import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    volatile Message msgBuffer[];
    int bufferSize;
    volatile int writeIndex = 0;
    volatile int readIndex = 0;
    int totmsg = 0;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
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
    public Message[] consume(int k) throws InterruptedException {
        Message[] ret = new Message[k];
        for (int i = 0; i < k; i++) {
            ret[i] = consume();
        }
        return ret;
    }

    @Override
    public Message[] consume(int k, long consumerIdForFeedBack) throws InterruptedException {
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                + " a demandé " + k + " messages");
        Message[] ret = new Message[k];
        for (int i = 0; i < k; i++) {
            ret[i] = consume(consumerIdForFeedBack);
        }
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                + " a consumé " + k + " messages");
        return ret;
    }

    @Override
    public void put(Message m, int n) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public void put(Message m, int n, long authorIdForFeedBack) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

}
