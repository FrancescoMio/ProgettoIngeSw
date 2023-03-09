package unibs.ids.ristorante;

import java.util.HashMap;

public class Merce {

    private HashMap<String, QuantitaMerce> merce;
    public Merce() {//costruttore
        merce = new HashMap<String, QuantitaMerce>();
    }


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

    //todo: controllare che vada bene
    public Merce aggregaMerci(Merce listaSpesa, Merce aggiunta){
        for(String nome : aggiunta.getMerce().keySet()) {
            if(listaSpesa.getMerce().containsKey(nome)) {
                int quantita = listaSpesa.getMerce().get(nome).getQuantita() + aggiunta.getMerce().get(nome).getQuantita();
                listaSpesa.getMerce().get(nome).setQuantita(quantita);
            } else {
                listaSpesa.aggiungiMerce(nome, aggiunta.getMerce().get(nome));
            }
        }
        return listaSpesa;
    }

    public Merce aggiungiIngrediente(HashMap<String, Integer> ingrediente){
        Merce lista = new Merce();
        for(String nome : ingrediente.keySet()) {
            QuantitaMerce quantita = new QuantitaMerce(ingrediente.get(nome), "g");
            lista.aggiungiMerce(nome, quantita);
        }
        return lista;
    }
}