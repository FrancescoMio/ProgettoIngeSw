package unibs.ids.ristorante;

public class Utente {
    private String nome;
    private String cognome;

    public Utente(String nome, String cognome) {//Costruttore
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }
}
