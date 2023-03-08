package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.Json;
import Libreria.MyUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static Libreria.Stringhe.*;

public class Ristorante {
    private String nomeRistorante;
    private int postiASedere;
    private int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto
    private Set<Piatto> piatti;//lista di piatti che il ristorante può offrire, comprende anche quelli non disponibili
    private RegistroMagazzino registroMagazzino;
    private Gestore gestore; //gestore del ristorante
    private AddettoPrenotazioni addettoPrenotazioni;//addetto alle prenotazioni del ristorante
    private Set<MenuTematico> menuTematici;
    private MenuCarta menuAllaCarta;
    private Set<Bevanda> bevande;
    private Set<GenereAlimentareExtra> generiAlimentari;
    private ArrayList<Prenotazione> prenotazioni;
    private ConsumoProCapiteBevande consumoProCapiteBevande;
    private ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra;

    /**
     * Costruttore dedicato alla inizializzazione dei dati di configurazione del ristorante
     */
    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        registroMagazzino = new RegistroMagazzino();
        creaGestore();
        creaConfigurazione();
        creaMenuTematici();
        creaMenuCarta();
        creaInsiemeBevande();
        creaConsumoProCapiteBevande();
        creaInsiemeGeneriExtra();
        creaConsumoProCapiteGeneriExtra();
        //creaAddettoPrenotazioni();
        caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
        //this.registroMagazzino.addGenereAlimentareExtra();
        //this.registroMagazzino.addBevanda();
        gestore.visualizzaMenuTematici(menuTematici);
        gestore.visualizzaMenuAllaCarta(menuAllaCarta);
        Json.salvaConfigurazione(this,piatti);
        Json.salvaMenuTematici(menuTematici);
        Json.salvaMenuCarta(menuAllaCarta);
    }

    public Ristorante(String caricaConfigurazione){
        gestore = new Gestore();
        piatti = new HashSet<>();
        registroMagazzino = new RegistroMagazzino();
        addettoPrenotazioni = new AddettoPrenotazioni();
        menuTematici = new HashSet<>();
        menuAllaCarta = new MenuCarta();
        //Json.salvaMenuCarta(menuAllaCarta);
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
        piatti = new HashSet<>();
        nomeRistorante = gestore.getNomeRistorante();
        postiASedere = gestore.postiASedere();
        piatti = gestore.inizializzaPiatti();
        caricoDiLavoroXPersona = gestore.caricoXpersona();
    }

    public void creaMenuTematici(){
        System.out.println(lineSeparator);
        System.out.println(creazioneMenuTematici);
        //creazione dei menù  tematici
        menuTematici = new HashSet<>();
        menuTematici = gestore.creaMenuTematici(piatti,caricoDiLavoroXPersona);
    }

    public void creaMenuCarta(){
        Set<Piatto> piattiDisponibili = new HashSet<>();
        for (Piatto piatto : piatti) {
            if(MyUtil.controlloData(piatto.getDataInizio(), piatto.getDataFine()))
                piattiDisponibili.add(piatto);
        }
        menuAllaCarta = new MenuCarta("MenùAllaCarta",piattiDisponibili,MyUtil.getDataOdierna());
    }

    public void creaInsiemeBevande(){
        bevande = new HashSet<>();
        bevande = gestore.inizializzaBevande();
    }

    public void creaInsiemeGeneriExtra(){
        generiAlimentari = new HashSet<>();
        generiAlimentari = gestore.inizializzaGeneriAlimentari();
    }

    public void creaConsumoProCapiteBevande(){
        System.out.println(lineSeparator);
        System.out.println("CONFIGURAZIONE CONSUMO PRO CAPITE MEDIO BEVANDE:");
        consumoProCapiteBevande = gestore.inizializzaConsumoBevande(bevande);
    }
    public void creaConsumoProCapiteGeneriExtra(){
        System.out.println(lineSeparator);
        System.out.println("CONFIGURAZIONE CONSUMO PRO CAPITE GENERI EXTRA:");
        consumoProCapiteGeneriExtra = gestore.inizializzaConsumoGeneriExtra(generiAlimentari);
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
        return piatti;
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
    public void setPiatti(Set<Piatto> piatti) {
        this.piatti = piatti;
    }
    public Set<Piatto> getPiatti() {
        return piatti;
    }

    public void setMenuTematici(Set<MenuTematico> menuTematici) {
        this.menuTematici = menuTematici;
    }

    public MenuCarta getMenuAllaCarta() {
        return menuAllaCarta;
    }

    public void setMenuAllaCarta(MenuCarta menuAllaCarta) {
        this.menuAllaCarta = menuAllaCarta;
    }

    public Set<Bevanda> getBevande() {
        return bevande;
    }

    public Set<GenereAlimentareExtra> getGeneriAlimentari() {
        return generiAlimentari;
    }
    public void setBevande(Set<Bevanda> bevande) {
        this.bevande = bevande;
    }

    public void setGeneriAlimentari(Set<GenereAlimentareExtra> generiAlimentari) {
        this.generiAlimentari = generiAlimentari;
    }

    @Override
    public String toString() {
        return "Ristorante{" +
                "nomeRistorante='" + nomeRistorante + '\'' +
                ", postiASedere=" + postiASedere +
                ", caricoDiLavoroSostenibile=" + caricoDiLavoroSostenibile +
                ", caricoDiLavoroXPersona=" + caricoDiLavoroXPersona +
                ", piattiDisponibili=" + piatti +
                ", registroMagazzino=" + registroMagazzino +
                ", gestore=" + gestore +
                ", addettoPrenotazioni=" + addettoPrenotazioni +
                ", menuTematici=" + menuTematici +
                ", menuAllaCarta=" + menuAllaCarta +
                '}';
    }

}
