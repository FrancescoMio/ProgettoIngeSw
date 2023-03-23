package unibs.ids.ristorante;

import java.util.ArrayList;
import java.util.HashMap;

public interface Ordinabile {
    public double getCaricoLavoro();
    public HashMap<String,QuantitaMerce> getListaIngredienti(int quantitaOrdine);
    public String getNome();
}
