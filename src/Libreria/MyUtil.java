package Libreria;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyUtil {
    public static boolean controlloData(LocalDate dataInizio, LocalDate dataFine){
        LocalDate oggi = LocalDate.now();
        if(oggi.isAfter(dataInizio) && oggi.isBefore(dataFine))
            return true;
        return false;
    }

    public static LocalDate getDataOdierna(){
        LocalDate oggi = LocalDate.now();
        return oggi;
    }
}
