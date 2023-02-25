package unibs.ids.ristorante;

import Libreria.MyUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

public class Menu {
    protected String nomeMenu;
    protected boolean disponibile;
    protected Set<Piatto> elencoPiatti;
    protected Date dataInizio;
    protected Date dataFine;

    public Menu(String nome,Set<Piatto> elencoPiatti,Date dataInizio,Date dataFine){
        this.nomeMenu = nome;
        elencoPiatti = new HashSet<>();
        this.elencoPiatti = elencoPiatti;
        this.disponibile = isDisponibile(dataInizio,dataFine);
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public boolean getDisponibilita(){
        return this.disponibile;
    }

    public void setDisponibilita(boolean disponibile){
        this.disponibile = disponibile;
    }

    public Set<Piatto> getElencoPiatti() {
        return elencoPiatti;
    }

    public void setElencoPiatti(Set<Piatto> elencoPiatti) {
        this.elencoPiatti = elencoPiatti;
    }
    public Date getDataInizio() {
        return dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * metodo per il controllo della disponibilità del menù
     * @param dataInizio
     * @param dataFine
     * @return
     */
    public boolean isDisponibile(Date dataInizio, Date dataFine){
        if(MyUtil.controlloData(dataInizio,dataFine))
            return true;
        return false;
    }
}
