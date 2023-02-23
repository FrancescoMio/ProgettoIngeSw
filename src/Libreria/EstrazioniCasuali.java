package Libreria;

import java.util.Random;

public class EstrazioniCasuali {
    private static final Random rand = new Random();

    public EstrazioniCasuali() {
    }

    public static int estraiIntero(int min, int max) {
        int range = max + 1 - min;
        int casual = rand.nextInt(range);
        return casual + min;
    }
}
