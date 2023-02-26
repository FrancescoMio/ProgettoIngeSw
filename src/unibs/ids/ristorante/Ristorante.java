package unibs.ids.ristorante;

import Libreria.InputDati;
import Libreria.MyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Ristorante {
    //dichiaro static perchè sono proprietà del ristorante che non vengono modificate
    private static String nome;
    private static int postiASedere;
    private static int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private static int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto
    private Set<Piatto> piattiDisponibili;//lista di piatti che il ristorante può offrire
    private RegistroMagazzino registroMagazzino;
    private Gestore gestore; //gestore del ristorante
    private Set<MenuTematico> menuTematici;
    private MenuCarta menuAllaCarta;

    public Ristorante() {
        //creo gestore con cui inizializzare tutto
        String nomeGestore = InputDati.leggiStringa("Inserire il nome del gestore: ");
        String cognomeGestore = InputDati.leggiStringa("Inserire il cognome del gestore: ");
        gestore = new Gestore(nomeGestore, cognomeGestore);
        registroMagazzino = new RegistroMagazzino();

        System.out.println("Buongiorno " + gestore.getNome() + " " + gestore.getCognome() + ", le chiediamo gentilmente di inserire i dati per il suo ristorante: ");

        this.nome = gestore.getNomeRistorante();
        this.postiASedere = gestore.postiASedere();
        this.piattiDisponibili = gestore.inizializzaPiatti();
        this.caricoDiLavoroXPersona = gestore.caricoXpersona();
        this.caricoDiLavoroSostenibile = this.caricoDiLavoroXPersona * this.postiASedere * 120 / 100;
        this.registroMagazzino.addGenereAlimentareExtra();
        this.registroMagazzino.addBevanda();
        this.menuTematici = new HashSet<>();
        String nomeMenuCarta = "Menù del " + MyUtil.getDataOdierna();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //String dataFormattata = MyUtil.getDataOdierna().format(dateFormat).toString();
        //Date dataOdierna = dateFormat.parse(dataFormattata);
        //menuAllaCarta = new MenuCarta(nomeMenuCarta,piattiDisponibili);
        menuTematici.addAll(gestore.creaMenuTematici(piattiDisponibili));
    }
    public static int getCaricoXPersona() {
        return caricoDiLavoroXPersona;
    }
    public int getPostiASedere() {
        return postiASedere;
    }

    public RegistroMagazzino getRegistroMagazzino() {
        return registroMagazzino;
    }

    public void setRegistroMagazzino(RegistroMagazzino registroMagazzino) {
        this.registroMagazzino = registroMagazzino;
    }
}
