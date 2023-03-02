package unibs.ids.ristorante;

import Libreria.InputDati;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RegistroMagazzino {
    private Merce ingredientiDisponibili; //lista dei prodotti disponibili nel magazzino
    private HashMap<Raggruppabile,QuantitaMerce> bevandeEExtra;

    public RegistroMagazzino(){
        ingredientiDisponibili = new Merce();
        bevandeEExtra = new HashMap<Raggruppabile,QuantitaMerce>();
    }

    public RegistroMagazzino(Merce disponibili, HashMap<Raggruppabile,QuantitaMerce> bevandeEExtra) {
        this.ingredientiDisponibili = disponibili;
        this.bevandeEExtra = new HashMap<Raggruppabile,QuantitaMerce>();
    }

    public void visualizzaMagazzino(){
        comunicaQuantitaIngredienti();
        visualizzaBevande();
        visualizzaGeneriExtra();
    }
    /**
     * Compito di registro magazzino di visualizzare gli ingredienti presenti in magazzino
     */
    private void comunicaQuantitaIngredienti(){
        System.out.println("I prodotti disponibili con le rispettive quantita' sono:");
        ingredientiDisponibili.visualizzaMerce();
    }

    /**
     * Compito di registro magazzino di aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * In particolare se gli ingredienti sono gia in magazzino si aggiorna la quantita'
     */
    public void aggiungiIngredientiComprati(Merce listaSpesa){
        if(listaSpesa.getMerce().isEmpty())//se la lista è vuota non aggiungo nulla
            System.out.println("Non ci sono prodotti acquistati");
        else
            //se compro ingredienti che non sono presenti li aggiungo direttamente in magazzino
            if(!controlloIngredientiGiaPresenti(listaSpesa)) {
                System.out.println("E' stato comprato un ingrediente non utile in lista della spesa");
            }
            else{//se sono gia presenti dovro' aggiornare la quantita'
                aggiuntaIngredientiGiaPresenti(listaSpesa);
            }

    }

    private boolean controlloIngredientiGiaPresenti( Merce listaSpesa) {
        for(String nome : listaSpesa.getMerce().keySet()){
            if(ingredientiDisponibili.getMerce().containsKey(nome))
                return true;
        }
        return false;
    }

    /**
     * Compito di registro magazzino di tenere aggiornate le merci a seconda degli ingredienti utilizzati
    * @param ingredienti utilizzati per la preparazione del piatto
     */
    public void portatiInCucina(Merce ingredienti){
        if(ingredientiDisponibili.getMerce().isEmpty())
            System.out.println("ingredienti non presenti in magazzino");
        else {
            System.out.println("I prodotti disponibili con le rispettive quantita' prima dell'ordine sono:");
            ingredientiDisponibili.visualizzaMerce();
            for(String nome : ingredienti.getMerce().keySet()){
                if(ingredientiDisponibili.getMerce().containsKey(nome)){//se c'è l'ingrediente in magazzino
                    //immagino di sfruttare sempre il 10% di scarto
                    int quantita = ingredientiDisponibili.getMerce().get(nome).getQuantita()*110/100 - ingredienti.getMerce().get(nome).getQuantita();
                    if(quantita < 0)
                        System.out.println("Errore: ingredienti non presenti in quantita accettabile in magazzino");
                    ingredientiDisponibili.getMerce().get(nome).setQuantita(quantita);//tolgo dal magazzino ciò che ho utilizzato
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
        Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = bevandeEExtra.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Raggruppabile, QuantitaMerce> entry = iterator.next();
            Raggruppabile key = entry.getKey();
            QuantitaMerce value = entry.getValue();
            if(value.getUnitaMisura().equalsIgnoreCase("l"))
                System.out.println("Bevanda: " + key.getNome() + " Quantita': " + value.getQuantita() + " " + value.getUnitaMisura());
        }
    }

    /**
     * Compito di registro magazzino di poter visualizzare le bevande disponibili
     */
    private void visualizzaGeneriExtra(){
        System.out.println("I generi extra disponibili sono:");
        Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = bevandeEExtra.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Raggruppabile, QuantitaMerce> entry = iterator.next();
            Raggruppabile key = entry.getKey();
            QuantitaMerce value = entry.getValue();
            if(!value.getUnitaMisura().equalsIgnoreCase("l"))
                System.out.println("Generi extra disponibili: " + key.getNome() + " Quantita': " + value.getQuantita() + " " + value.getUnitaMisura());
        }
    }


    /**
     * Metodo di supporto per aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * vige anche la logica per cui in magazzino le unità di misura devono essere uguali
     * @param listaSpesa
     */
    private void aggiuntaIngredientiGiaPresenti(Merce listaSpesa) {
        for(String nome : listaSpesa.getMerce().keySet()){
            if(ingredientiDisponibili.getMerce().containsKey(nome) && ingredientiDisponibili.getMerce().get(nome).getUnitaMisura().equals(listaSpesa.getMerce().get(nome).getUnitaMisura())){
                int quantita = ingredientiDisponibili.getMerce().get(nome).getQuantita() + listaSpesa.getMerce().get(nome).getQuantita();
                ingredientiDisponibili.getMerce().get(nome).setQuantita(quantita);
            }
            else if(bevandeEExtra.containsKey(nome) && bevandeEExtra.get(nome).getUnitaMisura().equals(listaSpesa.getMerce().get(nome).getUnitaMisura())){
                int quantita = bevandeEExtra.get(nome).getQuantita() + listaSpesa.getMerce().get(nome).getQuantita();
                bevandeEExtra.get(nome).setQuantita(quantita);
            }
            else
                System.out.println("Errore: unita di misura non corrispondenti");
        }
    }

    public void addGenereAlimentareExtra(){
        String nome = InputDati.leggiStringa("Inserisci il nome del genere alimentare extra:");
        //generiAlimentariExtra.add(new GenereAlimentareExtra(nome));
    }

    public void addBevanda(){
        String nome = InputDati.leggiStringa("Inserisci il nome della bevanda:");
        //bevande.add(new Bevanda(nome));
    }

    public static Set<Bevanda> getBevande() {
        //return bevande;
        return null;
    }

    public static void setBevande(Set<Bevanda> bevande) {
        bevande = bevande;
    }

    public static Set<GenereAlimentareExtra> getGeneriAlimentariExtra() {
        //return generiAlimentariExtra;
        return null;
    }

    public static void setGeneriAlimentariExtra(Set<GenereAlimentareExtra> generiAlimentariExtra) {
        generiAlimentariExtra = generiAlimentariExtra;
    }

    /**
     * Compito di registro magazzino di registrare il flusso di bevande e
     * generi extra in output (portati in sala)
     * @param bevandeEExtraPortatiInSala
     */
    public void setBevandeEExtraPortatiInSala(HashMap<Raggruppabile,QuantitaMerce> bevandeEExtraPortatiInSala) {
        if(bevandeEExtraPortatiInSala.isEmpty())
            System.out.println("Nessuna bevanda o genere extra portato in sala");
        else {
            Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = bevandeEExtraPortatiInSala.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Raggruppabile, QuantitaMerce> entry = iterator.next();
                Raggruppabile key = entry.getKey();
                QuantitaMerce value = entry.getValue();
                if(bevandeEExtra.containsKey(key.getNome())) {
                    int quantita = bevandeEExtra.get(key.getNome()).getQuantita() - value.getQuantita();
                    if(quantita < 0)
                        System.out.println("Errore: bevanda o genere extra non presenti in quantita accettabile in magazzino");
                    else
                        bevandeEExtra.get(key.getNome()).setQuantita(quantita);
                }
            }
        }
    }
}
