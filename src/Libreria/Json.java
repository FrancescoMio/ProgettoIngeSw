package Libreria;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unibs.ids.ristorante.*;

import java.io.File;
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
        salvaSuFile(obj,"dati/config.json");
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
        salvaSuFile(menuTematiciJson,"dati/menuTematici.json");
    }


    public static void salvaMenuCarta(MenuCarta menuCarta){
        JSONObject menuCartaJson = new JSONObject();
        menuCartaJson.put("nome",menuCarta.getNomeMenu());
        String data = menuCarta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        menuCartaJson.put("data",data);
        ArrayList<JSONObject> piattiJson = getElencoPiattiJson(menuCarta.getElencoPiatti());
        menuCartaJson.put("elencoPiatti", piattiJson);
        salvaSuFile(menuCartaJson, "dati/menuCarta.json");
    }

    public static void salvaConsumiProCapite(ConsumoProCapiteBevande bevande, ConsumoProCapiteGeneriExtra generi){
        JSONObject elenco = new JSONObject();
        ArrayList<JSONObject> elencoBevande = getElencoBevandeGeneriJson(bevande);
        elenco.put("elencoBevande",elencoBevande);
        ArrayList<JSONObject> elencoGeneriExtra = getElencoBevandeGeneriJson(generi);
        elenco.put("elencoGeneriExtra", elencoGeneriExtra);
        salvaSuFile(elenco,"dati/consumoProCapite.json");
    }

    public static void salvaPrenotazioni(ArrayList<Prenotazione> prenotazioni){
        JSONObject prenotazioniJson = new JSONObject();
        ArrayList<JSONObject> elencoPrenotazioni = getElencoPrenotazioniJson(prenotazioni);
        prenotazioniJson.put("prenotazioni",elencoPrenotazioni);
        salvaSuFile(prenotazioniJson,"dati/prenotazioni.json");
    }

    public static void salvaRegistroMagazzino(RegistroMagazzino registroMagazzino){
        JSONObject registroJson = new JSONObject();
        Merce merceMagazzino = registroMagazzino.getArticoliDisponibili();
        HashMap<String, QuantitaMerce> articoliMagazzino = merceMagazzino.getArticoli();
        ArrayList<JSONObject> elencoArticoliMagazzino = getArticoliMagazzinoJson(articoliMagazzino);
        registroJson.put("articoli",elencoArticoliMagazzino);
        salvaSuFile(registroJson,"dati/registroMagazzino.json");
    }

    //todo: rinominare il metodo "getArticoliMagazzinoJson"
    public static void salvaCucina(Merce merceDaPortareInCucina, Merce merceInCucina){
        JSONObject cucinaJson = new JSONObject();
        ArrayList<JSONObject> merceDaPortareInCucinaJson = getArticoliMagazzinoJson(merceDaPortareInCucina.getArticoli());
        ArrayList<JSONObject> merceInCucinaJson = getArticoliMagazzinoJson(merceInCucina.getArticoli());
        cucinaJson.put("merceDaPortareInCucina",merceDaPortareInCucinaJson);
        cucinaJson.put("merceInCucina",merceInCucinaJson);
        salvaSuFile(cucinaJson,"dati/cucina.json");
    }

    public static ArrayList<JSONObject> getArticoliMagazzinoJson(HashMap<String,QuantitaMerce> articoliMagazzino){
        ArrayList<JSONObject> elencoArticoliMagazzino = new ArrayList<>();
        for (Map.Entry<String, QuantitaMerce> entry : articoliMagazzino.entrySet()){
            JSONObject articoloJson = new JSONObject();
            String nomeArticolo = entry.getKey();
            QuantitaMerce quantitaArticolo = entry.getValue();
            double quantita = quantitaArticolo.getQuantita();
            String unitaMisura = quantitaArticolo.getUnitaMisura();
            articoloJson.put("nome",nomeArticolo);
            articoloJson.put("quantita",quantita);
            articoloJson.put("unitaMisura",unitaMisura);
            elencoArticoliMagazzino.add(articoloJson);
        }
        return elencoArticoliMagazzino;
    }

    public static ArrayList<JSONObject> getElencoPrenotazioniJson(ArrayList<Prenotazione> prenotazioni){
        ArrayList<JSONObject> elencoPrenotazioni = new ArrayList<>();
        for (Prenotazione p: prenotazioni) {
            JSONObject prenotazione = new JSONObject();
            String dataPrenotazione = p.getDataPrenotazione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            prenotazione.put("dataPrenotazione",dataPrenotazione);
            prenotazione.put("numeroCoperti", p.getNumeroCoperti());
            HashMap<Ordinabile, Integer> ordine = p.getOrdine();
            ArrayList<JSONObject> portate = getPortateJson(ordine);
            prenotazione.put("ordine",portate);
            elencoPrenotazioni.add(prenotazione);
        }
        return elencoPrenotazioni;
    }

    private static ArrayList<JSONObject> getPortateJson(HashMap<Ordinabile,Integer> ordine){
        ArrayList<JSONObject> portate = new ArrayList<>();
        for (Map.Entry<Ordinabile,Integer> entry : ordine.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("nome",entry.getKey().getNome());
            obj.put("quantita",entry.getValue());
            portate.add(obj);
        }
        return portate;
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

        Iterator<Map.Entry<String, QuantitaMerce>> iterator = ricetta.getIngredienti().entrySet().iterator();
        while (iterator.hasNext()) {
            JSONObject obj = new JSONObject();
            Map.Entry<String, QuantitaMerce> entry = iterator.next();
            obj.put("nome",entry.getKey());
            QuantitaMerce quantitaIngrediente = entry.getValue();
            obj.put("dose",quantitaIngrediente.getQuantita());
            obj.put("unitaMisura",quantitaIngrediente.getUnitaMisura());
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

        Set<Piatto> piatti = new HashSet<>();
        //caricamento della configurazione del ristorante
        try (FileReader reader = new FileReader("dati/config.json")) {
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

        Set<MenuTematico> menuTematici = new HashSet<>();
        //caricamento menù tematici del ristorante
        try (FileReader reader = new FileReader("dati/menuTematici.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> menuTematiciJson = (ArrayList<JSONObject>) jsonObject.get("menuTematici");

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

        //caricamento delle prenotazioni del ristorante
        ArrayList<Prenotazione> prenotazioni = Json.caricaPrenotazioni(menuTematici,piatti);
        ristorante.setPrenotazioni(prenotazioni);

        //caricamento delle bevande del ristorante
        Set<Bevanda> bevande = Json.caricaBevande();
        ristorante.setBevande(bevande);
        System.out.println(ristorante.getBevande());

        //caricamento dei generi alimentari extra
        Set<GenereAlimentareExtra> generiAlimentari = Json.caricaGeneriAlimentari();
        ristorante.setGeneriAlimentari(generiAlimentari);
        System.out.println(ristorante.getGeneriAlimentari());

        //caricamento consumo pro capite bevande
        ConsumoProCapiteBevande consumoProCapiteBevande = Json.caricaConsumoProCapiteBevande();
        ristorante.setConsumoProCapiteBevande(consumoProCapiteBevande);

        //caricamento consumo pro capite generi alimentari
        ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra = Json.caricaConsumoProCapiteGeneriAlimentari();
        ristorante.setConsumoProCapiteGeneriExtra(consumoProCapiteGeneriExtra);

        //caricamento prodotti da portare in cucina e di quelli già presenti
        Merce prodottiDaPortareInCucina = Json.caricaProdottiCucina("merceDaPortareInCucina");
        Merce prodottiInCucina = Json.caricaProdottiCucina("merceInCucina");
        ristorante.setMerceDaPortareInCucina(prodottiDaPortareInCucina);
        ristorante.setMerceInCucina(prodottiInCucina);

        RegistroMagazzino registroMagazzino = Json.caricaRegistroMagazzino();
        ristorante.setRegistroMagazzino(registroMagazzino);
        System.out.println(ANSI_CYAN+"REGISTRO MAGAZZINO"+ANSI_RESET);
        registroMagazzino.getArticoliDisponibili().visualizzaMerce();

        System.out.print("\n"+ANSI_YELLOW+"CARICAMENTO CONFIGURAZIONE IN CORSO");
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

        System.out.println(ANSI_RESET+"\n"+ANSI_GREEN + configurazioneCaricata + ANSI_RESET);
        return ristorante;
    }

    private static Merce caricaProdottiCucina(String prodottiDaCaricare){
        Merce prodottiDaPortare = new Merce();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/cucina.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> prodottiCucinaJson = (ArrayList<JSONObject>) jsonObject.get(prodottiDaCaricare);
            for(JSONObject prodottoJson : prodottiCucinaJson){
                String nomeArticolo = (String) prodottoJson.get("nome");
                String unitaMisura = (String) prodottoJson.get("unitaMisura");
                double quantita = (double) prodottoJson.get("quantita");
                QuantitaMerce quantitaProdotto = new QuantitaMerce(quantita,unitaMisura);
                prodottiDaPortare.aggiungiMerce(nomeArticolo,quantitaProdotto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return prodottiDaPortare;
    }

    private static RegistroMagazzino caricaRegistroMagazzino(){
        RegistroMagazzino registroMagazzino = new RegistroMagazzino();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/registroMagazzino.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> articoliMagazzinoJson = (ArrayList<JSONObject>) jsonObject.get("articoli");
            for(JSONObject articoloJson : articoliMagazzinoJson){
                String nomeArticolo = (String) articoloJson.get("nome");
                String unitaMisura = (String) articoloJson.get("unitaMisura");
                double quantita = (double) articoloJson.get("quantita");
                QuantitaMerce quantitaArticolo = new QuantitaMerce(quantita,unitaMisura);
                registroMagazzino.caricaArticolo(nomeArticolo,quantitaArticolo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return registroMagazzino;
    }

    private static Set<Bevanda> caricaBevande(){
        Set<Bevanda> bevande = new HashSet<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/consumoProCapite.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> elencoBevandeJson = (ArrayList<JSONObject>) jsonObject.get("elencoBevande");
            for(JSONObject bevandaJson : elencoBevandeJson){
                String nomeBevanda = (String) bevandaJson.get("nome");
                Bevanda bevanda = new Bevanda(nomeBevanda);
                bevande.add(bevanda);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bevande;
    }

    private static Set<GenereAlimentareExtra> caricaGeneriAlimentari(){
        Set<GenereAlimentareExtra> generiAlimentari = new HashSet<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/consumoProCapite.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> elencoGeneriJson = (ArrayList<JSONObject>) jsonObject.get("elencoGeneriExtra");
            for(JSONObject genereJson : elencoGeneriJson){
                String nomeGenere = (String) genereJson.get("nome");
                GenereAlimentareExtra genere = new GenereAlimentareExtra(nomeGenere);
                generiAlimentari.add(genere);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return generiAlimentari;
    }

    private static ConsumoProCapiteBevande caricaConsumoProCapiteBevande(){
        ConsumoProCapiteBevande consumoProCapiteBevande = new ConsumoProCapiteBevande();
        HashMap<Raggruppabile, QuantitaMerce> hashMapConsumoBevande= new HashMap<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/consumoProCapite.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> elencoBevandeJson = (ArrayList<JSONObject>) jsonObject.get("elencoBevande");
            for(JSONObject bevandaJson : elencoBevandeJson){
                String nomeBevanda = (String) bevandaJson.get("nome");
                double consumoProCapite = (double) bevandaJson.get("consumoProCapite");
                String unitaMisura = (String) bevandaJson.get("unitaMisura");
                Bevanda bevanda = new Bevanda(nomeBevanda);
                QuantitaMerce quantitaBevanda = new QuantitaMerce(consumoProCapite,unitaMisura);
                hashMapConsumoBevande.put(bevanda, quantitaBevanda);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        consumoProCapiteBevande.setConsumo(hashMapConsumoBevande);
        return consumoProCapiteBevande;
    }

    private static ConsumoProCapiteGeneriExtra caricaConsumoProCapiteGeneriAlimentari(){
        ConsumoProCapiteGeneriExtra consumoProCapiteGeneriExtra = new ConsumoProCapiteGeneriExtra();
        HashMap<Raggruppabile, QuantitaMerce> hashMapConsumoGeneri= new HashMap<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/consumoProCapite.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> elencoGeneriJson = (ArrayList<JSONObject>) jsonObject.get("elencoGeneriExtra");
            for(JSONObject bevandaJson : elencoGeneriJson){
                String nomeGenere = (String) bevandaJson.get("nome");
                double consumoProCapite = (double) bevandaJson.get("consumoProCapite");
                String unitaMisura = (String) bevandaJson.get("unitaMisura");
                GenereAlimentareExtra genereAlimentareExtra = new GenereAlimentareExtra(nomeGenere);
                QuantitaMerce quantitaGenere = new QuantitaMerce(consumoProCapite,unitaMisura);
                hashMapConsumoGeneri.put(genereAlimentareExtra, quantitaGenere);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        consumoProCapiteGeneriExtra.setConsumo(hashMapConsumoGeneri);
        return consumoProCapiteGeneriExtra;
    }

    private static ArrayList<Prenotazione> caricaPrenotazioni(Set<MenuTematico> menuTematici, Set<Piatto> piatti){
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("dati/prenotazioni.json")){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            ArrayList<JSONObject> prenotazioniJson = (ArrayList<JSONObject>) jsonObject.get("prenotazioni");
            for(JSONObject prenotazioneJson: prenotazioniJson){
                long numeroCoperti = (long) prenotazioneJson.get("numeroCoperti");
                String dataPrenotazioneStr = (String) prenotazioneJson.get("dataPrenotazione");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataPrenotazioneStr = dataPrenotazioneStr.replaceAll("'\\'", "");
                LocalDate dataPrenotazione = LocalDate.parse(dataPrenotazioneStr, formatter);
                ArrayList<JSONObject> portate = (ArrayList<JSONObject>) prenotazioneJson.get("ordine");
                HashMap<Ordinabile, Integer> ordine = getOrdine(portate,menuTematici,piatti);
                Prenotazione prenotazione = new Prenotazione((int)numeroCoperti,dataPrenotazione,ordine);
                prenotazioni.add(prenotazione);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return prenotazioni;
    }

    private static HashMap<Ordinabile,Integer> getOrdine(ArrayList<JSONObject> portate, Set<MenuTematico> menuTematici, Set<Piatto>piatti){
        HashMap<Ordinabile,Integer> ordine = new HashMap<>();
        for(JSONObject portata : portate){
            String nomePortata = (String) portata.get("nome");
            long quantita = (long) portata.get("quantita");
            Ordinabile ordinabile = getOrdinabile(nomePortata,menuTematici,piatti);
            ordine.put(ordinabile,(int)quantita);
        }
        return ordine;
    }

    private static Ordinabile getOrdinabile(String nomePortata,Set<MenuTematico> menuTematici, Set<Piatto>piatti){
        for (MenuTematico menu : menuTematici)
            if(menu.getNomeMenu().equalsIgnoreCase(nomePortata))
                return menu;
        for(Piatto piatto : piatti)
            if(piatto.getDenominazione().equalsIgnoreCase(nomePortata))
                return piatto;
        return null;
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
            HashMap<String,QuantitaMerce> ingredienti = new HashMap<>();
            for (JSONObject ingrediente: elencoIngredienti) {
                double dose = (double) ingrediente.get("dose");
                String nomeIngrediente = (String) ingrediente.get("nome");
                String unitaMisura = (String) ingrediente.get("unitaMisura");
                QuantitaMerce quantitaIngrediente = new QuantitaMerce(dose,unitaMisura);
                ingredienti.put(nomeIngrediente,quantitaIngrediente);
            }
            Ricetta ricetta = new Ricetta(ingredienti, (int)numeroPorzioni, (double)caricoLavoroXporzione);
            Piatto piatto = new Piatto(denominazione,ricetta,(int)tempoPreparazione,dataInizio,dataFine);
            piatti.add(piatto);
        }
        return piatti;
    }

    public static ArrayList<JSONObject> caricaCredenziali(String nomeFile){
        File file = new File(nomeFile);
        String absolutePath = file.getAbsolutePath();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(absolutePath)){
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
