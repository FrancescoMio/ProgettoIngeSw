package unibs.ids.ristorante;

import Libreria.MyUtil;

import java.time.LocalDate;
import java.util.*;

public class Magazziniere extends Utente {

    private Merce listaSpesa;

    public Magazziniere(String nome, String cognome) {
        super(nome, cognome);
    }
    public Magazziniere(){
        super();
    }
    public void creaListaSpesaGiornaliera(ArrayList<Prenotazione> prenotazioni, RegistroMagazzino registro, ConsumoProCapiteBevande consumoProCapiteBevande, ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra){
        listaSpesa = new Merce();
        int numeroCoperti = 0;
        ArrayList<Prenotazione> prenotazioniGiornaliere = filtraPrenotazioniGiornaliere(prenotazioni);
        for(Prenotazione prenotazione: prenotazioniGiornaliere)
            prenotazione.visualizzaPrenotazione();
        for(Prenotazione prenotazione : prenotazioniGiornaliere){
            numeroCoperti += prenotazione.getNumeroCoperti();
            HashMap<Ordinabile, Integer> ordinePrenotazione = prenotazione.getOrdine();
            for (Map.Entry<Ordinabile, Integer> entry : ordinePrenotazione.entrySet()) {
                Ordinabile ordinabile = entry.getKey();
                int quantitaOrdine = entry.getValue();
                HashMap<String, QuantitaMerce> listaIngredienti = ordinabile.getListaIngredienti(quantitaOrdine);
                listaSpesa.aggiungiIngredienti(listaIngredienti);
            }
        }
        listaSpesa.aggiungiBevandeGeneri(consumoProCapiteBevande,consumoProCapiteGeneriExtra, numeroCoperti);
        listaSpesa.incrementoPercentuale();
        listaSpesa.differenzaScorte(registro.getArticoliDisponibili());
        listaSpesa.visualizzaMerce();
    }

    /**
     * Metodo che filtra tutte le prenotazioni del ristorante e ritorna solo quelle corrispondenti al giorno odierno
     * @param prenotazioni
     * @return
     */
    private ArrayList<Prenotazione> filtraPrenotazioniGiornaliere(ArrayList<Prenotazione> prenotazioni){
        ArrayList<Prenotazione> prenotazioniGiornaliere = new ArrayList<>();
        for(Prenotazione prenotazione : prenotazioni){
            LocalDate dataPrenotazione = prenotazione.getDataPrenotazione();
            if(dataPrenotazione.isEqual(MyUtil.getDataOdierna()))
                prenotazioniGiornaliere.add(prenotazione);
        }
        return prenotazioniGiornaliere;
    }

    public Merce getListaSpesa() {
        return listaSpesa;
    }

    private Merce aggiuntaBevandeEExtra(Merce listaSpesa, Consumo bevandeEExtra){
        for (Map.Entry<Raggruppabile, QuantitaMerce> entry : bevandeEExtra.getConsumo().entrySet()) {
            Raggruppabile item = entry.getKey();
            QuantitaMerce quantita = entry.getValue();
            if(item instanceof Bevanda){
                listaSpesa.aggiungiMerce(item.getNome(), quantita);
            }
            else if(item instanceof GenereAlimentareExtra){
                listaSpesa.aggiungiMerce(item.getNome(), quantita);
            }
        }
        return listaSpesa;
    }

    public void stampaListaSpesa(){
        System.out.println("Lista spesa giornaliera: ");
        System.out.println(listaSpesa.toString());
    }

}
