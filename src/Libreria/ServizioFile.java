package Libreria;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServizioFile {
    private static final String MSG_NO_FILE = "ATTENZIONE: NON TROVO IL FILE ";
    private static final String MSG_NO_LETTURA = "ATTENZIONE: PROBLEMI CON LA LETTURA DEL FILE ";
    private static final String MSG_NO_SCRITTURA = "ATTENZIONE: PROBLEMI CON LA SCRITTURA DEL FILE ";
    private static final String MSG_NO_CHIUSURA = "ATTENZIONE: PROBLEMI CON LA CHIUSURA DEL FILE ";

    public ServizioFile() {
    }

    public static Object caricaSingoloOggetto(File f) {
        Object letto = null;
        ObjectInputStream ingresso = null;

        try {
            ingresso = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
            letto = ingresso.readObject();
        } catch (FileNotFoundException var16) {
            System.out.println("ATTENZIONE: NON TROVO IL FILE " + f.getName());
        } catch (IOException var17) {
            System.out.println("ATTENZIONE: PROBLEMI CON LA LETTURA DEL FILE " + f.getName());
        } catch (ClassNotFoundException var18) {
            System.out.println("ATTENZIONE: PROBLEMI CON LA LETTURA DEL FILE " + f.getName());
        } finally {
            if (ingresso != null) {
                try {
                    ingresso.close();
                } catch (IOException var15) {
                    System.out.println("ATTENZIONE: PROBLEMI CON LA CHIUSURA DEL FILE " + f.getName());
                }
            }

        }

        return letto;
    }

    public static void salvaSingoloOggetto(File f, Object daSalvare) {
        ObjectOutputStream uscita = null;

        try {
            uscita = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            uscita.writeObject(daSalvare);
        } catch (IOException var12) {
            System.out.println("ATTENZIONE: PROBLEMI CON LA SCRITTURA DEL FILE " + f.getName());
        } finally {
            if (uscita != null) {
                try {
                    uscita.close();
                } catch (IOException var11) {
                    System.out.println("ATTENZIONE: PROBLEMI CON LA CHIUSURA DEL FILE " + f.getName());
                }
            }

        }

    }
}
