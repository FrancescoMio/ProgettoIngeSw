package unibs.ids.ristorante;

import java.util.Date;
import java.util.Set;

public class MenuCarta extends Menu{

    private boolean disponibile;
    private Date dataInizio;
    private Date  dataFine;

    public MenuCarta (Set<Piatto> elencoPiatti) {//Costruttore
        super(elencoPiatti);
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
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
