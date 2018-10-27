package fr.layce.V1_0.controller;

import fr.layce.V1_0.modele.Config;
import fr.layce.V1_0.modele.FixTransaction;
import fr.layce.V1_0.modele.Gestion;
import fr.layce.V1_0.modele.Transaction;
import fr.layce.V1_0.vue.Fenetre;
import fr.layce.V1_0.vue.dialog.AssistantFixTransaction;
import fr.layce.V1_0.vue.dialog.AssistantTransaction;
import fr.layce.V1_0.vue.dialog.Dialogs;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe controllant les diff�rents �l�ments de l'application.
 * @author Layce
 */
public class Controller {
	private Gestion gestion;
	private Fenetre fenetre;
	
	/**
	 * Constructeur de la classe.
	 * @param gestion modele
	 * @param fenetre vue
	 */
	public Controller(Gestion gestion, Fenetre fenetre) {
		this.gestion = gestion;
		this.fenetre = fenetre;
	}
	
	/* LISTENERS */
	/**
	 * Cr�er un nouveau compte et charge les informations � l'�cran.
	 */
	public void nouveau() {
		if (this.gestion.aCompte()) {
			if (this.gestion.estModifier())
				confirmation("Sauvegarde - Confirmation", "Votre document a �t� modifi�. Voulez-vous le sauvegarder ?");
		}
		this.gestion.nouveauCompte();
		fenetre.afficherEtat("Nouveau compte cree");
		fenetre.initInformations(this);
		fenetre.enableSauvegarderSous();
	}
	
	/**
	 * Ouvre l'assistant de transaction afin de pouvoir la modifier.
	 * @param transaction � modifier
	 */
	public void modifierTransaction(Transaction transaction) {
		if (this.gestion.aCompte()) {
			Optional<Transaction> nv_trans = new AssistantTransaction("Assistant Transaction - Modifier", this.gestion, transaction).showAndWait();
			if (nv_trans.isPresent()) {
				this.gestion.modifierTransaction(transaction, nv_trans.get());
				this.gestion.miseAJourSoldes();
				fenetre.setData(gestion.getData());
				fenetre.enableSauvegarder();
				fenetre.updateTri();
				fenetre.updateSolde(gestion.getBalance());
			}
		}
	}
	
	/**
	 * Ouvre l'assistant d'ajout de transactione et ajoute la transaction au compte.
	 */
	public void ajouterTransaction() {
		if (this.gestion.aCompte()) {
			Optional<Transaction> trans = new AssistantTransaction("Assistant Transaction - Ajouter", this.gestion).showAndWait();
			if (trans.isPresent()) {
				this.gestion.ajouterTransaction(trans.get());
				this.gestion.miseAJourSoldes();
				fenetre.setData(gestion.getData());
				fenetre.enableSauvegarder();
				fenetre.updateTri();
				fenetre.updateSolde(gestion.getBalance());
			}
		} else {
			Dialogs.warning("Attention - Assistant d'ajout de Transaction", "Vous devez avoir un compte d'ouvert afin d'ajouter une transaction.");
		}
	}
	
	public void fixerTransaction(Transaction t) {
		if (this.gestion.aCompte()) {
			if (t != null) {
				Optional<FixTransaction> fix_trans = new AssistantFixTransaction("Assistant Transaction Fixe - Ajouter", t).showAndWait();
				if (fix_trans.isPresent()) {
					gestion.ajouterTransactionFixe(fix_trans.get());
					fenetre.setData(gestion.getData());
					fenetre.enableSauvegarder();
					fenetre.updateTri();
					fenetre.updateSolde(gestion.getBalance());
				}
			}
		}
	}
	
	/**
	 * Sauvegarde les donn�es dans le fichier initial.
	 */
	public void sauvegarder() {
		if (this.gestion.aCompte()) {
			if (this.gestion.estModifier()) {
				if (this.gestion.aFichier()) {
					try {
						this.fenetre.getScene().setCursor(Cursor.WAIT);
						this.fenetre.afficherEtat("Sauvegarde des donn�es...");
						this.gestion.sauvegarder();
						this.fenetre.afficherEtat("Donn�es sauvegard�es");
						fenetre.disableSauvegarder();
					} catch (IOException e) {
						Dialogs.error("Erreur - Sauvegarde", "Erreur lors de la sauvegarde. Fichier impossible � ouvrir !");
					}
				} else {
					sauvegarderSous();
				}
			} else {
				Dialogs.warning("Attention - Enregistrement", "Le fichier n'a pas �t� modif� depuis la derni�re sauvegarde.");
			}
		}
		this.fenetre.getScene().setCursor(Cursor.DEFAULT);
	}
	
	/**
	 * Sauvegarde les donn�es dans un fichier choisi par l'utilisateur.
	 */
	public void sauvegarderSous() {
		if (this.gestion.aCompte()) {
			FileChooser fc = new FileChooser();
			File directory = new File(Config.getConfigs().get("cheminDefaut"));
			fc.setInitialDirectory(directory);
			fc.getExtensionFilters().addAll(new ExtensionFilter("Fichiers gestion de compte", "*.gtc"));
			File f = fc.showSaveDialog(null);
			if (f != null) {
				try {
					this.fenetre.getScene().setCursor(Cursor.WAIT);
					this.fenetre.afficherEtat("Sauvegarde des donn�es...");
					this.gestion.sauvegarderSous(f);
					this.fenetre.afficherEtat("Donn�es sauvegard�es");
					this.fenetre.getScene().setCursor(Cursor.DEFAULT);
					Config.getConfigs().put("cheminDefaut", f.getParent());
					Config.save();
				} catch (IOException e) {
					e.printStackTrace();
					Dialogs.error("Erreur - Sauvegarde", "Erreur lors de la sauvegarde. Fichier impossible � ouvrir !");
				}
			}
		}
	}
	
