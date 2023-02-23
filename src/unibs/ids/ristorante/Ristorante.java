package unibs.ids.ristorante;

public class Ristorante {
    private String nome;
    private int postiASedere;
    private int caricoDiLavoroSostenibile;//sarà da ricavare  moltiplicando il carico di lavoro per persona per i posti per 120/100
    private int caricoDiLavoroXPersona;//impegno richiesto per preparare cibo per una persona in un singolo pasto

    public Ristorante(String nome, int postiASedere, int caricoDiLavoroSostenibile, int caricoDiLavoroXPersona) {//da completare e con tutti i controlli
        this.nome = nome;
        this.postiASedere = postiASedere;
        this.caricoDiLavoroSostenibile = caricoDiLavoroSostenibile;
        this.caricoDiLavoroXPersona = caricoDiLavoroXPersona;
    }
    public int getCaricoXPersona() {
        return caricoDiLavoroXPersona;
    }
    public int getPostiASedere() {
        return postiASedere;
    }
}
