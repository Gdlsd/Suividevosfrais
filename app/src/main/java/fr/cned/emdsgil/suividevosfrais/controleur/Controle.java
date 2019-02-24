package fr.cned.emdsgil.suividevosfrais.controleur;

import com.google.gson.Gson;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import fr.cned.emdsgil.suividevosfrais.AccesDistant;

import fr.cned.emdsgil.suividevosfrais.FraisMois;
import fr.cned.emdsgil.suividevosfrais.Global;

import fr.cned.emdsgil.suividevosfrais.MainActivity;
import fr.cned.emdsgil.suividevosfrais.Serializer;
import fr.cned.emdsgil.suividevosfrais.Visiteur;
import fr.cned.emdsgil.suividevosfrais.outils.JSONconvert;


public class Controle {

    private static Controle instance = null;
    private static Context contexte;
    private static Visiteur visiteur;
    private FraisMois fraisMois;
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

        Log.d("ID DU VISITEUR 2", "*************" + Global.idVisiteur);


    }

    public void synchroFrais(Context contexte)
    {
        JSONconvert conversion = new JSONconvert();
        Gson gson = new Gson();
        //Récupération des frais serialisés dans un tableau de type String
        String fraisMois = gson.toJson(Serializer.deSerialize(contexte, Global.filenameFrais));

        //Ajout de l'id du visiteur en cours
        Log.d("FRAIS MOIS VISITEUR", "***********" + fraisMois);

        //Conversion des frais au format JSON
        JSONObject fraisMoisJSON = conversion.stringToJSONObject(fraisMois);

        //Création d'un JSONArray à partir des données JSON pour pouvoir transférer les données
        JSONArray fraisMoisJSONArray = new JSONArray();
        fraisMoisJSONArray.put(Global.idVisiteur);
        fraisMoisJSONArray.put(fraisMoisJSON);


        Log.d("FRAIS MOIS JSO", "***********" + fraisMoisJSONArray);

        //Transfert des données au serveur
        accesDistant.envoi("synchronisation", fraisMoisJSONArray);

    }

    /*public void creerVisiteur(String id, String nom, String prenom, Context contexte){
        Log.d("Controleur-CREER VISITEUR ", "******" + visiteur.getId() + " " + visiteur.getPrenom() + " " + visiteur.getNom());
        //Serializer.serialize(Global.listFraisMois, context);
        Serializer.serialize(visiteur, contexte, Global.filenameFrais);
        accesDistant.envoi("recupFrais", visiteur.convertToJSONArray());
    }

    private static void recupSerializeVisiteur(Context contexte)
    {
        visiteur = (Visiteur)Serializer.deSerialize(contexte, Global.filenameFrais);
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
*/}
