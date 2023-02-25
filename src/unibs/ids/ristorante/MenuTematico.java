package unibs.ids.ristorante;
import java.util.Date;
import java.util.Set;

public class MenuTematico extends Menu implements Ordinabile{

    private double caricoLavoroMenuTematico;

    public MenuTematico (String nome,Set<Piatto> elencoPiatti, Date dataInizio, Date dataFine) {
        super(nome,elencoPiatti,dataInizio,dataFine);

    }

    public void addPiatto(Piatto piatto){
        this.elencoPiatti.add(piatto);
    }

    public double getCaricoLavoro() {
        return caricoLavoroMenuTematico;
    }

    public void setCaricoLavoro(double caricoLavoro) {
        this.caricoLavoroMenuTematico = caricoLavoro;
    }

}
