package fr.cned.emdsgil.suividevosfrais;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import fr.cned.emdsgil.suividevosfrais.controleur.Controle;

import static fr.cned.emdsgil.suividevosfrais.controleur.Controle.PREF_ID;

public class MainActivity extends AppCompatActivity {

    private static AccesDistant accesDistant;
    private Context context;
    private Controle controle;

    public MainActivity() {
        this.accesDistant = new AccesDistant();
        this.context = MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controle = Controle.getInstance(this);
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
                controle.getInstance(MainActivity.this).authentification(donnees);



                /*
                if(visiteur != null)
                {
                    accesMenuPrincipal();
                }
                else
                {
                    Toast.makeText(context, "Mot de passe ou identifiant erroné(s)", 5).show();
                }
                */

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

}
