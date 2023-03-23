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
     * @param dataInizio
     * @param dataFine
     * @return
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

    @Override
    public String toString() {
        return "- Nome: " + ANSI_CYAN + nomeMenu + ANSI_RESET + "\n- Carico lavoro: " + caricoLavoroMenuTematico
                + "\n- Periodo validità : " + dataInizio + " -> " + dataFine + "\nElenco piatti:\n" + elencoPiatti;
    }

}
