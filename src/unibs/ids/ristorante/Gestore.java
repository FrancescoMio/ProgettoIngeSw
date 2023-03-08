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
     * @return
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
     * @return
     */
    public Set<Piatto> inserisciPiatti() {
        Set<Piatto> piatti = new HashSet<>();
        boolean scelta = true;
        do {
            String nome = InputDati.leggiStringaConSpazi("Inserire nome del piatto: ");
            LocalDate dataInizio = InputDati.leggiData("Inserire data di inizio validità: ");
            LocalDate dataFine = InputDati.leggiData("Inserire data di fine validità: ");
            int tempoPreparazione = InputDati.leggiIntero("Inserire tempo di preparazione: ");
            //todo: controllare che vada tutto bene
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
     * @param ingredienti
     * @param piatti
     * @return
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
     * @param piatti
     * @return
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
            scelta = InputDati.yesOrNo(nuovoMenuTematico);
        } while (scelta);
        return menuTematici;
    }

    /**
     * metodo per l'aggiunta di piatti nel menù tematico
     * @param elencoPiatti
     * @return
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

    public Set<Bevanda> inizializzaBevande(){
        Set<Bevanda> bevande = new HashSet<>();
        System.out.println(lineSeparator);
        System.out.println("CONFIGURAZIONE INSIEME BEVANDE:");
        boolean scelta = true;
        do {
            String nomeBevanda = InputDati.leggiStringaNonVuota("Inserire nome bevanda: ");
            Bevanda bevanda = new Bevanda(nomeBevanda);
            bevande.add(bevanda);
            scelta = InputDati.yesOrNo(nuovaBevanda);
        } while (scelta);
        return bevande;
    }

    public ConsumoProCapiteBevande inizializzaConsumoBevande(Set<Bevanda> bevande){
        ConsumoProCapiteBevande consumoProCapiteBevande = new ConsumoProCapiteBevande();
        HashMap<Raggruppabile,QuantitaMerce> hashMapConsumo = new HashMap<>();
        for (Bevanda bevanda: bevande) {
            int quantita = InputDati.leggiInteroPositivo("Consumo pro capite '" + bevanda.getNome() + "': ");
            QuantitaMerce quantitaBevanda = new QuantitaMerce(quantita,"L");
            hashMapConsumo.put(bevanda, quantitaBevanda);
        }
        consumoProCapiteBevande.setConsumo(hashMapConsumo);
        return consumoProCapiteBevande;
    }

    public ConsumoProCapiteGeneriExtra inizializzaConsumoGeneriExtra(Set<GenereAlimentareExtra> generiAlimentari){
        ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra = new ConsumoProCapiteGeneriExtra();
        HashMap<Raggruppabile,QuantitaMerce> hashMapConsumo = new HashMap<>();
        for (GenereAlimentareExtra genere: generiAlimentari) {
            int quantita = InputDati.leggiInteroPositivo("Consumo pro capite '" + genere.getNome() + "': ");
            QuantitaMerce quantitaGenere = new QuantitaMerce(quantita,"HG");
            hashMapConsumo.put(genere, quantitaGenere);
        }
        consumoProCapiteGeneriExtra.setConsumo(hashMapConsumo);
        return consumoProCapiteGeneriExtra;
    }

    public Set<GenereAlimentareExtra> inizializzaGeneriAlimentari(){
        Set<GenereAlimentareExtra> generi = new HashSet<>();
        System.out.println(lineSeparator);
        System.out.println("CONFIGURAZIONE INSIEME GENERI ALIMENTARI EXTRA:");
        boolean scelta = true;
        do {
            String nomeGenere = InputDati.leggiStringaNonVuota("Inserire nome genere alimentare extra: ");
            GenereAlimentareExtra genere = new GenereAlimentareExtra(nomeGenere);
            generi.add(genere);
            scelta = InputDati.yesOrNo(nuovaGenere);
        } while (scelta);
        return generi;
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

}
