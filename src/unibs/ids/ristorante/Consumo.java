package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;

public class Consumo {
    private HashMap<Raggruppabile,QuantitaMerce> consumoProCapite;

    public Consumo(){
        this.consumoProCapite = new HashMap<>();
    }

    public Consumo(Raggruppabile insieme, QuantitaMerce quantita){
        this.consumoProCapite = new HashMap<>();
        this.consumoProCapite.put(insieme,quantita);
    }

    public HashMap<Raggruppabile,QuantitaMerce> getConsumo(){
        return this.consumoProCapite;
    }

    public void setConsumo(HashMap<Raggruppabile,QuantitaMerce> consumo){
        this.consumoProCapite.clear();
        this.consumoProCapite = consumo;
    }

    /**
     * Metodo per il controllo della giusta corrispondeza tra bevande e generi alimentari extra e la
     * nostra collezione "consumoProCapite"
     */
    public boolean controlloInsiemeGeneri(){
        ArrayList<Raggruppabile> insiemeGeneri = new ArrayList<>();
        insiemeGeneri.addAll(RegistroMagazzino.getGeneriAlimentariExtra());
        insiemeGeneri.addAll(RegistroMagazzino.getBevande());
        if(consumoProCapite.entrySet().size() != insiemeGeneri.size())
            return false;
        else{
            for (Raggruppabile item : insiemeGeneri)
                if(!consumoProCapite.containsKey(item))
                    return false;
            return true;
        }
    }

}
