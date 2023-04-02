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
        prenotazioni = new ArrayList<>();
        creaGestore();
        creaConfigurazione();
        creaMenuTematici();
        creaMenuCarta();
        creaInsiemeBevandeEGeneri();
        creaConsumoProCapite();
        creaAddettoPrenotazioni();
        creaMagazziniere();
        caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
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
        caricoDiLavoroXPersona = gestore.caricoXpersona();
        piatti = gestore.inizializzaPiatti(piatti,caricoDiLavoroXPersona);
    }

    public void creaMenuTematici(){
        System.out.println(lineSeparator);
        System.out.println(creazioneMenuTematici);
        menuTematici = new HashSet<>();
        menuTematici = gestore.creaMenuTematici(piatti,caricoDiLavoroXPersona,menuTematici);
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
                    break;
                case 4:
                    gestore.visualizzaInsiemeGeneri(generiAlimentariExtra);
                    break;
                case 5:
                    gestore.visualizzaConsumoProCapite(consumoProCapiteBevande,"CONSUMO PRO CAPITE BEVANDE");
                    break;
                case 6:
                    gestore.visualizzaConsumoProCapite(consumoProCapiteGeneriExtra, "CONSUMO PRO CAPITE GENERI ALIMENTARI EXTRA");
                    break;
                case 7:
                    gestore.visualizzaPiattoRicetta(piatti);
                    break;
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


    public void impostaCaricoDiLavoroXPersona(){
        this.caricoDiLavoroXPersona = gestore.setCaricoLavoroPersona();
        Json.salvaConfigurazione(this,piatti);
    }

    public void impostaPostiAsedere(){
        this.postiASedere = gestore.setPostiAsedere();
        Json.salvaConfigurazione(this,piatti);
    }

    /**
     * metodo che aggiunge una bevanda a quelle disponibili nel ristorante
     */
    public void addBevanda(){
        Bevanda bevanda = gestore.creaBevanda(bevande);
        QuantitaMerce quantitaConsumo = gestore.creaConsumoProCapiteBevanda(bevanda.getNome());
        bevande.add(bevanda);
        consumoProCapiteBevande.addConsumo(bevanda,quantitaConsumo);
        Json.salvaConsumiProCapite(consumoProCapiteBevande,consumoProCapiteGeneriExtra);
    }

    /**
     * metodo che aggiunge un genere alimentare extra a quelli disponibili nel ristorante
     */
    public void addGenereAlimentareExtra(){
        GenereAlimentareExtra genereAlimentareExtra = gestore.creaGenereAlimentare(generiAlimentariExtra);
        QuantitaMerce quantitaConsumo = gestore.creaConsumoProCapiteGenere(genereAlimentareExtra.getNome());
        generiAlimentariExtra.add(genereAlimentareExtra);
        consumoProCapiteGeneriExtra.addConsumo(genereAlimentareExtra,quantitaConsumo);
        Json.salvaConsumiProCapite(consumoProCapiteBevande,consumoProCapiteGeneriExtra);
    }

    /**
     * metodo che aggiunge un piatto a quelli disponibili nel menù alla carta
     */
    public void addPiatto(){
        Set<Piatto> piattiNuovi = gestore.inizializzaPiatti(piatti,caricoDiLavoroXPersona);
        piatti.addAll(piattiNuovi);
        Json.salvaConfigurazione(this,piatti);
    }

    /**
     * metodo che aggiunge un menu tematico a quelli disponibili nel ristorante
     */
    public void addMenuTematico(){
        Set<MenuTematico> nuoviMenuTematici = gestore.creaMenuTematici(piatti,caricoDiLavoroXPersona,menuTematici);
        this.menuTematici.addAll(nuoviMenuTematici);
        Json.salvaMenuTematici(menuTematici);
    }

    /**
     * metodo che crea la lista della spesa giornaliera e la aggiunge al registro magazzino
     */
    public void creaListaSpesa(){
        ArrayList<Prenotazione> prenotazioniGiornaliere = magazziniere.filtraPrenotazioniGiornaliere(prenotazioni);
        if(!prenotazioniGiornaliere.isEmpty()){
            magazziniere.creaListaSpesaGiornaliera(prenotazioniGiornaliere,registroMagazzino,consumoProCapiteBevande,consumoProCapiteGeneriExtra);
            Merce listaSpesa = magazziniere.getListaSpesa();
            registroMagazzino.aggiungiArticoliComprati(listaSpesa);
            System.out.println("\nLISTA DELLA SPESA:");
            listaSpesa.visualizzaMerce();
            merceDaPortareInCucina = magazziniere.portaIngredientiInCucina(prenotazioniGiornaliere);
            System.out.println("\nREGISTRO MAGAZZINO AGGIORNATO CON ARTICOLI COMPRATI:");
            registroMagazzino.getArticoliDisponibili().visualizzaMerce();
            Json.salvaRegistroMagazzino(registroMagazzino);
            Json.salvaCucina(merceDaPortareInCucina,merceInCucina);
            Json.salvaListaSpesa(listaSpesa);
        }else System.out.println("NESSUNA LISTA DELLA SPESA CREATA PERCHE' NON ESISTONO PRENOTAZIONI PER LA DATA ODIERNA!");
    }

    /**
     * metodo che simula il flusso di ingredienti dal magazzino alla cucina, e salva tutti i valori nei file json
     */
    public void portaIngredientiInCucina() {
        merceDaPortareInCucina.togliProdottiQuantitaZero();
        HashMap<String, QuantitaMerce> prodottiDaPortareInCucina = merceDaPortareInCucina.getArticoli();
        HashMap<String, QuantitaMerce> ingredientiDaAggiungere = new HashMap<>();
        if (prodottiDaPortareInCucina.isEmpty())
            System.out.println("\nNessun prodotto da portare in cucina!");
        else {
            do {
                System.out.println(lineSeparator);
                System.out.println("---INGREDIENTI DA PORTARE IN CUCINA---");
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
                double quantitaDaPortare = InputDati.leggiDoubleCompreso("Inserire quantità ingrediente da portare in cucina (" + unitaMisura + "): ", 0, quantitaMax);
                QuantitaMerce quantitaIngredienteDaPortare = new QuantitaMerce(quantitaDaPortare, unitaMisura);
                QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaMax - quantitaDaPortare, unitaMisura);
                prodottiDaPortareInCucina.replace(nomeIngrediente, quantitaIngrediente, quantitaAggiornata);
                if (ingredientiDaAggiungere.containsKey(nomeIngrediente)) {
                    QuantitaMerce quantitaProdottoOld = ingredientiDaAggiungere.get(nomeIngrediente);
                    double quantitaOld = quantitaProdottoOld.getQuantita();
                    QuantitaMerce quantitaAggiornataDaAggiungere = new QuantitaMerce(quantitaDaPortare + quantitaOld, unitaMisura);
                    ingredientiDaAggiungere.put(nomeIngrediente, quantitaAggiornataDaAggiungere);
                } else ingredientiDaAggiungere.put(nomeIngrediente, quantitaIngredienteDaPortare);
            } while (InputDati.yesOrNo("Portare un altro ingrediente in cucina?"));
            merceInCucina.aggiungiIngredienti(ingredientiDaAggiungere);
            System.out.println("\nMERCE PRESENTE IN CUCINA:");
            merceInCucina.visualizzaMerce();
            registroMagazzino.rimuoviProdotti(ingredientiDaAggiungere);
            registroMagazzino.getArticoliDisponibili().togliProdottiQuantitaZero();
            System.out.println("\nREGISTRO MAGAZZINO AGGIORNATO:");
            registroMagazzino.getArticoliDisponibili().visualizzaMerce();
            Json.salvaRegistroMagazzino(registroMagazzino);
            Json.salvaCucina(merceDaPortareInCucina, merceInCucina);
        }
    }

    /**
     * metodo che simula il flusso di bevande e generi extra dal magazzino alla sala, e salva tutti i valori nei file json
     */
    public void portaBevandaGenereInSala(){
        if(!registroMagazzino.getArticoliDisponibili().getArticoli().isEmpty()){
            Set<Raggruppabile> bevandeEGeneri = new HashSet<>();
            bevandeEGeneri.addAll(bevande);
            bevandeEGeneri.addAll(generiAlimentariExtra);
            HashMap<String, QuantitaMerce> prodottiInSala = magazziniere.portaBevandaGenereInSala(registroMagazzino,bevandeEGeneri);
            registroMagazzino.rimuoviProdotti(prodottiInSala);
            registroMagazzino.getArticoliDisponibili().togliProdottiQuantitaZero();
            System.out.println("\nREGISTRO MAGAZZINO AGGIORNATO:");
            registroMagazzino.getArticoliDisponibili().visualizzaMerce();
            Json.salvaRegistroMagazzino(registroMagazzino);
        }else System.out.println("NON E' POSSIBILIE PORTARE BEVANDE/GENERI ALIMENTARI EXTRA IN SALA PERCHE' IL MAGAZZINO E' VUOTO!");
    }

    /**
     * metodo che simula il flusso di prodotti non consumati dalla cucina alla sala, e salva tutti i valori nei file json
     */
    public void riportaInMagazzinoNonConsumati(){
        merceInCucina.togliProdottiQuantitaZero();
        if(!merceInCucina.getArticoli().isEmpty()){
            HashMap<String,QuantitaMerce> prodottiDaRiportare = magazziniere.riportaInMagazzino(merceInCucina);
            System.out.println("MERCE RIMASTA IN CUCINA:");
            merceInCucina.togliProdottiQuantitaZero();
            merceInCucina.visualizzaMerce();
            registroMagazzino.riportaProdotti(prodottiDaRiportare);
            System.out.println("REGISTRO MAGAZZINO AGGIORNATO:");
            registroMagazzino.getArticoliDisponibili().visualizzaMerce();
            Json.salvaRegistroMagazzino(registroMagazzino);
            Json.salvaCucina(merceDaPortareInCucina,merceInCucina);
        }else System.out.println("NESSUN INGREDIENTE PRESENTE IN CUCINA!");
    }

    /**
     * metodo che simula il flusso di prodotti scartati dal magazzino a causa di scadenza, e salva tutti i valori nei file json
     */
    public void rimuoviScartiDalMagazzino(){
        registroMagazzino.getArticoliDisponibili().togliProdottiQuantitaZero();
        if(!registroMagazzino.getArticoliDisponibili().getArticoli().isEmpty()){
            HashMap<String,QuantitaMerce> scarti = magazziniere.rimuoviScarti(registroMagazzino);
            registroMagazzino.rimuoviProdotti(scarti);
            registroMagazzino.getArticoliDisponibili().togliProdottiQuantitaZero();
            System.out.println("\nREGISTRO MAGAZZINO AGGIORNATO:");
            registroMagazzino.getArticoliDisponibili().visualizzaMerce();
            Json.salvaRegistroMagazzino(registroMagazzino);
        }else System.out.println("NESSUN SCARTO DA RIMUOVERE: MAGAZZINO VUOTO!");
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
