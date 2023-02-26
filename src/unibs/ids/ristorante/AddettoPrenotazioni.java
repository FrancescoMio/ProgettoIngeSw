package unibs.ids.ristorante;

import Libreria.InputDati;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class AddettoPrenotazioni extends Utente {

    public AddettoPrenotazioni(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    public Prenotazione creaPrenotazione(){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        int numeroCoperti = InputDati.leggiIntero("Inserire numero coperti: ");
        Date dataPrenotazione = InputDati.leggiData("Inserire data prenotazione: ");
        ordine = chiediOrdine();
        Prenotazione prenotazione = new Prenotazione(numeroCoperti, dataPrenotazione, ordine);
        return prenotazione;
    }

    private HashMap<Ordinabile,Integer> chiediOrdine(){
        //TODO
        return null;//da togliere
    }
}
