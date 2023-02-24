package unibs.ids.ristorante;

import java.util.Date;

public class Piatto implements Ordinabile {
    private String denominazione;
    private boolean disponibile;
    private Ricetta ricetta;
    private Date dataInizio;
    private Date dataFine;
    private double caricoLavoro;

    //Costruttore
    public Piatto(String denominazione, Ricetta ricetta, Date inizio, Date fine) {
        this.denominazione = denominazione;
        this.ricetta = ricetta;
        this.dataInizio = inizio;
        this.dataFine = fine;
        if(Libreria.MyUtil.controlloData(inizio, fine))//controllo le date per vedere se piatto è disponibile
            this.disponibile = true;
        else
            this.disponibile = false;
        this.caricoLavoro = ricetta.getCaricoLavoro();//setto il carico di lavoro del piatto come quello della ricetta corrispondente
    }

    public Ricetta getRicetta() {
        return ricetta;
    }
}