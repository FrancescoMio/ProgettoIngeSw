package unibs.ids.ristorante;

import Libreria.InputDati;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
    public ArrayList<Prenotazione> creaPrenotazioni(int copertiMax, double caricoMax, MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici) {
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        System.out.println("Inserire di seguito le prenotazioni da fare: ");
        do{
            Prenotazione prenotazione = creaPrenotazione(prenotazioni, copertiMax, caricoMax, menuAllaCarta, menuTematici);
            if(prenotazione != null)
                prenotazioni.add(prenotazione);
        }while (InputDati.yesOrNo("Vuoi creare un'altra prenotazione?"));
        return prenotazioni;
    }

    /**
     * Metodo principale di addettoPrenotazioni, permette di creare una prenotazione effettiva per una data in particolare
     * e comprende tutti i controlli su carico di lavoro e numero di coperti
     * @param copertiMax numero massimo di coperti del ristorante
     * @param caricoMax massimo carico sostenibile del ristorante
     */
    private Prenotazione creaPrenotazione(ArrayList<Prenotazione> prenotazioni, int copertiMax, double caricoMax,MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        int numeroCoperti = InputDati.leggiIntero("Inserire numero coperti: ");
        LocalDate dataPrenotazione = InputDati.leggiData("Inserire data prenotazione: ");
        Prenotazione prenotazione;
        //richiamo metodo che controlla se il numero di coperti inseriti supera il numero massimo di coperti raggiungibili in una giornata
        if(controlloCoperti(prenotazioni, numeroCoperti, dataPrenotazione, copertiMax)){
            //se numero di coperti è accettabile, creo l'ordine
            ordine = chiediOrdine(numeroCoperti, menuAllaCarta, menuTematici);
            //richiamo metodo che controlla se il carico di lavoro supera il carico massimo raggiungibile in una giornata
            if(controlloCaricoLavoro(prenotazioni, dataPrenotazione, ordine, caricoMax)){
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
            System.out.println("Prenotazione non effettuata, carico di coperti troppo elevato");
            System.out.println("Tornate un altro giorno e saremo lieti di servirvi!");
        }
        return null;
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
    private boolean controlloCoperti(ArrayList<Prenotazione> prenotazioni, int numeroCoperti, LocalDate dataPrenotazione, int copertiMax){
        int numeroCopertiGiaPrenotati = 0;
        for(Prenotazione p : prenotazioni){
            if(p.getDataPrenotazione().equals(dataPrenotazione)){
                numeroCopertiGiaPrenotati += p.getNumeroCoperti();
            }
        }
        if(numeroCopertiGiaPrenotati + numeroCoperti > copertiMax){
            System.out.println("Il numero di coperti inseriti supera il numero massimo di coperti raggiungibili in una giornata");
            System.out.println("Creare una nuova prenotazione con un numero di coperti inferiore:");
            return false;
        }
        else
            return true;
    }

    //todo: metodo da richiamare!
    /**
     * metodo che permette di eliminare automaticamente le prenotazioni scadute
     */
    private void togliPrenotazioniScadute(ArrayList<Prenotazione> prenotazioni){
        for(Prenotazione p : prenotazioni){
            if(p.getDataPrenotazione().isBefore(LocalDate.now())){
                prenotazioni.remove(p);
            }
        }
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
        System.out.println("Inserire cortesemente l'ordine per ogni persona al tavolo");
        for(int i = 0; i < numeroCoperti;){
            System.out.println("Ordine persona " + (i+1));
            Ordinabile ordinabile = chiediOrdinabile(menuAllaCarta, menuTematici);
            int quantita = InputDati.leggiIntero("Inserire quante persone hanno scelto questo menu/piatto: ");
            if(ordinabile instanceof MenuTematico){
                i = i + quantita;// ogni persona che ha preso un menutematico ha fatto il suo ordine
            }
            else if(ordinabile instanceof MenuCarta){//comunque se prendo due piatti uguali immagino siano per persone diverse
                i = i + quantita;//questi due if li tolgo se confermiamo questa logica, e lascio solo i += quantita
            }
            ordine.put(ordinabile, quantita);
        }
        if(InputDati.yesOrNo("E' stato ordinato almeno un piatto o menu tematico per persona, vuoi ordinare altri piatti dal menu alla carta?"))
        {
            Ordinabile ordinabile;
            do{
                ordinabile = chiediPiatto(menuAllaCarta);
                int quantita = InputDati.leggiIntero("Inserire quante persone hanno scelto questo menu/piatto: ");
                ordine.put(ordinabile,quantita);
            }while(InputDati.yesOrNo("Vuoi inserirne un altro?"));
        }
        return ordine;
    }

    /**
     * metodo di supporto a chiediOrdine che permette invece di chiedere all'utente
     * il singolo piatto dal Menu alla Carta o il Menu Tematico
     * @param menuAllaCarta menu alla carta
     * @param menuTematici menu tematici
     * @return piatto o menu tematico scelto
     */
    private Ordinabile chiediOrdinabile(MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        Ordinabile ordinabile = null;
        int scelta = 0;
        do{
            System.out.println("1) Menu");
            System.out.println("2) Piatto");
            scelta = InputDati.leggiIntero("Inserire:\n1 per scegliere un piatto dal menu alla carta,\n2 per scegliere un menu tematico");
            switch (scelta){
                case 1:
                    ordinabile = chiediMenu(menuTematici);
                    break;
                case 2:
                    ordinabile = chiediPiatto(menuAllaCarta);
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
        }while(scelta != 1 && scelta != 2);
        return ordinabile;
    }


    /**
     * Metodo di supporto a chiediOrdinabile che permette di chiedere all'utente
     * il singolo piatto dal menu alla carta che si desidera ordinare
     * @param menuAllaCarta menu alla carta
     * @return piatto scelto
     */
    private Ordinabile chiediPiatto(MenuCarta menuAllaCarta) {
        System.out.println("I piatti disponibili sono:");
        menuAllaCarta.visualizzaPiatti(); //DA TESTARE SE VA MEGLIO DEL TOSTRING
        Ordinabile piatto = null;
        int scelta = InputDati.leggiIntero("Inserire il numero del piatto che si desidera ordinare: ");
        Iterator<Piatto> iterator = menuAllaCarta.getElencoPiatti().iterator();
        if(scelta > menuAllaCarta.getElencoPiatti().size() || scelta < 1) {
            scelta = InputDati.leggiIntero("Scelta non valida, reinserire il numero del piatto che si desidera ordinare ");
        }
        for (int i = 0; i < scelta; i++) {
            if(i == scelta-1){
                piatto = iterator.next();
                break;
            }
            else
                iterator.next();
        }
        return piatto;
    }

    /**
     * Metodo di supporto a chiediOrdinabile che permette di chiedere all'utente
     * il menu tematico che si desidera ordinare
     * @param menuTematici menu tematici
     * @return menu tematico scelto
     */
    private Ordinabile chiediMenu(Set<MenuTematico> menuTematici) {
        Ordinabile menuTematico = null;
        System.out.println("Di seguito sono riportati i menu tematici del nostro ristorante, ordinandone uno sara' servito tutto il menu");
        int i = 1;
        for(MenuTematico m : menuTematici){
            System.out.println(i + ") " + m.toStringMenuTematicoDisponibile());
            i++;
        }
        int scelta = InputDati.leggiIntero("Inserire il numero del menu tematico che si desidera ordinare: ");
        if(scelta > menuTematici.size() || scelta < 1) {
            scelta = InputDati.leggiIntero("Scelta non valida, reinserire il numero del menu tematico che si desidera ordinare ");
        }

        Iterator<MenuTematico> iterator = menuTematici.iterator();
        for (int j = 0; j < scelta; j++) {
            if(j == scelta-1){
                menuTematico = iterator.next();
                break;
            }
            else
                iterator.next();
        }
        return menuTematico;
    }

}
