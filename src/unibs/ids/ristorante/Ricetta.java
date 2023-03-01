package unibs.ids.ristorante;

import java.util.HashMap;

public class Ricetta {
    private String nome;
    private HashMap<String, Integer> ingredienti;//ingrediente come chiave e valore opportuno come valore
    private int numeroPorzioni;
    private double caricoDiLavoroXPorzione; //sarà una frazione<1 di carico per persona, controllo poi in Ristorante


    public Ricetta(String nome, HashMap<String, Integer> ingredienti, int numeroPorzioni, double caricoDiLavoroXPorzione) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.numeroPorzioni = numeroPorzioni;
        this.caricoDiLavoroXPorzione = caricoDiLavoroXPorzione;
    }

    public HashMap<String, Integer> getIngredienti() {
        return ingredienti;
    }

    public String getNome() {
        return nome;
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

    @Override
    public String toString() {
        return "Ricetta{" +
                "nome='" + nome +
                "ingredienti=" + ingredienti +
                ", numeroPorzioni=" + numeroPorzioni +
                ", caricoDiLavoroXPorzione=" + caricoDiLavoroXPorzione +
                '}';
    }
}
