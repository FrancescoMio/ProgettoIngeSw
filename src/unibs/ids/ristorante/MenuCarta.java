package unibs.ids.ristorante;

import java.util.Date;
import java.util.Set;

public class MenuCarta extends Menu{

    private Date data;

    public MenuCarta (String nome,Set<Piatto> elencoPiatti,Date data) {
        super(nome,elencoPiatti);
        this.data = data;
    }

}
