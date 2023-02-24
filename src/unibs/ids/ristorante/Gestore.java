package unibs.ids.ristorante;

import Libreria.InputDati;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Gestore extends Utente {

    private Ristorante ristorante;

    public Gestore(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    /**
     * inizializza tutte le variabili ( Ristorante)
     */
    private void inizializzaRistorante(){
        ArrayList<Piatto> piatti = new ArrayList<>();

        System.out.println("Buongiorno gestore, le chiediamo gentilmente di inserire i dati per il suo ristorante: ");
        String nome = InputDati.leggiStringa("Inserire nome ristorante: ");
        int caricoXpersona = InputDati.leggiIntero("Inserire carico medio per persona:");
        int posti = InputDati.leggiIntero("Inserire posti a sedere");
        //ora inserisco i piatti
        piatti = inserisciPiatti();//metodo per inserire piatti a mano, da fare quello che li inserisce da file


        //inserire insieme bevande, fare metodo, magari metodo unico va bene se si fanno i controlli nelle rispettive classi
        //inserire insieme generi extra, fare metodo
        //inserire consumo procapire bevande
        //inserire consumo procapite beni extra
    }

    private String getCaricoXPersona(Ristorante rist){
        return "Carico per persona: " + rist.getCaricoXPersona();
    }

    /**
     * metodo per inserire piatti uno ad uno dall'utente
     * @return
     */
    private ArrayList<Piatto> inserisciPiatti() {
        ArrayList<Piatto> piatti = new ArrayList<>();
        boolean scelta = true;
        do {
            System.out.println("Inserire piatto: ");
            String nome = InputDati.leggiStringa("Inserire nome piatto: ");
            Date inizio = InputDati.leggiData("Inserire data inizio disponibilità nel formato dd/MM/yyyy: ");
            Date fine = InputDati.leggiData("Inserire data fine disponibilità nel formato dd/MM/yyyy: ");

            HashMap<String, Integer> ingredienti = inserisciIngredienti();

            Ricetta ricetta = controlloRicetta(ingredienti, piatti);//se ricetta esiste già, uso quella già presente, altrimenti la creo

            Piatto piatto = new Piatto(nome, ricetta, inizio, fine);
            piatti.add(piatto);
        } while (scelta);
        return piatti;
    }

    /**
     * metodo di utlita' per inserire gli ingredienti di un piatto
     * @return
     */
    private HashMap<String, Integer> inserisciIngredienti(){
        HashMap<String, Integer> ingredienti = new HashMap<>();
        boolean scelta = true;
        do {
            String Ingrediente = InputDati.leggiStringa("Inserire nome ingrediente: ");
            String dose = InputDati.leggiStringa("Inserire dose opportuna dell'ingrediente: ");
            ingredienti.put(Ingrediente, Integer.parseInt(dose));
            scelta = InputDati.yesOrNo("Vuoi inserire un altro ingrediente?");
        } while (scelta);
        return ingredienti;
    }

    /**
     * metodo di utilita' per controllare se la ricetta esiste già, se esiste la ritorna, altrimenti la crea e la ritorna
     * @param ingredienti
     * @param piatti
     * @return
     */
    private Ricetta controlloRicetta (HashMap<String, Integer> ingredienti, ArrayList<Piatto> piatti){
        for (Piatto piatto : piatti){ //controllo esistenza della ricetta
            if (piatto.getRicetta().getIngredienti().equals(ingredienti)){
                return piatto.getRicetta();
            }
        }//se non esiste la ricetta, la creo e la ritorno per la creazione del piatto
        int numeroPorzioni = InputDati.leggiIntero("Inserire numero porzioni che derivano dalla preparazione della ricetta: ");
        double caricoXPorzione = InputDati.leggiDouble("Inserire carico di lavoro per porzione: ");//DA METTERE A POSTO, DEVE ESSERE UNA PORZIONE DI CARICO DI LAVORO PER PERSONA
        Ricetta ricetta = new Ricetta(ingredienti, numeroPorzioni, caricoXPorzione);
        return ricetta;
    }
}
