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
                buffer.consume(this.getId());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
