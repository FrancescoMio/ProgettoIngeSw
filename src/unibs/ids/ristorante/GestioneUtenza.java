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

    /**
     * Metodo per la gestione del menu iniziale
     * @throws IOException se il file non esiste
     * @throws NoSuchAlgorithmException se l'algoritmo non esiste
     * precondizione: il file credenzialiGestore.json deve esistere
     */
    public void menuGestore() throws IOException, NoSuchAlgorithmException {
        if(PasswordManager.autenticazione("dati/credenzialiGestore.json")){
            ristorante = Json.caricaDati();
            ristorante.filtraPrenotazioni();
            assert ristorante != null;
            MyMenu menuIniziale = new MyMenu(titoloMenuGestore,vociMenuGestore);
            boolean finito = false;
            do {
                int scelta = menuIniziale.scegli();
                if (scelta == 1) {
                    ristorante = new Ristorante();
                } else if (scelta == 2) {
                    ristorante.visualizzaParametri();
                } else if (scelta == 3) {
                    ristorante.impostaCaricoDiLavoroXPersona();
                }else if (scelta == 4) {
                    ristorante.impostaPostiAsedere();
                }else if (scelta == 5) {
                    ristorante.addBevanda();
                } else if (scelta == 6) {
                    ristorante.addGenereAlimentareExtra();
                }else if (scelta == 7) {
                    ristorante.addPiatto();
                }else if (scelta == 8) {
                    ristorante.addMenuTematico();
                }else {
                    System.out.println(ANSI_GREEN + "---ARRIVEDERCI!---" + ANSI_RESET);
                    finito = true;
                }
            }while (!finito);
        }
    }

    /**
     * Metodo per la gestione del menu per l'addetto alla sala
     * @throws NoSuchAlgorithmException se l'algoritmo non esiste
     * precondizione: il file credenzialiAddettoSala.json deve esistere
     */
    public void menuAddettoPrenotazioni() throws NoSuchAlgorithmException {
        if(PasswordManager.autenticazione("dati/credenzialiAddettoPrenotazioni.json")){
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

    /**
     * Metodo per la gestione del menu per il magazziniere
     * @throws NoSuchAlgorithmException se l'algoritmo non esiste
     * precondizione: il file credenzialiCuoco.json deve esistere
     */
    public void menuMagazziniere() throws NoSuchAlgorithmException {
        if(PasswordManager.autenticazione("dati/credenzialiMagazziniere.json")){
            if(this.ristorante == null)
                ristorante = Json.caricaDati();
            ristorante.filtraPrenotazioni();
            if(!Json.fattoListaSpesa())
                ristorante.creaListaSpesa();
            else System.out.println("lista spesa già fatta");
            MyMenu menu = new MyMenu(titoloMenuMagazziniere, vociMenuMagazziniere);
            boolean finito = false;
            do {
                int scelta = menu.scegli();
                if (scelta == 1) {
                    ristorante.portaIngredientiInCucina();
                } else if (scelta == 2) {
                    ristorante.portaBevandaGenereInSala();
                } else if (scelta == 3) {
                    ristorante.riportaInMagazzinoNonConsumati();
                }else if (scelta == 4) {
                    ristorante.rimuoviScartiDalMagazzino();
                }else {
                    System.out.println(ANSI_GREEN + "---ARRIVEDERCI!---" + ANSI_RESET);
                    finito = true;
                }
            }while (!finito);
        }
    }
}
