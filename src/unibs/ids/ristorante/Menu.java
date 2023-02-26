package unibs.ids.ristorante;

import java.util.HashSet;
import java.util.Set;

public class Menu {
    protected String nomeMenu;
    protected Set<Piatto> elencoPiatti;

    public Menu(String nome,Set<Piatto> elencoPiatti){
        this.nomeMenu = nome;
        this.elencoPiatti = new HashSet<>();
        this.elencoPiatti = elencoPiatti;
    }

    public Set<Piatto> getElencoPiatti() {
        return elencoPiatti;
    }

    public void setElencoPiatti(Set<Piatto> elencoPiatti) {
        this.elencoPiatti = elencoPiatti;
    }

}
