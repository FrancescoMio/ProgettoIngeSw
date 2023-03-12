package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Magazziniere extends Utente {

    private Merce listaSpesa;

    public Magazziniere(String nome, String cognome) {//Costruttore
        super(nome, cognome);
        listaSpesa = new Merce();
    }

    //TODO: da gestire possibile incremento del quantitativo (110% tipo)
    public void creaListaSpesaGiornaliera(ArrayList<Prenotazione> prenotazioni, RegistroMagazzino registroMagazzino) {
        //ogni volta la riinizializzo a 0
        listaSpesa = new Merce();
        listaSpesa = aggiuntaBevandeEExtra(listaSpesa, registroMagazzino.getBevandeEExtra());
        for(Prenotazione prenotazione : prenotazioni){
            Iterator<Map.Entry<Ordinabile,Integer>> iterator= prenotazione.getOrdine().entrySet().iterator(); //Iteratore per scorrere la mappa
            do{
                Map.Entry<Ordinabile,Integer> entry = iterator.next();//seleziono elemento con chiave Ordinabile e valore quantita di piatti o menu scelti
                Ordinabile menu = entry.getKey();//seleziono il menu
                //si puo fare senza if perchè se è un menuTematico o un menuCarta è anche un ordinabile
                //dopo lo tolgo
                if(menu instanceof MenuTematico){
                    Merce listaIngredientiTematici = ((MenuTematico) menu).getListaIngredienti();
                    listaSpesa = listaSpesa.aggregaMerci(listaSpesa, listaIngredientiTematici);
                }
                else if(menu instanceof MenuCarta){
                    Merce listaIngredientiPiatti = ((MenuCarta) menu).getListaIngredienti();
                    listaSpesa = listaSpesa.aggregaMerci(listaSpesa, listaIngredientiPiatti);
                }
            }while(iterator.hasNext());
        }
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
