package unibs.ids.ristorante;

import java.util.Set;

public class Menu {
    private Set<Piatto> elencoPiatti;

    public Menu(Set<Piatto> elencoPiatti) {
        this.elencoPiatti = elencoPiatti;
    }

    public Set<Piatto> getElencoPiatti() {
        return elencoPiatti;
    }

    public void setElencoPiatti(Set<Piatto> elencoPiatti) {
        this.elencoPiatti = elencoPiatti;
    }
}
