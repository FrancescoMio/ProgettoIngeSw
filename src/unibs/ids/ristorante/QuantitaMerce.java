package unibs.ids.ristorante;

//classe che crea l'oggetto quantitaMerce per la lista della spesa

public class QuantitaMerce {
    private double quantita;
    private String unitaMisura;

    public QuantitaMerce(double quantita, String unitaMisura) {
        this.quantita = quantita;
        this.unitaMisura = unitaMisura;
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
