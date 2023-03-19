package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.Json;
import Libreria.MyMenu;
import Libreria.MyUtil;
import java.util.ArrayList;
import java.util.HashMap;
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
    private Magazziniere magazziniere;//magazziniere del ristorante
    private AddettoPrenotazioni addettoPrenotazioni;//addetto alle prenotazioni del ristorante
    private Set<MenuTematico> menuTematici;
    private MenuCarta menuAllaCarta;
    private Set<Bevanda> bevande;
    private Set<GenereAlimentareExtra> generiAlimentariExtra;
    private ArrayList<Prenotazione> prenotazioni;
    private ConsumoProCapiteBevande consumoProCapiteBevande;
    private ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra;

    /**
     * Costruttore dedicato all'inizializzazione dei dati di configurazione del ristorante
     */
    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        registroMagazzino = new RegistroMagazzino();

        creaGestore();
        creaConfigurazione();
        creaMenuTematici();
        creaMenuCarta();
        creaInsiemeBevandeEGeneri();
        creaConsumoProCapite();

        creaAddettoPrenotazioni();
        creaMagazziniere();
        caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
        gestore.visualizzaMenuTematici(menuTematici);
        gestore.visualizzaMenuAllaCarta(menuAllaCarta);
        Json.salvaConfigurazione(this,piatti);
        Json.salvaMenuTematici(menuTematici);
        Json.salvaMenuCarta(menuAllaCarta);
        Json.salvaConsumiProCapite(consumoProCapiteBevande,consumoProCapiteGeneriExtra);
    }

    //TODO: alla fine mettere privati tutti i metodi che non servono all'esterno
    public Ristorante(String caricaConfigurazione){
        gestore = new Gestore();
        piatti = new HashSet<>();
        registroMagazzino = new RegistroMagazzino();
        addettoPrenotazioni = new AddettoPrenotazioni();
        menuTematici = new HashSet<>();
        menuAllaCarta = new MenuCarta();
        prenotazioni = new ArrayList<>();
    }

    public void creaGestore(){
        System.out.println(lineSeparator);
        System.out.println(nuovaConfigurazione);
        String nome = InputDati.leggiStringaConSpazi("Inserire il nome del gestore: ");
        String cognome = InputDati.leggiStringaConSpazi("Inserire il cognome del gestore: ");
        gestore = new Gestore(nome, cognome);
    }

    private void creaMagazziniere(){
        System.out.println(lineSeparator);
        String nome = InputDati.leggiStringaConSpazi("Inserire il nome del magazziniere: ");
        String cognome = InputDati.leggiStringaConSpazi("Inserire il cognome: ");
        Magazziniere magazziniere = new Magazziniere(nome, cognome);
    }

    private void creaAddettoPrenotazioni(){
        System.out.println(lineSeparator);
        String nome = InputDati.leggiStringaConSpazi("Inserire il nome dell'addetto alle prenotazioni: ");
        String cognome = InputDati.leggiStringaConSpazi("Inserire il cognome: ");
        addettoPrenotazioni = new AddettoPrenotazioni(nome, cognome);
        //metodo lo metterei da un'altra parte, qua creo solo l'addetto
        //this.prenotazioni = addettoPrenotazioni.creaPrenotazioni(postiASedere,caricoDiLavoroSostenibile,menuAllaCarta,menuTematici,prenotazioni);
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

    /**
     * Metodo per l'inizializzazione delle bevande e generi alimentari extra
     */
    public void creaInsiemeBevandeEGeneri(){
        Set<Raggruppabile> bevandeEextra = gestore.inizializzaBevandeEgeneri();
        Set<Bevanda> insiemeBevande = new HashSet<>();
        Set<GenereAlimentareExtra> insiemeGeneriExtra = new HashSet<>();
        for (Raggruppabile item: bevandeEextra) {
            if(item instanceof Bevanda)
                insiemeBevande.add((Bevanda) item);
            else insiemeGeneriExtra.add((GenereAlimentareExtra) item);
        }
        this.bevande = insiemeBevande;
        this.generiAlimentariExtra = insiemeGeneriExtra;
    }

    /**
     * Metodo per l'inizializzazione del consumo pro capite delle bevande e generi alimentari extra,
     * L'ArrayList contiene le due hashMap relative rispettivamente ai consumi pro capite delle bevande e
     * dei generi alimentari extra
     */
    public void creaConsumoProCapite(){
        ArrayList<HashMap<Raggruppabile,QuantitaMerce>> consumi = gestore.inizializzaConsumi(bevande,generiAlimentariExtra);
        ConsumoProCapiteBevande consumoBevande = new ConsumoProCapiteBevande();
        ConsumoProCapiteGeneriExtra consumoGeneri = new ConsumoProCapiteGeneriExtra();
        consumoBevande.setConsumo(consumi.get(0));
        consumoGeneri.setConsumo(consumi.get(1));
        this.consumoProCapiteBevande = consumoBevande;
        this.consumoProCapiteGeneriExtra = consumoGeneri;
        System.out.println(consumoProCapiteBevande);
        System.out.println(consumoProCapiteGeneriExtra);
    }

    public void creaNuovaPrenotazione(){
        ArrayList<Prenotazione> nuovePrenotazioni = addettoPrenotazioni.creaPrenotazioni(postiASedere,caricoDiLavoroSostenibile,menuAllaCarta,menuTematici,prenotazioni);
        prenotazioni.addAll(nuovePrenotazioni);
        prenotazioni = addettoPrenotazioni.togliPrenotazioniScadute(prenotazioni);
        Json.salvaPrenotazioni(prenotazioni);
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
        return generiAlimentariExtra;
    }
    public void setBevande(Set<Bevanda> bevande) {
        this.bevande = bevande;
    }

    public void setGeneriAlimentari(Set<GenereAlimentareExtra> generiAlimentari) {
        this.generiAlimentariExtra = generiAlimentari;
    }
    public ArrayList<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(ArrayList<Prenotazione> prenotazioni){
        this.prenotazioni = prenotazioni;
    }

    public void visualizzaParametri(){
        MyMenu menuVisualizzazione = new MyMenu(titoloMenuVisualizzazione, vociMenuVisualizzazione);
        boolean finito = false;
        do{
            int scelta = menuVisualizzazione.scegli();
            switch (scelta){
                case 1:
                    gestore.visualizzaCaricoLavoroPersona(caricoDiLavoroXPersona);
                    break;
                case 2:
                    gestore.visualizzaPostiSedere(postiASedere);
                    break;
                case 3:
                    gestore.visualizzaInsiemeBevande(bevande);
                case 4:
                    gestore.visualizzaInsiemeGeneri(generiAlimentariExtra);
                    break;
                case 5:
                    gestore.visualizzaConsumoProCapite(consumoProCapiteBevande);
                    break;
                case 6:
                    gestore.visualizzaConsumoProCapite(consumoProCapiteGeneriExtra);
                    break;
                case 7:
                    gestore.visualizzaPiattoRicetta(piatti);
                case 8:
                    gestore.visualizzaPiattieValidita(piatti);
                    break;
                case 9:
                    gestore.visualizzaRicette(piatti);
                    break;
                case 10:
                    gestore.visualizzaMenuTematici(menuTematici);
                    break;
                default: finito = true;
            }
        }while(!finito);


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

    private void gestisciFlussoMagazzino(){
        magazziniere.creaListaSpesaGiornaliera(prenotazioni, registroMagazzino);//creo lista spesa
        magazziniere.stampaListaSpesa();

        //flusso di prodotti acquistati
        registroMagazzino.aggiungiIngredientiComprati(magazziniere.getListaSpesa());

        //prodotti portati in cucina
        //registroMagazzino.portatiInCucina(); come calcolo gli ingredienti portati in cucina?
        //bevande e generi alimentari portati in sala, a più riprese?
        //registroMagazzino.setBevandeEExtraPortatiInSala(); fare metodo che calcola bevande e extra per ogni persona?

        //ultimi due punti di registro magazzino da discutere fares
    }

}
