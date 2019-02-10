package fr.cned.emdsgil.suividevosfrais;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static AccesDistant accesDistant;

    public MainActivity() {
        this.accesDistant = new AccesDistant();
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
            public void onClick(View v) {
                String login = ((EditText)findViewById(R.id.txtEditLogin)).getText().toString();
                String mdp = ((EditText)findViewById(R.id.txtEditMdp)).getText().toString();
                List loginmdp = new ArrayList();
                loginmdp.add(login);
                loginmdp.add(mdp);
                JSONArray donnees = new JSONArray(loginmdp);
                accesDistant.envoi("authentification", donnees);
                accesMenuPrincipal();
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

    public void recupFrais() {

    }
}
