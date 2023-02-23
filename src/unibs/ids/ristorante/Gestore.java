package unibs.ids.ristorante;

public class Gestore extends Utente {

    public Gestore(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    //INIZIALIZZARE TUTTO


    private String getCaricoXPersona(Ristorante rist){
        return "Carico per persona: " + rist.getCaricoXPersona();
    }
}
