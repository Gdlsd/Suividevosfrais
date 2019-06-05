package fr.cned.emdsgil.suividevosfrais.outils;

import android.util.Log;

import org.json.JSONObject;

public class JSONconvert {

    public JSONObject stringToJSONObject(String message)
    {
        try {
            JSONObject obj = new JSONObject(message);
            return obj;
        } catch (Throwable t) {
            Log.e("ERREUR Conversion", "Problème de conversion JSON: \"" + message + "\"");
        }
        return null;
    }
}
