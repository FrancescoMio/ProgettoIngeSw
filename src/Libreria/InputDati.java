package Libreria;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.ZoneId;

public class InputDati {
    private static final String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
    private static final String ERRORE_MINIMO = "Attenzione: e' richiesto un valore maggiore o uguale a ";
    private static final String ERRORE_STRINGA_VUOTA = "Attenzione: non hai inserito alcun carattere";
    private static final String ERRORE_MASSIMO = "Attenzione: e' richiesto un valore minore o uguale a ";
    private static final String MESSAGGIO_AMMISSIBILI = "Attenzione: i caratteri ammissibili sono: ";
    private static final char RISPOSTA_SI = 'S';
    private static final char RISPOSTA_NO = 'N';
    private static final Scanner lettore = creaScanner();

    public InputDati() {
    }

    private static Scanner creaScanner() {
        Scanner creato = new Scanner(System.in);
        return creato;
    }

    public static String leggiStringa(String messaggio) {
        System.out.print(messaggio);
        return lettore.next();
    }

    public static String leggiStringaConSpazi(String messaggio){
        System.out.print(messaggio);
        Scanner scanner = creaScanner();
        return scanner.nextLine();
    }

    public static String leggiStringaNonVuota(String messaggio) {
        boolean finito = false;
        String lettura = null;

        do {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();
            if (lettura.length() > 0) {
                finito = true;
            } else {
                System.out.println("Attenzione: non hai inserito alcun carattere");
            }
        } while(!finito);

        return lettura;
    }

    public static char leggiChar(String messaggio) {
        boolean finito = false;
        char valoreLetto = 0;

        do {
            System.out.print(messaggio);
            String lettura = lettore.next();
            if (lettura.length() > 0) {
                valoreLetto = lettura.charAt(0);
                finito = true;
            } else {
                System.out.println("Attenzione: non hai inserito alcun carattere");
            }
        } while(!finito);

        return valoreLetto;
    }

    public static char leggiUpperChar(String messaggio, String ammissibili) {
        boolean finito = false;
        boolean var3 = false;

        char valoreLetto;
        do {
            valoreLetto = leggiChar(messaggio);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (ammissibili.indexOf(valoreLetto) != -1) {
                finito = true;
            } else {
                System.out.println("Attenzione: i caratteri ammissibili sono: " + ammissibili);
            }
        } while(!finito);

        return valoreLetto;
    }

    public static int leggiIntero(String messaggio) {
        boolean finito = false;
        int valoreLetto = 0;

        do {
            System.out.print(messaggio);

            try {
                valoreLetto = lettore.nextInt();
                finito = true;
            } catch (InputMismatchException var5) {
                System.out.println("Attenzione: il dato inserito non e' nel formato corretto");
                String var4 = lettore.next();
            }
        } while(!finito);

        return valoreLetto;
    }

    public static int leggiInteroPositivo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 1);
    }

    public static int leggiInteroNonNegativo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 0);
    }

    public static int leggiInteroConMinimo(String messaggio, int minimo) {
        boolean finito = false;
        boolean var3 = false;

        int valoreLetto;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo) {
                finito = true;
            } else {
                System.out.println("Attenzione: e' richiesto un valore maggiore o uguale a " + minimo);
            }
        } while(!finito);

        return valoreLetto;
    }

    public static int leggiIntero(String messaggio, int minimo, int massimo) {
        boolean finito = false;
        boolean var4 = false;

        int valoreLetto;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo && valoreLetto <= massimo) {
                finito = true;
            } else if (valoreLetto < minimo) {
                System.out.println("Attenzione: e' richiesto un valore maggiore o uguale a " + minimo);
            } else {
                System.out.println("Attenzione: e' richiesto un valore minore o uguale a " + massimo);
            }
        } while(!finito);

        return valoreLetto;
    }

    public static double leggiDouble(String messaggio) {
        boolean finito = false;
        double valoreLetto = 0.0D;

        do {
            System.out.print(messaggio);

            try {
                valoreLetto = lettore.nextDouble();
                finito = true;
            } catch (InputMismatchException var6) {
                System.out.println("Attenzione: il dato inserito non e' nel formato corretto");
                String var5 = lettore.next();
            }
        } while(!finito);

        return valoreLetto;
    }

    public static double leggiDoubleCompreso(String messaggio, double minimo, double massimo){
        boolean finito = false;
        double valoreLetto = 0.0D;

        do {
            valoreLetto = leggiDouble(messaggio);
            if (valoreLetto >= minimo && valoreLetto <= massimo) {
                finito = true;
            } else {
                System.err.println("Attenzione: e' richiesto un valore maggiore o uguale a " + minimo + " e minore o uguale a " + massimo);
            }
        } while(!finito);

        return valoreLetto;
    }

    public static double leggiDoubleConMinimo(String messaggio, double minimo) {
        boolean finito = false;
        double valoreLetto = 0.0D;

        do {
            valoreLetto = leggiDouble(messaggio);
            if (valoreLetto >= minimo) {
                finito = true;
            } else {
                System.out.println("Attenzione: e' richiesto un valore maggiore o uguale a " + minimo);
            }
        } while(!finito);

        return valoreLetto;
    }

    public static boolean yesOrNo(String messaggio) {
        String mioMessaggio = messaggio + "(S/N)";
        String var10001 = String.valueOf('S');
        char valoreLetto = leggiUpperChar(mioMessaggio, var10001 + "N");
        return valoreLetto == 'S';
    }

    /**
     * Metodo per la scelta fra bevanda o genere alimentare extra
     * @param messaggio
     * @return
     */
    public static boolean BevandaOGenere(String messaggio) {
        String mioMessaggio = messaggio + "(B/G)";
        String var10001 = String.valueOf('B');
        char valoreLetto = leggiUpperChar(mioMessaggio, var10001 + "G");
        return valoreLetto == 'B';
    }

    public static LocalDate leggiData(String messaggio) {
        boolean finito = false;
        Date data = null;
        String valoreLetto = "";
        LocalDate localDate = null;
        do {
            System.out.print(messaggio);
            try {
                valoreLetto = lettore.next();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormat.setLenient(false);
                    data = dateFormat.parse(valoreLetto);
                    localDate = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); //converte Date in LocalDate
                    System.out.println("Data inserita: " + localDate);
                    finito = true;  //se non viene lanciata eccezione, la data e' corretta
            } catch (Exception e) {
                System.err.println("formato data non valido");
                String var4 = lettore.next();
            }
        } while(!finito);

        return localDate;
    }

    public static boolean controllaData(String data){
        String []dataSplit = data.split("/");
        for(int i = 0; i < dataSplit.length; i++){
            try {
                Integer.parseInt(dataSplit[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

}
