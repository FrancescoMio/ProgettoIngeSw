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

    public void creaPrenotazioni(int copertiMax, double caricoMax){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        int numeroCoperti = InputDati.leggiIntero("Inserire numero coperti: ");
        LocalDate dataPrenotazione = InputDati.leggiData("Inserire data prenotazione: ");
        Prenotazione prenotazione;
        //richiamo metodo che controlla se il numero di coperti inseriti supera il numero massimo di coperti raggiungibili in una giornata
        if(controlloCoperti(numeroCoperti, dataPrenotazione, copertiMax)){
            ordine = chiediOrdine();
            if(controlloCaricoLavoro(dataPrenotazione, ordine, caricoMax)){
                prenotazione = new Prenotazione(numeroCoperti, dataPrenotazione, ordine);
                prenotazioni.add(prenotazione);
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

    private boolean controlloCaricoLavoro(LocalDate dataPrenotazione, HashMap<Ordinabile,Integer> ordine, double caricoMax){
        double caricoLavorodellaGiornata = CaricoLavoroInData(dataPrenotazione);
        for(Ordinabile o : ordine.keySet()){
            caricoLavorodellaGiornata += o.getCaricoLavoro()*ordine.get(o);
        }
        if(caricoLavorodellaGiornata > caricoMax)
            return false;
        else
            return true;
    }

    private double CaricoLavoroInData(LocalDate dataPrenotazione){
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
