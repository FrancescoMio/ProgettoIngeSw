package unibs.ids.ristorante;

import Libreria.InputDati;
import static Libreria.Stringhe.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RegistroMagazzino {
    private Merce articoliDisponibili; //lista dei prodotti disponibili nel magazzino

    public RegistroMagazzino(){
        articoliDisponibili = new Merce();
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

    public void setArticoliDisponibili(Merce articoliDisponibili) {
        this.articoliDisponibili = articoliDisponibili;
    }

    public Merce getArticoliDisponibili() {
        return articoliDisponibili;
    }
}
