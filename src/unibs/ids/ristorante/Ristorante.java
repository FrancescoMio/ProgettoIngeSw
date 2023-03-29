package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.Json;
import Libreria.MyMenu;
import Libreria.MyUtil;

import java.util.*;

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
    private Merce merceInCucina;
    private Merce merceDaPortareInCucina;

    /**
     * Costruttore dedicato all'inizializzazione dei dati di configurazione del ristorante
     */
    public Ristorante() {
        registroMagazzino = new RegistroMagazzino();
        merceInCucina = new Merce();
        merceDaPortareInCucina = new Merce();
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

    public Ristorante(String caricaConfigurazione){
        gestore = new Gestore();
        piatti = new HashSet<>();
        registroMagazzino = new RegistroMagazzino();
        addettoPrenotazioni = new AddettoPrenotazioni();
        magazziniere = new Magazziniere();
        menuTematici = new HashSet<>();
        menuAllaCarta = new MenuCarta();
        prenotazioni = new ArrayList<>();
        bevande = new HashSet<>();
        generiAlimentariExtra = new HashSet<>();
        consumoProCapiteBevande = new ConsumoProCapiteBevande();
        consumoProCapiteGeneriExtra = new ConsumoProCapiteGeneriExtra();
        merceInCucina = new Merce();
        merceDaPortareInCucina = new Merce();
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
        Json.salvaPrenotazioni(prenotazioni);
    }

    public void filtraPrenotazioni(){
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
    public void setConsumoProCapiteBevande(ConsumoProCapiteBevande consumoProCapiteBevande){
        this.consumoProCapiteBevande = consumoProCapiteBevande;
    }
    public void setConsumoProCapiteGeneriExtra(ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra){
        this.consumoProCapiteGeneriExtra = consumoProCapiteGeneriExtra;
    }

    public ConsumoProCapiteBevande getConsumoProCapiteBevande() {
        return consumoProCapiteBevande;
    }

    public ConsumoProCapiteGeneriExtra getConsumoProCapiteGeneriExtra() {
        return consumoProCapiteGeneriExtra;
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

    //todo: fare in modo che lista della spesa venga creata una sola volta in automatico
    public void creaListaSpesa(){
        magazziniere.creaListaSpesaGiornaliera(prenotazioni,registroMagazzino,consumoProCapiteBevande,consumoProCapiteGeneriExtra);
        Merce listaSpesa = magazziniere.getListaSpesa();
        registroMagazzino.aggiungiArticoliComprati(listaSpesa);
        merceDaPortareInCucina = magazziniere.portaIngredientiInCucina(prenotazioni);
        System.out.println(ANSI_CYAN+"MERCE DA PORTARE IN CUCINA:"+ANSI_RESET);
        merceDaPortareInCucina.visualizzaMerce();
        System.out.println(ANSI_CYAN+"REGISTRO MAGAZZINO:"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();
        Json.salvaRegistroMagazzino(registroMagazzino);
        Json.salvaCucina(merceDaPortareInCucina,merceInCucina);
    }
    public void portaIngredientiInCucina(){
        HashMap<String,QuantitaMerce> prodottiDaPortareInCucina = merceDaPortareInCucina.getArticoli();
        HashMap<String,QuantitaMerce> ingredientiDaAggiungere = new HashMap<>();
        do {
            System.out.println(lineSeparator);
            System.out.println(ANSI_BLUE + "---INGREDIENTI DA PORTARE IN CUCINA---" + ANSI_RESET);
            merceDaPortareInCucina.visualizzaMerce();
            boolean ingredientePresente = false;
            String nomeIngrediente = "";
            do {
                nomeIngrediente = InputDati.leggiStringaNonVuota("Inserire nome ingrediente da portare in cucina: ");
                if (prodottiDaPortareInCucina.containsKey(nomeIngrediente))
                    ingredientePresente = true;
                else System.err.println("Ingrediente non presente nella lista!");
            } while (!ingredientePresente);
            QuantitaMerce quantitaIngrediente = prodottiDaPortareInCucina.get(nomeIngrediente);
            double quantitaMax = quantitaIngrediente.getQuantita();
            String unitaMisura = quantitaIngrediente.getUnitaMisura();
            double quantitaDaPortare = InputDati.leggiDoubleCompreso("Inserire quantità ingrediente da portare in cucina ("+unitaMisura+"): ", 0, quantitaMax);
            QuantitaMerce quantitaIngredienteDaPortare = new QuantitaMerce(quantitaDaPortare, unitaMisura);
            QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaMax - quantitaDaPortare, unitaMisura);
            prodottiDaPortareInCucina.replace(nomeIngrediente, quantitaIngrediente, quantitaAggiornata);
            //ingredientiDaAggiungere.put(nomeIngrediente, quantitaIngredienteDaPortare);
            if(ingredientiDaAggiungere.containsKey(nomeIngrediente)){
                QuantitaMerce quantitaProdottoOld = ingredientiDaAggiungere.get(nomeIngrediente);
                double quantitaOld = quantitaProdottoOld.getQuantita();
                QuantitaMerce quantitaAggiornataDaAggiungere = new QuantitaMerce(quantitaDaPortare+quantitaOld,unitaMisura);
                ingredientiDaAggiungere.put(nomeIngrediente,quantitaAggiornataDaAggiungere);
            }else ingredientiDaAggiungere.put(nomeIngrediente,quantitaIngredienteDaPortare);
        }while (InputDati.yesOrNo(ANSI_GREEN + "Portare un altro ingrediente in cucina?" + ANSI_RESET));
        System.out.println(ANSI_CYAN+"INGREDIENTI DA AGGIUNGERE:"+ANSI_RESET);
        System.out.println(ingredientiDaAggiungere);
        merceInCucina.aggiungiIngredienti(ingredientiDaAggiungere);
        System.out.println(ANSI_CYAN+"MERCE PORTATA IN CUCINA:"+ANSI_RESET);
        merceInCucina.visualizzaMerce();
        System.out.println(ANSI_CYAN+"MERCE RIMASTA DA PORTARE IN CUCINA:"+ANSI_RESET);
        merceDaPortareInCucina.visualizzaMerce();
        registroMagazzino.rimuoviProdotti(ingredientiDaAggiungere); //metodo per la rimozione dal registro magazzino degli ingredienti portati in cucina
        System.out.println(ANSI_CYAN+"REGISTRO MAGAZZINO AGGIORNATO:"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();
        //Json.salvaRegistroMagazzino(registroMagazzino);
        Json.salvaCucina(merceDaPortareInCucina,merceInCucina);
    }
    public void portaBevandaGenereInSala(){
        Set<Raggruppabile> bevandeEGeneri = new HashSet<>();
        bevandeEGeneri.addAll(bevande);
        bevandeEGeneri.addAll(generiAlimentariExtra);
        HashMap<String, QuantitaMerce> prodottiInSala = magazziniere.portaBevandaGenereInSala(registroMagazzino,bevandeEGeneri);
        registroMagazzino.rimuoviProdotti(prodottiInSala);
        System.out.println(ANSI_CYAN+"REGISTRO MAGAZZINO AGGIORNATO:"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();
        //Json.salvaRegistroMagazzino(registroMagazzino);
    }

    public void riportaInMagazzinoNonConsumati(){
        HashMap<String,QuantitaMerce> prodottiDaRiportare = magazziniere.riportaInMagazzino(merceInCucina);
        System.out.println("prodotti da riportare in magazzino: ");
        System.out.println(prodottiDaRiportare);
        System.out.println(ANSI_CYAN+"MERCE IN CUCINA RIMASTA:"+ANSI_RESET);
        merceInCucina.visualizzaMerce();
        registroMagazzino.riportaProdotti(prodottiDaRiportare);
        System.out.println(ANSI_CYAN+"REGISTRO MAGAZZINO CON PRODOTTI RIPORTATI:"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();
        //Json.salvaRegistroMagazzino(registroMagazzino);
        Json.salvaCucina(merceDaPortareInCucina,merceInCucina);
    }

    public void rimuoviScartiDalMagazzino(){
        HashMap<String,QuantitaMerce> scarti = magazziniere.rimuoviScarti(registroMagazzino);
        System.out.println(ANSI_CYAN+"PRDOTTI DA SCARTARE:"+ANSI_RESET);
        System.out.println(scarti);
        registroMagazzino.rimuoviProdotti(scarti);
        System.out.println(ANSI_CYAN+"REGISTRO MAGAZZINO CON PRODOTTI SCARTATI:"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();
        //Json.salvaRegistroMagazzino(registroMagazzino);
    }
    public Merce getMerceInCucina() {
        return merceInCucina;
    }

    public Merce getMerceDaPortareInCucina() {
        return merceDaPortareInCucina;
    }

    public void setMerceInCucina(Merce merceInCucina) {
        this.merceInCucina = merceInCucina;
    }

    public void setMerceDaPortareInCucina(Merce merceDaPortareInCucina) {
        this.merceDaPortareInCucina = merceDaPortareInCucina;
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
