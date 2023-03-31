package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.MyUtil;

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
    public Set<Piatto> inizializzaPiatti(Set<Piatto> piatti, int caricoLavoroXpersona) {
        return inserisciPiatti(piatti, caricoLavoroXpersona);
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
     * @return set di piatti
     * @param piattiDelMenu set di piatti del ristorante
     * @param caricoLavoroXpersona carico di lavoro per persona
     * precondizione: caricoLavoroXpersona > 0, piattiDelMenu != null
     * postcondizione: piatti != null
     */
    private Set<Piatto> inserisciPiatti(Set<Piatto> piattiDelMenu, int caricoLavoroXpersona) {
        Set<Piatto> piatti = new HashSet<>();
        boolean scelta = true;
        System.out.println("Inserire di seguito i piatti che faranno parte dei menu del ristorante: ");
        do {
            boolean presente = true;
            String nomePiatto = "";
            do{
                nomePiatto = InputDati.leggiStringaConSpazi("Inserire nome del piatto: ");
                if(!piattoGiaPresente(nomePiatto,piattiDelMenu))
                    presente = false;
                else System.err.println("Piatto già presente nel ristorante!");
            }while(presente);
            ArrayList<LocalDate> date = new ArrayList<>();
            date = inserisciDate();
            int tempoPreparazione = InputDati.leggiIntero("Inserire tempo di preparazione in minuti: ");
            Ricetta ricetta = creaRicetta(caricoLavoroXpersona);
            Piatto piatto = new Piatto(nomePiatto, ricetta, tempoPreparazione, date.get(0), date.get(1));
            piatti.add(piatto);
            scelta = InputDati.yesOrNo("Vuoi inserire un altro piatto?");
        } while (scelta);
        return piatti;
    }

    /**
     * metodo di utlita' per inserire gli ingredienti di un piatto
     *
     * @return hashmap con nome ingrediente e dose
     * postcondizione: ingredienti != null
     */
    public HashMap<String, QuantitaMerce> inserisciIngredienti() {
        HashMap<String, QuantitaMerce> ingredienti = new HashMap<>();
        boolean scelta = true;
        do {
            String nomIngrediente = InputDati.leggiStringa("Inserire nome ingrediente: ");
            double dose = InputDati.leggiDoubleConMinimo("Inserire dose opportuna dell'ingrediente: ",0);
            boolean unitaMisuraGiusta = false;
            String unitaMisura = "";
            do{
                unitaMisura = InputDati.leggiStringaNonVuota("Inserire unità di misura: ");
                if(MyUtil.controlloUnitaMisura(unitaMisura))
                    unitaMisuraGiusta = true;
                else System.err.println("Unità di misura inserita non valida!");
            }while (!unitaMisuraGiusta);
            QuantitaMerce quantitaIngrediente = new QuantitaMerce(dose,unitaMisura);
            ingredienti.put(nomIngrediente,quantitaIngrediente);
            scelta = InputDati.yesOrNo("Vuoi inserire un altro ingrediente?");
        } while (scelta);
        return ingredienti;
    }

    /**
     * metodo di utilità per controllare che il carico di lavoro per porzione sia una frazione del carico di lavoro per persona
     * @param caricoXPorzione carico di lavoro per porzione
     * @param caricoLavoroXpersona carico di lavoro per persona
     * @return carico di lavoro per porzione
     * precondizione: caricoXPorzione > 0, caricoLavoroXpersona > 0
     */
    private double controlloCaricoXPorzione(double caricoXPorzione, int caricoLavoroXpersona) {
        boolean ok = false;
        do {
            if (caricoXPorzione < caricoLavoroXpersona) {
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
     * precondizione: piatti != null, caricoLavoroPersona > 0, menuTematiciPresenti != null
     * postcondizione: menuTematici != null
     */
    public Set<MenuTematico> creaMenuTematici(Set<Piatto> piatti,int caricoLavoroPersona,Set<MenuTematico> menuTematiciPresenti) {
        Set<MenuTematico> menuTematici = new HashSet<>();
        do {
            boolean presente = true;
            String nomeMenu = "";
            do{
                nomeMenu = InputDati.leggiStringaConSpazi(nomeMenuTematico);
                if(!menuGiaPresente( nomeMenu,menuTematiciPresenti))
                    presente = false;
                else System.err.println("Menù tematico già presente nel ristorante!");
            }while(presente);
            ArrayList<LocalDate> date = new ArrayList<>();
            date = inserisciDate();
            Set<Piatto> piattiDelMenu = new HashSet<>();
            piattiDelMenu = inserisciPiattiMenuTematico(piatti,caricoLavoroPersona);
            double caricoLavoroMenu = calcoloLavoroMenuTematico(piattiDelMenu);
            MenuTematico myMenu = new MenuTematico( nomeMenu, piattiDelMenu,date.get(0),date.get(1),caricoLavoroMenu);
            menuTematici.add(myMenu);
        } while(InputDati.yesOrNo(nuovoMenuTematico));
        return menuTematici;
    }

    /**
     * metodo di utilità per controllare che il menù tematico non sia già presente nel ristorante
     * @param nome nome del menù tematico
     * @param menuTematiciPresenti lista dei menù tematici presenti nel ristorante
     * @return true se il menù è già presente, false altrimenti
     * precondizione: nome != null, menuTematiciPresenti != null
     */
    private boolean menuGiaPresente(String nome, Set<MenuTematico> menuTematiciPresenti){
        for(MenuTematico menuTematico : menuTematiciPresenti){
            if(menuTematico.getNomeMenu().equalsIgnoreCase(nome))
                return true;
        }
        return false;
    }

    /**
     * metodo di utilità per controllare che il piatto non sia già presente nel menù
     * @param nome nome del piatto
     * @param piattiPresenti lista dei piatti presenti nel menù
     * @return true se il piatto è già presente, false altrimenti
     * precondizione: nome != null, piattiPresenti != null
     */
    private boolean piattoGiaPresente(String nome, Set<Piatto> piattiPresenti){
        for(Piatto piatto : piattiPresenti){
            if(piatto.getNome().equalsIgnoreCase(nome))
                return true;
        }
        return false;
    }


    /**
     * metodo per l'aggiunta di piatti nel menù tematico
     * @param elencoPiatti set di piatti
     * @return set di piatti del menù tematico
     * precondizione: elencoPiatti != null, caricoLavoroPersona > 0
     * postcondizione: piattiDelMenu != null
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
     * precondizione: piattiDelMenuTematico != null
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
        LocalDate dataInizio = null;
        LocalDate dataFine = null;
        boolean dataIGiusta = false;
        do{
            dataInizio = InputDati.leggiData(inserisciDataInizio);
            if(MyUtil.controlloDataIniziale(dataInizio))
                dataIGiusta = true;
            else System.err.println("Data inserita non valida!");
        }while (!dataIGiusta);
        boolean dataFGiusta = false;
        do{
            dataFine = InputDati.leggiData(inserisciDataFine);
            if(MyUtil.controlloDataFinale(dataFine,dataInizio))
                dataFGiusta = true;
            else System.err.println("Data inserita non valida!");
        }while (!dataFGiusta);
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

    public int setCaricoLavoroPersona(){
        int caricoLavoroPersona = InputDati.leggiInteroPositivo("Inserire carico lavoro per persona: ");
        return caricoLavoroPersona;
    }

    public int setPostiAsedere(){
        int posti = InputDati.leggiInteroPositivo("Inserire numero di posti a sedere del ristorante: ");
        return posti;
    }

    /**
     * metodo di creazione di una nuova bevanda
     * @param bevande set di bevande
     * @return nuova bevanda
     * precondizione: bevande != null
     */
    public Bevanda creaBevanda(Set<Bevanda> bevande){
        boolean presente = true;
        String nome = "";
        do{
            nome = InputDati.leggiStringaConSpazi("Inserire nome della bevanda: ");
            if(!bevandaPresente(nome,bevande))
                presente = false;
            else System.err.println("Bevanda già presente nel magazzino!");
        }while (presente);
        Bevanda bevanda = new Bevanda(nome);
        return bevanda;
    }

    /**
     * metodo di creazione di un nuovo genere alimentare extra
     * @param generi set di generi alimentari extra
     * @return nuovo genere alimentare extra
     * precondizione: generi != null
     */
    public GenereAlimentareExtra creaGenereAlimentare(Set<GenereAlimentareExtra> generi){
        boolean presente = true;
        String nome = "";
        do{
            nome = InputDati.leggiStringaConSpazi("Inserire nome del genere alimentare extra: ");
            if(!generePresente(nome,generi))
                presente = false;
            else System.err.println("Genere già presente nel magazzino!");
        }while (presente);
        GenereAlimentareExtra genere = new GenereAlimentareExtra(nome);
        return genere;
    }

    /**
     * metodo per controllare che una nuova bevanda creata non sia uguale ad una già presente
     * @param nome nome della bevanda
     * @param bevande set di bevande
     * @return true se la bevanda è già presente, false altrimenti
     * precondizione: bevande != null
     */
    private boolean bevandaPresente(String nome , Set<Bevanda> bevande){
        for(Bevanda bevanda : bevande){
            if(bevanda.getNome().equalsIgnoreCase(nome))
                return true;
        }
        return false;
    }

    /**
     * metodo per controllare che un nuovo genere alimentare extra creato non sia uguale ad uno già presente
     * @param nome nome del genere alimentare extra
     * @param generi set di generi alimentari extra
     * @return true se il genere è già presente, false altrimenti
     * precondizione: generi != null
     */
    private boolean generePresente(String nome , Set<GenereAlimentareExtra> generi){
        for(GenereAlimentareExtra genere : generi){
            if(genere.getNome().equalsIgnoreCase(nome))
                return true;
        }
        return false;
    }

    public QuantitaMerce creaConsumoProCapiteBevanda(String nome){
        double quantita = InputDati.leggiDoubleConMinimo("Consumo pro capite '" + nome + "' (l) : ",0.0);
        QuantitaMerce quantitaMerce = new QuantitaMerce(quantita, "l");
        return quantitaMerce;
    }
    public QuantitaMerce creaConsumoProCapiteGenere(String nome){
        double quantita = InputDati.leggiDoubleConMinimo("Consumo pro capite '" + nome + "' (hg) : ",0.0);
        QuantitaMerce quantitaMerce = new QuantitaMerce(quantita, "hg");
        return quantitaMerce;
    }

    /**
     * metodo per l'inserimento di bevande e generi alimentari extra
     * @return set di bevande e generi alimentari extra
     * postcondizione: insieme != null
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
     * precondizione: bevande != null, generi != null
     * postcondizione: consumi != null
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


    /**
     * metodo per la creazione di una nuova ricetta
     * @return Ricetta
     * precondizione: caricoLavoroXpersona > 0, ingredienti != null
     */
    private Ricetta creaRicetta(int caricoLavoroXpersona){
        HashMap<String,QuantitaMerce> ingredienti = inserisciIngredienti();
        int numeroPorzioni = InputDati.leggiIntero("Inserire numero porzioni che derivano dalla preparazione della ricetta: ");
        double caricoXPorzione = InputDati.leggiDouble("Inserire carico di lavoro per porzione: ");
        caricoXPorzione = controlloCaricoXPorzione(caricoXPorzione, caricoLavoroXpersona);
        Ricetta ricetta = new Ricetta(ingredienti, numeroPorzioni, caricoXPorzione);
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
    public void visualizzaConsumoProCapite(Consumo consumo, String tipologia){
        System.out.println(lineSeparator);
        System.out.println(tipologia);
        System.out.println(consumo);
        System.out.println(lineSeparator);
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
