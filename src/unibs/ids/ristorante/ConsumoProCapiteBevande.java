package unibs.ids.ristorante;

public class ConsumoProCapiteBevande extends Consumo{

    public ConsumoProCapiteBevande(Raggruppabile insieme, QuantitaMerce quantita){
        super(insieme,quantita);
    }

    public ConsumoProCapiteBevande(){
        super();
    }


    @Override
    public String toString() {
        return "ConsumoProCapiteBevande{" +
                "consumoProCapite=" + consumoProCapite +
                '}';
    }
}
