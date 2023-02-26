package Libreria;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;

public class MyUtil {
    public static boolean controlloData(Date inizio, Date fine){
        Date oggi = Date.from(Instant.now());
        if (inizio.before(oggi) && fine.after(oggi)){
            return true;
        }
        else{
            return false;
        }
    }

    public static LocalDate getDataOdierna(){
        LocalDate oggi = LocalDate.now();
        return oggi;
    }
}
