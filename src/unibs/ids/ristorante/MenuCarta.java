package unibs.ids.ristorante;

import java.time.LocalDate;
import java.util.Date;
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

    private String visualizzaPiatti(){
        String piatti = "";
        for(Piatto p : elencoPiatti){
            int i = 1;
            System.out.println("Piatto " + i + ": " + p.getDenominazione());
            i++;
        }
        return piatti;
    }

}
