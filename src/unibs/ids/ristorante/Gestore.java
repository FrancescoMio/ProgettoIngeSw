package unibs.ids.ristorante;

import Libreria.InputDati;

import java.time.LocalDate;
import java.util.*;

import static Libreria.Stringhe.*;

public class Gestore extends Utente {

    public Gestore(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    public Gestore(){
        super();
    }

    /**
     * metodo per inserire piatti a mano
     * @return set di piatti
     */
    public Set<Piatto> inizializzaPiatti() {
        return inserisciPiatti();
    }

    public String getNomeRistorante() {
        String nome = InputDati.leggiStringa("Inserire nome ristorante: ");
        return nome;
    }

    public int caricoXpersona() {
        int carico = InputDati.leggiIntero("Inserire carico medio per persona:");
        return carico;
    }

    public int postiASedere() {
        int posti = InputDati.leggiIntero("Inserire posti a sedere: ");
        return posti;
    }

    /**
     * metodo per inserire piatti uno ad uno dall'utente
     *
     * @return set di piatti
     */
    private Set<Piatto> inserisciPiatti() {
        Set<Piatto> piatti = new HashSet<>();
        boolean scelta = true;
        System.out.println("Inserire di seguito i piatti che comporranno i menu del ristorante: ");
        do {
            String nome = InputDati.leggiStringaConSpazi("Inserire nome del piatto: ");
            LocalDate dataInizio = InputDati.leggiData("Inserire data di inizio validità: ");
            LocalDate dataFine = InputDati.leggiData("Inserire data di fine validità: ");
            int tempoPreparazione = InputDati.leggiIntero("Inserire tempo di preparazione in minuti: ");
            Ricetta ricetta = creaRicetta(piatti);
            Piatto piatto = new Piatto(nome, ricetta, tempoPreparazione, dataInizio, dataFine);
            piatti.add(piatto);
            scelta = InputDati.yesOrNo("Vuoi inserire un altro piatto?");
        } while (scelta);
        return piatti;
    }

    /**
     * metodo di utlita' per inserire gli ingredienti di un piatto
     *
     * @return hashmap con nome ingrediente e dose
     */
    public HashMap<String, Integer> inserisciIngredienti() {
        HashMap<String, Integer> ingredienti = new HashMap<>();
        boolean scelta = true;
        do {
            String Ingrediente = InputDati.leggiStringa("Inserire nome ingrediente: ");
            //todo: come mai avevamo fatto cosi e non double o int la dose?
            String dose = InputDati.leggiStringa("Inserire dose opportuna dell'ingrediente: ");
            ingredienti.put(Ingrediente, Integer.parseInt(dose));
            scelta = InputDati.yesOrNo("Vuoi inserire un altro ingrediente?");
        } while (scelta);
        return ingredienti;
    }

    /**
     * metodo di utilita' per controllare se la ricetta esiste già, se esiste la ritorna, altrimenti la crea e la ritorna
     *
     * @param ingredienti set di ingredienti
     * @param piatti     set di piatti
     * @return ricetta
     */
    public Ricetta controlloRicetta(HashMap<String, Integer> ingredienti, Set<Piatto> piatti) {
        for (Piatto piatto : piatti) { //controllo esistenza della ricetta
            if (piatto.getRicetta().getIngredienti().equals(ingredienti)) {
                return piatto.getRicetta();
            }
        }//se non esiste la ricetta, la creo e la ritorno per la creazione del piatto
        int numeroPorzioni = InputDati.leggiIntero("Inserire numero porzioni che derivano dalla preparazione della ricetta: ");
        double caricoXPorzione = InputDati.leggiDouble("Inserire carico di lavoro per porzione: ");//DA METTERE A POSTO, DEVE ESSERE UNA PORZIONE DI CARICO DI LAVORO PER PERSONA
        caricoXPorzione = controlloCaricoXPorzione(caricoXPorzione);
        Ricetta ricetta = new Ricetta(ingredienti, numeroPorzioni, caricoXPorzione);
        return ricetta;
    }

    //todo: da sistemare
    private double controlloCaricoXPorzione(double caricoXPorzione) {
        boolean ok = false;
        do {
            if (caricoXPorzione < 10000) {  //Ristorante.getCaricoXPersona()
                ok = true;
            } else {
                System.out.println("Carico di lavoro per porzione non valido, deve essere minore del carico di lavoro per persona");
                caricoXPorzione = InputDati.leggiDouble("Inserire carico di lavoro per porzione: ");
            }
        } while (!ok);
        return caricoXPorzione;
    }

    /**
     * Metodo per l'aggiunta di un menù tematico
     * @param piatti set di piatti
     * @return menun tematici del ristorante
     */
    public Set<MenuTematico> creaMenuTematici(Set<Piatto> piatti,int caricoLavoroPersona) {
        Set<MenuTematico> menuTematici = new HashSet<>();
        boolean scelta = true;
        do {
            String nome = InputDati.leggiStringaConSpazi(nomeMenuTematico);
            ArrayList<LocalDate> date = new ArrayList<>();
            date = inserisciDate();
            Set<Piatto> piattiDelMenu = new HashSet<>();
            piattiDelMenu = inserisciPiattiMenuTematico(piatti,caricoLavoroPersona);
            double caricoLavoroMenu = calcoloLavoroMenuTematico(piattiDelMenu);
            MenuTematico myMenu = new MenuTematico(nome, piattiDelMenu,date.get(0),date.get(1),caricoLavoroMenu);
            menuTematici.add(myMenu);
        } while(InputDati.yesOrNo(nuovoMenuTematico));
        return menuTematici;
    }

    /**
     * metodo per l'aggiunta di piatti nel menù tematico
     * @param elencoPiatti set di piatti
     * @return set di piatti del menù tematico
     */
    public Set<Piatto> inserisciPiattiMenuTematico(Set<Piatto> elencoPiatti,int caricoLavoroPersona) {
        Piatto[] piatti = elencoPiatti.toArray(new Piatto[elencoPiatti.size()]);
        Set<Piatto> piattiDelMenu = new HashSet<>();
        boolean scelta = true;
        double sommaCaricoLavoroPiatti = 0;
        do {
            int i = 1;
            int numeroPiatto;
            boolean sceltaCorretta = false;
            for (Piatto piatto : piatti) {
                System.out.println(i + "-" + piatto.getDenominazione());
                i++;
            }
            do{
                numeroPiatto = InputDati.leggiInteroPositivo(sceltaNumeroPiatto);
                if(numeroPiatto >= 1 && numeroPiatto <= piatti.length)
                    sceltaCorretta = true;
                else
                    System.err.println(erroreSceltaPiatto);
            }while (!sceltaCorretta);
            sommaCaricoLavoroPiatti += piatti[numeroPiatto - 1].getCaricoLavoro();
            if(sommaCaricoLavoroPiatti > 4/3*caricoLavoroPersona){
                System.err.println(caricoLavoroMenuTematicoNonValido);
                break;
            }
            piattiDelMenu.add(piatti[numeroPiatto - 1]);
            scelta = InputDati.yesOrNo(nuovoPiattoMenuTematico);
        } while (scelta);
        return piattiDelMenu;
    }

    /**
     * Metodc per il calcolo del carico di lavoro del menù tematico
     * @param piattiDelMenuTematico set di piatti del menù tematico
     * @return carico di lavoro del menù tematico
     */
    public double calcoloLavoroMenuTematico(Set<Piatto> piattiDelMenuTematico){
        double somma = 0;
        for (Piatto piatto: piattiDelMenuTematico)
            somma += piatto.getCaricoLavoro();
        return somma;
    }

    /**
     * metodo per l'inserimento delle date in cui è disponibile un menù tematico
     * @return arraylist di date
     */
    public ArrayList<LocalDate> inserisciDate(){
        ArrayList<LocalDate> date = new ArrayList<>();
        LocalDate dataInizio = InputDati.leggiData(inserisciDataInizio);
        LocalDate dataFine = InputDati.leggiData(inserisciDataFine);
        date.add(dataInizio);
        date.add(dataFine);
        return date;
    }
    public void visualizzaMenuTematici(Set<MenuTematico> menuTematici){
        System.out.println(lineSeparator);
        System.out.println("MENU' TEMATICI:");
        for (MenuTematico menu: menuTematici)
            System.out.println(menu.toString());

    }

    public void visualizzaMenuAllaCarta(MenuCarta menuAllaCarta){
        System.out.println(lineSeparator);
        System.out.println("MENU' ALLA CARTA: ");
        System.out.println(menuAllaCarta.toString());
    }

    public void visualizzaMagazzino(RegistroMagazzino magazzino){
        magazzino.visualizzaMagazzino();
    }

    /**
     * metodo per l'inserimento di bevande e generi alimentari extra
     * @return set di bevande e generi alimentari extra
     */
    public Set<Raggruppabile> inizializzaBevandeEgeneri(){
        Set<Raggruppabile> insieme = new HashSet<>();
        System.out.println(lineSeparator);
        System.out.println("CONFIGURAZIONE INSIEME BEVANDE E GENERI ALIMENTARI EXTRA:");
        boolean nuovo = true;
        boolean scletaBevanda;
        String nome = "";
        do {
            scletaBevanda = InputDati.BevandaOGenere("Inserire bevanda o genere alimentare?");
            if(scletaBevanda){
                nome = InputDati.leggiStringaNonVuota("Inserire nome bevanda: ");
                Bevanda bevanda = new Bevanda(nome);
                insieme.add(bevanda);
            }
            else{
                nome = InputDati.leggiStringaNonVuota("Inserire nome genere alimentare extra: ");
                GenereAlimentareExtra genereExtra = new GenereAlimentareExtra(nome);
                insieme.add(genereExtra);
            }
            nuovo = InputDati.yesOrNo(nuovaBevandaOgenere);
        } while (nuovo);
        return insieme;
    }

    /**
     * Metodo per l'inizializzazione delle bevande e generi alimentari extra
     * hashMapBevande contiene i consumi di tutte le bevande passate come parametro
     * hashMapGeneri contiene i consumi di tutti generi passati come parametro
     * @param bevande set di bevande
     * @param generi set di generi alimentari extra
     * @return arraylist di hashMap contenenti i consumi di bevande e generi alimentari extra
     */
    public ArrayList<HashMap<Raggruppabile,QuantitaMerce>> inizializzaConsumi(Set<Bevanda> bevande, Set<GenereAlimentareExtra> generi){
        ArrayList<HashMap<Raggruppabile,QuantitaMerce>> consumi = new ArrayList<>();
        HashMap<Raggruppabile,QuantitaMerce> hashMapBevande = new HashMap<>();
        HashMap<Raggruppabile,QuantitaMerce> hashMapGeneri = new HashMap<>();
        System.out.println(lineSeparator);
        System.out.println("CONFIGURAZIONE CONSUMO PRO CAPITE BEVANDE:");
        for (Bevanda bevanda: bevande) {
            double quantita = InputDati.leggiDoubleConMinimo("Consumo pro capite '" + bevanda.getNome() + "' (l) : ",0.0);
            QuantitaMerce quantitaMerce = new QuantitaMerce(quantita, "l");
            hashMapBevande.put(bevanda,quantitaMerce);
        }
        System.out.println("CONFIGURAZIONE CONSUMO PRO CAPITE GENERI ALIMENTARI EXTRA:");
        for (GenereAlimentareExtra genere: generi) {
            double quantita = InputDati.leggiDoubleConMinimo("Consumo pro capite '" + genere.getNome() + "' (hg) : ",0.0);
            QuantitaMerce quantitaMerce = new QuantitaMerce(quantita, "hg");
            hashMapGeneri.put(genere,quantitaMerce);
        }
        consumi.add(hashMapBevande);
        consumi.add(hashMapGeneri);
        return consumi;
    }
    private Ricetta creaRicetta(Set<Piatto> piatti){
        HashMap<String,Integer> ingredienti = inserisciIngredienti();
        Ricetta ricetta = controlloRicetta(ingredienti, piatti);
        return ricetta;
    }

    public void setGestore(String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
    }

    public void visualizzaCaricoLavoroPersona(int caricoLavoroPersona){
        System.out.println(ANSI_CYAN + "Carico lavoro per persona: " + ANSI_RESET + caricoLavoroPersona);
    }

    public void visualizzaPostiSedere(int posti){
        System.out.println(ANSI_CYAN + "Posti a sedere disponibili nel ristorante: " + ANSI_RESET + posti);
    }
    public void visualizzaInsiemeBevande(Set<Bevanda> bevande){
        System.out.println(ANSI_CYAN + "Insieme delle bevande:" + ANSI_RESET);
        for (Bevanda bevanda : bevande)
            System.out.println(bevanda);
    }
    public void visualizzaInsiemeGeneri(Set<GenereAlimentareExtra> generi){
        System.out.println(ANSI_CYAN + "Insieme dei generi alimentari extra:" + ANSI_RESET);
        for (GenereAlimentareExtra genere : generi)
            System.out.println(genere);
    }
    public void visualizzaConsumoProCapite(Consumo consumo){
        System.out.println(consumo.getConsumo());
    }

    public void visualizzaPiattoRicetta(Set<Piatto> piatti){
        for (Piatto piatto: piatti)
            System.out.println(piatto);
    }

    public void visualizzaPiattieValidita(Set<Piatto> piatti){
        for (Piatto piatto : piatti) {
            System.out.println(lineSeparator);
            System.out.println("- Denominazione: " + piatto.getDenominazione());
            System.out.println("- Periodo validità: " + piatto.getDataInizio() + " -> " + piatto.getDataFine());
        }
    }

    public void visualizzaRicette(Set<Piatto> piatti){
        for(Piatto piatto : piatti){
            System.out.println(lineSeparator);
            System.out.println(piatto.getRicetta());
        }
    }

}
