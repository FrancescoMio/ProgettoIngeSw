package unibs.ids.ristorante;

import Libreria.InputDati;

import java.util.ArrayList;

public class RegistroMagazzino {
    private static ListaSpesa acquistati;
    private static ArrayList<Bevanda> bevande;
    private static ArrayList<GenereAlimentareExtra> generiAlimentariExtra;

    public RegistroMagazzino(){
        acquistati = new ListaSpesa();
        bevande = new ArrayList<>();
        generiAlimentariExtra = new ArrayList<>();
    }

    public void addGenereAlimentareExtra(){
        String nome = InputDati.leggiStringa("Inserisci il nome del genere alimentare extra:");
        generiAlimentariExtra.add(new GenereAlimentareExtra(nome));
    }

    public void addBevanda(){
        String nome = InputDati.leggiStringa("Inserisci il nome della bevanda:");
        bevande.add(new Bevanda(nome));
    }

    public static ArrayList<Bevanda> getBevande() {
        return bevande;
    }

    public static void setBevande(ArrayList<Bevanda> bevande) {
        bevande = bevande;
    }

    public static ArrayList<GenereAlimentareExtra> getGeneriAlimentariExtra() {
        return generiAlimentariExtra;
    }

    public static void setGeneriAlimentariExtra(ArrayList<GenereAlimentareExtra> generiAlimentariExtra) {
        generiAlimentariExtra = generiAlimentariExtra;
    }


    //prodotti acquistati(di tipo ordinabile?)
    //prodotti disponibili (Hashmap)
    //bevanda e generi extra disponibili
    //...da fare poi
}
