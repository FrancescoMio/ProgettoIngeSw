package unibs.ids.ristorante;

import java.util.Date;
import java.util.Set;

public class MenuCarta extends Menu{

    public MenuCarta (String nome,Set<Piatto> elencoPiatti, Date dataInizio, Date dataFine) {//Costruttore
        super(nome,elencoPiatti,dataInizio,dataFine);
    }

}
