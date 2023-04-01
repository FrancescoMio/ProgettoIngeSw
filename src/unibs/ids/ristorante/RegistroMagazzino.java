package unibs.ids.ristorante;

import Libreria.InputDati;
import static Libreria.Stringhe.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RegistroMagazzino {
    private Merce articoliDisponibili; //lista dei prodotti disponibili nel magazzino
    private Consumo bevandeEExtra;

    public RegistroMagazzino(){
        articoliDisponibili = new Merce();
        bevandeEExtra = new Consumo();
    }

    public RegistroMagazzino(Merce disponibili, Consumo bevandeEExtra) {
        this.articoliDisponibili = disponibili;
        this.bevandeEExtra = bevandeEExtra;
    }

    public void visualizzaMagazzino(){
        comunicaQuantitaIngredienti();
        visualizzaBevande();
        visualizzaGeneriExtra();
    }

    /**
     * Compito di registro magazzino di visualizzare gli ingredienti presenti in magazzino
     */
    private void comunicaQuantitaIngredienti(){
        System.out.println("I prodotti disponibili con le rispettive quantita' sono:");
        articoliDisponibili.visualizzaMerce();
    }

    /**
     * Compito di registro magazzino di aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * In particolare se gli ingredienti sono gia in magazzino si aggiorna la quantita'
     * @param listaSpesa lista degli ingredienti acquistati
     */
    public void aggiungiArticoliComprati(Merce listaSpesa){
        HashMap<String, QuantitaMerce> articoliAcquistati = listaSpesa.getArticoli();
        HashMap<String, QuantitaMerce> hashMaparticoliDisponibili = articoliDisponibili.getArticoli();
        if(articoliAcquistati.isEmpty())
            System.out.println("ATTENZIONE: Nessun prodotto acquistato perchè il magazzino dispone di scorte sufficienti" );
        else {
            for (Map.Entry<String, QuantitaMerce> entry : articoliAcquistati.entrySet()){
                String nomeArticolo = entry.getKey();
                QuantitaMerce quantitaArticoloAcquistato = entry.getValue();
                double quantitaArticolo = quantitaArticoloAcquistato.getQuantita();
                String unitaMisura = quantitaArticoloAcquistato.getUnitaMisura();
                if(articoloGiaPresente(nomeArticolo)){
                    QuantitaMerce quantitaArticoloOld = hashMaparticoliDisponibili.get(nomeArticolo);
                    double quantitaOld = quantitaArticoloOld.getQuantita();
                    QuantitaMerce quantitaArticoloNuova = new QuantitaMerce(quantitaOld+quantitaArticolo,unitaMisura);
                    hashMaparticoliDisponibili.put(nomeArticolo,quantitaArticoloNuova);
                }
                else hashMaparticoliDisponibili.put(nomeArticolo,quantitaArticoloAcquistato);
            }
            articoliDisponibili.setArticoli(hashMaparticoliDisponibili);
        }
    }

    public boolean articoloGiaPresente(String nomeArticolo){
        HashMap<String, QuantitaMerce> hashMaparticoliDisponibili = articoliDisponibili.getArticoli();
        for (Map.Entry<String, QuantitaMerce> entry : hashMaparticoliDisponibili.entrySet()){
            if(entry.getKey().equalsIgnoreCase(nomeArticolo))
                return true;
        }
        return false;
    }

    /**
     * Metodo per la rimozione dei prodotti dal magazzino
     * @param prodottiDaRimuovere lista dei prodotti da rimuovere
     * precondizione: i prodotti da rimuovere sono presenti nel magazzino, prodottiDaRimuovere != null
     */
    public void rimuoviProdotti(HashMap<String,QuantitaMerce> prodottiDaRimuovere){
        HashMap<String,QuantitaMerce> articoliMagazzino = articoliDisponibili.getArticoli();
        for (Map.Entry<String, QuantitaMerce> entry : prodottiDaRimuovere.entrySet()){
            String nomeArticolo = entry.getKey();
            QuantitaMerce quantitaArticolo = entry.getValue();
            double quantita = quantitaArticolo.getQuantita();
            String unitaMisura = quantitaArticolo.getUnitaMisura();
            if(articoliMagazzino.containsKey(nomeArticolo)){
                QuantitaMerce quantitaMerceOld = articoliMagazzino.get(nomeArticolo);
                double quantitaOld = quantitaMerceOld.getQuantita();
                QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaOld-quantita,unitaMisura);
                articoliMagazzino.replace(nomeArticolo,quantitaAggiornata);
            }
        }
        articoliDisponibili.setArticoli(articoliMagazzino);
    }

    public void riportaProdotti(HashMap<String,QuantitaMerce> prodottiDaRiportare){
        articoliDisponibili.aggiungiIngredienti(prodottiDaRiportare);
    }

    public void caricaArticolo(String nomeArticolo, QuantitaMerce quantitaArticolo){
        articoliDisponibili.aggiungiMerce(nomeArticolo,quantitaArticolo);
    }

    /**
     * Compito di registro magazzino di poter visualizzare le bevande disponibili
     * precondizione: bevandeEExtra != null
     */
    private void visualizzaBevande(){
        System.out.println("Le bevande disponibili sono:");
        HashMap<Raggruppabile, QuantitaMerce> bevande = bevandeEExtra.getConsumo();
        Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = bevande.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Raggruppabile, QuantitaMerce> entry = iterator.next();
            Raggruppabile key = entry.getKey();
            QuantitaMerce value = entry.getValue();
            if(value.getUnitaMisura().equalsIgnoreCase("l"))
                System.out.println("Bevanda: " + key.getNome() + " Quantita': " + value.getQuantita() + " " + value.getUnitaMisura());
        }
    }

    /**
     * Compito di registro magazzino di poter visualizzare le bevande disponibili
     * precondizione: bevandeEExtra != null
     */
    private void visualizzaGeneriExtra(){
        System.out.println("I generi extra disponibili sono:");
        HashMap<Raggruppabile, QuantitaMerce> extra = bevandeEExtra.getConsumo();
        Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = extra.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Raggruppabile, QuantitaMerce> entry = iterator.next();
            Raggruppabile key = entry.getKey();
            QuantitaMerce value = entry.getValue();
            if(!value.getUnitaMisura().equalsIgnoreCase("l"))
                System.out.println("Generi extra disponibili: " + key.getNome() + " Quantita': " + value.getQuantita() + " " + value.getUnitaMisura());
        }
    }
    public void setArticoliDisponibili(Merce articoliDisponibili) {
        this.articoliDisponibili = articoliDisponibili;
    }

    public Merce getArticoliDisponibili() {
        return articoliDisponibili;
    }
}
