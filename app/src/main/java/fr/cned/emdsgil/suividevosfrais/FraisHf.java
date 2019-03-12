package fr.cned.emdsgil.suividevosfrais;

import java.io.Serializable;

/**
 * Classe métier contenant la description d'un frais hors forfait
 *
 */
class FraisHf  implements Serializable {

	private final Float montant ;
	private final String motif ;
	private final Integer jour ;
	private Boolean nouveau = false;

	public FraisHf(Float montant, String motif, Integer jour, Boolean nouveau) {
		this.montant = montant ;
		this.motif = motif ;
		this.jour = jour ;
		this.nouveau = nouveau;
	}

	public Float getMontant() {
		return montant;
	}

	public String getMotif() {
		return motif;
	}

	public Integer getJour() {
		return jour;
	}

	public Boolean getNouveau() { return nouveau; }
}
