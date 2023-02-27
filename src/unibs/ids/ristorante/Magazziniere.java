package unibs.ids.ristorante;

public class Magazziniere extends Utente {

    Merce listaSpesa;

    public Magazziniere(String nome, String cognome) {//Costruttore
        super(nome, cognome);
    }

    public void creaListaSpesaGiornaliera(Merce listaSpesa) {
        this.listaSpesa = listaSpesa;
    }


}
