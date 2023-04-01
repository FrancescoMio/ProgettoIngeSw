package unibs.ids.ristorante;

import Libreria.MyUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static Libreria.Stringhe.*;


public class MenuTematico extends Menu implements Ordinabile{

    private double caricoLavoroMenuTematico;
    private boolean disponibile;
    private LocalDate dataInizio;
    private LocalDate dataFine;

    /**
     * Costruttore del menù tematico
     * @param nome nome del menù tematico
     * @param elencoPiatti elenco di piatti che costituiscono il menù tematico
     * @param dataInizio  data di inzio disponibilità del menù tematico
     * @param dataFine data di fine disponibilità del menù tematico
     * @param caricoLavoro carico lavoro del menù tematico
     * precondizione: dataInizio < dataFine, caricoLavoro > 0, elencoPiatti != null
     */
    public MenuTematico (String nome,Set<Piatto> elencoPiatti, LocalDate dataInizio, LocalDate dataFine,double caricoLavoro) {
        super(nome,elencoPiatti);
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.disponibile = isDisponibile(dataInizio,dataFine);
        this.caricoLavoroMenuTematico = caricoLavoro;
    }

    public void addPiatto(Piatto piatto){
        this.elencoPiatti.add(piatto);
    }
    public boolean getDisponibilita(){
        return this.disponibile;
    }

    public void setDisponibilita(boolean disponibile){
        this.disponibile = disponibile;
    }

    public double getCaricoLavoro() {
        return caricoLavoroMenuTematico;
    }

    public void setCaricoLavoro(double caricoLavoro) {
        this.caricoLavoroMenuTematico = caricoLavoro;
    }


    /**
     * metodo per il controllo della disponibilità del menù
     * @param dataInizio data di inizio disponibilità
     * @param dataFine data di fine disponibilità
     * @return true se il menù è disponibile, false altrimenti
     */
    public boolean isDisponibile(LocalDate dataInizio, LocalDate dataFine){
        if(MyUtil.controlloData(dataInizio,dataFine))
            return true;
        return false;
    }
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * metodo per rilevare l'intera lista degli ingredienti necessari per la preparazione di tutti i piatti del menù tematico
     * @param quantitaOrdine quantità di menù tematici ordinati
     * @return lista degli ingredienti necessari per la preparazione di tutti i piatti del menù tematico
     * precondizione: quantitaOrdine > 0
     * postcondizione: listaIngredientiPiatti != null
     */
    public HashMap<String,QuantitaMerce> getListaIngredienti(int quantitaOrdine) {
        HashMap<String, QuantitaMerce> listaIngredientiPiatti = new HashMap<>();
        for (Piatto piatto : elencoPiatti) {
            HashMap<String, QuantitaMerce> listaIngredientiPiatto = piatto.getListaIngredienti(1);
            for (Map.Entry<String, QuantitaMerce> entry : listaIngredientiPiatto.entrySet()){
                String nomeIngrediente = entry.getKey();
                QuantitaMerce quantitaMerce = entry.getValue();
                double quantita = quantitaMerce.getQuantita();
                String unitaMisura = quantitaMerce.getUnitaMisura();
                if(listaIngredientiPiatti.containsKey(nomeIngrediente)){
                    QuantitaMerce quantitaMerceOld = listaIngredientiPiatti.get(nomeIngrediente);
                    double quantitaOld = quantitaMerceOld.getQuantita();
                    QuantitaMerce quantitaNuova = new QuantitaMerce(quantitaOld + quantita,unitaMisura);
                    listaIngredientiPiatti.put(nomeIngrediente, quantitaNuova);
                }
                else{
                    listaIngredientiPiatti.put(nomeIngrediente,quantitaMerce);
                }
            }
        }
        for (Map.Entry<String, QuantitaMerce> entry : listaIngredientiPiatti.entrySet()){
            String nomeIngrediente = entry.getKey();
            QuantitaMerce quantitaIngrediente = entry.getValue();
            String unitaMisura = quantitaIngrediente.getUnitaMisura();
            double quantitaOld = quantitaIngrediente.getQuantita();
            double quantitaAggiornata = quantitaOld * quantitaOrdine;
            QuantitaMerce quantitaIngredienteNuova = new QuantitaMerce(quantitaAggiornata,unitaMisura);
            listaIngredientiPiatti.put(nomeIngrediente,quantitaIngredienteNuova);
        }
        return  listaIngredientiPiatti;
    }

    public String getNome(){
        return nomeMenu;
    }

    private String getNomiPiatti(Set<Piatto> elencoPiatti){
        StringBuilder sb = new StringBuilder();
        for (Piatto piatto : elencoPiatti) {
            sb.append("  - ")
                    .append(piatto.getNome())
                    .append("\n");
        }
        return sb.toString();
    }

    public String menuCliente() {
        return "- Nome: "  + nomeMenu  + "\n    Elenco piatti:\n" + getNomiPiatti(elencoPiatti);
    }
    @Override
    public String toString() {
        return "- Nome: "  + nomeMenu  + "\n- Carico lavoro: " + caricoLavoroMenuTematico
                + "\n- Periodo validità : " + dataInizio + " -> " + dataFine + "\n Elenco piatti:\n" + getNomiPiatti(elencoPiatti);
    }

}
