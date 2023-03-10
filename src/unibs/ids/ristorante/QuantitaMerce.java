package unibs.ids.ristorante;

//classe che crea l'oggetto quantitaMerce per la lista della spesa

public class QuantitaMerce {
    private double quantita;
    private String unitaMisura;

    public QuantitaMerce(double quantita, String unitaMisura) {//Costruttore
        this.quantita = quantita;
        this.unitaMisura = controlloUnitaMisura(unitaMisura);
    }

    /**
     * controllo unita di misura inserita, se non è valida chiede di inserirla di nuovo
     * @return the quantita
     */
    private String controlloUnitaMisura(String unitaMisura) {
        if (unitaMisura.equals("kg") || unitaMisura.equals("hg")|| unitaMisura.equals("g") || unitaMisura.equals("l")) {
            return unitaMisura;
        } else {
            while(!unitaMisura.equals("kg") || !unitaMisura.equals("hg")|| !unitaMisura.equals("g") || !unitaMisura.equals("l")) {
                System.out.println("Unità di misura non valida");
                unitaMisura = Libreria.InputDati.leggiStringa("Inserire unità di misura valida: ");
            }
            return unitaMisura;
        }
    }

    public double getQuantita() {
        return quantita;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setQuantita(double quantita) {
        if(quantita < 0)
            this.quantita = 0;
        else
            this.quantita = quantita;
    }
    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }
    @Override
    public String toString() {
        return "QuantitaMerce{" +
                "quantita=" + quantita +
                ", unitaMisura='" + unitaMisura + '\'' +
                '}';
    }


}
