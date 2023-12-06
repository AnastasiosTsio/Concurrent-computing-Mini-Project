package prodcons.v1;

import java.util.Properties;
import prodcons.*;

// A rendre le 8 au matin
public class ProdConsTestV1 {
    static boolean debugMode;

    public static void main(String[] args) throws Exception {
        debugMode = true;
        if (args.length != 0) {
            if (args[0].equals("--muted")) {
                debugMode = false;
            }
        }
        // Lecture des options
        Properties properties = new Properties();
        properties.loadFromXML(ProdConsTestV1.class.getClassLoader().getResourceAsStream("options.xml"));
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
        int nProd = Integer.parseInt(properties.getProperty("nProd"));
        int nCons = Integer.parseInt(properties.getProperty("nCons"));
        int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
        int consTime = Integer.parseInt(properties.getProperty("consTime"));
        int minProd = Integer.parseInt(properties.getProperty("minProd"));
        int maxProd = Integer.parseInt(properties.getProperty("maxProd"));

        // Initialisation du buffer
        ProdConsBuffer buffer = new ProdConsBuffer(bufSz);

        // Initialisation des producteurs et des consommateurs
        if (debugMode)
            System.out.println("ProdConsTest: lancement de " + nProd + " producteurs");

        Producteur[] prods = new Producteur[nProd];
        for (int i = 0; i < nProd; i++) {
            prods[i] = new Producteur(minProd, maxProd, prodTime, buffer, debugMode);
            prods[i].start();
        }
        if (debugMode)
            System.out.println("ProdConsTest: lancement de " + nCons + " consommateurs");

        Consomateur[] cons = new Consomateur[nCons];
        for (int i = 0; i < nCons; i++) {
            cons[i] = new Consomateur(consTime, buffer, debugMode);
            cons[i].start();
        }

        for (int i = 0; i < nProd; i++)
            prods[i].join();

        while (!buffer.isEmpty())
            ;

        if (debugMode)
            System.out.println("ProdConsTest: Tous les messages ont étés consommés. Fin du test.");

    }
}
