package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.Json;
import Libreria.MyMenu;
import Libreria.PasswordManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static Libreria.Stringhe.*;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
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


    public static void menuGestore() throws IOException,NoSuchAlgorithmException {
        if(PasswordManager.autenticazione()){
            MyMenu menu = new MyMenu(titoloMenuGestore,vociMenuGestore);
            int scelta = menu.scegli();
            if(scelta == 1){
                Ristorante ristorante = new Ristorante();
            } else if (scelta == 2) {
                Ristorante ristorante = Json.caricaDati();
                System.out.println(ristorante.toString());
            }else return;
        }
    }

}
