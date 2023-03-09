package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;

public class Ricetta {
    private HashMap<String, Integer> ingredienti;//ingrediente come chiave e valore opportuno come valore
    private int numeroPorzioni;
    private double caricoDiLavoroXPorzione; //sarà una frazione<1 di carico per persona, controllo poi in Ristorante

    public Ricetta() {
    }
    public Ricetta( HashMap<String, Integer> ingredienti, int numeroPorzioni, double caricoDiLavoroXPorzione) {
        this.ingredienti = ingredienti;
        this.numeroPorzioni = numeroPorzioni;
        this.caricoDiLavoroXPorzione = caricoDiLavoroXPorzione;
    }

    public HashMap<String, Integer> getIngredienti() {
        return ingredienti;
    }

    /**
     * ritorna la dose opportuna di un ingrediente per una porzione
     * @param ingrediente
     * @return
     */
    public int getValoreIngrediente(String ingrediente){
        return ingredienti.get(ingrediente);
    }

    public double getCaricoLavoro() {
        return caricoDiLavoroXPorzione;
    }
    public int getNumeroPorzioni() {
        return numeroPorzioni;
    }

    @Override
    public String toString() {
        return "Ricetta{" +
                "ingredienti=" + ingredienti +
                ", numeroPorzioni=" + numeroPorzioni +
                ", caricoDiLavoroXPorzione=" + caricoDiLavoroXPorzione +
                '}';
    }

    public Merce getListaIngredienti() {
        Merce listaIngredienti = getListaIngredienti().aggiungiIngrediente(ingredienti);
        return listaIngredienti;
    }
}
