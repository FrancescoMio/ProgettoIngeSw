package Libreria;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unibs.ids.ristorante.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static Libreria.Stringhe.*;

public class Json {

    public Json() throws IOException {

    }

    public static void salvaConfigurazione(Ristorante ristorante, Set<Piatto> piatti){
        JSONObject obj = new JSONObject();
        obj.put("nomeGestore",ristorante.getNomeGestore());
        obj.put("cognomeGestore",ristorante.getCognomeGestore());
        obj.put("nomeRistorante",ristorante.getNomeRistorante());
        obj.put("postiAsedere",ristorante.getPostiASedere());
        obj.put("caricoLavoroPersona",ristorante.getCaricoXPersona());
        obj.put("caricoLavoroSostenibile",ristorante.getCaricoDiLavoroSostenibile());
        ArrayList<JSONObject> elencoPiattiJson = getElencoPiattiJson(piatti);
        obj.put("elencoPiatti",elencoPiattiJson);
        //ArrayList<JSONObject>  elencoBevande = getRaggruppabileJSon(ristorante.getBevande());
        //ArrayList<JSONObject>  elencoGeneriExtra = getRaggruppabileJSon(ristorante.getGeneriAlimentari());
        salvaSuFile(obj,"./config.json");
    }

    /**
     * Metodo per il salvataggio dei menù tematici nel file JSON "menuTematici.json"
     * @param elencoMenuTematici
     */
    public static void salvaMenuTematici(Set<MenuTematico> elencoMenuTematici){
        JSONObject menuTematiciJson = new JSONObject();
        ArrayList<JSONObject> elencoMenuTematiciJson = new ArrayList<>();
        for(MenuTematico menu : elencoMenuTematici){
            ArrayList<JSONObject> elencoPiattiJson = new ArrayList<>();
            JSONObject menuJson = new JSONObject();
            menuJson.put("nome", menu.getNomeMenu());
            menuJson.put("caricoLavoro", menu.getCaricoLavoro());
            menuJson.put("disponibile", menu.getDisponibilita());
            String dataInizio = menu.getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); //converto la data in una stringa perchè JSON non supporta le date come valore ammissibile
            String dataFine = menu.getDataFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            menuJson.put("dataInizio", dataInizio);
            menuJson.put("dataFine", dataFine);
            elencoPiattiJson = getElencoPiattiJson(menu.getElencoPiatti());
            menuJson.put("elencoPiatti", elencoPiattiJson);
            elencoMenuTematiciJson.add(menuJson);
        }
        menuTematiciJson.put("menuTematici", elencoMenuTematiciJson);
        salvaSuFile(menuTematiciJson,"./menuTematici.json");
    }


    public static void salvaMenuCarta(MenuCarta menuCarta){
        JSONObject menuCartaJson = new JSONObject();
        menuCartaJson.put("nome",menuCarta.getNomeMenu());
        String data = menuCarta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        menuCartaJson.put("data",data);
        ArrayList<JSONObject> piattiJson = getElencoPiattiJson(menuCarta.getElencoPiatti());
        menuCartaJson.put("elencoPiatti", piattiJson);
        salvaSuFile(menuCartaJson, "./menuCarta.json");
    }

    public static void salvaConsumiProCapite(ConsumoProCapiteBevande bevande, ConsumoProCapiteGeneriExtra generi){
        JSONObject elenco = new JSONObject();
        ArrayList<JSONObject> elencoBevande = getElencoBevandeGeneriJson(bevande);
        elenco.put("elencoBevande",elencoBevande);
        ArrayList<JSONObject> elencoGeneriExtra = getElencoBevandeGeneriJson(generi);
        elenco.put("elencoGeneriExtra", elencoGeneriExtra);
        salvaSuFile(elenco,"./consumoProCapite.json");
    }

    public static ArrayList<JSONObject> getElencoBevandeGeneriJson(Consumo consumi){
        ArrayList<JSONObject> elenco = new ArrayList<>();
        HashMap<Raggruppabile,QuantitaMerce> hashMapConsumi = consumi.getConsumo();
        for (Map.Entry<Raggruppabile, QuantitaMerce> entry : hashMapConsumi.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("nome",entry.getKey().getNome());
            QuantitaMerce quantitaMerce = entry.getValue();
            double quantita = quantitaMerce.getQuantita();
            String unitaMisura = quantitaMerce.getUnitaMisura();
            obj.put("consumoProCapite", quantita);
            obj.put("unitaMisura",unitaMisura);
            elenco.add(obj);
        }
        return elenco;
    }


    /**
     * Metodo grazie al quale passando come parametro un elenco di piatti ritorna l'elenco di tali piatti in formato JSON
     * @param elencoPiatti
     * @return
     */
    public static ArrayList<JSONObject> getElencoPiattiJson(Set<Piatto> elencoPiatti){
        ArrayList<JSONObject> elencoPiattiJson = new ArrayList<>();
        for (Piatto piatto: elencoPiatti) {
            JSONObject piattoJson = new JSONObject();
            piattoJson.put("nome",piatto.getDenominazione());
            String dataInizio = piatto.getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); //converto la data in una stringa perchè JSON non supporta le date come valore ammissibile
            String dataFine = piatto.getDataFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            piattoJson.put("dataInizio", dataInizio);
            piattoJson.put("dataFine", dataFine);
            piattoJson.put("tempoPreparazione",piatto.getTempoPreparazione());
            piattoJson.put("caricoLavoro",piatto.getCaricoLavoro());
            JSONObject ricettaJson = new JSONObject();
            ArrayList<JSONObject> ingredientiJson = setRicettaJson(piatto.getRicetta());
            ricettaJson.put("numeroPorzioni",piatto.getRicetta().getNumeroPorzioni());
            ricettaJson.put("caricoLavoroXporzione",piatto.getRicetta().getCaricoLavoro());
            ricettaJson.put("elencoIngredienti",ingredientiJson);
            piattoJson.put("ricetta",ricettaJson);
            elencoPiattiJson.add(piattoJson);
        }
        return elencoPiattiJson;
    }

    public static ArrayList<JSONObject> setRicettaJson(Ricetta ricetta){
        ArrayList<JSONObject> ingredientiJson = new ArrayList<>();

        Iterator<Map.Entry<String, Integer>> iterator = ricetta.getIngredienti().entrySet().iterator();
        while (iterator.hasNext()) {
            JSONObject obj = new JSONObject();
            Map.Entry<String, Integer> entry = iterator.next();
            obj.put("nome",entry.getKey());
            obj.put("dose",entry.getValue());
            ingredientiJson.add(obj);
        }
        return  ingredientiJson;
    }

    public static void salvaSuFile(JSONObject object, String nomeFile){
        try (FileWriter file = new FileWriter(nomeFile)) {
            // Scrivi il JSON nel file
            file.write(object.toJSONString());
            file.flush();
            file.close();
            //System.out.println("Il file JSON '"+ nomeFile + "' è stato scritto con successo!");

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

        //caricamento della configurazione del ristorante
        try (FileReader reader = new FileReader("./config.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            String nomeRistorante = (String) jsonObject.get("nomeRistorante");
            ristorante.setNomeRistorante(nomeRistorante);

            String nomeGestore = (String) jsonObject.get("nomeGestore");
            String cognomeGestore = (String) jsonObject.get("cognomeGestore");
            ristorante.setGestore(nomeGestore,cognomeGestore);

            long postiASedere = (long) jsonObject.get("postiAsedere");
            ristorante.setPostiASedere((int)postiASedere);

            long caricoLavoroPersona = (long) jsonObject.get("caricoLavoroPersona");
            ristorante.setCaricoLavoroPersona((int)caricoLavoroPersona);

            long caricoLavoroSostenibile = (long) jsonObject.get("caricoLavoroSostenibile");
            ristorante.setCaricoLavoroSostenibile((int)caricoLavoroSostenibile);

            //carico i piatti del ristorante
            ArrayList<JSONObject> elencoPiatti = (ArrayList<JSONObject>) jsonObject.get("elencoPiatti");
            Set<Piatto> piatti = new HashSet<>();
            piatti = getPiatti(elencoPiatti);
            ristorante.setPiatti(piatti);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //creazione nuovo menù alla carta in base ai piatti disponibili e salvataggio del menù
        //ad ogni caricamento verranno salvati nel file JSON del menù alla carta solamente i piatti disponibili
        ristorante.creaMenuCarta();
        MenuCarta menuCarta = ristorante.getMenuAllaCarta();
        salvaMenuCarta(menuCarta);

        //caricamento menù tematici del ristorante
        try (FileReader reader = new FileReader("./menuTematici.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> menuTematiciJson = (ArrayList<JSONObject>) jsonObject.get("menuTematici");
            Set<MenuTematico> menuTematici = new HashSet<>();
            for (JSONObject menuTematicoJson: menuTematiciJson) {
                //recupero nome del menù tematico
                String nomeMenu = (String) menuTematicoJson.get("nome");
                //recupero caricaLavoro del menù tematico
                double caricoLavoro = (double) menuTematicoJson.get("caricoLavoro");
                //recupero della data di inizio e fine del menù tematico
                String dataInizioStr = (String) menuTematicoJson.get("dataInizio");
                dataInizioStr = dataInizioStr.replaceAll("'\\'", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                // Parsing della stringa in un oggetto LocalDate
                LocalDate dataInizio = LocalDate.parse(dataInizioStr, formatter);

                String dataFineStr = (String) menuTematicoJson.get("dataInizio");
                dataFineStr = dataFineStr.replaceAll("'\\'", "");
                LocalDate dataFine = LocalDate.parse(dataFineStr, formatter);

                //recupero dei piatti del menù tematico
                ArrayList<JSONObject> elencoPiatti = (ArrayList<JSONObject>) menuTematicoJson.get("elencoPiatti");
                Set<Piatto> piattiDelMenu = getPiatti(elencoPiatti);

                MenuTematico myMenu = new MenuTematico(nomeMenu,piattiDelMenu,dataInizio,dataFine,caricoLavoro);
                menuTematici.add(myMenu);
            }
            ristorante.setMenuTematici(menuTematici);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        System.out.print("CARICAMENTO CONFIGURAZIONE");
        String str = "....\n";
        int delay = 500; // ritardo in millisecondi tra i caratteri
        for (int i = 0; i < str.length(); i++) {
            System.out.print(str.charAt(i)); // stampa il carattere corrente
            try {
                Thread.sleep(delay); // metti in pausa l'esecuzione per il ritardo specificato
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(ANSI_GREEN + configurazioneCaricata + ANSI_RESET);
        return ristorante;
    }

    /**
     * Metodo che partendo dall' ArrayList di oggetti JSON dei piatti ritorna il Set<Piatto> dei piatti da caricare
     * nel ristorante
     * @param elencoPiatti
     * @return
     */
    public static Set<Piatto> getPiatti(ArrayList<JSONObject> elencoPiatti){
        Set<Piatto> piatti = new HashSet<>();
        for (JSONObject obj: elencoPiatti) { //scorro tutti i piatti contenuti nel nel JSON
            String denominazione = (String) obj.get("nome");

            String dataInizioStr = (String) obj.get("dataInizio");
            dataInizioStr = dataInizioStr.replaceAll("'\\'", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Parsing della stringa in un oggetto LocalDate
            LocalDate dataInizio = LocalDate.parse(dataInizioStr, formatter);

            String dataFineStr = (String) obj.get("dataFine");
            dataFineStr = dataFineStr.replaceAll("'\\'", "");
            // Parsing della stringa in un oggetto LocalDate
            LocalDate dataFine = LocalDate.parse(dataFineStr, formatter);

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
            Piatto piatto = new Piatto(denominazione,ricetta,(int)tempoPreparazione,dataInizio,dataFine);
            piatti.add(piatto);
        }
        return piatti;
    }

    public static ArrayList<JSONObject> caricaCredenziali(String nomeFile){
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(nomeFile)){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> elencoUtentiJson = (ArrayList<JSONObject>) jsonObject.get("elencoUtenti");
            return elencoUtentiJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void salvaCredenziali(JSONObject utenti,String nomeFile){
        salvaSuFile(utenti, nomeFile);
    }
}
