package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Libreria.Stringhe.*;

public class Ricetta {
    private HashMap<String, QuantitaMerce> ingredienti;//ingrediente come chiave e valore opportuno come valore
    private int numeroPorzioni;
    private double caricoDiLavoroXPorzione; //sarà una frazione<1 di carico per persona, controllo poi in Ristorante

    public Ricetta( HashMap<String, QuantitaMerce> ingredienti, int numeroPorzioni, double caricoDiLavoroXPorzione) {
        this.ingredienti = ingredienti;
        this.numeroPorzioni = numeroPorzioni;
        this.caricoDiLavoroXPorzione = caricoDiLavoroXPorzione;
    }

    public HashMap<String, QuantitaMerce> getIngredienti() {
        return ingredienti;
    }

    public double getCaricoLavoro() {
        return caricoDiLavoroXPorzione;
    }
    public int getNumeroPorzioni() {
        return numeroPorzioni;
    }

    public String getStrIngredienti() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, QuantitaMerce> entry : ingredienti.entrySet()) {
            sb.append("- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue().getQuantita())
                    .append(" ")
                    .append(entry.getValue().getUnitaMisura())
                    .append("\n");
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        return  "* RICETTA:\n" +"- Numero porzioni: " + numeroPorzioni + "\n- Carico lavoro per porzione: " + caricoDiLavoroXPorzione +
                 "\n INGREDIENTI:\n" + getStrIngredienti();
    }
}
