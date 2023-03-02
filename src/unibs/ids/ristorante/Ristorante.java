package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.MyUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static unibs.ids.ristorante.Stringhe.*;

public class Ristorante {
    //dichiaro static perchè sono proprietà del ristorante che non vengono modificate
    private static String nomeRistorante;
    private static int postiASedere;
    private static int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private static int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto
    private Set<Ricetta> libroRicette;
    private Set<Piatto> piattiDisponibili;//lista di piatti che il ristorante può offrire
    private RegistroMagazzino registroMagazzino;
    private Gestore gestore; //gestore del ristorante
    private Set<MenuTematico> menuTematici;
    private MenuCarta menuAllaCarta;

    /**
     * Costruttore dedicato alla inizializzazione dei dati di configurazione del ristorante
     */
    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        registroMagazzino = new RegistroMagazzino();
        creaGestore();
        creaConfigurazione();
        creaMenu();
        caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
        this.registroMagazzino.addGenereAlimentareExtra();
        this.registroMagazzino.addBevanda();
        gestore.visualizzaMenuTematici(menuTematici);
        gestore.visualizzaMenuAllaCarta(menuAllaCarta);
    }

    public void creaGestore(){
        System.out.println(lineSeparator);
        System.out.println(nuovaConfigurazione);
        String nome = InputDati.leggiStringa("Inserire il nome del gestore: ");
        String cognome = InputDati.leggiStringa("Inserire il cognome del gestore: ");
        gestore = new Gestore(nome, cognome);
    }

    public void creaConfigurazione(){
        libroRicette = new HashSet<>();
        piattiDisponibili = new HashSet<>();
        nomeRistorante = gestore.getNomeRistorante();
        postiASedere = gestore.postiASedere();
        libroRicette = gestore.inizializzaRicette();
        piattiDisponibili = gestore.inizializzaPiatti(libroRicette);
        caricoDiLavoroXPersona = gestore.caricoXpersona();
    }

    public void creaMenu(){
        System.out.println(lineSeparator);
        System.out.println(creazioneMenuTematici);
        //creazione dei menù  tematici
        menuTematici = new HashSet<>();
        menuTematici = gestore.creaMenuTematici(piattiDisponibili);
        //creazione menù alla carta
        String nomeMenuCarta = "Menù del " + MyUtil.getDataOdierna();
        menuAllaCarta = new MenuCarta(nomeMenuCarta,piattiDisponibili,MyUtil.getDataOdierna());
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
