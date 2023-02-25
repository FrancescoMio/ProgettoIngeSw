package unibs.ids.ristorante;

import Libreria.MyMenu;

public class Main {
    public static void main(String[] args) {

        String [] voci = {"1-Gestore","2-Addetto alle prenotazioni","3-Magazziniere"};
        MyMenu menu = new MyMenu("Menu",voci);
        int scelta = menu.scegli();
        if(scelta == 1){
            Ristorante ristorante = new Ristorante();
        }

    }
}
