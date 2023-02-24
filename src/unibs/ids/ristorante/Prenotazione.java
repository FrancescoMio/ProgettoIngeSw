package unibs.ids.ristorante;
import java.util.Date;
import java.util.HashMap;

/**
 * Questa classe viene utilizzata per creare e gestire la prenotazione effettuata da un cliente
 */
public class Prenotazione {
    private int numeroCoperti;
    private Date dataPrenotazione;
    private HashMap<Ordinabile,Integer> ordine;

    public Prenotazione(int numeroCoperti, Date dataPrenotazione){

    }

    public int getNumeroCoperti() {
        return numeroCoperti;
    }

    public void setNumeroCoperti(int numeroCoperti) {
        this.numeroCoperti = numeroCoperti;
    }

    public Date getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(Date dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public HashMap<Ordinabile, Integer> getOrdine() {
        return ordine;
    }

    public void setOrdine(HashMap<Ordinabile, Integer> ordine) {
        this.ordine = ordine;
    }
}
