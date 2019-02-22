package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fr.cned.emdsgil.suividevosfrais.AccesDistant;

import fr.cned.emdsgil.suividevosfrais.Global;

import fr.cned.emdsgil.suividevosfrais.MainActivity;
import fr.cned.emdsgil.suividevosfrais.Serializer;
import fr.cned.emdsgil.suividevosfrais.Visiteur;



public class Controle {

    private static Controle instance = null;
    private static Context contexte;
    private static Visiteur visiteur;
    private static AccesDistant accesDistant;


    private Controle() {super();}

    public static final  Controle getInstance(Context contexte)
    {
        if(contexte != null){
            Controle.contexte = contexte;
        }
        if(Controle.instance == null)
        {
            Controle.instance = new Controle();

            //Param accès BDD distante
            accesDistant = new AccesDistant();
        }

        return Controle.instance;
    }

    public void authentification( String login, String password)
    {
        List loginmdp = new ArrayList();
        loginmdp.add(login);
        loginmdp.add(password);
        JSONArray donnees = new JSONArray(loginmdp);

        accesDistant.envoi("authentification", donnees);

        if(visiteur != null)
        {
            creerVisiteur(visiteur.getId(), visiteur.getNom(), visiteur.getPrenom(), contexte);

        }
        else
        {
            Toast.makeText(contexte, "Mot de passe ou identifiant erroné(s)", Toast.LENGTH_LONG).show();
            Log.d("Controleur-AUTH ", "******PROBLEME" );
        }
    }


    public void creerVisiteur(String id, String nom, String prenom, Context contexte){
        Log.d("Controleur-CREER VISITEUR ", "******" + visiteur.getId() + " " + visiteur.getPrenom() + " " + visiteur.getNom());
        //Serializer.serialize(Global.listFraisMois, context);

        Serializer.serialize(visiteur, contexte);
        /*accesDistant.envoi("recupFrais", visiteur.convertToJSONArray());*/
    }

    private static void recupSerializeVisiteur(Context contexte)
    {
        visiteur = (Visiteur)Serializer.deSerialize(contexte);
        Log.d("Visiteur DESERIALIZE ", "******" + visiteur.getId() + " " + visiteur.getPrenom() + " " + visiteur.getNom());
    }

    public boolean visiteurExiste()
    {
        recupSerializeVisiteur(contexte);
        //Log.d("AH", visiteur.getId());
        Log.d("Controleur-testVisiteur ", "******" + visiteur.getId() + " " + visiteur.getPrenom() + " " + visiteur.getNom());
        if(visiteur.getId() != null)
        {
            Log.d("TEST VRAI", "********** VRAI");
            return true;
        }
            Log.d("TEST FAUX", "********** FAUX");
        return false;
    }
    public void setVisiteur(Visiteur visiteur){
        Controle.visiteur = visiteur;
    }


}
