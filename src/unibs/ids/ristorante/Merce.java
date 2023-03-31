package unibs.ids.ristorante;

import java.util.HashMap;
import java.util.Map;

public class Merce{
    private HashMap<String, QuantitaMerce> articoli;
    public Merce() {
        articoli = new HashMap<>();
    }

    public void aggiungiMerce(String nome, QuantitaMerce quantitaMerce) {
        articoli.put(nome, quantitaMerce);
    }

    /**
     * Metodo per rimuovere una determinata quantità di un prodotto
     * @param prodottiDaRimuovere prodotti da rimuovere
     * precondizione: i prodotti da rimuovere devono essere presenti nella lista della spesa
     *                e la quantità da rimuovere deve essere minore o uguale a quella presente
     */
    public void rimuoviProdotti(HashMap<String,QuantitaMerce> prodottiDaRimuovere) {
        for (Map.Entry<String, QuantitaMerce> entry : prodottiDaRimuovere.entrySet()){
            String nomeArticolo = entry.getKey();
            QuantitaMerce quantitaArticolo = entry.getValue();
            double quantita = quantitaArticolo.getQuantita();
            String unitaMisura = quantitaArticolo.getUnitaMisura();

            QuantitaMerce quantitaArticoloOld = articoli.get(nomeArticolo);
            double quantitaOld = quantitaArticoloOld.getQuantita();
            QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaOld-quantita,unitaMisura);
            articoli.replace(nomeArticolo,quantitaArticoloOld,quantitaAggiornata);
        }
    }
    public HashMap<String, QuantitaMerce> getArticoli() {
        return articoli;
    }
    public void setArticoli(HashMap<String, QuantitaMerce> articoli) {
        this.articoli = articoli;
    }

    public Merce aggregaMerci(Merce listaSpesa, Merce aggiunta){
        for(String nome : aggiunta.getArticoli().keySet()) {
            if(listaSpesa.getArticoli().containsKey(nome)) {
                double quantita = listaSpesa.getArticoli().get(nome).getQuantita() + aggiunta.getArticoli().get(nome).getQuantita();
                listaSpesa.getArticoli().get(nome).setQuantita(quantita);
            } else {
                listaSpesa.aggiungiMerce(nome, aggiunta.getArticoli().get(nome));
            }
        }
        return listaSpesa;
    }

    public Merce aggiungiIngrediente(HashMap<String, Double> ingrediente){
        Merce lista = new Merce();
        for(String nome : ingrediente.keySet()) {
            QuantitaMerce quantita = new QuantitaMerce(ingrediente.get(nome), "g");
            lista.aggiungiMerce(nome, quantita);
        }
        return lista;
    }

    /**
     * Metodo per aggiornare la lista della spesa in base alle diverse prenotazioni
     * Il valore dell'hashmap rappresenta la quantità dell'ingrediente in GRAMMI
     * @param listaIngredienti lista degli ingredienti
     * precondizione: la lista degli ingredienti deve essere presente nel magazzino
     */
    public void aggiungiIngredienti(HashMap<String, QuantitaMerce> listaIngredienti){
        for (Map.Entry<String, QuantitaMerce> entry : listaIngredienti.entrySet()) {
            String nomeIngrediente = entry.getKey();
            QuantitaMerce quantitaIngrediente = entry.getValue();
            double quantita = quantitaIngrediente.getQuantita();
            String unitaMisura = quantitaIngrediente.getUnitaMisura();
            if(articoli.containsKey(nomeIngrediente)){
                QuantitaMerce quantitaMerce = articoli.get(nomeIngrediente);
                double quantitaOld = quantitaMerce.getQuantita();
                QuantitaMerce quantitaMerceNuova = new QuantitaMerce(quantitaOld+quantita,unitaMisura);
                articoli.put(nomeIngrediente,quantitaMerceNuova);
            }else{
                QuantitaMerce quantitaMerceNuova = new QuantitaMerce(quantita,unitaMisura);
                articoli.put(nomeIngrediente,quantitaMerceNuova);
            }
        }
    }

    /**
     * Metodo che calcola la differenza tra la lista della spesa e la merce disponibile nel magazzino, in modo che venga
     * comprata solo quella necessaria
     * @param merceMagazzino merce del magazzino
     * postcondizione: la lista della spesa viene aggiornata con la quantità di merce necessaria
     */
    public void differenzaScorte(Merce merceMagazzino){
        HashMap<String, QuantitaMerce> articoliDisponibiliMagazzino = merceMagazzino.getArticoli();
        HashMap<String, QuantitaMerce> articoliTemp = new HashMap<>();
        articoliTemp.putAll(articoli);
        for (Map.Entry<String, QuantitaMerce> entry : articoliTemp.entrySet()){
            String nomeArticolo = entry.getKey();
            if(articoliDisponibiliMagazzino.containsKey(nomeArticolo)){
                QuantitaMerce quantitaMagazzino = articoliDisponibiliMagazzino.get(nomeArticolo);
                double quantitaNelMagazzino = quantitaMagazzino.getQuantita();
                QuantitaMerce quantitaArticoloDaAcquistare = articoli.get(nomeArticolo);
                double quantitaDaAcquistare = quantitaArticoloDaAcquistare.getQuantita();
                String unitaMisura = quantitaArticoloDaAcquistare.getUnitaMisura();
                if(quantitaNelMagazzino < quantitaDaAcquistare){
                    QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaDaAcquistare - quantitaNelMagazzino,unitaMisura);
                    articoli.put(nomeArticolo,quantitaAggiornata);
                }else articoli.remove(nomeArticolo);
            }
        }
    }

    /**
     * Metodo per incrementare la quantità di un articolo del 10%
     */
    public void incrementoPercentuale(){
        for (Map.Entry<String, QuantitaMerce> entry : articoli.entrySet()){
            String nomeArticolo = entry.getKey();
            QuantitaMerce quantita = entry.getValue();
            double quantitaPercentuale = quantita.getQuantita() * 110 / 100;
            String unitaMisura = quantita.getUnitaMisura();
            QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaPercentuale,unitaMisura);
            articoli.put(nomeArticolo,quantitaAggiornata);
        }
    }

    /**
     * Metodo per aggiungere le bevande e i generi extra ad una lista
     * @param consumoProCapiteBevande consumo delle bevande per capo
     * @param consumoProCapiteGeneriExtra consumo dei generi extra per capo
     * @param numeroCoperti numero di coperti
     * precondizione: numeroCoperti > 0
     */
    public void aggiungiBevandeGeneri(ConsumoProCapiteBevande consumoProCapiteBevande, ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra, int numeroCoperti){
        HashMap<Raggruppabile, QuantitaMerce> consumoBevande = consumoProCapiteBevande.getConsumo();
        HashMap<Raggruppabile, QuantitaMerce> consumoGeneri = consumoProCapiteGeneriExtra.getConsumo();
        for (Map.Entry<Raggruppabile, QuantitaMerce> entry : consumoBevande.entrySet()){
            Raggruppabile raggruppabile = entry.getKey();
            QuantitaMerce quantitaMerce = entry.getValue();
            double quantitaBevanda = quantitaMerce.getQuantita() * numeroCoperti;
            QuantitaMerce quantitaNuova = new QuantitaMerce(quantitaBevanda,"l");
            articoli.put(raggruppabile.getNome(),quantitaNuova);
        }
        for (Map.Entry<Raggruppabile, QuantitaMerce> entry : consumoGeneri.entrySet()){
            Raggruppabile raggruppabile = entry.getKey();
            QuantitaMerce quantitaMerce = entry.getValue();
            double quantitaGenere = quantitaMerce.getQuantita() * numeroCoperti;
            QuantitaMerce quantitaNuova = new QuantitaMerce(quantitaGenere,"hg");
            articoli.put(raggruppabile.getNome(),quantitaNuova);
        }
    }

    public void visualizzaMerce() {
        System.out.println("MERCE:");
        for (Map.Entry<String, QuantitaMerce> entry : articoli.entrySet()) {
            String nome = entry.getKey();
            QuantitaMerce quantitaMerce = entry.getValue();
            System.out.println("Nome: " + nome +
                                "\nQuantità: " + quantitaMerce.getQuantita() + quantitaMerce.getUnitaMisura());
        }
    }
}