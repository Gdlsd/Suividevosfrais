package fr.cned.emdsgil.suividevosfrais;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import fr.cned.emdsgil.suividevosfrais.controleur.Controle;
import fr.cned.emdsgil.suividevosfrais.outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.outils.AsyncResponse;


public class AccesDistant implements AsyncResponse {

    //constante
    private static final String SERVERADDR = "http://IpAdress/suividevosfrais/serveursuivifrais.php";


    private Controle controle;
    private Context contexte;


    public AccesDistant(Context contexte) {
        controle = Controle.getInstance(contexte);
        this.contexte = contexte;
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


        //s'il y a 2 cases
        if (message.length > 1) {
            if (message[0].equals("authentification")) {
                Log.d("authentification", "***************" + message[1]);
                try {
                    if (message[1].equals("\"erreurLogin\"")) {
                        Toast.makeText(contexte, "Paramètre(s) de connexion incorrect(s)", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject info = new JSONObject(message[1]);
                        String id = info.getString("id");
                        String nom = info.getString("nom");
                        String prenom = info.getString("prenom");

                        Visiteur visiteur = new Visiteur(id, nom, prenom);
                        Global.idVisiteur = visiteur.getId();

                        controle.recupFrais(Global.idVisiteur);

                        this.contexte.startActivity(new Intent(this.contexte, MenuActivity.class));

                        Toast.makeText(contexte, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("erreur", "conversion JSON impossible" + e.toString());
                }
            }
            else if (message[0].equals("synchronisation")) {
                    Log.d("synchronisation", "***************" + message[1]);
                    if (message[1].equals("synchroOk")) {
                        //Mise à jour des frais dans l'appareil avec les données distantes
                        //Dans le cas ou des frais auraient été ajoutés depuis l'application web
                        controle.recupFrais(Global.idVisiteur);
                    }

            }
            else if(message[0].equals("recupFrais"))
            {
                Log.d("recupFrais", "***************" + message[1]);
                try {
                    if (!message[1].equals("\"aucunFrais\"")) {
                        //Récupération et enregistrement des frais sauvegardés sur la base distante
                        JSONObject recupFrais = new JSONObject(message[1]);

                        //Récupération des données
                        JSONObject lesFrais = recupFrais.getJSONObject("fraisBdd");
                        Integer annee   = lesFrais.getInt("annee");
                        Integer mois    = lesFrais.getInt("mois");
                        Integer etape   = lesFrais.getInt("etape");
                        Integer km      = lesFrais.getInt("km");
                        Integer nuitee  = lesFrais.getInt("nuitee");
                        Integer repas   = lesFrais.getInt("repas");
                        JSONArray lesFraisHf = lesFrais.getJSONArray("lesFraisHf");

                        //Suppression des frais existants
                        Global.listFraisMois.clear();

                        Integer key = annee*100+mois ;

                        if (!Global.listFraisMois.containsKey(key)) {
                            // creation du mois et de l'annee s'ils n'existent pas déjà
                            Global.listFraisMois.put(key, new FraisMois(annee, mois)) ;
                        }
                        Global.listFraisMois.get(key).setEtape(etape);
                        Global.listFraisMois.get(key).setKm(km);
                        Global.listFraisMois.get(key).setNuitee(nuitee);
                        Global.listFraisMois.get(key).setRepas(repas);
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

//                            if (!Global.listFraisMois.containsKey(key)) {
//                                // creation du mois et de l'annee s'ils n'existent pas déjà
//                                Global.listFraisMois.put(key, new FraisMois(annee, mois)) ;
//                            }
                            Global.listFraisMois.get(key).addFraisHf(montantFraisHf, motifFraisHf, jourFraisHf, false) ;
                        }

                        //Serialisation des frais à la place des frais existants
                        Serializer.serialize(Global.listFraisMois, contexte, Global.filenameFrais);
                    }
                    else
                    {
                        //Si aucune fiche, initialisation d'une fiche locale
                        Global.listFraisMois.clear();

                        //Reformation de la clé key en récupérant annee et mois
                        Calendar calendar = Calendar.getInstance();
                        int annee = calendar.get(Calendar.YEAR);
                        int mois = calendar.get(Calendar.MONTH);

                        int key = annee*100+mois;
                        Global.listFraisMois.put(key, new FraisMois(annee, mois));
                        Serializer.serialize(Global.listFraisMois, contexte, Global.filenameFrais);
                    }
                } catch (JSONException e) {
                    Log.d("erreur", "conversion JSON impossible" + e.toString());
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
