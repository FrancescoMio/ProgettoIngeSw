package unibs.ids.ristorante;

import java.time.LocalDate;
import java.util.Set;

public class MenuCarta extends Menu{
    private LocalDate data;

    public MenuCarta (String nome,Set<Piatto> elencoPiatti,LocalDate data) {
        super(nome,elencoPiatti);
        this.data = data;
    }

    public MenuCarta(){
        super();
    }
    public Set<Piatto> getElencoPiatti() {
        return elencoPiatti;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MenuCarta{" +
                "data=" + data +
                ", nomeMenu='" + nomeMenu + '\'' +
                ", elencoPiatti=" + elencoPiatti/*visualizzaPiatti() */+
                '}';
    }
}
