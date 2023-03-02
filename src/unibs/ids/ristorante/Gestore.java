package unibs.ids.ristorante;

import Libreria.InputDati;

import java.time.LocalDate;
import java.util.*;

import static unibs.ids.ristorante.Stringhe.*;

public class Gestore extends Utente {

    public Gestore(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    /**
     * metodo per inserire piatti a mano
     * @return
     */
    public Set<Piatto> inizializzaPiatti(Set<Ricetta> ricette) {
        return inserisciPiatti(ricette);
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
     * @return
     */
    public Set<Piatto> inserisciPiatti(Set<Ricetta> ricette) {
        Set<Piatto> piatti = new HashSet<>();
        boolean scelta = true;
        do {
            String nome = InputDati.leggiStringaConSpazi("Inserire nome del piatto: ");
            int tempoPreparazione = InputDati.leggiIntero("Inserire tempo di preparazione: ");
            String nomeRicetta = InputDati.leggiStringa("Inserire nome ricetta: ");
            //todo: controllare che vada tutto bene
            Ricetta ricettaEsistente = controlloRicetta(nomeRicetta, ricette);//se ricetta esiste già, uso quella già presente, altrimenti la creo

            Piatto piatto = new Piatto(nome, nomeRicetta, tempoPreparazione, ricettaEsistente.getCaricoLavoro());
            piatti.add(piatto);
            scelta = InputDati.yesOrNo("Vuoi inserire un altro piatto?");
        } while (scelta);
        return piatti;
    }

    /**
     * metodo di utlita' per inserire gli ingredienti di un piatto
     *
     * @return
     */
    public HashMap<String, Integer> inserisciIngredienti() {
        HashMap<String, Integer> ingredienti = new HashMap<>();
        boolean scelta = true;
        do {
            String Ingrediente = InputDati.leggiStringa("Inserire nome ingrediente: ");
            String dose = InputDati.leggiStringa("Inserire dose opportuna dell'ingrediente: ");
            ingredienti.put(Ingrediente, Integer.parseInt(dose));
            scelta = InputDati.yesOrNo("Vuoi inserire un altro ingrediente?");
        } while (scelta);
        return ingredienti;
    }

    /**
     * metodo di utilita' per controllare se la ricetta esiste già, se esiste la ritorna, altrimenti la crea e la ritorna
     *
     * @param nome
     * @param ricette
     * @return
     */
    public Ricetta controlloRicetta(String nome, Set<Ricetta> ricette) {
        for (Ricetta ricetta : ricette) {
            if (ricetta.getNome().equals(nome)) {
                return ricetta;
            }
        }
        System.out.println("Ricetta non trovata, inserisci i dati per aggiungerla alla lista delle ricette: ");
        //se non esiste la ricetta, la creo e la ritorno per la creazione del piatto
        Ricetta ricetta = creaRicetta();
        ricette.add(ricetta);
        return ricetta;
    }

    //da sistemare
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
     * @param piattiDisponibili
     * @return
     */
    public Set<MenuTematico> creaMenuTematici(Set<Piatto> piattiDisponibili) {
        Set<MenuTematico> menuTematici = new HashSet<>();
        boolean scelta = true;
        do {
            String nome = InputDati.leggiStringaConSpazi(nomeMenuTematico);
            ArrayList<LocalDate> date = new ArrayList<>();
            date = inserisciDate();
            Set<Piatto> piattiDelMenu = new HashSet<>();
            piattiDelMenu = inserisciPiattiMenuTematico(piattiDisponibili);
            double caricoLavoroMenu = calcoloLavoroMenuTematico(piattiDelMenu);
            MenuTematico myMenu = new MenuTematico(nome, piattiDelMenu,date.get(0),date.get(1),caricoLavoroMenu);
            menuTematici.add(myMenu);
            scelta = InputDati.yesOrNo(nuovoMenuTematico);
        } while (scelta);
        return menuTematici;
    }

    /**
     * metodo per l'aggiunta di piatti nel menù tematico
     * @param piattiDisponibili
     * @return
     */
    public Set<Piatto> inserisciPiattiMenuTematico(Set<Piatto> piattiDisponibili) {
        Piatto[] piatti = piattiDisponibili.toArray(new Piatto[piattiDisponibili.size()]);
        Set<Piatto> piattiDelMenu = new HashSet<>();
        boolean scelta = true;
        double sommaCaricoLavoroPiatti = 0;
        do {
            int i = 1;
            for (Piatto piatto : piatti) {
                System.out.println(i + "-" + piatto.getDenominazione());
                i++;
            }
            int numeroPiatto = InputDati.leggiIntero(sceltaNumeroPiatto);
            sommaCaricoLavoroPiatti += piatti[numeroPiatto - 1].getCaricoLavoro();
            if(sommaCaricoLavoroPiatti > 4/3*Ristorante.getCaricoXPersona()){
                System.err.println(Stringhe.caricoLavoroMenuTematicoNonValido);
                break;
            }
            piattiDelMenu.add(piatti[numeroPiatto - 1]);
            scelta = InputDati.yesOrNo(nuovoPiattoMenuTematico);
        } while (scelta);
        return piattiDelMenu;
    }

    /**
     * Metodc per il calcolo del carico di lavoro del menù tematico
     * @param piattiDelMenuTematico
     * @return
     */
    public double calcoloLavoroMenuTematico(Set<Piatto> piattiDelMenuTematico){
        double somma = 0;
        for (Piatto piatto: piattiDelMenuTematico)
            somma += piatto.getCaricoLavoro();
        return somma;
    }

    /**
     * metodo per l'inserimento delle date in cui è disponibile un menù tematico
     * @return
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
        System.out.println("MENU' TEMATICI: ");
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

    public Consumo inizializzaBevande(RegistroMagazzino magazzino){
        System.out.println("Le chiediamo di seguito di inserire le bevande presenti nel suo ristorante");
        while(InputDati.yesOrNo("Vuole inserire una nuova bevanda?")){
            String nome = InputDati.leggiStringaConSpazi("Inserire il nome della bevanda: ");
            int quantita = InputDati.leggiIntero("Inserire la quantità di bevanda tipicamente consumata: ");
            Bevanda bevanda = new Bevanda(nome);
            QuantitaMerce quantitaMerce = new QuantitaMerce(quantita, "l");
            Consumo consumoBevande = new Consumo(bevanda, quantitaMerce);
            //da finire, bisogna decidere come gestire maagzzino e bevande+ extra
            //magazzino.aggiungiBevanda(new Bevanda(nome,consumoBevande));
        }
        return null;//da togliere
    }

    public Set<Ricetta> inizializzaRicette(){
        Set<Ricetta> ricette = new HashSet<>();
        System.out.println("Le chiediamo di seguire le seguenti istruzioni per l'inserimento delle ricette");
        do{
            String nome = InputDati.leggiStringaConSpazi("Inserire il nome della ricetta: ");

            if(controlloNome(nome, ricette)){
                System.out.println("Ricetta già presente, se ne vuoi creare una simile dai un nome diverso: ");
                nome = InputDati.leggiStringaConSpazi("Inserire il nome della ricetta: ");
            }
            HashMap<String,Integer> ingredienti = inserisciIngredienti();

            int numeroPorzioni = InputDati.leggiIntero("Inserire il numero di porzioni tipicamente consumate: ");

            double caricoXPorzione = InputDati.leggiDouble("Inserire il carico di lavoro per porzione: ");
            caricoXPorzione = controlloCaricoXPorzione(caricoXPorzione);

            Ricetta ricetta = new Ricetta(nome,ingredienti,numeroPorzioni,caricoXPorzione);
            ricette.add(ricetta);
        }while(InputDati.yesOrNo("Vuole inserire una nuova ricetta?"));
        return ricette;
    }

    private boolean controlloNome(String nome, Set<Ricetta> ricette) {
        for(Ricetta ricetta: ricette){
            if(ricetta.getNome().equals(nome))
                return true;
        }
        return false;
    }

    private Ricetta creaRicetta(){
        String nome = InputDati.leggiStringaConSpazi("Inserire il nome della ricetta: ");
        HashMap<String,Integer> ingredienti = inserisciIngredienti();
        int numeroPorzioni = InputDati.leggiIntero("Inserire il numero di porzioni tipicamente consumate: ");
        double caricoXPorzione = InputDati.leggiDouble("Inserire il carico di lavoro per porzione: ");
        caricoXPorzione = controlloCaricoXPorzione(caricoXPorzione);
        Ricetta ricetta = new Ricetta(nome,ingredienti,numeroPorzioni,caricoXPorzione);
        return ricetta;
    }

}
