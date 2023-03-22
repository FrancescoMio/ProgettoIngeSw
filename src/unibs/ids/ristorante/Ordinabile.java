package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;

public interface Ordinabile {
    public double getCaricoLavoro();
    public HashMap<String,Double> getListaIngredienti(int quantitaOrdine);
    public String getNome();
}
