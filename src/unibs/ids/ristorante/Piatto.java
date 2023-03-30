package unibs.ids.ristorante;

import static Libreria.Stringhe.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Piatto implements Ordinabile {
    private String denominazione;
    private Ricetta ricetta;

    private int tempoPreparazione;//in minuti
    private double caricoLavoro;
    private LocalDate dataInizio;
    private LocalDate dataFine;


    //Costruttore
    public Piatto(String denominazione, Ricetta ricetta, int tempoPreparazione, LocalDate dataInizio, LocalDate dataFine) {
        this.denominazione = denominazione;
        this.ricetta = ricetta;
        this.tempoPreparazione = tempoPreparazione;
        this.caricoLavoro = ricetta.getCaricoLavoro();//setto il carico di lavoro del piatto come quello della ricetta corrispondente
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public Ricetta getRicetta() {
        return ricetta;
    }

    public String getDenominazione(){
        return this.denominazione;
    }
    public double getCaricoLavoro() {
        return caricoLavoro;
    }
    public void setCaricoLavoro(double caricoLavoro) {
        this.caricoLavoro = caricoLavoro;
    }
    public int getTempoPreparazione() {
        return tempoPreparazione;
    }
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }
    public String getNome(){
        return this.denominazione;
    }
    /**
     * metodo che ritorna la lista degli ingredienti
     * @return
     */
    public HashMap<String,QuantitaMerce> getListaIngredienti(int quantitaOrdine){
        int numeroPorzioni = ricetta.getNumeroPorzioni();
        HashMap<String, QuantitaMerce> ingredienti = ricetta.getIngredienti();
        if(numeroPorzioni == 1)
            return ingredienti;
        else{
            HashMap<String, QuantitaMerce> listaIngredienti = new HashMap<>();
            for (Map.Entry<String, QuantitaMerce> entry : ingredienti.entrySet()) {
                String nomeIngrediente = entry.getKey();
                QuantitaMerce quantitaIngrediente = entry.getValue();
                String unitaMisura = quantitaIngrediente.getUnitaMisura();
                double quantita = quantitaIngrediente.getQuantita();
                double quantitaAggiornata = quantita / numeroPorzioni * quantitaOrdine;
                QuantitaMerce quantitaIngredienteAggiornata = new QuantitaMerce(quantitaAggiornata,unitaMisura);
                listaIngredienti.put(nomeIngrediente, quantitaIngredienteAggiornata);
            }
            return listaIngredienti;
        }
    }
    @Override
    public String toString() {
        return  lineSeparator + "\n* DENOMINAZIONE: " + denominazione + "\n- Tempo preparazione: " + tempoPreparazione
                + "min\n" + ricetta;
    }
}