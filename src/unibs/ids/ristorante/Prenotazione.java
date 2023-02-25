package unibs.ids.ristorante;
import java.util.Date;
import java.util.HashMap;

/**
 * Questa classe viene utilizzata per creare e gestire la prenotazione effettuata da un cliente
 */
public class Prenotazione {
    private int numeroCoperti;
    private Date dataPrenotazione;
    private HashMap<Ordinabile,Integer> ordine; //Integer rappresenta il numero di persone che hanno scelto il menuTematico o il piatto che costituisce la chive

    public Prenotazione(int numeroCoperti, Date dataPrenotazione, HashMap<Ordinabile,Integer> ordine){
        this.numeroCoperti = numeroCoperti;
        this.dataPrenotazione = dataPrenotazione;
        this.ordine = new HashMap<>();
        this.ordine = ordine;
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
