package prodcons.v1;

import java.util.Random;

import prodcons.Message;

public class Producteur extends Thread {
    Message[] messages;
    int minProd;
    int maxProd;
    int currentMessage = 0;
    ProdConsBuffer buffer;
    int prodTime;

    public Producteur(int minProd, int maxProd, int prodTime, ProdConsBuffer buffer) {
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
                buffer.produce(messages[currentMessage], this.getId());
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
