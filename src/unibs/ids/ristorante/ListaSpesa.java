package unibs.ids.ristorante;

import Libreria.ServizioFile;

import java.io.File;
import java.util.HashMap;

public class ListaSpesa {
    private HashMap<String, QuantitaMerce> listaSpesa;

    public ListaSpesa() {//costruttore
        listaSpesa = new HashMap<String, QuantitaMerce>();
    }

    /*public ListaSpesa(File file){
        this.listaSpesa = ServizioFile.leggiListaSpesa(file);
    }*/

    public void aggiungiMerce(String nome, QuantitaMerce merce) {
        listaSpesa.put(nome, merce);
    }

    public void rimuoviMerce(String nome) {
        if(listaSpesa.containsKey(nome))
            listaSpesa.remove(nome);
        else
            System.out.println("Merce non presente nella lista");
    }

    public void visualizzaLista() {
        for(String nome : listaSpesa.keySet()) {
            System.out.println("Nome: " + nome + " Quantità: " + listaSpesa.get(nome).getQuantita() + listaSpesa.get(nome).getUnitaMisura());
        }
    }
}
