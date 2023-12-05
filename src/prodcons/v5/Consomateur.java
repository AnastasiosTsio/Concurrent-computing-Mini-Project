package prodcons.v5;


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
                // Choose a random number
                int nb = (int) (Math.random() * 10);
                // Consume nb messages
                buffer.get(nb,this.getId());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
