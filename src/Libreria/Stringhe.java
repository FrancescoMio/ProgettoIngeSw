package Libreria;

public class Stringhe {
    public static final String inserisciDataInizio = "Inserire data di inizio (dd/MM/yyyy): ";
    public static final String inserisciDataFine = "Inserire data di fine (dd/MM/yyyy): ";
    public static final String caricoLavoroMenuTematicoNonValido = "Il carico di lavoro del menù tematico ha superato i 4/3 del carico lavoro per persona pertanto non è possibile aggiungere ulteriori piatti a questo menù";
    public static final String nuovoMenuTematico = "Vuole Inserire un nuovo menù tematico?";
    public static final String nuovaBevandaOgenere = "Inserire una nuova bevanda o genere alimentare?";
    public static final String nuovaGenere = "Vuole Inserire un nuovo genere alimentare extra?";
    public static final String nomeMenuTematico = "Inserire nome del menù tematico: ";
    public static final String sceltaNumeroPiatto = "Inserire il numero del piatto che vorrebbe aggiungere al menu tematico:";
    public static final String nuovoPiattoMenuTematico = "Vuole inserire un altro piatto nel menù tematico?";
    public static final String nuovaConfigurazione = "COMPILARE LA CONFIGURAZIONE DEL RISTORANTE";
    public static final String lineSeparator = "--------------------------------------------------";
    public static final String creazioneMenuTematici = "CREAZIONE MENU' TEMATICI: ";
    public static final String titoloMenu = "\u001B[34mSELEZIONARE TIPOLOGIA DI UTENTE: \u001B[0m";
    public static String [] vociMenu = {"Gestore","Addetto alle prenotazioni","Magazziniere"};

    public static final String titoloMenuGestore = "SELEZIONARE MODALITA' UTILIZZO: ";
    public static String [] vociMenuGestore = {"Crea nuova configurazione","Carica configurazione","Visualizza parametri ristorante"};
    public static String titoloMenuVisualizzazione = "\u001B[34mSELEZIONARE COSA DESIDERA VISUALIZZARE\u001B[0m";
    public static String [] vociMenuVisualizzazione = {"Carico lavoro per persona", "Posti a sedere","Insieme delle bevande",
                                                         "Insieme dei generi alimentari extra","Consumo pro-capite bevande",
                                                        "Consumo pro-capite generi alimentari extra","Corrispondenza piatto-ricetta",
                                                        "Denominazione e periodo validità di ciascun piatto","Ricette","Menù tematici"};
    public static final String titoloMenuAuth = "\u001B[33mFASE DI AUTENTICAZIONE:\u001B[0m";
    public static String [] vociMenuAuth = {"REGISTRATI","ACCEDI"};

    public static final String erroreSceltaPiatto = "Numero piatto inserito non valido!";
    public static final String erroreSceltaMenu = "Numero menù inserito non valido!";

    public static final String titoloMenuAddetto = "\u001B[36mSELEZIONARE OPERAZIONE:\u001B[0m";
    public static String [] vociMenuAddetto = {"Crea nuova prenotazione"};
    public static String configurazioneCaricata = "CONFIGURAZIONE CARICATA CORRETTAMENTE";
    public static String titoloOrdine = "SELEZIONA ORDINE:";
    public static String [] vociOrdine = {"Menù tematico","Piatto del menù alla carta"};
    public static final String ANSI_GREEN = "\u001B[32m";
    //ANSI escape sequences. These escape sequences are special character sequences that are interpreted by the console as instructions to change the text color
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void presentazione(){
        String presentazione = "PROGETTO INGEGNERIA DEL SOFTWARE (A.A 2022/2023)\nAUTORI:\n-Francesco Mio\n-Matteo Cropelli";
        incorniciaStringa(presentazione);
    }
    public static void incorniciaStringa(String stringa){
        String stringaa = "Stringa da incornici";
        String cornice = "╔════════════════════════╗\n";
        cornice += "║                        ║\n";
        cornice += "╚════════════════════════╝\n";
        String stringaIncorniciata = String.format("%s║ %-22s ║\n%s", cornice, stringaa, cornice);
        System.out.println(stringaIncorniciata);
    }
}
