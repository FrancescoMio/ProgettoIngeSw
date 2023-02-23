package unibs.ids.ristorante;

import java.util.HashMap;

public class Ricetta {
    private HashMap<String, Integer> ingredienti;
    private int numeroPorzioni;
    private float caricoDiLavoroXPorzione; //sarà una frazione<1 di carico per persona, controllo poi in Ristorante

    public Ricetta(HashMap<String, Integer> ingredienti, int numeroPorzioni, float caricoDiLavoroXPorzione) {
        this.ingredienti = ingredienti;
        this.numeroPorzioni = numeroPorzioni;
        this.caricoDiLavoroXPorzione = caricoDiLavoroXPorzione;
    }

    public Ricetta(){//per inizializzare a 0
        this.ingredienti = new HashMap<>();
        this.numeroPorzioni = 0;
        this.caricoDiLavoroXPorzione = 0;
    }
}
