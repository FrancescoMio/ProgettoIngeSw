package unibs.ids.ristorante;
import Libreria.MyUtil;

import java.util.Date;
import java.util.Set;

public class MenuTematico extends Menu implements Ordinabile{

    private double caricoLavoroMenuTematico;
    private boolean disponibile;
    private Date dataInizio;
    private Date dataFine;

    public MenuTematico (String nome,Set<Piatto> elencoPiatti, Date dataInizio, Date dataFine) {
        super(nome,elencoPiatti);
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.disponibile = isDisponibile(dataInizio,dataFine);
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
