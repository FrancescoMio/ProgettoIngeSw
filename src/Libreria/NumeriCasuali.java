package Libreria;

import java.util.Random;

public class NumeriCasuali {
    private static final Random estrattore = new Random();

    public NumeriCasuali() {
    }

    public static int estraiIntero(int min, int max) {
        int range = max + 1 - min;
        int casual = estrattore.nextInt(range);
        return casual + min;
    }

    public static double estraiDouble(double min, double max) {
        double range = max - min;
        double casual = estrattore.nextDouble();
        double posEstratto = range * casual;
        return posEstratto + min;
    }
}
