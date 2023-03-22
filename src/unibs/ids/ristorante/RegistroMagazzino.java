package unibs.ids.ristorante;

import Libreria.InputDati;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RegistroMagazzino {
    private Merce articoliDisponibili; //lista dei prodotti disponibili nel magazzino
    private Consumo bevandeEExtra;

    public RegistroMagazzino(){
        articoliDisponibili = new Merce();
        bevandeEExtra = new Consumo();
    }

    public RegistroMagazzino(Merce disponibili, Consumo bevandeEExtra) {
        this.articoliDisponibili = disponibili;
        this.bevandeEExtra = bevandeEExtra;
    }

    //TODO: DA GESTIRE BENE L'UNITA' DI MISURA IN TUTTI I METODI
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
        articoliDisponibili.visualizzaMerce();
    }

    /**
     * Compito di registro magazzino di aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * In particolare se gli ingredienti sono gia in magazzino si aggiorna la quantita'
     */
    public void aggiungiIngredientiComprati(Merce listaSpesa){
        if(listaSpesa.getArticoli().isEmpty())//se la lista è vuota non aggiungo nulla
            System.out.println("Non ci sono prodotti acquistati");
        else
            aggiuntaArticoliGiaPresenti(listaSpesa);
    }

   /**
     * Metodo di supporto per aggiungere gli ingredienti acquistati alla lista dei prodotti disponibili
     * vige anche la logica per cui in magazzino le unità di misura devono essere uguali
     * @param listaSpesa
     */
    private void aggiuntaArticoliGiaPresenti(Merce listaSpesa) {
        HashMap<Raggruppabile, QuantitaMerce> bevEExtra = bevandeEExtra.getConsumo();
        for(String nome : listaSpesa.getArticoli().keySet()){
            if(articoliDisponibili.getArticoli().containsKey(nome)){
                inputQuantita(nome, listaSpesa.getArticoli().get(nome).getQuantita());
            }
            else if(bevEExtra.containsKey(nome)){
                inputQuantita(nome, listaSpesa.getArticoli().get(nome).getQuantita());
            }
            else
                System.err.println("Errore");
        }
    }

    /**
     * Compito di registro magazzino di tenere aggiornate le merci a seconda degli ingredienti utilizzati
    * @param ingredienti utilizzati per la preparazione del piatto
     */
    public void portatiInCucina(Merce ingredienti){
        if(articoliDisponibili.getArticoli().isEmpty())
            System.out.println("ingredienti non presenti in magazzino");
        else {
            System.out.println("I prodotti disponibili con le rispettive quantita' prima dell'ordine sono:");
            articoliDisponibili.visualizzaMerce();
            for(String nome : ingredienti.getArticoli().keySet()){
                if(articoliDisponibili.getArticoli().containsKey(nome)){//se c'è l'ingrediente in magazzino
                    //il 10% di scarto lo tengo? o lo considero nella lista della spesa e basta?
                    double quantita = articoliDisponibili.getArticoli().get(nome).getQuantita()*110/100 - ingredienti.getArticoli().get(nome).getQuantita();
                    if(quantita < 0)
                        System.out.println("Errore: ingredienti non presenti in quantita accettabile in magazzino");
                    articoliDisponibili.getArticoli().get(nome).setQuantita(quantita);//tolgo dal magazzino ciò che ho utilizzato
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
        HashMap<Raggruppabile, QuantitaMerce> bevande = bevandeEExtra.getConsumo();
        Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = bevande.entrySet().iterator();
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
        HashMap<Raggruppabile, QuantitaMerce> extra = bevandeEExtra.getConsumo();
        Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = extra.entrySet().iterator();
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
        HashMap<Raggruppabile, QuantitaMerce> bevEExtra = bevandeEExtra.getConsumo();
        for(String nome : listaSpesa.getMerce().keySet()){
            if(ingredientiDisponibili.getMerce().containsKey(nome)){
                inputQuantita(nome, listaSpesa.getMerce().get(nome).getQuantita());
            }
            else if(bevEExtra.containsKey(nome)){
                inputQuantita(nome, listaSpesa.getMerce().get(nome).getQuantita());
            }
            else
                System.out.println("Errore");
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

    public Consumo getBevandeEExtra() {
        return bevandeEExtra;
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
        HashMap<Raggruppabile, QuantitaMerce> bevEextra = bevandeEExtra.getConsumo();
        if(bevandeEExtraPortatiInSala.isEmpty())
            System.out.println("Nessuna bevanda o genere extra portato in sala");
        else {
            Iterator<Map.Entry<Raggruppabile, QuantitaMerce>> iterator = bevandeEExtraPortatiInSala.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Raggruppabile, QuantitaMerce> entry = iterator.next();
                Raggruppabile key = entry.getKey();
                QuantitaMerce value = entry.getValue();
                if(bevEextra.containsKey(key.getNome())) {
                    double quantita = bevEextra.get(key.getNome()).getQuantita() - value.getQuantita();
                    if(quantita < 0)
                        System.out.println("Errore: bevanda o genere extra non presenti in quantita accettabile in magazzino");
                    else
                        bevEextra.get(key.getNome()).setQuantita(quantita);
                }
            }
        }
    }

    //suppongo che per generi extra e  ingredienti l'utente inserisca sempre la quantità in grammi
    private void outputQuantita(String nome, double quantita){
        if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("g")){
            double rimanenti = articoliDisponibili.getArticoli().get(nome).getQuantita() - quantita;
            if(rimanenti < 0)
                System.out.println("Errore: ingrediente non presente in quantita accettabile in magazzino");
            else
                articoliDisponibili.getArticoli().get(nome).setQuantita(rimanenti);
        }
        else if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("l")){
            double rimanenti = articoliDisponibili.getArticoli().get(nome).getQuantita() - quantita;
            if(rimanenti < 0)
                System.out.println("Errore: bevanda non presenta in quantita accettabile in magazzino");
            else
                articoliDisponibili.getArticoli().get(nome).setQuantita(rimanenti);
        }
        else if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("kg")){
            double quantitaInGrammi = articoliDisponibili.getArticoli().get(nome).getQuantita() * 1000;
            double rimanenti = quantitaInGrammi - quantita;
            if(rimanenti < 0)
                System.out.println("Errore: ingrediente non presente in quantita accettabile in magazzino");
            else
                //riporto in kg
                articoliDisponibili.getArticoli().get(nome).setQuantita(rimanenti / 1000);
        }
        else if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("hg")){
            double quantitaInGrammi = articoliDisponibili.getArticoli().get(nome).getQuantita() * 100;
            double rimanenti = quantitaInGrammi - quantita;
            if(rimanenti < 0)
                System.out.println("Errore: ingrediente non presente in quantita accettabile in magazzino");
            else
                //riporto in hg
                articoliDisponibili.getArticoli().get(nome).setQuantita(rimanenti / 100);
        }
    }

    private void inputQuantita(String nome, double quantita){
        if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("g")){
            double totale = articoliDisponibili.getArticoli().get(nome).getQuantita() + quantita;
            articoliDisponibili.getArticoli().get(nome).setQuantita(totale);
        }
        else if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("l")){
            double totale = articoliDisponibili.getArticoli().get(nome).getQuantita() + quantita;
            articoliDisponibili.getArticoli().get(nome).setQuantita(totale);
        }
        else if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("kg")){
            double quantitaInGrammi = articoliDisponibili.getArticoli().get(nome).getQuantita() * 1000;
            double totale = quantitaInGrammi + quantita;
            //riporto in kg
            articoliDisponibili.getArticoli().get(nome).setQuantita(totale / 1000);
        }
        else if(articoliDisponibili.getArticoli().get(nome).getUnitaMisura().equalsIgnoreCase("hg")){
            double quantitaInGrammi = articoliDisponibili.getArticoli().get(nome).getQuantita() * 100;
            double totale = quantitaInGrammi + quantita;
            //riporto in hg
            articoliDisponibili.getArticoli().get(nome).setQuantita(totale / 100);
        }
    }
    public void setArticoliDisponibili(Merce articoliDisponibili) {
        this.articoliDisponibili = articoliDisponibili;
    }

    public Merce getArticoliDisponibili() {
        return articoliDisponibili;
    }
}
