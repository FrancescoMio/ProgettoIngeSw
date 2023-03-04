package unibs.ids.ristorante;

public class Utente {
    protected String nome;
    protected String cognome;

    public Utente(String nome, String cognome) {//Costruttore
        this.nome = nome;
        this.cognome = cognome;
    }

    public Utente(){

    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}
