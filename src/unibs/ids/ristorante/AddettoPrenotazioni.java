package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.MyMenu;
import Libreria.MyUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static Libreria.Stringhe.*;

public class AddettoPrenotazioni extends Utente {

    public AddettoPrenotazioni(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    public AddettoPrenotazioni(){
        super();
    }

    /**
     * metodo per la creazione delle prenotazioni
     * @param copertiMax numero di coperti massimo del ristorante
     * @param caricoMax carico massimo sostenibile del ristorante
     * @param menuAllaCarta menu alla carta del ristorante, comprensivo di tutti i piatti disponibili
     * @param menuTematici menu tematici del ristorante, ognuno comprendente piu piatti
     * @return tutte le prenotazioni create
     */
    public ArrayList<Prenotazione> creaPrenotazioni(int copertiMax, double caricoMax, MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici, ArrayList<Prenotazione> storicoPrenotazioni) {
        System.out.println(lineSeparator);
        System.out.println(ANSI_YELLOW+"CREAZIONE NUOVE PRENOTAZIONI"+ANSI_RESET);
        ArrayList<Prenotazione> nuovePrenotazioni = new ArrayList<>();
        do{
            Prenotazione prenotazione = creaPrenotazione(storicoPrenotazioni, copertiMax, caricoMax, menuAllaCarta, menuTematici);
            if(prenotazione != null){
                nuovePrenotazioni.add(prenotazione);
                System.out.println(ANSI_GREEN+"---PRENOTAZIONE CREATA CORRETTAMENTE---"+ANSI_RESET);
            }
            else
                System.out.println(ANSI_RED+"---LA PRENOTAZIONE E' STATA CANCELLATA!---"+ANSI_RESET);
        }while (InputDati.yesOrNo(ANSI_CYAN+"Creare un'altra prenotazione?"+ANSI_RESET));
        return nuovePrenotazioni;
    }

    /**
     * Metodo che filtra i menù tematici che saranno disponibili fino alla data di prenotazione compresa
     * @param menuTematici menù tematici del ristorante (non filtrati)
     * @param dataPrenotazione
     * @return
     */
    private Set<MenuTematico> filtraMenutematici(Set<MenuTematico> menuTematici, LocalDate dataPrenotazione){
        Set<MenuTematico> menuTematiciDisponibili = new HashSet<>();
        for(MenuTematico menuTematico : menuTematici){
            if(menuTematico.getDataFine().isAfter(dataPrenotazione) || menuTematico.getDataFine().isEqual(dataPrenotazione))
                menuTematiciDisponibili.add(menuTematico);
        }
        return menuTematiciDisponibili;
    }

    /**
     * Metodo principale di addettoPrenotazioni, permette di creare una prenotazione effettiva per una data in particolare
     * e comprende tutti i controlli su carico di lavoro e numero di coperti
     * @param copertiMax numero massimo di coperti del ristorante
     * @param caricoMax massimo carico sostenibile del ristorante
     */
    private Prenotazione creaPrenotazione(ArrayList<Prenotazione> storicoPrenotazioni, int copertiMax, double caricoMax,MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        LocalDate dataOdierna = LocalDate.now();
        if(controlloDataOdierna(dataOdierna)){
            HashMap<Ordinabile,Integer> ordine = new HashMap<>();
            int numeroCoperti = InputDati.leggiIntero("Inserire numero coperti: ");
            LocalDate dataPrenotazione;
            do{
                dataPrenotazione = InputDati.leggiData("Inserire data prenotazione (dd/MM/yyyy): ");
            }while(!prenotazioneValida(dataPrenotazione));
            Set<MenuTematico> menuTematiciDisponibili = filtraMenutematici(menuTematici,dataPrenotazione);
            Prenotazione prenotazione;
            //richiamo metodo che controlla se il numero di coperti inseriti supera il numero massimo di coperti raggiungibili in una giornata
            if(controlloCoperti(storicoPrenotazioni, numeroCoperti, dataPrenotazione, copertiMax)){
                //se numero di coperti è accettabile, creo l'ordine
                ordine = chiediOrdine(numeroCoperti, menuAllaCarta, menuTematiciDisponibili);
                if(ordine.equals(null))
                    return null;
                //richiamo metodo che controlla se il carico di lavoro supera il carico massimo raggiungibile in una giornata
                if(controlloCaricoLavoro(storicoPrenotazioni, dataPrenotazione, ordine, caricoMax)){
                    //se il carico di lavoro è accettabile, creo la prenotazione
                    prenotazione = new Prenotazione(numeroCoperti, dataPrenotazione, ordine);
                    return prenotazione;
                }
                else{
                    System.out.println("Prenotazione non effettuata, carico di lavoro troppo elevato");
                    System.out.println("Tornate un altro giorno e saremo lieti di servirvi!");
                }
            }
            else{
                System.out.println(ANSI_YELLOW+"Prenotazione non effettuata, carico di coperti troppo elevato"+ANSI_RESET);
                System.out.println(ANSI_YELLOW+"Tornate un altro giorno e saremo lieti di servirvi!"+ANSI_RESET);
            }
        }
        return null;
    }

    private boolean prenotazioneValida(LocalDate dataPrenotazione){
        LocalDate dataOdierna = MyUtil.getDataOdierna();
        if(dataPrenotazione.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            System.err.println("Non è possibile prenotare per un giorno festivo!");
            return false;
        } else if (dataPrenotazione.isEqual(dataOdierna) ) {
            System.err.println("Non è possibile prenotare per il giorno corrente!");
            return false;
        } else if (dataPrenotazione.isBefore(dataOdierna)) {
            System.err.println("Data inserita non valida!");
            return false;
        }else return true;
    }

    private boolean controlloDataOdierna(LocalDate dataOdierna){
        if(dataOdierna.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            System.out.println("Non è possibile prenotare in un giorno festivo!");
            return false;
        }
        return true;
    }

    /**
     * metodo che permette di controllare che il carico di lavoro sia accettabile nella giornata scelta
     * @param dataPrenotazione data della prenotazione
     * @param ordine coppia menu/piatto e quantità associata
     * @param caricoMax carico massimo raggiungibile in una giornata
     * @return true se il carico di lavoro è accettabile, false altrimenti
     */
    private boolean controlloCaricoLavoro(ArrayList<Prenotazione> prenotazioni, LocalDate dataPrenotazione, HashMap<Ordinabile,Integer> ordine, double caricoMax){
        //salvo il carico delle prenotazioni già presenti nella data in questione
        double caricoLavorodellaGiornata = caricoLavoroInData(prenotazioni, dataPrenotazione);

        for(Ordinabile o : ordine.keySet()){
            //aggiungo il carico di lavoro dell'ordine
            caricoLavorodellaGiornata += o.getCaricoLavoro()*ordine.get(o);
        }
        if(caricoLavorodellaGiornata > caricoMax)
            return false;
        else
            return true;
    }

    /**
     * metodo che permette di calcolare il carico di lavoro delle prenotazioni già presenti in una data
     * @param dataPrenotazione data della prenotazione
     * @return carico di lavoro delle prenotazioni già presenti in una data
     */
    private double caricoLavoroInData(ArrayList<Prenotazione> prenotazioni ,LocalDate dataPrenotazione){
        double sommaCarichiLavoro = 0;
        ArrayList<Prenotazione> prenotazioniUtili = new ArrayList<>();
        for(Prenotazione p : prenotazioni){
            if(p.getDataPrenotazione().equals(dataPrenotazione)){
                prenotazioniUtili.add(p);
            }
        }
        for (Prenotazione p : prenotazioniUtili){
            sommaCarichiLavoro += p.getCaricoLavoro();
        }
        return sommaCarichiLavoro;
    }

    /**
     * metodo che permette di controllare che il numero di coperti inseriti non superi il numero
     * massimo di coperti raggiungibili in una giornata
     * @param numeroCoperti numero di coperti inseriti
     * @param dataPrenotazione data della prenotazione
     * @param copertiMax  numero massimo di coperti raggiungibili in una giornata
     * @return true se il numero di coperti inseriti è accettabile, false altrimenti
     */
    private boolean controlloCoperti(ArrayList<Prenotazione> storicoPrenotazioni, int numeroCoperti, LocalDate dataPrenotazione, int copertiMax){
        int numeroCopertiGiaPrenotati = 0;
        for(Prenotazione p : storicoPrenotazioni){
            if(p.getDataPrenotazione().equals(dataPrenotazione)){
                numeroCopertiGiaPrenotati += p.getNumeroCoperti();
            }
        }
        if(numeroCopertiGiaPrenotati + numeroCoperti > copertiMax){
            System.err.println("Il numero di coperti inseriti supera il numero massimo di coperti raggiungibili in una giornata!");
            System.out.println(ANSI_YELLOW+"Creare una nuova prenotazione con un numero di coperti inferiore!"+ANSI_RESET);
            return false;
        }
        else
            return true;
    }

    /**
     * metodo che permette di eliminare automaticamente le prenotazioni scadute
     */
    public ArrayList<Prenotazione> togliPrenotazioniScadute(ArrayList<Prenotazione> prenotazioni){
        ArrayList<Prenotazione> prenotazioniAggiornate = new ArrayList<>();
        for(Prenotazione p : prenotazioni){
            if(!p.getDataPrenotazione().isBefore(LocalDate.now()))
                prenotazioniAggiornate.add(p);
        }
        return prenotazioniAggiornate;
    }

    /**
     * metodo che permette di chiedere all'utente l'ordine per ogni persona al tavolo,
     * ovvero una coppia menuTematico/piatto e quantità
     * @param numeroCoperti numero di coperti inseriti
     * @param menuAllaCarta menu alla carta
     * @param menuTematici menu tematici
     * @return ordine per ogni persona al tavolo
     */
    private HashMap<Ordinabile,Integer> chiediOrdine(int numeroCoperti, MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        MyMenu menuOrdine = new MyMenu(titoloOrdine, vociOrdine);
        for(int i = 0; i < numeroCoperti; i++){
            System.out.println("\n"+ANSI_CYAN+"-ORDINE PERSONA " + (i+1) + ":"+ANSI_RESET);
            int scelta = menuOrdine.scegli();
            if(scelta == 1){
                if(!menuTematici.isEmpty()){
                    MenuTematico menu = chiediMenu(menuTematici);
                    if(presenteInOrdine(ordine,menu)){
                        int quantita = ordine.get(menu);
                        ordine.put(menu,quantita+1);
                    }
                    else ordine.put(menu,1);
                }
                else System.out.println("Non sono presenti menù tematici disponibili!");
            } else if (scelta == 2) {
                do{
                    Piatto piatto = chiediPiatto(menuAllaCarta);
                    if(presenteInOrdine(ordine,piatto)){
                        int quantita = ordine.get(piatto);
                        ordine.put(piatto,quantita+1);
                    }
                    else ordine.put(piatto, 1);
                }while(InputDati.yesOrNo("INSERIRE ANCORA UN PIATTO ALL'ORDINE?"));
            }
            else return null;
        }
        return ordine;
    }

    private boolean presenteInOrdine(HashMap<Ordinabile, Integer> ordine, Ordinabile item){
        for (Map.Entry<Ordinabile, Integer> entry : ordine.entrySet()) {
            Ordinabile key = entry.getKey();
            if(item.equals(key))
                return true;
        }
        return false;
    }

    /**
     * metodo di supporto a chiediOrdine che permette di chiedere all'utente
     * il singolo piatto dal Menu alla Carta o il Menu Tematico
     * @param menuAllaCarta menu alla carta
     * @param menuTematici menu tematici
     * @return piatto o menu tematico scelto
     */
    /*private Ordinabile chiediOrdinabile(MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        Ordinabile ordinabile = null;
        int scelta = 0;

        do{
            scelta = InputDati.leggiIntero("Inserire:\n1 per scegliere un piatto dal menu alla carta\n2 per scegliere un menu tematico");
            switch (scelta){
                case 1:
                    ordinabile = chiediPiatto(menuAllaCarta);
                    break;
                case 2:
                    ordinabile = chiediMenu(menuTematici);
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
        }while(scelta != 1 && scelta != 2);
        return ordinabile;
    }*/


    /**
     * Metodo di supporto a chiediOrdinabile che permette di chiedere all'utente
     * il singolo piatto dal menu alla carta che si desidera ordinare
     * @param menuAllaCarta menu alla carta
     * @return piatto scelto
     */
    private Piatto chiediPiatto(MenuCarta menuAllaCarta) {
        Set<Piatto> elencoPiatti = menuAllaCarta.getElencoPiatti();
        Piatto[] piatti = elencoPiatti.toArray(new Piatto[elencoPiatti.size()]);
        int i = 1;
        int numeroPiatto;
        boolean sceltaCorretta = false;
        for (Piatto piatto : piatti) {
            System.out.println(i + "-" + piatto.getDenominazione());
            i++;
        }
        do {
            numeroPiatto = InputDati.leggiInteroPositivo("Inserire numero piatto da aggiungere all'ordine: ");
            if (numeroPiatto >= 1 && numeroPiatto <= piatti.length)
                sceltaCorretta = true;
            else
                System.err.println(erroreSceltaPiatto);
        } while (!sceltaCorretta);
        Piatto piatto = piatti[numeroPiatto-1];
        return piatto;
    }

    /**
     * Metodo di supporto a chiediOrdinabile che permette di chiedere all'utente
     * il menu tematico che si desidera ordinare
     * @param menuTematici menu tematici
     * @return menu tematico scelto
     */
    private MenuTematico chiediMenu(Set<MenuTematico> menuTematici) {
        MenuTematico[] elencoMenuTematici = menuTematici.toArray(new MenuTematico[menuTematici.size()]);
        int i = 1;
        int numeroMenu;
        boolean sceltaCorretta = false;
        for (MenuTematico m : elencoMenuTematici) {
            System.out.println(i + " " + m.menuCliente());
            i++;
        }
        do {
            numeroMenu = InputDati.leggiInteroPositivo("Inserire numero menu tematico da aggiungere all'ordine: ");
            if (numeroMenu >= 1 && numeroMenu <= elencoMenuTematici.length)
                sceltaCorretta = true;
            else
                System.err.println(erroreSceltaMenu);
        } while (!sceltaCorretta);
        MenuTematico menu = elencoMenuTematici[numeroMenu - 1];
        return menu;
    }

}
