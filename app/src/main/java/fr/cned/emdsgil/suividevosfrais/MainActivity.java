package fr.cned.emdsgil.suividevosfrais;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static AccesDistant accesDistant;
    private Context context;

    public MainActivity() {
        this.accesDistant = new AccesDistant();
        this.context = MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cmdValider_clic();
    }

    /**
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdValider_clic() {
        findViewById(R.id.btnValider).setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                String login = ((EditText)findViewById(R.id.txtEditLogin)).getText().toString();
                String mdp = ((EditText)findViewById(R.id.txtEditMdp)).getText().toString();
                List loginmdp = new ArrayList();
                loginmdp.add(login);
                loginmdp.add(mdp);
                JSONArray donnees = new JSONArray(loginmdp);

                accesDistant.envoi("authentification", donnees);

                Visiteur visiteur = recupSerializeVisiteur();

                if(visiteur != null)
                {
                    accesMenuPrincipal();
                }
                else
                {
                    Toast.makeText(context, "Mot de passe ou identifiant erroné(s)", 5).show();
                }

            }
        });
    }

    /**
     * Envoie l'utilisateur sur le menu principal
     */
    public void accesMenuPrincipal(){
        Intent menuActivity = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(menuActivity);
    }

    private Visiteur recupSerializeVisiteur() {
        /* Pour éviter le warning "Unchecked cast from Object to Hash" produit par un casting direct :
         * Global.listFraisMois = (Hashtable<Integer, FraisMois>) Serializer.deSerialize(Global.filename, MenuActivity.this);
         * On créé un Hashtable générique <?,?> dans lequel on récupère l'Object retourné par la méthode deSerialize, puis
         * on cast chaque valeur dans le type attendu.
         * Seulement ensuite on affecte cet Hastable à Global.listFraisMois.
         */
        Hashtable<?, ?> monHash = (Hashtable<?, ?>) Serializer.deSerialize(MainActivity.this);
        if (monHash != null) {
            Hashtable<Integer, Visiteur> monHashCast = new Hashtable<>();
            for (Hashtable.Entry<?, ?> entry : monHash.entrySet()) {
                monHashCast.put((Integer) entry.getKey(), (Visiteur) entry.getValue());
                return (Visiteur) entry.getValue();
            }
            Global.unVisiteur = monHashCast;
        }
        // si rien n'a été récupéré, il faut créer la liste
        if (Global.unVisiteur == null) {
            Global.unVisiteur = new Hashtable<>();
            /* Retrait du type de l'HashTable (Optimisation Android Studio)
             * Original : Typage explicit =
             * Global.listFraisMois = new Hashtable<Integer, FraisMois>();
             */

        }
        return null;
    }
}
