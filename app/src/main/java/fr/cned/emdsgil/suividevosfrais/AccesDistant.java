package fr.cned.emdsgil.suividevosfrais;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import fr.cned.emdsgil.suividevosfrais.controleur.Controle;
import fr.cned.emdsgil.suividevosfrais.outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.outils.AsyncResponse;


public class AccesDistant implements AsyncResponse {

    //constante
    private static final String SERVERADDR = "http:/******/suividevosfrais/serveursuivifrais.php";
    private Controle controle;
    private Context contexte;


    public AccesDistant(Context contexte) {
        controle = Controle.getInstance(contexte);
        this.contexte = contexte;
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


        //s'il y a 2 cases
        if (message.length > 1) {
            if (message[0].equals("authentification")) {
                Log.d("authentification", "***************" + message[1]);
                try {
                    if(message[1].equals("\"erreurLogin\""))
                    {
                        AlertDialogManager alertDialogManager = new AlertDialogManager();
                        alertDialogManager.showAlertDialog(contexte,
                                "Connexion impossible",
                                "Paramètre(s) de connexion incorrect(s)" );
                    }
                    else
                    {
                        JSONObject info = new JSONObject(message[1]);
                        String id = info.getString("id");
                        String nom = info.getString("nom");
                        String prenom = info.getString("prenom");

                        Visiteur visiteur = new Visiteur(id, nom, prenom);
                        Global.idVisiteur = visiteur.getId();
                        this.contexte.startActivity(new Intent(this.contexte, MenuActivity.class));

                        Toast.makeText(contexte, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("erreur", "conversion JSON impossible" + e.toString());
                }
            } else {
                if (message[0].equals("synchronisation")) {
                    Log.d("synchronisation", "***************" + message[1]);
                    try {
                    if(message[1].length() > 0)
                    {
                        //Récupération et enregistrement des frais sauvegardés sur la base distante
                        JSONObject recupFrais = new JSONObject(message[1]);
                        Log.d("LES FRAIS JSON RETOUR SERV", "*******" + recupFrais);

//                        //Effacer les données sauvegardées pour les réécrire.
//                        Global.listFraisMois.clear();
//                        Serializer.serialize(Global.listFraisMois, contexte, Global.filenameFrais);

                        //Récupération des données
                        JSONObject lesFrais = recupFrais.getJSONObject("");
                        Integer annee   = lesFrais.getInt("annee");
                        Integer mois    = lesFrais.getInt("mois");
                        Integer etape   = lesFrais.getInt("etape");
                        Integer km      = lesFrais.getInt("km");
                        Integer nuitee  = lesFrais.getInt("nuitee");
                        Integer repas   = lesFrais.getInt("repas");
                        JSONArray lesFraisHf = lesFrais.getJSONArray("lesFraisHf");

                        FraisMois frais = new FraisMois(annee, mois);
                        frais.setEtape(etape);
                        frais.setKm(km);
                        frais.setNuitee(nuitee);
                        frais.setRepas(repas);

                        for(int i = 0; i < lesFraisHf.length(); i++)
                        {
                            JSONObject unFrais = lesFraisHf.getJSONObject(i);
                            String date = unFrais.getString("date");
                            String[] dateSplit = date.split("-");
//                            Integer anneeFraisHf = Integer.parseInt(dateSplit[0]);
//                            Integer moisFraisHf = Integer.parseInt(dateSplit[1]);
                            Integer jourFraisHf = Integer.parseInt(dateSplit[2]);
                            Float montantFraisHf = Float.parseFloat(unFrais.getString("montant"));
                            String motifFraisHf = unFrais.getString("libelle");

                            frais.addFraisHf(montantFraisHf, motifFraisHf, jourFraisHf);
                        }

                        //Serialisation du frais à la place du frais existant
                        Serializer.serialize(frais, contexte, Global.filenameFrais);
//                        Log.d("TEST", "************"+message[1]);
//                        for(int i = 0; i < lesFraisHf.length(); i++)
//                        {
//                            JSONObject unFrais = lesFraisHf.getJSONObject(i);
//                            String date = unFrais.getString("date");
//                            String[] dateSplit = date.split("-");
//                            Integer annee = Integer.parseInt(dateSplit[0]);
//                            Integer mois = Integer.parseInt(dateSplit[1]);
//                            Integer jour = Integer.parseInt(dateSplit[2]);
//                            Float montant = Float.parseFloat(unFrais.getString("montant"));
//                            String motif = unFrais.getString("libelle");
//                            Integer key = Integer.parseInt(unFrais.getString("mois"));
//
//
//                            if (!Global.listFraisMois.containsKey(key)) {
//                            // creation du mois et de l'annee s'ils n'existent pas déjà
//                            Global.listFraisMois.put(key, new FraisMois(annee, mois)) ;
//                            }
//                            Global.listFraisMois.get(key).addFraisHf(montant, motif, jour) ;
//                        }
//                        Serializer.serialize(Global.listFraisMois, contexte, Global.filenameFrais);
                    }
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
