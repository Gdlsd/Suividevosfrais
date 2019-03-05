package fr.cned.emdsgil.suividevosfrais;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.cned.emdsgil.suividevosfrais.controleur.Controle;



public class MainActivity extends AppCompatActivity {

    private static AccesDistant accesDistant;
    private Context context;
    private Controle controle;

    public MainActivity() {
        this.accesDistant = new AccesDistant(this);
        this.context = this;
        this.controle = controle.getInstance(this);
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
                String password = ((EditText)findViewById(R.id.txtEditMdp)).getText().toString();

                controle.authentification(login, password);
            }
        });
    }

    /**
     * Envoie l'utilisateur sur le menu principal
     */
    public void accesMenuPrincipal(){
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
