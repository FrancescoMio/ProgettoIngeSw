package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;
import static Libreria.Stringhe.*;

public class Ricetta {
    private HashMap<String, Double> ingredienti;//ingrediente come chiave e valore opportuno come valore
    private int numeroPorzioni;
    private double caricoDiLavoroXPorzione; //sarà una frazione<1 di carico per persona, controllo poi in Ristorante

    public Ricetta() {
    }
    public Ricetta( HashMap<String, Double> ingredienti, int numeroPorzioni, double caricoDiLavoroXPorzione) {
        this.ingredienti = ingredienti;
        this.numeroPorzioni = numeroPorzioni;
        this.caricoDiLavoroXPorzione = caricoDiLavoroXPorzione;
    }

    public HashMap<String, Double> getIngredienti() {
        return ingredienti;
    }

    /**
     * ritorna la dose opportuna di un ingrediente per una porzione
     * @param ingrediente
     * @return
     */
    public double getValoreIngrediente(String ingrediente){
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
        return ANSI_BLUE + "* RICETTA:\n"+ ANSI_RESET +"- Numero porzioni: " + numeroPorzioni + "\n- Carico lavoro per porzione: " + caricoDiLavoroXPorzione + ANSI_RESET+
                ANSI_CYAN + "\n* Ingredienti:\n" + ANSI_RESET + ingredienti;
    }
}
