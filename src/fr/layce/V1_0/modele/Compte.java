package fr.layce.V1_0.modele;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fr.layce.V1_0.modele.exception.BanqueNonRenseigneException;
import fr.layce.V1_0.modele.exception.MauvaisAttrException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Classe représentant un compte en banque.
 * Un compte est caractérisé par un nom et un solde.
 * Il peut appartenir à une banque.
 * @author Layce
 */
public class Compte {
	private int mois = 0;
	private int annee = 0;
	private String nom;
	private double balance;
	private String banque;
	
	// <Annee, <Mois, Transactions>>
	private HashMap<Integer, HashMap<Integer, ArrayList<Transaction>>> transferts;
	private ArrayList<FixTransaction> fixed_transferts;
	
	private ObservableList<Transaction> view_transferts;
	
	/* CONSTRUCTEURS */
	/**
	 * Constructeur par défaut de la classe.
	 * Instancie le nom à "non renseigne" et le solde à 0.
	 */
	public Compte() {
		this.nom = "Non renseigne";
		this.balance = 0;
		this.banque = null;
		this.transferts = new HashMap<Integer, HashMap<Integer, ArrayList<Transaction>>>();
		this.fixed_transferts = new ArrayList<FixTransaction>();
		this.view_transferts = FXCollections.observableArrayList();
	}
	
	/**
	 * Constructeur alternatif de la classe.
	 * @param nom du compte en banque
	 * @param balance solde du compte
	 * @throws MauvaisAttrException erreur si le nom est nul
	 */
	public Compte(String nom, double balance) throws MauvaisAttrException {
		if (nom.equals("") || nom.equals(null))
			throw new MauvaisAttrException("Un compte doit contenir un nom");
		this.nom = nom;
		this.balance = balance;
	}
	
	/**
	 * Constructeur alternatif de la classe
	 * @param nom du compte en banque
	 * @param balance solde du compte
	 * @param banque nom de la banque auquel appartient le compte
	 * @throws MauvaisAttrException erreur si le nom est nul
	 */
	public Compte(String nom, double balance, String banque) throws MauvaisAttrException {
		if (nom.equals("") || nom.equals(null))
			throw new MauvaisAttrException("Un compte doit contenir un nom");
		this.nom = nom;
		this.balance = balance;
		this.banque = banque;
	}
	
	public void ajouterTransactionFixe(FixTransaction ft) {
		if (ft != null) {
			if (!fixed_transferts.contains(ft)) {
				fixed_transferts.add(ft);
			}
		}
		checkFixed();
		miseAJourSoldes();
		updateViewTransferts();
	}
	
