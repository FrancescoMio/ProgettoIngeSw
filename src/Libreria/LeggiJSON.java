package Libreria;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unibs.ids.ristorante.Ristorante;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

    /**
     * Metodo per la lettura del file JSON e caricamento della configurazione del ristorante
     * @return
     */
    public static Ristorante caricaDati(){
        Ristorante ristorante = new Ristorante("caricaDati");
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("./output.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            String nomeRistorante = (String) jsonObject.get("NomeRistorante");
            ristorante.setNomeRistorante(nomeRistorante);
            System.out.println(nomeRistorante);

            String nomeGestore = (String) jsonObject.get("NomeGestore");
            String cognomeGestore = (String) jsonObject.get("CognomeGestore");
            ristorante.setGestore(nomeGestore,cognomeGestore);

            /*int postiASedere = (int) jsonObject.get("PostiAsedere");
            ristorante.setPostiASedere(postiASedere);

            int caricoLavoroPersona = (int) jsonObject.get("CaricoLavoroPersona");
            ristorante.setCaricoLavoroPersona(caricoLavoroPersona);

            int caricoLavoroSostenibile = (int) jsonObject.get("CaricoLavoroSostenibile");
            ristorante.setCaricoLavoroSostenibile(caricoLavoroSostenibile);*/

            /*JSONObject addressObject = (JSONObject) jsonObject.get("address");
            String street = (String) addressObject.get("street");
            String city = (String) addressObject.get("city");
            String state = (String) addressObject.get("state");*/

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return ristorante;
    }
}
