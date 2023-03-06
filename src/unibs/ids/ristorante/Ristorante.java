package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.LeggiJSON;
import Libreria.MyUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static unibs.ids.ristorante.Stringhe.*;

public class Ristorante {
    private String nomeRistorante;
    private int postiASedere;
    private int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto
    private Set<Piatto> piattiDisponibili;//lista di piatti che il ristorante può offrire
    private RegistroMagazzino registroMagazzino;
    private Gestore gestore; //gestore del ristorante
    private AddettoPrenotazioni addettoPrenotazioni;//addetto alle prenotazioni del ristorante
    private Set<MenuTematico> menuTematici;
    private MenuCarta menuAllaCarta;
    private ArrayList<Prenotazione> prenotazioni;

    /**
     * Costruttore dedicato alla inizializzazione dei dati di configurazione del ristorante
     */
    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        registroMagazzino = new RegistroMagazzino();
        creaGestore();
        creaConfigurazione();
        creaMenu();
        creaAddettoPrenotazioni();
        caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
        this.registroMagazzino.addGenereAlimentareExtra();
        this.registroMagazzino.addBevanda();
        gestore.visualizzaMenuTematici(menuTematici);
        gestore.visualizzaMenuAllaCarta(menuAllaCarta);
        LeggiJSON.salvaConfigurazione(this,piattiDisponibili);
        LeggiJSON.salvaMenuTematici(menuTematici);

    }

    public Ristorante(String caricaConfigurazione){
        gestore = new Gestore();
        piattiDisponibili = new HashSet<>();
        registroMagazzino = new RegistroMagazzino();
        addettoPrenotazioni = new AddettoPrenotazioni();
        menuTematici = new HashSet<>();
        menuAllaCarta = new MenuCarta();
    }

    public void creaGestore(){
        System.out.println(lineSeparator);
        System.out.println(nuovaConfigurazione);
        String nome = InputDati.leggiStringaConSpazi("Inserire il nome del gestore: ");
        String cognome = InputDati.leggiStringaConSpazi("Inserire il cognome del gestore: ");
        gestore = new Gestore(nome, cognome);
    }

    public void creaAddettoPrenotazioni(){
        System.out.println(lineSeparator);
        System.out.println(nuovaConfigurazione);
        String nome = InputDati.leggiStringaConSpazi("Inserire il nome dell'addetto alle prenotazioni: ");
        String cognome = InputDati.leggiStringaConSpazi("Inserire il cognome: ");
        addettoPrenotazioni = new AddettoPrenotazioni(nome, cognome);
        this.prenotazioni = addettoPrenotazioni.creaPrenotazioni(postiASedere,caricoDiLavoroSostenibile,menuAllaCarta,menuTematici);
    }

    public void creaConfigurazione(){
        piattiDisponibili = new HashSet<>();
        nomeRistorante = gestore.getNomeRistorante();
        postiASedere = gestore.postiASedere();
        piattiDisponibili = gestore.inizializzaPiatti();
        caricoDiLavoroXPersona = gestore.caricoXpersona();
    }

    public void creaMenu(){
        System.out.println(lineSeparator);
        System.out.println(creazioneMenuTematici);
        //creazione dei menù  tematici
        menuTematici = new HashSet<>();
        menuTematici = gestore.creaMenuTematici(piattiDisponibili,caricoDiLavoroXPersona);
        //creazione menù alla carta
        String nomeMenuCarta = "Menù del " + MyUtil.getDataOdierna();
        menuAllaCarta = new MenuCarta(nomeMenuCarta,piattiDisponibili,MyUtil.getDataOdierna());
    }
    public  int getCaricoXPersona() {
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
    public String getNomeRistorante() {
        return nomeRistorante;
    }
    public int getCaricoDiLavoroSostenibile() {
        return caricoDiLavoroSostenibile;
    }
    public Set<Piatto> getPiattiDisponibili() {
        return piattiDisponibili;
    }

    public String getNomeGestore() {
        return gestore.getNome();
    }

    public String getCognomeGestore(){
        return gestore.getCognome();
    }

    public void setGestore(String nome, String cognome){
        gestore.setGestore(nome,cognome);
    }
    public void setPostiASedere(int posti){
        postiASedere = posti;
    }
    public void setCaricoLavoroPersona(int carico){
        caricoDiLavoroXPersona = carico;
    }

    public void setCaricoLavoroSostenibile(int carico){
        caricoDiLavoroSostenibile = carico;
    }

    public void setNomeRistorante(String nome){
        nomeRistorante = nome;
    }
    public void setPiattiDisponibili(Set<Piatto> piattiDisponibili) {
        this.piattiDisponibili = piattiDisponibili;
    }

    @Override
    public String toString() {
        return "Ristorante{" +
                "nomeRistorante='" + nomeRistorante + '\'' +
                ", postiASedere=" + postiASedere +
                ", caricoDiLavoroSostenibile=" + caricoDiLavoroSostenibile +
                ", caricoDiLavoroXPersona=" + caricoDiLavoroXPersona +
                ", piattiDisponibili=" + piattiDisponibili +
                ", registroMagazzino=" + registroMagazzino +
                ", gestore=" + gestore +
                ", addettoPrenotazioni=" + addettoPrenotazioni +
                ", menuTematici=" + menuTematici +
                ", menuAllaCarta=" + menuAllaCarta +
                '}';
    }

}
