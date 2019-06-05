package fr.cned.emdsgil.suividevosfrais;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

class FraisHfAdapter extends BaseAdapter {

	private final ArrayList<FraisHf> lesFrais ; // liste des frais du mois
	private final LayoutInflater inflater ;
	private Context context;
    /**
	 * Constructeur de l'adapter pour valoriser les propriétés
     * @param context Accès au contexte de l'application
     * @param lesFrais Liste des frais hors forfait
     */
	public FraisHfAdapter(Context context, ArrayList<FraisHf> lesFrais) {
		inflater = LayoutInflater.from(context) ;
		this.lesFrais = lesFrais ;
		this.context = context;
    }
	
	/**
	 * retourne le nombre d'éléments de la listview
	 */
	@Override
	public int getCount() {
		return lesFrais.size() ;
	}

	/**
	 * retourne l'item de la listview à un index précis
	 */
	@Override
	public Object getItem(int index) {
		return lesFrais.get(index) ;
	}

	/**
	 * retourne l'index de l'élément actuel
	 */
	@Override
	public long getItemId(int index) {
		return index;
	}

	/**
	 * structure contenant les éléments d'une ligne
	 */
	private class ViewHolder {
		TextView txtListJour ;
		TextView txtListMontant ;
		TextView txtListMotif ;
		ImageButton cmdSuppHf;
	}
	
	/**
	 * Affichage dans la liste
	 */
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		//holder est un objet de la petite classe déclarée plus haut
		final ViewHolder holder ;
		//si la ligne active reçue en paramètre n'existe pas encore
		if (convertView == null) {
			holder = new ViewHolder() ;
			//la ligne est construite à partir de la structure de la ligne (récupérée dans layout_liste)
			convertView = inflater.inflate(R.layout.layout_liste, parent, false) ;
			//chaque propriété de holder (correspondant aux objets graphiques) est liée à un des objets graphiques
			holder.txtListJour = convertView.findViewById(R.id.txtListJour);
			holder.txtListMontant = convertView.findViewById(R.id.txtListMontant);
			holder.txtListMotif = convertView.findViewById(R.id.txtListMotif);
			holder.cmdSuppHf = convertView.findViewById(R.id.cmdSuppHf);

			if(lesFrais.get(index).getNouveau() == false )
			{
				holder.cmdSuppHf.setVisibility(View.INVISIBLE);
			}
			//affectation du holder comme tag (étiquette) de la ligne
			convertView.setTag(holder) ;
		}else{
			//Si la ligne existe déjà, holder récupère le holder de la ligne
			holder = (ViewHolder)convertView.getTag();
		}
		//Holder est lié à une ligne graphique
		//Il faut maintenant valoriser les propriétés avec les frais de lesFrais à un indice précis (index)
		holder.txtListJour.setText(String.format(Locale.FRANCE, "%d", lesFrais.get(index).getJour()));
		holder.txtListMontant.setText(String.format(Locale.FRANCE, "%.2f", lesFrais.get(index).getMontant())) ;
		holder.txtListMotif.setText(lesFrais.get(index).getMotif()) ;
		//Mémorisation de l'indice de ligne en étiquette de cmdSuppHf pour récupérer cet indice dans l'évènement
		holder.cmdSuppHf.setTag(index);


		//Gestion de l'évènement click sur cmdSuppHf
		holder.cmdSuppHf.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				int index = (int)v.getTag();
			    lesFrais.remove(lesFrais.get(index));
				Serializer.serialize(Global.listFraisMois, context, Global.filenameFrais);
                //Rafraichissement de la liste visuelle
                notifyDataSetChanged();
			}
		});
		return convertView ;
	}
	
}
