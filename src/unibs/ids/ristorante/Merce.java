package unibs.ids.ristorante;

import java.util.HashMap;

public class Merce {

    private HashMap<String, QuantitaMerce> merce;
    public Merce() {//costruttore
        merce = new HashMap<String, QuantitaMerce>();
    }

    /*public ListaSpesa(File file){
        this.listaSpesa = ServizioFile.leggiListaSpesa(file);
    }*/

    public void aggiungiMerce(String nome, QuantitaMerce quantitaMerce) {
        merce.put(nome, quantitaMerce);
    }

    public void rimuoviMerce(String nome) {
        if(merce.containsKey(nome))
            merce.remove(nome);
        else
            System.out.println("Merce non presente nella lista");
    }

    public void visualizzaMerce() {
        for(String nome : merce.keySet()) {
            System.out.println("Nome: " + nome + " Quantità: " + merce.get(nome).getQuantita() + merce.get(nome).getUnitaMisura());
        }
    }
    public HashMap<String, QuantitaMerce> getMerce() {
        return merce;
    }
}