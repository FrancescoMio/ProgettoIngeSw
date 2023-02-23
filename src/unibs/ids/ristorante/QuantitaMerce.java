package unibs.ids.ristorante;

public class QuantitaMerce {
    private int quantita;
    private String unitaMisura;

    public QuantitaMerce(int quantita, String unitaMisura) {//Costruttore
        this.quantita = quantita;
        this.unitaMisura = unitaMisura;
    }

    /**
     * controllo unita di misura inserita, si potrebbe estendere
     * @return the quantita
     */
    private void controlloUnitaMisura(String unitaMisura) {
        if (unitaMisura.equals("kg") || unitaMisura.equals("hg")|| unitaMisura.equals("dg")|| unitaMisura.equals("g") || unitaMisura.equals("l") || unitaMisura.equals("hl")|| unitaMisura.equals("dl")|| unitaMisura.equals("ml")) {
            this.unitaMisura = unitaMisura;
        } else {
            System.out.println("Unità di misura non valida");
        }
    }
}
