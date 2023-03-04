package unibs.ids.ristorante;

import java.time.LocalDate;
import java.util.Date;

public class Piatto implements Ordinabile {
    private String denominazione;
    //private boolean disponibile;
    private Ricetta ricetta = new Ricetta();
    private int tempoPreparazione;//in minuti
    private double caricoLavoro;


    //Costruttore
    public Piatto(String denominazione, Ricetta ricetta, int tempoPreparazione) {
        this.denominazione = denominazione;
        this.ricetta = ricetta;
        this.tempoPreparazione = tempoPreparazione;
        this.caricoLavoro = ricetta.getCaricoLavoro();//setto il carico di lavoro del piatto come quello della ricetta corrispondente
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

    @Override
    public String toString() {
        return "Piatto{" +
                "denominazione='" + denominazione + '\'' +
                ", ricetta=" + ricetta +
                ", tempo di perparazione=" + tempoPreparazione +
                ", caricoLavoro=" + caricoLavoro +
                '}';
    }
}