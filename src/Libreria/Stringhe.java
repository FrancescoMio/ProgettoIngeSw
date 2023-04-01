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
    public static final String titoloMenu = "SELEZIONARE TIPOLOGIA DI UTENTE:";
    public static String [] vociMenu = {"Gestore","Addetto alle prenotazioni","Magazziniere"};

    public static final String titoloMenuGestore = "SELEZIONARE MODALITA' UTILIZZO: ";
    public static String [] vociMenuGestore = {"Crea nuova configurazione","Visualizza parametri ristorante"
                                                ,"Imposta carico lavoro per persona","Imposta posti a sedere disponibili nel ristorante","Aggiungi bevanda","Aggiungi genere alimentare extra",
                                                "Aggiungi nuovo piatto","Aggiungi nuovo menù tematico"};
    public static String titoloMenuVisualizzazione = "SELEZIONARE COSA DESIDERA VISUALIZZARE";
    public static String [] vociMenuVisualizzazione = {"Carico lavoro per persona", "Posti a sedere","Insieme delle bevande",
                                                         "Insieme dei generi alimentari extra","Consumo pro-capite bevande",
                                                        "Consumo pro-capite generi alimentari extra","Corrispondenza piatto-ricetta",
                                                        "Denominazione e periodo validità di ciascun piatto","Ricette","Menù tematici"};
    public static final String titoloMenuAuth = "FASE DI AUTENTICAZIONE:";
    public static String [] vociMenuAuth = {"REGISTRATI","ACCEDI"};
    public static final String titoloMenuMagazziniere = "SELEZIONARE OPERAZIONE:";
    public static String [] vociMenuMagazziniere = {"Porta ingredienti in cucina","Porta bevanda/genere " +
                                                    "alimentare in sala", "Riporta in magazzino ingredienti non consumati",
                                                    "Rimuovi scarti dal magazzino"};

    public static final String erroreSceltaPiatto = "Numero piatto inserito non valido!";
    public static final String erroreSceltaMenu = "Numero menù inserito non valido!";

    public static final String titoloMenuAddetto = "SELEZIONARE OPERAZIONE:";
    public static String [] vociMenuAddetto = {"Crea nuova prenotazione"};
    public static String configurazioneCaricata = "CONFIGURAZIONE CARICATA CORRETTAMENTE";
    public static String titoloOrdine = "SELEZIONA ORDINE:";
    public static String [] vociOrdine = {"Menù tematico","Piatto del menù alla carta"};


}
