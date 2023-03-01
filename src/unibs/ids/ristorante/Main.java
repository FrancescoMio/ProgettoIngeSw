package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.LeggiJSON;
import Libreria.MyMenu;
import Libreria.MyUtil;

import java.time.LocalDate;

import static unibs.ids.ristorante.Stringhe.*;

public class Main {
    public static void main(String[] args) {
        LeggiJSON json = new LeggiJSON();
        MyMenu menuUtenza = new MyMenu(titoloMenu,vociMenu);
        int scelta = menuUtenza.scegli();
        switch(scelta){
            case 1:
                menuGestore();
                break;
            case 2:
                // code block for value2
                break;
            case 3:
                // code block for value3
                break;
            default:
                // code block if none of the values match
                break;
        }
    }


    public static void menuGestore(){
        MyMenu menu = new MyMenu(titoloMenuGestore,vociMenuGestore);
        int scelta = menu.scegli();
        if(scelta == 1){
            Ristorante ristorante = new Ristorante();
        }
        else{
            //carica configurazione tramite file JSON
        }
    }

}
