package unibs.ids.ristorante;

import Libreria.InputDati;

public class Gestore extends Utente {

    private Ristorante ristorante;

    public Gestore(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    /**
     * inizializza tutte le variabili ( Ristorante)
     */
    private void inizializzaRistorante(){
        System.out.println("Buongiorno gestore, le chiediamo gentilmente di inserire i dati per il suo ristorante: ");
        String nome = InputDati.leggiStringa("Inserire nome ristorante: ");
        int caricoXpersona = InputDati.leggiIntero("Inserire carico medio per persona:");
        int posti = InputDati.leggiIntero("Inserire posti a sedere");
        //inserire insieme bevande, fare metodo, magari metodo unico va bene se si fanno i controlli nelle rispettive classi
        //inserire insieme generi extra, fare metodo
        //inserire consumo procapire bevande
        //inserire consumo procapite beni extra
        //inserire tutti i piatti
    }

    private String getCaricoXPersona(Ristorante rist){
        return "Carico per persona: " + rist.getCaricoXPersona();
    }
}
