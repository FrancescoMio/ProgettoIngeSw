package unibs.ids.ristorante;

import Libreria.InputDati;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Ristorante {
    //dichiaro static perchè sono proprietà del ristorante che non vengono modificate
    private static String nome;
    private static int postiASedere;
    private static int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private static int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto
    private Set<Piatto> piatti;//lista di piatti che il ristorante può offrire
    private RegistroMagazzino registroMagazzino;
    private Gestore gestore; //gestore del ristorante
    private Set<MenuTematico> menuTematici;
    private Set<MenuCarta> menuAllaCarta;

    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        String nomeGestore = InputDati.leggiStringa("Inserire il nome del gestore: ");
        String cognomeGestore = InputDati.leggiStringa("Inserire il cognome del gestore: ");
        gestore = new Gestore(nomeGestore, cognomeGestore);
        registroMagazzino = new RegistroMagazzino();

        System.out.println("Buongiorno " + gestore.getNome() + " " + gestore.getCognome() + ", le chiediamo gentilmente di inserire i dati per il suo ristorante: ");

        this.nome = gestore.getNomeRistorante();
        this.postiASedere = gestore.postiASedere();
        this.piatti = gestore.inizializzaPiatti();
        this.caricoDiLavoroXPersona = gestore.caricoXpersona();
        this.caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
        this.registroMagazzino.addGenereAlimentareExtra();
        this.registroMagazzino.addBevanda();
        this.menuTematici = new HashSet<>();
        this.menuAllaCarta = new HashSet<>();
        menuTematici.addAll(gestore.creaMenuTematici(piatti));
    }
    public static int getCaricoXPersona() {
        return caricoDiLavoroXPersona;
    }
    public int getPostiASedere() {
        return postiASedere;
    }

    public RegistroMagazzino getRegistroMagazzino() {
        return registroMagazzino;
    }

    public void setRegistroMagazzino(RegistroMagazzino registroMagazzino) {
        this.registroMagazzino = registroMagazzino;
    }
}
