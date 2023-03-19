package unibs.ids.ristorante;

public class Bevanda implements Raggruppabile{
    private String nome;

    public Bevanda(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "- " + nome + "\n";
    }
}