	/* METHODES */
	/**
	 * Fonction ajoutant une nouvelle transaction.
	 * @param montant Valeur transféré (positif ou nulle)
	 * @param date à laquelle c'est fait le transfert
	 * @param type de transfert opéré {@link fr.layce.V1_0.modele.Type}
	 * @param societe vers ou de laquelle a été fait le transfert
	 */
	public void ajouterTransfert(double montant, LocalDate date, Type type, String societe) {
		int year = date.getYear();
		if (!transferts.containsKey(year)) {
			HashMap<Integer, ArrayList<Transaction>> hm = new HashMap<Integer, ArrayList<Transaction>>();
			ArrayList<Transaction> arr = new ArrayList<Transaction>();
			arr.add(new Transaction(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), type, societe, montant, balance + montant));
			hm.put(year, arr);
			transferts.put(year, hm);
		} else {
			int mois = date.getMonthValue();
			if (transferts.get(year).containsKey(mois)) {
				transferts.get(year).get(mois).add(new Transaction(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), type, societe, montant, balance + montant));
			} else {
				ArrayList<Transaction> arr = new ArrayList<Transaction>();
				arr.add(new Transaction(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), type, societe, montant, balance + montant));
				transferts.get(year).put(mois, arr);
			}
		}
		
		this.balance = this.balance + montant;
		miseAJourSoldes();
		updateViewTransferts();
	}
	
	/**
	 * Fonction ajoutant une nouvelle transaction
	 * @param transaction objet obtenu par l'assistant d'ajout
	 */
	public void ajouterTransfert(Transaction transaction) {
		int year = transaction.date().getYear();
		int mois = transaction.date().getMonthValue();
		if (!transferts.containsKey(year)) {
			HashMap<Integer, ArrayList<Transaction>> hm = new HashMap<Integer, ArrayList<Transaction>>();
			ArrayList<Transaction> arr = new ArrayList<Transaction>();
			arr.add(transaction);
			hm.put(mois, arr);
			transferts.put(year, hm);
		} else {
			if (transferts.get(year).containsKey(mois)) {
				transferts.get(year).get(mois).add(transaction);
			} else {
				ArrayList<Transaction> arr = new ArrayList<Transaction>();
				arr.add(transaction);
				transferts.get(year).put(mois, arr);
			}
		}
		
		this.balance = this.balance + transaction.getMontantNumber();
		miseAJourSoldes();
		updateViewTransferts();
	}
	
	/**
	 * Fonction modifiant une transaction.
	 * @param old ancienne transaction qui va être modifier
	 * @param nv nouvelle transaction avec les modifications apportées
	 */
	public void modifierTransaction(Transaction old, Transaction nv) {
		int year = old.date().getYear();
		int mois = old.date().getMonthValue();
		this.transferts.get(year).get(mois).remove(old);
		this.transferts.get(year).get(mois).add(nv);
		miseAJourSoldes();
		updateViewTransferts();
	}
	
	/**
	 * Fonction supprimant une transaction.
	 * @param transfert à supprimer
	 */
	public boolean supprimerTransaction(Transaction transfert) {
		int year = transfert.date().getYear();
		int mois = transfert.date().getMonthValue();
		if (this.transferts.get(year).get(mois).contains(transfert)) {
			this.transferts.get(year).get(mois).remove(transfert);
			miseAJourSoldes();
			updateViewTransferts();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Fonction sauvegardant les données.
	 * @param fichier dans lequel va s'enregistrer les données
	 * @throws IOException erreur lors de l'écriture dans le fichier
	 */
	public void sauvegarderSous(File fichier) throws IOException {
		FileOutputStream fos = new FileOutputStream(fichier);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(new HashMap<Integer, HashMap<Integer, ArrayList<Transaction>>>(this.transferts));
		oos.writeObject(fixed_transferts);
		oos.close();
	}
	
	/**
	 * Fonction chargeant les données.
	 * @param fichier dans lequel sont chargées les données
	 * @throws ClassNotFoundException erreur lors du cast de la classe (ne devrait pas apparaître)
	 * @throws IOException erreur lors de la lecture du fichier
	 */
	@SuppressWarnings("unchecked")
	public void charger(File fichier) throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream(fichier);
		ObjectInputStream ois = new ObjectInputStream(fis);
		this.transferts = (HashMap<Integer, HashMap<Integer, ArrayList<Transaction>>>)ois.readObject();
		try {
			this.fixed_transferts = (ArrayList<FixTransaction>)ois.readObject();
		} catch (Exception e) {
			// Ignore
		}
		
		ois.close();
		checkFixed();
		miseAJourSoldes();
		updateViewTransferts();
	}
	
	private void checkFixed() {
		if (!fixed_transferts.isEmpty()) {
			for (FixTransaction ft : fixed_transferts) {
				if (testDate(ft)) {
					try {
						ft.setDate(ft.getJour());
						transferts.get(ft.date().getYear()).get(ft.date().getMonthValue()).add((Transaction) ft);
					} catch (NullPointerException e){ // Si mois non existant
						ArrayList<Transaction> arr = new ArrayList<Transaction>();
						arr.add((Transaction) ft);
						try {
							transferts.get(ft.date().getYear()).put(ft.date().getMonthValue(), arr);
						} catch (NullPointerException e2) { // Si année non existantes
							HashMap<Integer, ArrayList<Transaction>> hash = new HashMap<Integer, ArrayList<Transaction>>();
							hash.put(ft.date().getMonthValue(), arr);
							transferts.put(ft.date().getYear(), hash);
						}
					}
					
					switch (ft.getFix()) {
						case JOURNALIER:
							ft.setJour(ft.getJour().plusDays(1));
							break;
						case QUOTIDIEN:
							ft.setJour(ft.getJour().plusWeeks(1));
							break;
						case HEBDOMADAIRE:
							ft.setJour(ft.getJour().plusMonths(1));
							break;
						case ANNUEL:
							ft.setJour(ft.getJour().plusYears(1));
							break;
					}
					
					ft.fixer();
				}
			}
		}
	}
	
	private boolean testDate(FixTransaction ft) {
		int annee = new Date().toInstant().atZone(ZoneId.systemDefault()).getYear();
		int mois = new Date().toInstant().atZone(ZoneId.systemDefault()).getMonthValue();
		int jour = new Date().toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth();
		boolean ok = false;
		if (annee <= ft.getJour().getYear()) {
			if (mois <= ft.getJour().getMonthValue()) {
				if (jour <= ft.getJour().getDayOfMonth())
					ok = true;
			}
		}
		return ok;
	}
	
	/**
	 * Fonction recalculant les soldes de chaque transaction par ordre croissant.
	 */
	public void miseAJourSoldes() {
		balance = 0;
		for (int annee : transferts.keySet()) {
			for (int mois : transferts.get(annee).keySet()) {
				for (Transaction t : transferts.get(annee).get(mois)) {
					balance += t.getMontantNumber();
					t.setSolde(balance);
				}
			}
		}
	}
	
	/**
	 * Fonction transformant l'arraylist en liste observable par la vue.
	 */
	public void updateViewTransferts() {
		ArrayList<Transaction> arr = new ArrayList<Transaction>();
		if (this.annee == 0) {
			if (this.mois == 0) { // TOUS
				for (int annee : transferts.keySet()) {
					for (int mois : transferts.get(annee).keySet()) {
						for (Transaction t : transferts.get(annee).get(mois)) {
							arr.add(t);
						}
					}
				}
			} else { // TOUTES ANNEES ET MOIS SELECT
				for (int annee : transferts.keySet()) {
					if (transferts.get(annee).containsKey(this.mois)) {
						for (Transaction t : transferts.get(annee).get(mois)) {
							arr.add(t);
						}
					}
				}
			}
		} else {
			if (this.mois == 0) { // ANNEE SELECT ET TOUS LES MOIS
				for (int mois : transferts.get(this.annee).keySet()) {
					for (Transaction t : transferts.get(this.annee).get(mois)) {
						arr.add(t);
					}
				}
			} else { // ANNEE SELECT ET MOIS SELECT
				for (Transaction t : transferts.get(this.annee).get(this.mois)) {
					arr.add(t);
				}
			}
		}
		
		this.view_transferts = FXCollections.observableArrayList(arr);
	}
	
	public ArrayList<Integer> getAnnes(){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int annee : transferts.keySet()) {
			arr.add(annee);
		}
		return arr;
	}
	
	public ArrayList<Integer> getMois() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		if (this.annee == 0) {
			for (int annee : transferts.keySet()) {
				for (int mois : transferts.get(annee).keySet()) {
					if (!arr.contains(mois))
						arr.add(mois);
				}
			}
		} else {
			for (int mois : transferts.get(this.annee).keySet()) {
				if (!arr.contains(mois))
					arr.add(mois);
			}
		}
		return arr;
	}
	
	public void changerAnnee(String annee) {
		if (annee != null && annee != "") {
			if (annee == "Toutes")
				this.annee = 0;
			else
				this.annee = Integer.valueOf(annee);
			updateViewTransferts();
		}
	}
	
	public void changerMois(String mois) {
		if (mois != null && mois != "") {
			this.mois = stringToMois(mois);
			updateViewTransferts();
		}
	}
	
	private int stringToMois(String s) {
		switch (s) {
		case "Janvier":
			return 1;
		case "Février":
			return 2;
		case "Mars":
			return 3;
		case "Avril":
			return 4;
		case "Mai":
			return 5;
		case "Juin":
			return 6;
		case "Juillet":
			return 7;
		case "Août":
			return 8;
		case "Septembre":
			return 9;
		case "Octobre":
			return 10;
		case "Novembre":
			return 11;
		case "Décembre":
			return 12;
		default:
			return 0;
		}
	}
	
	/* GETTERS */
	public String getNom() {
		return this.nom;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public String getBanque() throws BanqueNonRenseigneException {
		if (this.banque == null)
			throw new BanqueNonRenseigneException("Votre compte est attribuÃ© Ã  aucune banque");
		return this.banque;
	}
	
	public ObservableList<Transaction> getData(){
		updateViewTransferts();
		return this.view_transferts;
	}
	
	/* SETTERS */
	public void setBanque(String b) {
		this.banque = b;
	}
	
	public void setMois(int m) {
		this.mois = m;
		updateViewTransferts();
	}
}
