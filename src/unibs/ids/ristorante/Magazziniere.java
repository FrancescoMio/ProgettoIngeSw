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

    /**
     * Metodo che crea la lista della spesa ogni giorno
     * @param prenotazioni lista delle prenotazioni
     * @param registro registro magazzino
     * @param consumoProCapiteBevande consumo pro capite delle bevande
     * @param consumoProCapiteGeneriExtra consumo pro capite dei generi extra
     * precondizioni: prenotazioni.size() > 0, registro != null, consumoProCapiteBevande != null, consumoProCapiteGeneriExtra != null
     */
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
     * Metodo che simula il flusso di ingredienti in uscita dal magazzino verso la cucina
     * @param prenotazioni lista delle prenotazioni
     * @return merce da portare in cucina
     * precondizioni: prenotazioni.size() > 0
     * postcondizioni: prodottiDaPortareInCucina != null
     */
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

    /**
     * metodo che simula la rimozione degli scarti a discrezione del magazziniere
     * @param registroMagazzino registro magazzino
     * @return scarti rimossi
     * precondizioni: registroMagazzino != null
     */
    public HashMap<String,QuantitaMerce> rimuoviScarti(RegistroMagazzino registroMagazzino){
        HashMap<String,QuantitaMerce> articoliMagazzino = registroMagazzino.getArticoliDisponibili().getArticoli();
        HashMap<String,QuantitaMerce> scarti = new HashMap<>();
        System.out.println(ANSI_CYAN+"MERCE IN MAGAZZINO:"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();
        do{
            boolean prodottoPresente = false;
            String nomeProdotto = "";
            do {
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

    /**
     * metodo che simula i prodotti riportati in magazzino dalla cucina
     * @param merceInCucina merce in cucina
     * @return prodotti da riportare in magazzino
     * precondizioni: merceInCucina != null
     */
    public HashMap<String,QuantitaMerce> riportaInMagazzino(Merce merceInCucina){
        HashMap<String,QuantitaMerce> prodottiDaRiportare = new HashMap<>();
        HashMap<String,QuantitaMerce> prodottiInCucina = merceInCucina.getArticoli();
        do{
            boolean prodottoPresente = false;
            String nomeProdotto = "";
            System.out.println(ANSI_CYAN+"MERCE IN CUCINA:"+ANSI_RESET);
            merceInCucina.visualizzaMerce();
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

    /**
     * metodo che simula il flusso in uscita verso la cucina delle bevande e dei generi extra
     * @param registroMagazzino registro magazzino
     * @param insieme insieme di bevande e generi alimentari extra
     * @return prodotti in sala
     * precondizioni: registroMagazzino != null && insieme != null
     * postcondizioni: prodottiInSala != null
     */
    public HashMap<String,QuantitaMerce> portaBevandaGenereInSala(RegistroMagazzino registroMagazzino, Set<Raggruppabile> insieme){
        HashMap<String, QuantitaMerce> prodottiInSala = new HashMap<>();
        HashMap<String,QuantitaMerce> articoliInMagazzino = registroMagazzino.getArticoliDisponibili().getArticoli();
        System.out.println(lineSeparator);
        do{
            String nomeArticolo = "";
            boolean prodottoPresente = false;
            do{
                System.out.println(ANSI_BLUE + "---BEVANDE/GENERI ALIMENTARI NEL MAGAZZINO---" + ANSI_RESET);
                for(Raggruppabile raggruppabile : insieme)
                    System.out.println(raggruppabile);
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
     * @param prenotazioni lista di tutte le prenotazioni
     * @return prenotazioniGiornaliere lista di prenotazioni corrispondenti al giorno odierno
     * precondizioni: prenotazioni.size() > 0
     * postcondizioni: prenotazioniGiornaliere.size() >= 0
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
