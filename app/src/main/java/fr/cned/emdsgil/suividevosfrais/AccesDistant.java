package fr.cned.emdsgil.suividevosfrais;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.cned.emdsgil.suividevosfrais.controleur.Controle;
import fr.cned.emdsgil.suividevosfrais.outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.outils.AsyncResponse;

import static android.support.v4.content.ContextCompat.startActivity;

public class AccesDistant implements AsyncResponse {

    //constante
    private static final String SERVERADDR = "http://192.168.1.18/suividevosfrais/serveursuivifrais.php";
    private Controle controle;

    public AccesDistant() {
        super();
    }

    /**
     * retour du serveur distant
     *
     * @param output
     */
    @Override
    public void processFinish(String output) {
        Log.d("serveur", "******************" + output);
        //découpage du message reçu avec %
        String[] message = output.split("%");
        //dans message[0] : "authentification", "envoi", "reception", "Erreur !"
        //dans message[1] : reste du message

        //s'il y a 2 cases
        if (message.length > 1) {
            if (message[0].equals("authentification")) {
                Log.d("authentification", "***************" + message[1]);
                try {
                    JSONObject info = new JSONObject(message[1]);
                    String id = info.getString("id");
                    String nom = info.getString("nom");
                    String prenom = info.getString("prenom");

                    Visiteur visiteur = new Visiteur(id, nom, prenom);

                    if (id != null) {
                        Log.d("authentification OK", "******************* C'est OK pour " + message[1]);
                        //controle.setVisiteur(visiteur);
                    }
                } catch (JSONException e) {
                    Log.d("erreur", "conversion JSON impossible" + e.toString());
                }
            }else {
                if (message[0].equals("envoi")) {
                    Log.d("envoi", "***************" + message[1]);
                } else {
                    if (message[0].equals("reception")) {
                        Log.d("reception", "***************" + message[1]);
                    } else {
                        if (message[0].equals("Erreur !")) {
                            Log.d("Erreur !", "***************" + message[1]);
                        }
                    }
                }
            }
        }
        else {
            Log.d("authentification impossible", "*************** AUTH IMPOSSIBLE");
        }
    }


    public void envoi(String operation, JSONArray lesDonneesJSON) {
        AccesHTTP accesDonnees = new AccesHTTP();
        //lien de délégation
        accesDonnees.delegate = this;
        //ajout paramètres
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("lesdonnees", lesDonneesJSON.toString());
        //appel au serveur
        accesDonnees.execute(SERVERADDR);
    }

}
