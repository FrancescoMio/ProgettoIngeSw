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
        int numeroCoperti = 0;
        for(Prenotazione prenotazione : prenotazioniGiornaliere){
            numeroCoperti += prenotazione.getNumeroCoperti();
            HashMap<Ordinabile, Integer> ordinePrenotazione = prenotazione.getOrdine();
            for (Map.Entry<Ordinabile, Integer> entry : ordinePrenotazione.entrySet()) {
                Ordinabile ordinabile = entry.getKey();
                int quantitaOrdine = entry.getValue();
                HashMap<String, QuantitaMerce> listaIngredienti = ordinabile.getListaIngredienti(quantitaOrdine);
                prodottiDaPortareInCucina.aggiungiIngredienti(listaIngredienti);
            }
        }
        return prodottiDaPortareInCucina;
    }

    public HashMap<String,QuantitaMerce> portaBevandaGenereInSala(RegistroMagazzino registroMagazzino, Set<Bevanda> bevande, Set<GenereAlimentareExtra> generiAlimentari){
        HashMap<String, QuantitaMerce> prodottiInSala = new HashMap<>();
        HashMap<String,QuantitaMerce> articoliInMagazzino = registroMagazzino.getArticoliDisponibili().getArticoli();
        System.out.println(lineSeparator);
        do{
            String nomeArticolo = "";
            boolean prodottoPresente = false;
            do{
                System.out.println(ANSI_BLUE + "---BEVANDE/GENERI ALIMENTARI NEL MAGAZZINO---" + ANSI_RESET);
                System.out.println(bevande);
                System.out.println(generiAlimentari);
                nomeArticolo = InputDati.leggiStringaConSpazi("Inserire nome della bevanda/genere alimentare extra: ");
                if(!registroMagazzino.articoloGiaPresente(nomeArticolo)){
                    System.err.println("Bevanda/Genere alimentare non presente!");
                }else prodottoPresente = true;
            }while (!prodottoPresente);
            QuantitaMerce quantitaProdottoMagazzino = articoliInMagazzino.get(nomeArticolo);
            double quantitaMaxMagazzino = quantitaProdottoMagazzino.getQuantita();
            String unitaMisura = quantitaProdottoMagazzino.getUnitaMisura();
            double quantitaDaPortare = InputDati.leggiDoubleCompreso("Inserire quantita da portare in sala (" + unitaMisura +"): ",0,quantitaMaxMagazzino);
            QuantitaMerce quantitaProdotto = new QuantitaMerce(quantitaDaPortare,unitaMisura);
            QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaMaxMagazzino - quantitaDaPortare,unitaMisura);
            //articoliInMagazzino.replace(nomeArticolo,quantitaProdottoMagazzino,quantitaAggiornata);
            prodottiInSala.put(nomeArticolo,quantitaProdotto);
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
