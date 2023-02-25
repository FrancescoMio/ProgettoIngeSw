package Libreria;

import unibs.ids.ristorante.ListaSpesa;
import unibs.ids.ristorante.QuantitaMerce;

import java.io.*;
import java.util.HashMap;

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

    /*public static HashMap<String, QuantitaMerce> leggiListaSpesa(File f) {
        HashMap<String, QuantitaMerce> listaSpesa = new HashMap();

        try {
            BufferedReader ingresso = new BufferedReader(new FileReader(f));
            String merce;
            QuantitaMerce quantitaMerce = null;
            String riga;
            int iter = 0;
            boolean ok = false;
            do{
                riga = ingresso.readLine();
                iter++;
                if(iter == 200){
                    System.out.println("ListaSpesa non trovata");
                    break;
                }
            }while(!riga.equalsIgnoreCase("ListaSpesa"));

            if(riga.equalsIgnoreCase("ListaSpesa"))
                ok = true;

            while(ok){
                riga = ingresso.readLine();
                if(riga == "FineListaSpesa")
                    ok = false;
                else{
                    String[] rigaSplittata = riga.split(",");
                    merce = rigaSplittata[0];
                    int quantita = Integer.parseInt(rigaSplittata[1]);
                    String umisura = rigaSplittata[2];
                    quantitaMerce = new QuantitaMerce(quantita, umisura);
                    listaSpesa.put(merce,quantitaMerce );
                }
            }
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
        return listaSpesa;
    }*/
}
