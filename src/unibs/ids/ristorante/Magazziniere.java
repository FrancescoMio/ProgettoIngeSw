package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Magazziniere extends Utente {

    Merce listaSpesa;

    public Magazziniere(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    //TODO: da gestire possibile incremento del quantitativo e inserire bevande e generi extra
    public void creaListaSpesaGiornaliera(Prenotazione prenotazione, RegistroMagazzino registroMagazzino) {
        Merce listaSpesa = new Merce();
        // todo: listaSpesa = aggiuntaBevande(listaSpesa,...);   saranno da aggiungere altri paramentri
        //todo: listaSpesa = aggiuntaGeneriExtra(listaSpesa,...);  saranno da aggiungere altri paramentri
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

    public Merce getListaSpesa() {
        return listaSpesa;
    }

}
