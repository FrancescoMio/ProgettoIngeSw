package Libreria;

import org.json.simple.JSONObject;
import unibs.ids.ristorante.Ristorante;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LeggiJSON {

    public LeggiJSON() throws IOException {

    }

    public static void salvaConfigurazione(Ristorante ristorante,ArrayList<JSONObject> piattiDisponibili){
        JSONObject obj = new JSONObject();
        obj.put("NomeRistorante",ristorante.getNomeRistorante());
        obj.put("PostiAsedere",ristorante.getPostiASedere());
        obj.put("CaricoLavoroPersona",ristorante.getCaricoXPersona());
        obj.put("CaricoLavoroSostenibile",ristorante.getCaricoDiLavoroSostenibile());
        obj.put("PiattiDisponibili", piattiDisponibili);
        salvaSuFile(obj);
    }

    public static void salvaSuFile(JSONObject object){
        try (FileWriter file = new FileWriter("./output.json")) {
            // Scrivi il JSON nel file
            file.write(object.toJSONString());
            file.flush();
            file.close();
            System.out.println("Il file JSON è stato scritto con successo!");

        } catch (Exception e) {
            System.err.println("Errore durante la scrittura del file JSON: " + e.getMessage());
        }
    }
}
