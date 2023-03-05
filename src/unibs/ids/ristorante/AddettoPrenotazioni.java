package unibs.ids.ristorante;

import Libreria.InputDati;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AddettoPrenotazioni extends Utente {

    public AddettoPrenotazioni(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    public AddettoPrenotazioni(){
        super();
    }

    public ArrayList<Prenotazione> creaPrenotazioni(int copertiMax, double caricoMax, MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici) {
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
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
     * @param copertiMax
     * @param caricoMax
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
     * @param dataPrenotazione
     * @param ordine
     * @param caricoMax
     * @return
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
     * @param dataPrenotazione
     * @return
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
     * @param numeroCoperti
     * @param dataPrenotazione
     * @param copertiMax
     * @return
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

    private HashMap<Ordinabile,Integer> chiediOrdine(int numeroCoperti, MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        System.out.println("Inserire cortesemente l'ordine per ogni persona al tavolo");
        for(int i = 0; i < numeroCoperti; i++){
            System.out.println("Ordine persona " + (i+1));
            Ordinabile ordinabile = chiediOrdinabile(menuAllaCarta, menuTematici);
            int quantita = InputDati.leggiIntero("Inserire quante persone hanno scelto questo menu/piatto: ");
            ordine.put(ordinabile, quantita);
        }
        return ordine;
    }

    private Ordinabile chiediOrdinabile(MenuCarta menuAllaCarta, Set<MenuTematico> menuTematici){
        Ordinabile ordinabile = null;
        int scelta = 0;
        do{
            System.out.println("1) Menu");
            System.out.println("2) Piatto");
            scelta = InputDati.leggiIntero("Inserire:\n1 per scegliere un piatto dal menu alla carta,\n2 per scegliere un menu tematico");
            switch (scelta){
                case 1:
                    //ordinabile = chiediMenu(menuTematici);
                    break;
                case 2:
                    //ordinabile = chiediPiatto(menuAllaCarta);
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
        }while(scelta != 1 && scelta != 2);
        return ordinabile;
    }

    /*
    private Ordinabile chiediPiatto(MenuCarta menuAllaCarta) {
        System.out.println("I piatti disponibili sono:");
        for(Piatto p : menuAllaCarta.getPiatti()){
            System.out.println(p);
        }
    }

    private Ordinabile chiediMenu(Set<MenuTematico> menuTematici) {

    }
    */

}
