package fr.cned.emdsgil.suividevosfrais;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

    /**
     * Affichage d'une boite de dialogue
     */

    public void showAlertDialog(Context contexte, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(contexte).create();

        //Parametrage du titre
        alertDialog.setTitle(title);

        //Parametrage du message
        alertDialog.setMessage(message);

        //Parametrage des boutons
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Montrer message
        alertDialog.show();
    }
}
