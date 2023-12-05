package prodcons.v5;

import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    volatile Message msgBuffer[];
    int bufferSize;
    volatile int writeIndex = 0;
    volatile int readIndex = 0;
    int totmsg = 0;
    boolean isConsuming = false;;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
    }

    @Override
    public synchronized void put(Message m) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        msgBuffer[writeIndex % bufferSize] = m;
        writeIndex++;
        totmsg++;

        notify();
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
        notify();
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
    public synchronized Message[] get(int k) throws InterruptedException {
        while (isConsuming) {
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
            notifyAll();
            ret[i] = m;
        }

        isConsuming = false;

        notifyAll();
        return ret;
    }

    @Override
    public synchronized Message[] get(int k, long consumerIdForFeedBack) throws InterruptedException {
        while (isConsuming) {
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
