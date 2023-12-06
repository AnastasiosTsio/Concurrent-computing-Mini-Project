package prodcons.v6;

import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    volatile Message msgBuffer[];
    int bufferSize;
    volatile int writeIndex = 0;
    volatile int readIndex = 0;
    int totmsg = 0;
    boolean isConsuming = false;;
    int nmsgRemaining = 0;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
    }

    
    @Override
    public synchronized void put(Message m) throws InterruptedException {
        put(m, 1);
    }

    @Override
    public synchronized void put(Message m, long authorIdForFeedBack) throws InterruptedException {
        put(m,1,authorIdForFeedBack);
    }

    @Override
    public synchronized void put(Message m, int n) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        nmsgRemaining = n;
        for (int i = 0; i < n; i++) {
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;
        }
    }

    @Override
    public synchronized void put(Message m, int n, long authorIdForFeedBack) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        nmsgRemaining = n;
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                + " a demander de produit " + n + " messages");
        for (int i = 0; i < n; i++) {
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;

            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                    + " a produit " + m);
            notifyAll();
        }
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                + " a produit " + n + " messages");
    }

    @Override
    public Message get() throws InterruptedException {
        return get(1)[0];
    }

    @Override
    public Message get(long consumerIdForFeedBack) throws InterruptedException {
        return get(1, consumerIdForFeedBack)[0];
    }

    @Override
    public synchronized Message[] get(int k) throws InterruptedException {
        while (isConsuming && nmsgRemaining != 0) {
            wait();
        }
        isConsuming = true;

        Message[] ret = new Message[k];
        for (int i = 0; i < k; i++) {
            while (isEmpty()) {
                wait();
            }
            Message m = msgBuffer[readIndex % bufferSize];
            readIndex++;
            nmsgRemaining--;
            notifyAll();
            ret[i] = m;
        }

        isConsuming = false;

        notifyAll();
        return ret;
    }

    @Override
    public synchronized Message[] get(int k, long consumerIdForFeedBack) throws InterruptedException {
        while (isConsuming && nmsgRemaining != 0) {
            wait();
        }

        isConsuming = true;

        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                + " a demandé " + k + " messages");
        Message[] ret = new Message[k];
        for (int i = 0; i < k; i++) {
            while (isEmpty()) {
                wait();
            }
            Message m = msgBuffer[readIndex % bufferSize];
            readIndex++;
            nmsgRemaining--;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                    + " a consumé " + m);
            notifyAll();
            ret[i] = m;
        }
        System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                + " a consumé " + k + " messages");
        isConsuming = false;

        notify();
        return ret;
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
