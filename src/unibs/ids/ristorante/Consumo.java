package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Consumo {
    protected HashMap<Raggruppabile,QuantitaMerce> consumoProCapite;

    public Consumo(Raggruppabile insieme, QuantitaMerce quantita){
        this.consumoProCapite = new HashMap<>();
        this.consumoProCapite.put(insieme,quantita);
    }

    public Consumo(){
        consumoProCapite = new HashMap<>();
    }

    public HashMap<Raggruppabile,QuantitaMerce> getConsumo(){
        return this.consumoProCapite;
    }

    public void setConsumo(HashMap<Raggruppabile,QuantitaMerce> consumo){
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

    @Override
    public String toString() {
        String consumo = "";
        for (Map.Entry<Raggruppabile, QuantitaMerce> entry : consumoProCapite.entrySet()) {
            String nome = entry.getKey().getNome();
            QuantitaMerce quantitaMerce = entry.getValue();
            double quantita = quantitaMerce.getQuantita();
            String unitaMisura = quantitaMerce.getUnitaMisura();
            consumo += "- " + nome + ": " + quantita + unitaMisura + "\n";
        }
        return consumo;
    }
}
