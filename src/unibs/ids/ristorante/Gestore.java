package unibs.ids.ristorante;

import Libreria.InputDati;

import java.util.*;

import static unibs.ids.ristorante.Stringhe.*;

public class Gestore extends Utente {

    public Gestore(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    //metodi da aggiungere

    /**
     * inizializza tutte le variabili (Ristorante)
     */
    public Set<Piatto> inizializzaPiatti() {

        return inserisciPiatti();//metodo per inserire piatti a mano, da fare quello che li inserisce da file
    }

    public String getNomeRistorante() {
        String nome = InputDati.leggiStringa("Inserire nome ristorante: ");
        return nome;
    }

    public int caricoXpersona() {
        int carico = InputDati.leggiIntero("Inserire carico medio per persona:");
        return carico;
    }

    public int postiASedere() {
        int posti = InputDati.leggiIntero("Inserire posti a sedere");
        return posti;
    }

    /**
     * metodo per inserire piatti uno ad uno dall'utente
     *
     * @return
     */
    public Set<Piatto> inserisciPiatti() {
        Set<Piatto> piatti = new HashSet<>();
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
            scelta = InputDati.yesOrNo("Vuoi inserire un altro piatto?");
        } while (scelta);
        return piatti;
    }

    /**
     * metodo di utlita' per inserire gli ingredienti di un piatto
     *
     * @return
     */
    public HashMap<String, Integer> inserisciIngredienti() {
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
     *
     * @param ingredienti
     * @param piatti
     * @return
     */
    public Ricetta controlloRicetta(HashMap<String, Integer> ingredienti, Set<Piatto> piatti) {
        for (Piatto piatto : piatti) { //controllo esistenza della ricetta
            if (piatto.getRicetta().getIngredienti().equals(ingredienti)) {
                return piatto.getRicetta();
            }
        }//se non esiste la ricetta, la creo e la ritorno per la creazione del piatto
        int numeroPorzioni = InputDati.leggiIntero("Inserire numero porzioni che derivano dalla preparazione della ricetta: ");
        double caricoXPorzione = InputDati.leggiDouble("Inserire carico di lavoro per porzione: ");//DA METTERE A POSTO, DEVE ESSERE UNA PORZIONE DI CARICO DI LAVORO PER PERSONA
        boolean ok = false;
        do {
            if (caricoXPorzione < Ristorante.getCaricoXPersona()) {
                ok = true;
            } else {
                System.out.println("Carico di lavoro per porzione non valido, deve essere minore del carico di lavoro per persona");
                caricoXPorzione = InputDati.leggiDouble("Inserire carico di lavoro per porzione: ");
            }
        } while (!ok);
        Ricetta ricetta = new Ricetta(ingredienti, numeroPorzioni, caricoXPorzione);
        return ricetta;
    }

    /**
     * Metodo per l'aggiunta di un menù tematico
     * @param piattiDisponibili
     * @return
     */
    public Set<MenuTematico> creaMenuTematici(Set<Piatto> piattiDisponibili) {
        Set<MenuTematico> menuTematici = new HashSet<>();
        Piatto[] piatti = piattiDisponibili.toArray(new Piatto[piattiDisponibili.size()]);
        boolean scelta = true;
        do {
            String nome = InputDati.leggiStringa(nomeMenuTematico);
            ArrayList<Date> date = inserisciDate();
            MenuTematico myMenu = new MenuTematico(nome, inserisciPiattiMenuTematico(piattiDisponibili),date.get(0),date.get(1));
            menuTematici.add(myMenu);
            scelta = InputDati.yesOrNo(nuovoMenuTematico);
        } while (scelta);
        return menuTematici;
    }

    /**
     * metodo per l'aggiunta di piatti nel menù tematico
     * @param piattiDisponibili
     * @return
     */
    public Set<Piatto> inserisciPiattiMenuTematico(Set<Piatto> piattiDisponibili) {
        Piatto[] piatti = piattiDisponibili.toArray(new Piatto[piattiDisponibili.size()]);
        Set<Piatto> piattiDelMenu = new HashSet<>();
        boolean scelta = true;
        double sommaCaricoLavoroPiatti = 0;
        do {
            int i = 1;
            for (Piatto piatto : piattiDisponibili) {
                System.out.println(i + "-" + piatto.getDenominazione());
                i++;
            }
            int numeroPiatto = InputDati.leggiIntero(sceltaNumeroPiatto);
            sommaCaricoLavoroPiatti += piatti[numeroPiatto - 1].getCaricoLavoro();
            if(sommaCaricoLavoroPiatti > 4/3*Ristorante.getCaricoXPersona()){
                System.err.println(Stringhe.caricoLavoroMenuTematicoNonValido);
                break;
            }
            piattiDelMenu.add(piatti[numeroPiatto - 1]);
            scelta = InputDati.yesOrNo(nuovoPiattoMenuTematico);
        } while (scelta);
        return piattiDelMenu;
    }

    /**
     * metodo per l'inserimento delle date in cui è disponibile un menù tematico
     * @return
     */
    public ArrayList<Date> inserisciDate(){
        ArrayList<Date> date = new ArrayList<>();
        String scelta = InputDati.leggiStringa(inserimentoData);
        Date dataInizio = InputDati.leggiData(inserisciDataInizio);
        Date dataFine = InputDati.leggiData(inserisciDataFine);
        date.add(dataInizio);
        date.add(dataFine);
        return date;
    }
}
