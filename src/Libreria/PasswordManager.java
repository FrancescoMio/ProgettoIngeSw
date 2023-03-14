package Libreria;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static Libreria.Stringhe.*;

/**
 * Viene utilizzata la libreria Java Security in particolare la classe MessageDigest per generare un hash della password
 * utilizzando l'algoritmo SHA-256
 */
public class PasswordManager {

    /**
     * La password in input viene convertita in un array di byte, e quindi l'algoritmo SHA-256 viene applicato a questo
     * array di byte per produrre un hash. L'hash viene quindi convertito in una stringa esadecimale e restituito.
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());

        // Converte l'array di byte in una stringa esadecimale
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Sistemare il metodo: sostituire gli if con un switch case, circondare il tutto con do-while, creare due metodi separati
     * per la registrazione ed uno per l'accesso, creare un metodo unico per la lettura dal file JSON per la verifica della presenza
     * delle credenziali
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean autenticazione(String nomeFile) throws NoSuchAlgorithmException {
        JSONObject utentiJson = new JSONObject();
        ArrayList<JSONObject> elencoUtenti = Json.caricaCredenziali(nomeFile);
        MyMenu menuAuth = new MyMenu(titoloMenuAuth, vociMenuAuth);
        boolean auth = false;
        do {
            int scelta = menuAuth.scegli();
            if (scelta == 1) {
                String nome = InputDati.leggiStringaConSpazi("Inserire nome: ");
                String cognome = InputDati.leggiStringaConSpazi("Inserire cognome: ");
                if (!controlloEsistenza(nome, cognome,"","registrazione",nomeFile)) {
                    String password = InputDati.leggiStringaNonVuota("Inserisci password: ");
                    String hashPassword = hashPassword(password);
                    JSONObject utenteJson = new JSONObject();
                    utenteJson.put("nome", nome);
                    utenteJson.put("cognome", cognome);
                    utenteJson.put("hashPassword", hashPassword);
                    elencoUtenti.add(utenteJson);
                    utentiJson.put("elencoUtenti", elencoUtenti);
                    Json.salvaCredenziali(utentiJson,nomeFile);
                    System.out.println(ANSI_GREEN + "REGISTRAZIONE EFFETTUATA CORRETTAMENTE!" + ANSI_RESET);
                    return true;
                }
            } else if (scelta == 2) {
                String nome = InputDati.leggiStringaConSpazi("Inserire nome: ");
                String cognome = InputDati.leggiStringaConSpazi("Inserire cognome: ");
                String password = InputDati.leggiStringaNonVuota("Inserisci password: ");
                String hashPassword = hashPassword(password);
                if(controlloEsistenza(nome,cognome,hashPassword,"login",nomeFile))
                    return true;
                System.err.println(ANSI_RED + "\nACCESSO NEGATO\n" + ANSI_RESET);
            } else {
                System.out.println(ANSI_YELLOW + "---ARRIVEDERCI---" + ANSI_RESET);
                auth = true;
            }
        }while (!auth) ;
        return false;
    }

    public static boolean controlloEsistenza(String nome, String cognome, String password, String tipo,String nomeFile){
        ArrayList<JSONObject> elencoUtenti = Json.caricaCredenziali(nomeFile);
        if(tipo.equalsIgnoreCase("registrazione")){
            for (JSONObject utente : elencoUtenti) {
                String nomeUtente = (String) utente.get("nome");
                String cognomeUtente = (String) utente.get("cognome");
                if (nome.equalsIgnoreCase(nomeUtente) && cognome.equalsIgnoreCase(cognomeUtente)) {
                    System.out.println(ANSI_RED + "\nUTENTE GIA' REGISTRATO\n" + ANSI_RESET);
                    return true;
                }
            }
        } else if (tipo.equalsIgnoreCase("login")) {
            for (JSONObject utente : elencoUtenti) {
                String nomeUtente = (String) utente.get("nome");
                String cognomeUtente = (String) utente.get("cognome");
                String hashPasswordUtente = (String) utente.get("hashPassword");
                if (nome.equalsIgnoreCase(nomeUtente) && cognome.equalsIgnoreCase(cognomeUtente) && hashPasswordUtente.equalsIgnoreCase(password)) {
                    System.out.println(ANSI_GREEN + "\nACCESSO EFFETUATO\n" + ANSI_RESET);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Metodo per mascherare la password
     * @param messaggio
     * @return
     */
    public static String leggiPassword (String messaggio) {
        EraserThread et = new EraserThread(messaggio);
        Thread mask = new Thread(et);
        mask.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            String password = in.readLine();
            et.stopMasking();
            System.out.println(password);
            return password;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}