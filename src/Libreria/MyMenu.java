package Libreria;

import Libreria.InputDati;

public class MyMenu {
    private static final String CORNICE = "--------------------------------";
    private static final String VOCE_USCITA = "0\tEsci";
    private static final String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";
    private final String titolo;
    private final String[] voci;

    public MyMenu(String titolo, String[] voci) {
        this.titolo = titolo;
        this.voci = voci;
    }

    public int scegli() {
        this.stampaMenu();
        return InputDati.leggiIntero("Digita il numero dell'opzione desiderata > ", 0, this.voci.length);
    }

    public void stampaMenu() {
        System.out.println("--------------------------------");
        System.out.println(this.titolo);
        System.out.println("--------------------------------");

        for(int i = 0; i < this.voci.length; ++i) {
            System.out.println(i + 1 + "\t" + this.voci[i]);
        }

        System.out.println();
        System.out.println("0\tEsci");
        System.out.println();
    }
}
