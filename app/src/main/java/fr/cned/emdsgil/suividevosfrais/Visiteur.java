package fr.cned.emdsgil.suividevosfrais;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Visiteur implements Serializable {
    private String id;
    private String nom;
    private String prenom;

    public Visiteur(String id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom;}

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public void destroy(){
        System.exit(0);
    }
    /**
     * Conversion du visiteur au format JSONArray
     */
    public JSONArray convertToJSONArray(){
        List laListe = new ArrayList();
        laListe.add(id);
        laListe.add(nom);
        laListe.add(prenom);
        return new JSONArray(laListe);
    }
}
