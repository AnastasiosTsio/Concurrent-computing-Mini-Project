package prodcons;



public class Consomateur extends Thread {
    boolean debugMode;
    IProdConsBuffer buffer;
    int consTime;

    public Consomateur(int consTime, IProdConsBuffer buffer, boolean debugMode) {
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
                if (debugMode)
                    buffer.get(this.getId());
                else
                    buffer.get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}