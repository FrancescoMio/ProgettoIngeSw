package unibs.ids.ristorante;

import Libreria.InputDati;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class AddettoPrenotazioni extends Utente {

    private ArrayList<Prenotazione> prenotazioni = new ArrayList<>();

    public AddettoPrenotazioni(String nome, String cognome) {//Costruttore
        super(nome, cognome);
        this.prenotazioni = new ArrayList<>();
    }

    public AddettoPrenotazioni(){
        super();
    }

    /**
     * Metodo principale di addettoPrenotazioni, permette di creare una prenotazione effettiva per una data in particolare
     * e comprende tutti i controlli su carico di lavoro e numero di coperti
     * @param copertiMax
     * @param caricoMax
     */
    public void creaPrenotazioni(int copertiMax, double caricoMax){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        int numeroCoperti = InputDati.leggiIntero("Inserire numero coperti: ");
        LocalDate dataPrenotazione = InputDati.leggiData("Inserire data prenotazione: ");
        Prenotazione prenotazione;
        //richiamo metodo che controlla se il numero di coperti inseriti supera il numero massimo di coperti raggiungibili in una giornata
        if(controlloCoperti(numeroCoperti, dataPrenotazione, copertiMax)){
            //se numero di coperti è accettabile, creo l'ordine
            ordine = chiediOrdine();
            //richiamo metodo che controlla se il carico di lavoro supera il carico massimo raggiungibile in una giornata
            if(controlloCaricoLavoro(dataPrenotazione, ordine, caricoMax)){
                //se il carico di lavoro è accettabile, creo la prenotazione
                prenotazione = new Prenotazione(numeroCoperti, dataPrenotazione, ordine);
                prenotazioni.add(prenotazione);//aggiungo la prenotazione alla lista delle prenotazioni
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

    }

    /**
     * metodo che permette di controllare che il carico di lavoro sia accettabile nella giornata scelta
     * @param dataPrenotazione
     * @param ordine
     * @param caricoMax
     * @return
     */
    private boolean controlloCaricoLavoro(LocalDate dataPrenotazione, HashMap<Ordinabile,Integer> ordine, double caricoMax){
        //salvo il carico delle prenotazioni già presenti nella data in questione
        double caricoLavorodellaGiornata = caricoLavoroInData(dataPrenotazione);

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
    private double caricoLavoroInData(LocalDate dataPrenotazione){
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
    private boolean controlloCoperti(int numeroCoperti, LocalDate dataPrenotazione, int copertiMax){
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
    private void togliPrenotazioniScadute(){
        for(Prenotazione p : prenotazioni){
            if(p.getDataPrenotazione().isBefore(LocalDate.now())){
                prenotazioni.remove(p);
            }
        }
    }

    private HashMap<Ordinabile,Integer> chiediOrdine(){
        //TODO
        return null;//da togliere
    }
}
