package unibs.ids.ristorante;

import Libreria.InputDati;

import java.util.HashSet;
import java.util.Set;

public class RegistroMagazzino {
    private Merce disponibili; //lista dei prodotti disponibili nel magazzino
    private Merce lista; //lista dei prodotti acquistati e disponibili nel magazzino
    private static Set<Bevanda> bevande;
    private static Set<GenereAlimentareExtra> generiAlimentariExtra;

    public RegistroMagazzino(){
        disponibili = new Merce();
        lista = new Merce();
        bevande = new HashSet<>();
        generiAlimentariExtra = new HashSet<>();
    }

    public RegistroMagazzino(Merce disponibili, Merce lista, Set<Bevanda> bevande, Set<GenereAlimentareExtra> generiAlimentariExtra) {
        this.disponibili = disponibili;
        this.lista = lista;
        this.bevande = bevande;
        this.generiAlimentariExtra = generiAlimentariExtra;
    }

    public void visualizzaMagazzino(){
        comunicaQuantitaIngredienti();
        visualizzaBevande();
        visualizzaGeneriAlimentariExtra();
    }
    /**
     * Compito di registro magazzino di visualizzare gli ingredienti presenti in magazzino
     */
    private void comunicaQuantitaIngredienti(){
        System.out.println("I prodotti disponibili con le rispettive quantita' sono:");
        disponibili.visualizzaMerce();
    }

    /**
     * Compito di registro magazzino di aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * In particolare se gli ingredienti sono gia in magazzino si aggiorna la quantita'
     */
    public void aggiungiIngredientiComprati(){
        if(lista.getMerce().isEmpty())//se la lista è vuota non aggiungo nulla
            System.out.println("Non ci sono prodotti acquistati");
        else{
            //se compro ingredienti che non sono presenti li aggiungo direttamente in magazzino
            if(!controlloIngredientiGiaPresenti(disponibili, lista))//si puo togliere e fare solo ciò che c'è nell'else
                for(String nome : lista.getMerce().keySet()){
                    disponibili.aggiungiMerce(nome, lista.getMerce().get(nome));
                }
            else{//se sono gia presenti dovro' aggiornare la quantita'
                aggiuntaIngredientiGiaPresenti(disponibili, lista);
            }

        }
    }

/**
     * Compito di registro magazzino di tenere aggiornate le merci a seconda degli ingredienti utilizzati
    * @param ingredienti utilizzati per la preparazione del piatto
     */
    public void portatiInCucina(Merce ingredienti){
        if(disponibili.getMerce().isEmpty())
            System.out.println("ingredienti non presenti in magazzino");
        else {
            System.out.println("I prodotti disponibili con le rispettive quantita' prima dell'ordine sono:");
            disponibili.visualizzaMerce();
            for(String nome : ingredienti.getMerce().keySet()){
                if(disponibili.getMerce().containsKey(nome)){//se c'è l'ingrediente in magazzino
                    //immagino di sfruttare sempre il 10% di scarto
                    int quantita = disponibili.getMerce().get(nome).getQuantita()*110/100 - ingredienti.getMerce().get(nome).getQuantita();
                    if(quantita < 0)
                        System.out.println("Errore: ingredienti non presenti in quantita accettabile in magazzino");
                    disponibili.getMerce().get(nome).setQuantita(quantita);//tolgo dal magazzino ciò che ho utilizzato
                }
                else
                    System.out.println("Errore: ingredienti non presenti in magazzino");
            }

        }
    }

    /**
     * Compito di registro magazzino di poter visualizzare le bevande disponibili
     */
    private void visualizzaBevande(){
        System.out.println("Le bevande disponibili sono:");
        for(Bevanda b : bevande){
            System.out.println(b.getNome());
        }
    }

    /**
     * Compito di registro magazzino di poter visualizzare i generi alimentari extra disponibili
     */
    private void visualizzaGeneriAlimentariExtra(){
        System.out.println("I generi alimentari extra disponibili sono:");
        for(GenereAlimentareExtra g : generiAlimentariExtra){
            System.out.println(g.getNome());
        }
    }

    /**
     * Metodo di supporto per aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * vige anche la logica per cui in magazzino le unità di misura devono essere uguali
     * @param disponibili
     * @param lista
     */
    private void aggiuntaIngredientiGiaPresenti(Merce disponibili, Merce lista) {
        for(String nome : lista.getMerce().keySet()){
            if(disponibili.getMerce().containsKey(nome) && disponibili.getMerce().get(nome).getUnitaMisura().equals(lista.getMerce().get(nome).getUnitaMisura())){
                int quantita = disponibili.getMerce().get(nome).getQuantita() + lista.getMerce().get(nome).getQuantita();
                disponibili.getMerce().get(nome).setQuantita(quantita);
            }
        }
    }

    //possibilmente da togliere
    private boolean controlloIngredientiGiaPresenti(Merce disponibili, Merce lista) {
        for(String nome : lista.getMerce().keySet()){
            if(disponibili.getMerce().containsKey(nome))
                return true;
        }
        return false;
    }

    public void addGenereAlimentareExtra(){
        String nome = InputDati.leggiStringa("Inserisci il nome del genere alimentare extra:");
        generiAlimentariExtra.add(new GenereAlimentareExtra(nome));
    }

    public void addBevanda(){
        String nome = InputDati.leggiStringa("Inserisci il nome della bevanda:");
        bevande.add(new Bevanda(nome));
    }

    public static Set<Bevanda> getBevande() {
        return bevande;
    }

    public static void setBevande(Set<Bevanda> bevande) {
        bevande = bevande;
    }

    public static Set<GenereAlimentareExtra> getGeneriAlimentariExtra() {
        return generiAlimentariExtra;
    }

    public static void setGeneriAlimentariExtra(Set<GenereAlimentareExtra> generiAlimentariExtra) {
        generiAlimentariExtra = generiAlimentariExtra;
    }
}
