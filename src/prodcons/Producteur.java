package prodcons;

import java.util.Random;


public class Producteur extends Thread {
    boolean debugMode;
    Message[] messages;
    int minProd;
    int maxProd;
    int currentMessage = 0;
    IProdConsBuffer buffer;
    int prodTime;

    public Producteur(int minProd, int maxProd, int prodTime, IProdConsBuffer buffer, boolean debugMode) {
        Random rand = new Random();
        this.buffer = buffer;
        this.minProd = minProd;
        this.maxProd = maxProd;
        this.prodTime = prodTime;
        int nbMessages = minProd + rand.nextInt(maxProd - minProd);
        messages = new Message[nbMessages];

        for (int i = 0; i < nbMessages; i++) {
            messages[i] = createMessage();
        }
    }

    public void run() {
        while (currentMessage < messages.length) {
            try {
                sleep(prodTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (debugMode)
                    buffer.put(messages[currentMessage], this.getId());
                else
                    buffer.put(messages[currentMessage]);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            currentMessage++;
        }

    }

    public Message createMessage() {
        return new Message("Hello, World! I am" + this.getId() + "with currentMessage = " + currentMessage);
    }
}
