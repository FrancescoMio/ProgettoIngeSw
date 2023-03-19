package unibs.ids.ristorante;

import static Libreria.Stringhe.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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


    @Override
    public String toString() {
        return  lineSeparator + "\n- Denominazione: " + denominazione + "\n- Tempo preparazione: " + tempoPreparazione
                + "min\n" + ricetta;
    }

    /**
     * metodo che ritorna la lista degli ingredienti
     * @return
     */
    public Merce getListaIngredienti(){
        return this.ricetta.getListaIngredienti();
    }
}