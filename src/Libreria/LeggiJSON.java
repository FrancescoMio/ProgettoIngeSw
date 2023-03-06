package Libreria;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unibs.ids.ristorante.Piatto;
import unibs.ids.ristorante.Ricetta;
import unibs.ids.ristorante.Ristorante;
import unibs.ids.ristorante.Stringhe;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static unibs.ids.ristorante.Stringhe.lineSeparator;

public class LeggiJSON {

    public LeggiJSON() throws IOException {

    }

    public static void salvaConfigurazione(Ristorante ristorante,ArrayList<JSONObject> piattiDisponibili){
        JSONObject obj = new JSONObject();
        obj.put("NomeGestore",ristorante.getNomeGestore());
        obj.put("CognomeGestore",ristorante.getCognomeGestore());
        obj.put("NomeRistorante",ristorante.getNomeRistorante());
        obj.put("PostiAsedere",ristorante.getPostiASedere());
        obj.put("CaricoLavoroPersona",ristorante.getCaricoXPersona());
        obj.put("CaricoLavoroSostenibile",ristorante.getCaricoDiLavoroSostenibile());
        obj.put("PiattiDisponibili", piattiDisponibili);
        salvaSuFile(obj,"./config.json");
    }

    public static void salvaMenuTematici(Ristorante ristorante, ArrayList<JSONObject> elencoMenuTematici){
        JSONObject obj = new JSONObject();
        obj.put("MenuTematici", elencoMenuTematici);
        salvaSuFile(obj,"./menuTematici.json");
    }

    public static void salvaSuFile(JSONObject object, String nomeFile){
        try (FileWriter file = new FileWriter(nomeFile)) {
            // Scrivi il JSON nel file
            file.write(object.toJSONString());
            file.flush();
            file.close();
            System.out.println("Il file JSON è stato scritto con successo!");

        } catch (Exception e) {
            System.err.println("Errore durante la scrittura del file JSON: " + e.getMessage());
        }
    }

    /**
     * Metodo per la lettura del file JSON e caricamento della configurazione del ristorante
     * @return
     */
    public static Ristorante caricaDati(){
        Ristorante ristorante = new Ristorante("caricaDati");
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("./config.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            String nomeRistorante = (String) jsonObject.get("NomeRistorante");
            ristorante.setNomeRistorante(nomeRistorante);

            String nomeGestore = (String) jsonObject.get("NomeGestore");
            String cognomeGestore = (String) jsonObject.get("CognomeGestore");
            ristorante.setGestore(nomeGestore,cognomeGestore);

            long postiASedere = (long) jsonObject.get("PostiAsedere");
            ristorante.setPostiASedere((int)postiASedere);

            long caricoLavoroPersona = (long) jsonObject.get("CaricoLavoroPersona");
            ristorante.setCaricoLavoroPersona((int)caricoLavoroPersona);

            long caricoLavoroSostenibile = (long) jsonObject.get("CaricoLavoroSostenibile");
            ristorante.setCaricoLavoroSostenibile((int)caricoLavoroSostenibile);

            ArrayList<JSONObject> piatti = (ArrayList<JSONObject>) jsonObject.get("PiattiDisponibili");
            Set<Piatto> piattiDisponibili = new HashSet<>();

            for (JSONObject obj: piatti) { //scorro tutti i piatti contenuti nel nel JSON
                String denominazione = (String) obj.get("denominazione");
                long tempoPreparazione = (long) obj.get("tempoPreparazione");
                JSONObject ricettaJson = (JSONObject) obj.get("ricetta");
                long numeroPorzioni = (long) ricettaJson.get("numeroPorzioni");
                double caricoLavoroXporzione = (double) ricettaJson.get("caricoLavoroXporzione");
                ArrayList<JSONObject> elencoIngredienti = (ArrayList<JSONObject>)ricettaJson.get("elencoIngredienti");
                HashMap<String,Integer> ingredienti = new HashMap<>();
                for (JSONObject ingrediente: elencoIngredienti) {
                    long dose = (long) ingrediente.get("dose");
                    String nomeIngrediente = (String) ingrediente.get("nome");
                    ingredienti.put(nomeIngrediente,(int)dose);
                }
                Ricetta ricetta = new Ricetta(ingredienti, (int)numeroPorzioni, (double)caricoLavoroXporzione);
                Piatto piatto = new Piatto(denominazione,ricetta,(int)tempoPreparazione);
                piattiDisponibili.add(piatto);
            }

            ristorante.setPiattiDisponibili(piattiDisponibili);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return ristorante;
    }
}
