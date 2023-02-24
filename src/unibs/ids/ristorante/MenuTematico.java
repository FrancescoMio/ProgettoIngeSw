package unibs.ids.ristorante;
import java.util.Date;
import java.util.Set;

public class MenuTematico extends Menu implements Ordinabile{

    private boolean disponibile;
    private double caricoLavoro;
    private Date dataInizio;
    private Date  dataFine;

    public MenuTematico (Set<Piatto> elencoPiatti) {//Costruttore
        super(elencoPiatti);
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public double getCaricoLavoro() {
        return caricoLavoro;
    }

    public void setCaricoLavoro(double caricoLavoro) {
        this.caricoLavoro = caricoLavoro;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }
}
