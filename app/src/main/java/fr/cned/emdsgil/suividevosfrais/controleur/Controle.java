package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;

import fr.cned.emdsgil.suividevosfrais.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.Visiteur;

import static android.content.Context.MODE_PRIVATE;

public class Controle {

    private static Controle instance = null;
    private static Context contexte;
    private static Visiteur visiteur;
    private static AccesDistant accesDistant = new AccesDistant();

    public final static  String PREF_ID = "ID_VISITEUR";


    private Controle() {super();}

    public final static Controle getInstance(Context contexte)
    {
        if(contexte != null)
        {
            Controle.contexte = contexte;
        }
        if(Controle.instance == null)
        {
            Controle.instance = new Controle();
        }

        return Controle.instance;
    }

    public void authentification(JSONArray donnees)
    {
        accesDistant.envoi("authentification", donnees);
    }



    public void creerVisiteur(String id, String nom, String prenom, Context contexte){
        Visiteur visiteur = new Visiteur(id, nom, prenom);
        Log.d("Controleur ", "******" + visiteur.getId() + " " + visiteur.getPrenom() + " " + visiteur.getNom());
        SharedPreferences.Editor editor = contexte.getSharedPreferences(PREF_ID, MODE_PRIVATE).edit();
        editor.putString("idVisiteur", visiteur.getId());
        editor.apply();


        SharedPreferences prefs = contexte.getSharedPreferences(PREF_ID, MODE_PRIVATE);
        String restoreText = prefs.getString("idVisiteur", "non");
        Log.d("RECUP DATA : ", "*******" + restoreText);
        /*accesDistant.envoi("recupFrais", visiteur.convertToJSONArray());*/
    }

    public void setVisiteur(Visiteur visiteur){
        Controle.visiteur = visiteur;
    }


}
