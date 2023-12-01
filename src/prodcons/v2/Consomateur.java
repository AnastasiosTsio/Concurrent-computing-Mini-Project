package prodcons.v2;

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
    
    public void consume() throws InterruptedException {
        synchronized (buffer) {
            buffer.mutex.acquire();
            while (buffer.isEmpty()) {
                buffer.mutex.release();
                buffer.wait();
                buffer.mutex.acquire();
            }
    
            Message m = buffer.get();
            System.out.println("[" + buffer.readIndex + "/" + buffer.writeIndex + "] Consomateur a consumé" + m);
            buffer.mutex.release();
            buffer.notifyAll();
        }
    }
}
