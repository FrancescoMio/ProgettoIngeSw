package Libreria;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;

public class MyUtil {

    /**
     * Metodo per controllare se la data di inizio e di fine siano valide
     * @param dataInizio data di inizio
     * @param dataFine data di fine
     * @return true se la data di inizio e di fine sono valide, false altrimenti
     * precondizione: dataInizio < dataFine
     */
    public static boolean controlloData(LocalDate dataInizio, LocalDate dataFine){
        LocalDate oggi = LocalDate.now();
        if((oggi.isEqual(dataInizio) || oggi.isAfter(dataInizio)) && ( oggi.isBefore(dataFine) || oggi.isEqual(dataFine)))
            return true;
        return false;
    }

    public static boolean controlloDataIniziale(LocalDate dataIniziale){
        LocalDate oggi = LocalDate.now();
        if(dataIniziale.isBefore(oggi))
            return false;
        return true;
    }

    public static boolean controlloDataFinale(LocalDate dataFinale, LocalDate dataIniziale){
        if(dataFinale.isBefore(dataIniziale))
            return false;
        return true;
    }

    public static boolean controlloUnitaMisura(String unitaMisura){
        if(unitaMisura.equalsIgnoreCase("kg") || unitaMisura.equalsIgnoreCase("hg") || unitaMisura.equalsIgnoreCase("g")
           || unitaMisura.equalsIgnoreCase("l") || unitaMisura.equalsIgnoreCase("ml"))
            return true;
        return false;
    }

    public static LocalDate getDataOdierna(){
        LocalDate oggi = LocalDate.now();
        return oggi;
    }
}
