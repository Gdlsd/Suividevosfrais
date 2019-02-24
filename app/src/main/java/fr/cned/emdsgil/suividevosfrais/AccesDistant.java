package fr.cned.emdsgil.suividevosfrais;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.cned.emdsgil.suividevosfrais.controleur.Controle;
import fr.cned.emdsgil.suividevosfrais.outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.outils.AsyncResponse;


public class AccesDistant implements AsyncResponse {

    //constante
    private static final String SERVERADDR = "http://*******/suividevosfrais/serveursuivifrais.php";
    private Controle controle;



    public AccesDistant() {
        controle = Controle.getInstance(null);
    }

    /**
     * retour du serveur distant
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
                    Global.idVisiteur = visiteur.getId();
                } catch (JSONException e) {
                    Log.d("erreur", "conversion JSON impossible" + e.toString());
                }
            } else {
                if (message[0].equals("synchronisation")) {
                    Log.d("synchronisation", "***************" + message[1]);
                    /////////////////////////////////////////////////////
                    // Ajout de code pour un éventuel retour de données//
                    /////////////////////////////////////////////////////
                    try {
                        JSONObject info = new JSONObject(message[1]);
                    } catch (JSONException e) {
                        Log.d("erreur", "conversion JSON impossible" + e.toString());
                    }
                }
            }
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
