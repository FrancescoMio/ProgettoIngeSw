package unibs.ids.ristorante;

import Libreria.MyUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MenuTematico extends Menu implements Ordinabile{

    private double caricoLavoroMenuTematico;
    private boolean disponibile;
    private LocalDate dataInizio;
    private LocalDate dataFine;

    /**
     * Costruttore del menù tematico
     * @param nome nome del menù tematico
     * @param elencoPiatti elenco di piatti che costituiscono il menù tematico
     * @param dataInizio  data di inzio disponibilità del menù tematico
     * @param dataFine data di fine disponibilità del menù tematico
     */
    public MenuTematico (String nome,Set<Piatto> elencoPiatti, LocalDate dataInizio, LocalDate dataFine,double caricoLavoro) {
        super(nome,elencoPiatti);
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.disponibile = isDisponibile(dataInizio,dataFine);
        this.caricoLavoroMenuTematico = caricoLavoro;
    }

    public void addPiatto(Piatto piatto){
        this.elencoPiatti.add(piatto);
    }
    public boolean getDisponibilita(){
        return this.disponibile;
    }

    public void setDisponibilita(boolean disponibile){
        this.disponibile = disponibile;
    }

    public double getCaricoLavoro() {
        return caricoLavoroMenuTematico;
    }

    public void setCaricoLavoro(double caricoLavoro) {
        this.caricoLavoroMenuTematico = caricoLavoro;
    }


    /**
     * metodo per il controllo della disponibilità del menù
     * @param dataInizio
     * @param dataFine
     * @return
     */
    public boolean isDisponibile(LocalDate dataInizio, LocalDate dataFine){
        if(MyUtil.controlloData(dataInizio,dataFine))
            return true;
        return false;
    }
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    @Override
    public String toString() {
        return "MenuTematico{" +
                "nomeMenu='" + nomeMenu +
                ", caricoLavoroMenuTematico=" + caricoLavoroMenuTematico +
                ", disponibile=" + disponibile +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", elencoPiatti=" + elencoPiatti +
                '}';
    }
    public Merce getListaIngredienti(){
        Merce listaIngredienti = new Merce();
        Merce listaIngredientiPiatto = new Merce();
        for(Piatto piatto : elencoPiatti){
            //per ogni piatto del menu associo la lista di ingredienti
            listaIngredientiPiatto = piatto.getRicetta().getListaIngredienti();
            listaIngredienti = listaIngredienti.aggregaMerci(listaIngredienti, listaIngredientiPiatto);//sta roba è bruttissima
        }
        return listaIngredienti;
    }

    public String getNome(){
        return nomeMenu;
    }

    public String toStringMenuTematicoDisponibile(){
        if(disponibile)
            return "MenuTematico{" +
                    "nomeMenu='" + nomeMenu +
                    ", caricoLavoroMenuTematico=" + caricoLavoroMenuTematico +
                    ", disponibile=" + disponibile +
                    ", dataInizio=" + dataInizio +
                    ", dataFine=" + dataFine +
                    ", elencoPiatti=" + elencoPiatti +
                    '}';
        else
            return "Non disponibile";
    }

}
