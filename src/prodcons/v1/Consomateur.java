package prodcons.v1;

import prodcons.Message;

public class Consomateur extends Thread {
    ProdConsBuffer buffer;
    int consTime;

    public Consomateur(int consTime, ProdConsBuffer buffer) {
        this.buffer = buffer;
        this.consTime = consTime;
        this.setDaemon(true);
    }

    public void run() {
        while (true) {
            try {
                sleep(consTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                consume();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public synchronized void consume() throws InterruptedException {
        while (buffer.isEmpty()) {
            buffer.wait();
        }

        Message m = buffer.get();
        System.out.println("[" + buffer.readIndex + "/" + buffer.writeIndex + "] Consomateur a consumé" + m);
        buffer.notifyAll();
    }
}
