package unibs.ids.ristorante;

import Libreria.Json;
import Libreria.MyMenu;
import Libreria.PasswordManager;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import static Libreria.Stringhe.*;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        MyMenu menuUtenza = new MyMenu(titoloMenu,vociMenu);
        GestioneUtenza gestioneUtenza = new GestioneUtenza();
        int scelta = menuUtenza.scegli();
        switch(scelta){
            case 1:
                gestioneUtenza.menuGestore();
                break;
            case 2:
                gestioneUtenza.menuAddettoPrenotazioni();
                break;
            case 3:
                gestioneUtenza.menuMagazziniere();
                break;
            default:
                break;
        }
    }

}
