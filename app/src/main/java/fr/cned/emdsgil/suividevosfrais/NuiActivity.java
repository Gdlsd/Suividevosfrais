package fr.cned.emdsgil.suividevosfrais;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker.OnDateChangedListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Locale;

public class NuiActivity extends AppCompatActivity {

    //Informations affichées dans l'activité
    private Integer annee;
    private Integer mois;
    private Integer qte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nui);
        setTitle("GSB : Frais Nuitées");
        //modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datNui), false);
        // valorisation des propriétés
        valoriseProprietes();
        //chargement des méthodes événementielles
        imgReturn_clic();
        cmdValider_clic();
        cmdPlus_clic();
        cmdMoins_clic();
        dat_clic();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            retourActivityPrincipale() ;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Valorisation des propriétés avec les informations affichées
     */
    private void valoriseProprietes() {
        annee = ((DatePicker)findViewById(R.id.datNui)).getYear();
        mois = ((DatePicker)findViewById(R.id.datNui)).getMonth() + 1;
        //récupération de la qte correspondant au mois actuel
        qte = 0 ;
        Integer key = annee*100+mois;
        if(Global.listFraisMois.containsKey(key)) {
            qte = Global.listFraisMois.get(key).getNuitee();
        }
        ((EditText)findViewById(R.id.txtNui)).setText(String.format(Locale.FRANCE, "%d", qte));
    }

    /**
     * Retour à l'activity principale au clic sur l'image
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgNuiReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale() ;
            }
        }) ;
    }

    /**
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdValider_clic(){
        findViewById(R.id.cmdNuiValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, NuiActivity.this, Global.filenameFrais);
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur le clic du bouton plus : ajout de 1 dans la quantité
     */
    private void cmdPlus_clic(){
        findViewById(R.id.cmdNuiPlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                qte+=1;
                enregNewQte();

            }
        });
    }

    /**
     * Sur le clic du bouton moins : enlève 1 dans la quantité si c'est possible
     */
    private void cmdMoins_clic(){
        findViewById(R.id.cmdNuiMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                qte = Math.max(0, qte-1); // suppression de 1 si possible
                enregNewQte();
            }
        });
    }

    /**
     * Sur le changement de date : mise à jour de l'affichage de la qte
     */
    private void dat_clic() {
        final DatePicker uneDate = (DatePicker) findViewById(R.id.datNui);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                valoriseProprietes();
            }
        });
    }

    /**
     * Enregistrement dans la zone de texte et dans la liste de la nouvelle qte, à la date choisie
     */
    private void enregNewQte() {
        // enregistrement dans la zone de texte
        ((EditText)findViewById(R.id.txtNui)).setText(String.format(Locale.FRANCE, "%d", qte)) ;
        // enregistrement dans la liste
        Integer key = annee*100+mois ;
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas déjà
            Global.listFraisMois.put(key, new FraisMois(annee, mois)) ;
        }
        Global.listFraisMois.get(key).setNuitee(qte);
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(NuiActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}


