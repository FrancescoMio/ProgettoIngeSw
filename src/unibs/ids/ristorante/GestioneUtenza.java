package unibs.ids.ristorante;

import Libreria.Json;
import Libreria.MyMenu;
import Libreria.PasswordManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static Libreria.Stringhe.*;

/**
 * Classe per la gestione della fase di avvio del programma
 */
public class GestioneUtenza {

    private Ristorante ristorante;
    public GestioneUtenza(){}

    public void menuGestore() throws IOException, NoSuchAlgorithmException {
        if(PasswordManager.autenticazione("./credenzialiGestore.json")){
            MyMenu menuIniziale = new MyMenu(titoloMenuGestore,vociMenuGestore);
            boolean finito = false;
            do {
                int scelta = menuIniziale.scegli();
                if (scelta == 1) {
                    ristorante = new Ristorante();
                } else if (scelta == 2) {
                    ristorante = Json.caricaDati();
                } else if (scelta == 3) {
                    ristorante.visualizzaParametri();
                } else {
                    System.out.println(ANSI_GREEN + "---ARRIVEDERCI!---" + ANSI_RESET);
                    finito = true;
                }
            }while (!finito);
        }
    }

    public void menuAddettoPrenotazioni() throws NoSuchAlgorithmException {
        if(PasswordManager.autenticazione("./credenzialiAddettoPrenotazioni.json")){
            if(this.ristorante == null)
                ristorante = Json.caricaDati();
            ristorante.filtraPrenotazioni();
            MyMenu menu = new MyMenu(titoloMenuAddetto,vociMenuAddetto);
            boolean finito = false;
            do{
                int scelta = menu.scegli();
                switch (scelta){
                    case 1:
                        ristorante.creaNuovaPrenotazione();
                        break;
                    default:
                        System.out.println(ANSI_GREEN + "---ARRIVEDERCI!---" + ANSI_RESET);
                        finito = true;
                        break;
                }
            }while (!finito);
        }
    }

    public void menuMagazziniere() throws NoSuchAlgorithmException {
        if(PasswordManager.autenticazione("./credenzialiMagazziniere.json")){
            if(this.ristorante == null)
                ristorante = Json.caricaDati();
            ristorante.filtraPrenotazioni();
            MyMenu menu = new MyMenu(titoloMenuMagazziniere, vociMenuMagazziniere);
            boolean finito = false;
            do {
                int scelta = menu.scegli();
                if (scelta == 1) {
                    ristorante.creaListaSpesa();
                } else if (scelta == 2) {
                    ristorante.portaIngredientiInCucina();
                } else if (scelta == 3) {
                    ristorante.portaBevandaGenereInSala();
                }else if (scelta == 4) {
                    ristorante.riportaIngredientiNonConsumati();
                } else if (scelta == 5) {
                    ristorante.rimuoviScartiDalMagazzino();
                }else {
                    System.out.println(ANSI_GREEN + "---ARRIVEDERCI!---" + ANSI_RESET);
                    finito = true;
                }
            }while (!finito);
        }
    }
}
