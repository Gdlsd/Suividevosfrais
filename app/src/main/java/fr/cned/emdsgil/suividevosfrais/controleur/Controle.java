package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.Context;

import fr.cned.emdsgil.suividevosfrais.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.MainActivity;
import fr.cned.emdsgil.suividevosfrais.Visiteur;

public class Controle {

    private static Controle instance = null;
    private static Context contexte;
    private static Visiteur visiteur;
    private static AccesDistant accesDistant;



    private Controle() {super();}

    public void creerVisiteur(String id, String nom, String prenom){
        Visiteur visiteur = new Visiteur(id, nom, prenom);
        accesDistant.envoi("recupFrais", visiteur.convertToJSONArray());
    }

    public void setVisiteur(Visiteur visiteur){
        Controle.visiteur = visiteur;
    }


}
