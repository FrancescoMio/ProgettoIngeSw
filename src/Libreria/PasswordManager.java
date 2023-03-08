package Libreria;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static Libreria.Stringhe.titoloMenuAuth;
import static Libreria.Stringhe.vociMenuAuth;

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

    public static boolean autenticazione() throws NoSuchAlgorithmException {
        JSONObject utentiJson = new JSONObject();
        ArrayList<JSONObject> elencoUtenti = Json.caricaCredenziali();
        MyMenu menuAuth = new MyMenu(titoloMenuAuth,vociMenuAuth);
        int scelta = menuAuth.scegli();
        if(scelta == 1){
            String nome = InputDati.leggiStringaConSpazi("Inserire nome: ");
            String cognome = InputDati.leggiStringaConSpazi("Inserire cognome: ");
            String password = InputDati.leggiStringaNonVuota("Inserisci password: ");
            String hashPassword = hashPassword(password);
            JSONObject utenteJson = new JSONObject();
            utenteJson.put("nome",nome);
            utenteJson.put("cognome",cognome);
            utenteJson.put("hashPassword",hashPassword);
            elencoUtenti.add(utenteJson);
            utentiJson.put("elencoUtenti",elencoUtenti);
            Json.salvaCredenziali(utentiJson);
            System.out.println("REGISTRAZIONE EFFETTUATA CORRETTAMENTE!");
            return true;
        }else if(scelta == 2){
            String nome = InputDati.leggiStringaConSpazi("Inserire nome: ");
            String cognome = InputDati.leggiStringaConSpazi("Inserire cognome: ");
            String password = InputDati.leggiStringaNonVuota("Inserisci password: ");
            String hashPassword = hashPassword(password);

            JSONParser parser = new JSONParser();
            try (FileReader reader = new FileReader("./credenziali.json")){
                JSONObject jsonObject = (JSONObject) parser.parse(reader);
                for (JSONObject utente: elencoUtenti) {
                    String nomeUtente = (String) utente.get("nome");
                    String cognomeUtente = (String) utente.get("cognome");
                    String hashPasswordUtente = (String) utente.get("hashPassword");
                    if(nome.equalsIgnoreCase(nomeUtente) && cognome.equalsIgnoreCase(cognomeUtente) && hashPassword.equals(hashPasswordUtente)){
                        System.out.println("ACCESSO EFFETTUATO");
                        return true;
                    }
                }
                System.err.println("ACCESSO NEGATO");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("---ARRIVEDERCI---");
            return false;
        }
        return false;
    }

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
