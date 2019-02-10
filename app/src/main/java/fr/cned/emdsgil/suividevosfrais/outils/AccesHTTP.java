package fr.cned.emdsgil.suividevosfrais.outils;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Classe technique de connexion distante HTTP
 */

public class AccesHTTP extends AsyncTask<String, Integer, Long> {
	public String ret="";
	private ArrayList<NameValuePair> parametres;
	public AsyncResponse delegate=null;

	/**
	 * Constructeur
	 */
	public AccesHTTP(){
		parametres = new ArrayList<NameValuePair>();
	}


	/**
	 * Ajout d'un paramètre post
	 * @param nom
	 * @param valeur
	 */
	public void addParam(String nom, String valeur){
		parametres.add(new BasicNameValuePair(nom,valeur));
	}


	/**
	 * Connexion en tâche de fonc dans un thread séparé
	 * @param urls
	 * @return
	 */
	@Override
	protected Long doInBackground(String... urls) {

		HttpClient cnxHttp = new DefaultHttpClient();
		HttpPost paramCnx = new HttpPost(urls[0]);
		
		try {
			//Encodage des paramètres
		    paramCnx.setEntity(new UrlEncodedFormEntity(parametres));
		    //Connexion et envoi des parametres, attente de réponse
		    HttpResponse reponse = cnxHttp.execute(paramCnx);
		    //Transformation de la réponse
		    ret = EntityUtils.toString(reponse.getEntity());

		} catch (ClientProtocolException e) {
		    Log.d("Erreur ClientProtocol", "***************" + e.toString()) ;
		} catch (IOException e) {
            Log.d("Erreur IOException", "***************" +  e.toString()) ;
		}

        return null;
	}

	@Override
	protected void onPostExecute(Long result) {
		// ret contient l'information récupérée
		delegate.processFinish(this.ret.toString());
    }
	
}
