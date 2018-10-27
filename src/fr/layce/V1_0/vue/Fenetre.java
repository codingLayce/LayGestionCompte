package fr.layce.V1_0.vue;

import fr.layce.V1_0.controller.Controller;
import fr.layce.V1_0.modele.Gestion;
import fr.layce.V1_0.modele.Transaction;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Classe représentant la fenetre de l'application.
 * C'est elle qui contient tous les différents éléments visibles.
 * @author Layce
 */
public class Fenetre extends BorderPane{
	private BarMenu bm;
	private BarEtat be;
	private Informations in;
	private BarOutil ou;
	
	private Gestion g;
	
	/**
	 * Constructeur de la classe
	 * @param gestion modele
	 */
	public Fenetre(Gestion gestion) {
		this.g = gestion;
		this.bm = new BarMenu();
		this.ou = new BarOutil();
		VBox vb = new VBox();
		vb.getChildren().addAll(this.bm, this.ou);
		this.setTop(vb);
		this.be = new BarEtat();
		this.setBottom(this.be);
	}
	
	/**
	 * Fonction définissant le controlleur que la barre de menu doit utiliser
	 * @param control controller
	 */
	public void controller(Controller control) {
		this.bm.setController(control);
		this.ou.setController(control);
	}
	
	/**
	 * Change le statut dans la barre d'état
	 * @param statut nouveau texte du statut
	 */
	public void afficherEtat(String statut) {
		this.be.etat(statut);
	}
	
	/**
	 * Initialise le centre de la fenetre en affichant les informations
	 */
	public void initInformations(Controller control) {
		this.in = new Informations(g.getData());
		this.in.setController(control);
		this.setCenter(this.in);
	}
	
	public void updateTri() {
		this.ou.updateAnneeChoices(g.getAnnees());
		this.ou.updateMoisChoices(g.getMois());
	}
	
	public void updateMois() {
		this.ou.updateMoisChoices(g.getMois());
	}
	
	public void setData(ObservableList<Transaction> transferts) {
		this.in.setData(transferts);
	}
	
	public void disableSauvegarder() {
		this.bm.disableSauvegarder();
		this.ou.disableSauvegarder();
	}
	
	public void enableSauvegarder() {
		this.bm.enableSauvegarder();
		this.ou.enableSauvegarder();
	}
	
	public void disableSauvegarderSous() {
		this.bm.disableSauvegarderSous();
		this.ou.disableSauvegarderSous();
	}
	
	public void enableSauvegarderSous() {
		this.bm.enableSauvegarderSous();
		this.ou.enableSauvegarderSous();
	}
	
	public void updateSolde(double s) {
		this.ou.updateSolde(s);
	}
	
	public Transaction getSelectedRow() {
		return this.in.getSelectedTransaction();
	}
}
