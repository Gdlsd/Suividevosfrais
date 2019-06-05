package fr.cned.emdsgil.suividevosfrais.controleur;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.cned.emdsgil.suividevosfrais.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.Global;
import fr.cned.emdsgil.suividevosfrais.Serializer;
import fr.cned.emdsgil.suividevosfrais.outils.JSONconvert;


public class Controle {

    private static Controle instance = null;
    private static Context contexte;
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
            accesDistant = new AccesDistant(contexte);
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
    }

    /**
     * Récupération des frais du mois en cour pour mettre à jour les frais de l'application
     */
    public void recupFrais(String idVisiteur)
    {
        //Demande de récupération des frais de l'utilisateur
        JSONArray idVisiteurJSON = new JSONArray();
        idVisiteurJSON.put(Global.idVisiteur);
        accesDistant.envoi("recupFrais", idVisiteurJSON);
    }

    public void synchroFrais(Context contexte)
    {
        JSONconvert conversion = new JSONconvert();
        Gson gson = new Gson();
        //Récupération des frais serialisés dans un tableau de type String
        String fraisMois = gson.toJson(Serializer.deSerialize(contexte, Global.filenameFrais));

        //Conversion des frais au format JSON
        JSONObject fraisMoisJSON = conversion.stringToJSONObject(fraisMois);

        //Création d'un JSONArray à partir des données JSON pour pouvoir transférer les données
        //Ajout de l'id du visiteur en cours
        JSONArray fraisMoisJSONArray = new JSONArray();
        fraisMoisJSONArray.put(Global.idVisiteur);
        fraisMoisJSONArray.put(fraisMoisJSON);
        Toast.makeText(contexte, "Vos frais ont été synchronisés.", Toast.LENGTH_SHORT).show();

        //Transfert des données au serveur
        accesDistant.envoi("synchronisation", fraisMoisJSONArray);
    }
}
