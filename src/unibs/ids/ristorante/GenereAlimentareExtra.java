package unibs.ids.ristorante;

public class GenereAlimentareExtra implements Raggruppabile{
    private String nome;

    public GenereAlimentareExtra(String nome){
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
        return "GenereAlimentareExtra{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
