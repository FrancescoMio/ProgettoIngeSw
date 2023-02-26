package Libreria;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;

public class MyUtil {
    public static boolean controlloData(LocalDate dataInizio, LocalDate dataFine){
        LocalDate oggi = LocalDate.now();
        if(oggi.isEqual(dataInizio) || oggi.isAfter(dataInizio) || oggi.isBefore(dataFine) || oggi.isEqual(dataFine))
            return true;
        return false;
    }

    public static LocalDate getDataOdierna(){
        LocalDate oggi = LocalDate.now();
        return oggi;
    }
}
