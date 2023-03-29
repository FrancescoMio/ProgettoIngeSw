package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.MyUtil;

import java.time.LocalDate;
import java.util.*;

import static Libreria.Stringhe.*;

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

    public Merce portaIngredientiInCucina(ArrayList<Prenotazione> prenotazioni){
        Merce prodottiDaPortareInCucina = new Merce();
        ArrayList<Prenotazione> prenotazioniGiornaliere = filtraPrenotazioniGiornaliere(prenotazioni);
        for(Prenotazione prenotazione : prenotazioniGiornaliere){
            HashMap<Ordinabile, Integer> ordinePrenotazione = prenotazione.getOrdine();
            for (Map.Entry<Ordinabile, Integer> entry : ordinePrenotazione.entrySet()) {
                Ordinabile ordinabile = entry.getKey();
                int quantitaOrdine = entry.getValue();
                HashMap<String, QuantitaMerce> listaIngredienti = ordinabile.getListaIngredienti(quantitaOrdine);
                prodottiDaPortareInCucina.aggiungiIngredienti(listaIngredienti);
            }
        }
        prodottiDaPortareInCucina.incrementoPercentuale();
        return prodottiDaPortareInCucina;
    }

    public HashMap<String,QuantitaMerce> rimuoviScarti(RegistroMagazzino registroMagazzino){
        HashMap<String,QuantitaMerce> articoliMagazzino = registroMagazzino.getArticoliDisponibili().getArticoli();
        HashMap<String,QuantitaMerce> scarti = new HashMap<>();
        do{
            boolean prodottoPresente = false;
            String nomeProdotto = "";
            do {
                System.out.println(ANSI_CYAN+"MERCE IN MAGAZZINO:"+ANSI_RESET);
                registroMagazzino.getArticoliDisponibili().visualizzaMerce();
                nomeProdotto = InputDati.leggiStringaNonVuota("Inserire nome del prodotto da scartare: ");
                if (articoliMagazzino.containsKey(nomeProdotto))
                    prodottoPresente = true;
                else System.err.println("Prodotto non presente in magazzino!");
            } while (!prodottoPresente);
            QuantitaMerce quantitaProdottoInMagazzino = articoliMagazzino.get(nomeProdotto);
            double quantitaInMagazzino = quantitaProdottoInMagazzino.getQuantita();
            String unitaMisura = quantitaProdottoInMagazzino.getUnitaMisura();
            double quantitaDaScartare = InputDati.leggiDoubleCompreso("Inserire quantità da scartare (" + unitaMisura +"): ",0,quantitaInMagazzino);
            QuantitaMerce quantitaProdotto = new QuantitaMerce(quantitaDaScartare,unitaMisura);
            if(scarti.containsKey(nomeProdotto)){
                QuantitaMerce quantitaProdottoOld = scarti.get(nomeProdotto);
                double quantitaOld = quantitaProdottoOld.getQuantita();
                QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaDaScartare+quantitaOld,unitaMisura);
                scarti.put(nomeProdotto,quantitaAggiornata);
            }else scarti.put(nomeProdotto,quantitaProdotto);
        }while(InputDati.yesOrNo("Scartare un altro prodotto?"));
        return scarti;
    }

    public HashMap<String,QuantitaMerce> riportaInMagazzino(Merce merceInCucina){
        HashMap<String,QuantitaMerce> prodottiDaRiportare = new HashMap<>();
        HashMap<String,QuantitaMerce> prodottiInCucina = merceInCucina.getArticoli();
        do{
            boolean prodottoPresente = false;
            String nomeProdotto = "";
            System.out.println(ANSI_CYAN+"MERCE IN CUCINA:"+ANSI_RESET);
            System.out.println(prodottiInCucina);
            do {
                nomeProdotto = InputDati.leggiStringaNonVuota("Inserire nome del prodotto da riportare in magazzino: ");
                if (merceInCucina.getArticoli().containsKey(nomeProdotto))
                    prodottoPresente = true;
                else System.err.println("Prodotto non presente in cucina!");
            } while (!prodottoPresente);
            QuantitaMerce quantitaProdottoInCucina = prodottiInCucina.get(nomeProdotto);
            double quantitaInCucina = quantitaProdottoInCucina.getQuantita();
            String unitaMisura = quantitaProdottoInCucina.getUnitaMisura();
            double quantitaDaPortare = InputDati.leggiDoubleCompreso("Inserire quantita da riportare in magazzino (" + unitaMisura +"): ",0,quantitaInCucina);
            QuantitaMerce quantitaInCucinaAggiornata = new QuantitaMerce(quantitaInCucina - quantitaDaPortare, unitaMisura);
            prodottiInCucina.replace(nomeProdotto,quantitaInCucinaAggiornata);
            QuantitaMerce quantitaProdotto = new QuantitaMerce(quantitaDaPortare,unitaMisura);
            if(prodottiDaRiportare.containsKey(nomeProdotto)){
                QuantitaMerce quantitaProdottoOld = prodottiDaRiportare.get(nomeProdotto);
                double quantitaOld = quantitaProdottoOld.getQuantita();
                QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaDaPortare+quantitaOld,unitaMisura);
                prodottiDaRiportare.put(nomeProdotto,quantitaAggiornata);
            }else prodottiDaRiportare.put(nomeProdotto,quantitaProdotto);
        }while(InputDati.yesOrNo("Riportare un altro prodotto in magazzino?"));
        return prodottiDaRiportare;
    }

    public HashMap<String,QuantitaMerce> portaBevandaGenereInSala(RegistroMagazzino registroMagazzino, Set<Raggruppabile> insieme){
        HashMap<String, QuantitaMerce> prodottiInSala = new HashMap<>();
        HashMap<String,QuantitaMerce> articoliInMagazzino = registroMagazzino.getArticoliDisponibili().getArticoli();
        System.out.println(lineSeparator);
        do{
            String nomeArticolo = "";
            boolean prodottoPresente = false;
            do{
                System.out.println(ANSI_BLUE + "---BEVANDE/GENERI ALIMENTARI NEL MAGAZZINO---" + ANSI_RESET);
                System.out.println(insieme);
                nomeArticolo = InputDati.leggiStringaConSpazi("Inserire nome della bevanda/genere alimentare extra: ");
                for(Raggruppabile item : insieme) {
                    if (item.getNome().equalsIgnoreCase(nomeArticolo)){
                        prodottoPresente = true;
                        break;
                    }
                }
                if(!prodottoPresente)
                    System.err.println("Bevanda/Genere alimentare non presente!");
            }while (!prodottoPresente);
            QuantitaMerce quantitaProdottoMagazzino = articoliInMagazzino.get(nomeArticolo);
            double quantitaMaxMagazzino = quantitaProdottoMagazzino.getQuantita();
            String unitaMisura = quantitaProdottoMagazzino.getUnitaMisura();
            double quantitaDaPortare = InputDati.leggiDoubleCompreso("Inserire quantita da portare in sala (" + unitaMisura +"): ",0,quantitaMaxMagazzino);
            QuantitaMerce quantitaProdotto = new QuantitaMerce(quantitaDaPortare,unitaMisura);
            if(prodottiInSala.containsKey(nomeArticolo)){
                QuantitaMerce quantitaProdottoOld = prodottiInSala.get(nomeArticolo);
                double quantitaOld = quantitaProdottoOld.getQuantita();
                QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaDaPortare+quantitaOld,unitaMisura);
                prodottiInSala.put(nomeArticolo,quantitaAggiornata);
            }else prodottiInSala.put(nomeArticolo,quantitaProdotto);
        }while(InputDati.yesOrNo("Portare ancora una bevanda o genere alimentare extra in sala?"));
        return prodottiInSala;
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
}
