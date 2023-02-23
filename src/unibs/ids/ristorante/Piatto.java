package unibs.ids.ristorante;

public class Piatto {//deve implementare ordinabile
    private String denominazione;
    private boolean disponibile;
    private Ricetta ricetta = new Ricetta();//inizializza ricetta a 0

    public Piatto(String denominazione, boolean disponibile, Ricetta ricetta) {
        this.denominazione = denominazione;
        this.disponibile = disponibile;
        this.ricetta = ricetta;
    }
}