	/**
	 * Charge les donn�es dans un nouveau compte � partir d'un fichier choisi par un utilisateur.
	 */
	public void charger() {
		FileChooser fc = new FileChooser();
		File directory = new File(Config.getConfigs().get("cheminDefaut"));
		fc.setInitialDirectory(directory);
		fc.getExtensionFilters().addAll(new ExtensionFilter("Fichiers gestion de compte", "*.gtc"));
		File f = fc.showOpenDialog(null);
		if (f != null) {
			try {
				this.fenetre.afficherEtat("Chargement des donn�es...");
				this.gestion.charger(f);
				this.fenetre.afficherEtat("Affichage des donn�es...");
				this.fenetre.initInformations(this);
				this.fenetre.afficherEtat("Donn�es charg�es");
				Config.getConfigs().put("cheminDefaut", f.getParent());
				Config.save();
				fenetre.setData(gestion.getData());
				fenetre.disableSauvegarder();
				fenetre.updateTri();
				fenetre.updateSolde(gestion.getBalance());				
			} catch (IOException | ClassNotFoundException e) {
				Dialogs.error("Erreur - Chargement", "Erreur lors du chargement. Fichier impossible � ouvrir !");
			}
		}
	}
	
	/**
	 * Fonction supprimant une transaction s�lectionn�e.
	 * FOnction appel�e par les menu d�roulant (Clic droit sur contenu).
	 */
	public void supprimerTransaction() {
		if (this.gestion.aCompte()) {
			Transaction t = this.fenetre.getSelectedRow();
			if (t != null) {
				if (confirmation("Suppression - Confirmation", "Voulez-vous vraiment supprimer cette transaction ?")) {
					this.gestion.supprimerTransaction(t);
					Dialogs.information("Assistant de transaction - Supression", "Votre transaction a bien �t� supprim�.");
					fenetre.setData(gestion.getData());
					fenetre.enableSauvegarder();
					fenetre.updateTri();
					fenetre.updateSolde(gestion.getBalance());
				}
			} else {
				Dialogs.warning("Assistant de Transaction - Attention", "Vous devez s�lectionner une transaction afin de la supprimer.");
			}
		} else {
			Dialogs.warning("Assistant de Transaction - Attention", "Vous devez avoir un compte d'ouvert afin de supprimer une transaction.");
		}
	}
	
	/**
	 * Fonction supprimant une transaction s�lectionn�e.
	 * Fonction appel�e par les boutons de la bar d'outil et du menu "Edition".
	 * @param transfert � supprimer.
	 */
	public void supprimerTransaction(Transaction transfert) {
		if (this.gestion.aCompte()) {
			if (confirmation("Suppression - Confirmation", "Voulez-vous vraiment supprimer cette transaction ?")) {
				if (this.gestion.supprimerTransaction(transfert)) {
					Dialogs.information("Assistant de transaction - Supression", "Votre transaction a bien �t� supprim�");
					fenetre.setData(gestion.getData());
					fenetre.enableSauvegarder();
					fenetre.updateTri();
					fenetre.updateSolde(gestion.getBalance());
				} else {
				Dialogs.error("Assistant de transaction - Supression", "Votre transaction n'a pas �t� supprim�. Elle n'est pas dans la liste");
				}	
			}
		}
	}
	
	/**
	 * Fonction demandant une confirmation
	 * @return True: si autoris�, False: sinon
	 */
	public boolean confirmation(String titre, String question) {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setTitle(titre);
		a.setHeaderText(null);
		a.setContentText(question);
		a.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = a.showAndWait();
		if (result.get() == ButtonType.YES)
			return true;
		else
			return false;
	}
			
	
	public void changerAnnee(String annee) {
		if (annee != null && !annee.equals("")) {
			if (gestion.aCompte()) {
				gestion.changerAnnee(annee);
				fenetre.updateMois();
				fenetre.setData(gestion.getData());
			}
		}
	}
	
	public void changerMois(String mois) {
		if (mois != null && !mois.equals("")) {
			if (gestion.aCompte()) {
				gestion.changerMois(mois);
				fenetre.setData(gestion.getData());
			}
		}
	}
	
	/**
	 * Ferme l'application.
	 */
	public void quitter() {
		if (this.gestion.estModifier()) {
			confirmation("Sauvegarde - Attention", "Vous n'avez pas enregistr� vos donn�es. Voulez-vous les sauvegarder ?");
		}
		System.exit(0);
	}
	
	public void notesVersion() {
		File notes = new File("notes.txt");
		ArrayList<String> lines = new ArrayList<String>();
		Scanner in;
		try {
			in = new Scanner(new FileReader(notes.getName()));
			while (in.hasNext())
				lines.add(in.nextLine());
			in.close();
		} catch (FileNotFoundException e) {
			Dialogs.error("Notes version - Erreur", "Impossible de lire le fichier des notes de version.");
		}
		Dialogs.information("Notes version - Information", lines);
	}
}
