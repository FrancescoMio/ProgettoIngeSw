package unibs.ids.ristorante;

import Libreria.InputDati;

import java.util.ArrayList;

public class Ristorante {
    private String nome;
    private int postiASedere;
    private int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto
    private ArrayList<Piatto> piatti;//lista di piatti che il ristorante può offrire

    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        String nomeGestore = InputDati.leggiStringa("Inserisci il nome del gestore: ");
        String cognomeGestore = InputDati.leggiStringa("Inserisci il cognome del gestore: ");
        Gestore gestore = new Gestore(nomeGestore, cognomeGestore);

        System.out.println("Buongiorno " + gestore.getNome() + " " + gestore.getCognome() + ", le chiediamo gentilmente di inserire i dati per il suo ristorante: ");

        this.nome = gestore.getNomeRistorante();
        this.postiASedere = gestore.postiASedere();
        this.piatti = gestore.inizializzaPiatti();
        this.caricoDiLavoroXPersona = gestore.caricoXpersona();
        this.caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
    }
    public int getCaricoXPersona() {
        return caricoDiLavoroXPersona;
    }
    public int getPostiASedere() {
        return postiASedere;
    }
}
