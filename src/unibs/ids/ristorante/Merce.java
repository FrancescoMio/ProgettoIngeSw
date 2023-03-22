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

    public void rimuoviMerce(String nome) {
        if(articoli.containsKey(nome))
            articoli.remove(nome);
        else
            System.out.println("Merce non presente nella lista");
    }
    public HashMap<String, QuantitaMerce> getArticoli() {
        return articoli;
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
     * @param listaIngredienti
     */
    public void aggiungiIngredienti(HashMap<String, Double> listaIngredienti){
        for (Map.Entry<String, Double> entry : listaIngredienti.entrySet()) {
            String nomeIngrediente = entry.getKey();
            double quantita = entry.getValue();
            if(articoli.containsKey(nomeIngrediente)){
                QuantitaMerce quantitaMerce = articoli.get(nomeIngrediente);
                double quantitaOld = quantitaMerce.getQuantita();
                QuantitaMerce quantitaMerceNuova = new QuantitaMerce(quantitaOld+quantita,"g");
                articoli.put(nomeIngrediente,quantitaMerceNuova);
            }else{
                QuantitaMerce quantitaMerceNuova = new QuantitaMerce(quantita,"g");
                articoli.put(nomeIngrediente,quantitaMerceNuova);
            }
        }
    }

    public void differenzaScorte(Merce merceMagazzino){
        HashMap<String, QuantitaMerce> articoliDisponibiliMagazzino = merceMagazzino.getArticoli();
        for (Map.Entry<String, QuantitaMerce> entry : articoli.entrySet()){
            String nomeArticolo = entry.getKey();
            if(articoliDisponibiliMagazzino.containsKey(nomeArticolo)){
                QuantitaMerce quantitaMagazzino = articoliDisponibiliMagazzino.get(nomeArticolo);
                double quantitaNelMagazzino = quantitaMagazzino.getQuantita();
                QuantitaMerce quantitaArticolo = articoli.get(nomeArticolo);
                double quantitaAttuale = quantitaArticolo.getQuantita();
                QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaAttuale - quantitaNelMagazzino,"g");
                articoli.put(nomeArticolo,quantitaAggiornata);
            }
        }
    }

    public void incrementoPercentuale(){
        for (Map.Entry<String, QuantitaMerce> entry : articoli.entrySet()){
            String nomeArticolo = entry.getKey();
            QuantitaMerce quantita = entry.getValue();
            double quantitaPercentuale = quantita.getQuantita() * 110 / 100;
            QuantitaMerce quantitaAggiornata = new QuantitaMerce(quantitaPercentuale,"g");
            articoli.put(nomeArticolo,quantitaAggiornata);
        }
    }

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