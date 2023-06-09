package unibs.ids.ristorante;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Questa classe viene utilizzata per creare e gestire la prenotazione effettuata da un cliente
 */
public class Prenotazione {
    private int numeroCoperti;
    private LocalDate dataPrenotazione;
    private HashMap<Ordinabile,Integer> ordine; //Integer rappresenta il numero di persone che hanno scelto il menuTematico o il piatto che costituisce la chive

    public Prenotazione(int numeroCoperti, LocalDate dataPrenotazione, HashMap<Ordinabile,Integer> ordine){
        this.numeroCoperti = numeroCoperti;
        this.dataPrenotazione = dataPrenotazione;
        this.ordine = ordine;
    }

    public int getNumeroCoperti() {
        return numeroCoperti;
    }

    public void setNumeroCoperti(int numeroCoperti) {
        this.numeroCoperti = numeroCoperti;
    }

    public LocalDate getDataPrenotazione() {
        return dataPrenotazione;
    }

    public double getCaricoLavoro(){
        double caricoLavoro = 0;
        for(Ordinabile o : ordine.keySet()){
            caricoLavoro += o.getCaricoLavoro()*ordine.get(o);
        }
        return caricoLavoro;
    }
    public HashMap<Ordinabile, Integer> getOrdine() {
        return ordine;
    }

    public void visualizzaPrenotazione(){
        System.out.println("Prenotazione: "+dataPrenotazione+"\nnumero coperti: " + numeroCoperti);
        for (Map.Entry<Ordinabile, Integer> entry : ordine.entrySet()){
            System.out.println("ordine: " + entry.getKey().getNome() + " quantita: " + entry.getValue());
        }
    }
    @Override
    public String toString() {
        return "Prenotazione{" +
                "numeroCoperti=" + numeroCoperti +
                ", dataPrenotazione=" + dataPrenotazione +
                ", ordine=" + ordine +
                '}';
    }
}
