package unibs.ids.ristorante;

import java.util.Date;

public class Piatto implements Ordinabile {//deve implementare ordinabile
    private String denominazione;
    private boolean disponibile;
    private Ricetta ricetta;
    private Date dataInizio;
    private Date dataFine;


    public Piatto(String denominazione, boolean disponibile, Ricetta ricetta, Date inizio, Date fine) {
        this.denominazione = denominazione;
        this.ricetta = ricetta;
        this.dataInizio = inizio;
        this.dataFine = fine;
        if(Libreria.MyUtil.controlloData(inizio, fine))
            this.disponibile = disponibile;
        else
            this.disponibile = false;
    }
}