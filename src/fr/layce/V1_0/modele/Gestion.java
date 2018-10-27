package fr.layce.V1_0.modele;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.ObservableList;

/**
 * Modele principal de l'appplication.
 * Classe gérant les comptes.
 * @author Layce
 */
public class Gestion {
	private Compte compte;
	private boolean modifier;
	private File fichier;
	
	/* CONSTRUCTEUR */
	/**
	 * Constructeur de la classe.
	 * Instancie un compte nul.
	 */
	public Gestion() {
		this.compte = null;
		this.modifier = false;
		this.fichier = null;
	}
	
	/* METHODES */
	/**
	 * Fonction créant un nouveau compte vierge.
	 */
	public void nouveauCompte() {
		this.compte = new Compte();
	}
	
	public void ajouterTransactionFixe(FixTransaction ft) {
		compte.ajouterTransactionFixe(ft);
		modifier = true;
	}
	
	/**
	 * Update les soldes de façon à être bons en fonction de la date.
	 */
	public void miseAJourSoldes() {
		this.compte.miseAJourSoldes();
	}
	
	/**
	 * Fonction ajoutant une nouvelle transaction. (appelle ajouterTransfert de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @param montant Valeur transféré (positif ou nulle)
	 * @param date à laquelle c'est fait le transfert
	 * @param type de transfert opéré {@link fr.layce.V1_0.modele.Type}
	 * @param societe vers ou de laquelle a été fait le transfert
	 */
	public void ajouterTransaction(double montant, LocalDate date, Type type, String societe) {
		this.compte.ajouterTransfert(montant, date, type, societe);
		this.modifier = true;
	}
	
	/**
	 * Fonction ajoutant une transaction. (appelle ajouterTransafert de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @param transaction récupérée par l'assistant d'ajout
	 */
	public void ajouterTransaction(Transaction transaction) {
		this.compte.ajouterTransfert(transaction);
		this.modifier = true;
	}
	
	/**
	 * Modifie la transaction. (appelle modifierTransaction de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @param old ancienne transaction
	 * @param nv nouvelle transaction
	 */
	public void modifierTransaction(Transaction old, Transaction nv) {
		this.compte.modifierTransaction(old, nv);
		this.modifier = true;
	}
	
	/**
	 * Fonction supprimant une transaction. (appele supprimerTransaction de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @param transfert à supprimer
	 */
	public boolean supprimerTransaction(Transaction transfert) {
		boolean b = this.compte.supprimerTransaction(transfert);
		if (b)
			this.modifier = true;
		return b;
	}

	/**
	 * Fonction sauvegardant les données dans le fichier initial. (appelle enregistrer de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @throws IOException erreur lors de l'écriture dans le fichier
	 */
	public void sauvegarder() throws IOException {
		this.compte.sauvegarderSous(this.fichier);
		this.modifier = false;
	}
	
	/**
	 * Fonction sauvegardant les données. (appelle enregistrer de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @param fichier dans lequel va s'enregistrer les données
	 * @throws IOException erreur lors de l'écriture dans le fichier
	 */
	public void sauvegarderSous(File fichier) throws IOException {
		this.compte.sauvegarderSous(fichier);
		this.modifier = false;
		this.fichier = fichier;
	}
	
	/**
	 * Fonction chargeant les données. (appelle charger de la classe {@link fr.layce.V1_0.modele.Compte})
	 * @param fichier dans lequel sont chargées les données
	 * @throws ClassNotFoundException erreur lors du cast de la classe (ne devrait pas apparaître)
	 * @throws IOException erreur lors de la lecture du fichier
	 */
	public void charger(File fichier) throws ClassNotFoundException, IOException {
		nouveauCompte();
		this.compte.charger(fichier);
		this.fichier = fichier;
	}
	
	public ArrayList<Integer> getAnnees(){
		return compte.getAnnes();
	}
	
	public ArrayList<Integer> getMois(){
		return compte.getMois();
	}
	
	public void changerAnnee(String annee) {
		compte.changerAnnee(annee);
	}
	
	public void changerMois(String mois) {
		compte.changerMois(mois);
	}
	
	/* GETTERS */
	public boolean estModifier() {
		return this.modifier;
	}
	
	public boolean aCompte() {
		return this.compte != null;
	}
	
	public double solde() {
		return this.compte.getBalance();
	}
	
	public ObservableList<Transaction> getData(){
		return this.compte.getData();
	}
	
	public boolean aFichier() {
		return this.fichier != null;
	}
	
	public void setMois(int m) {
		this.compte.setMois(m);
	}
	
	public double getBalance() {
		return this.compte.getBalance();
	}
}
