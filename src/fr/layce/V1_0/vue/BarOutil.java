package fr.layce.V1_0.vue;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import fr.layce.V1_0.controller.Controller;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
/**
 * Classe représentant la barre d'outils de l'application.
 * @author Layce
 */
public class BarOutil extends ToolBar {
	private Controller ctrl;
	private Button sauvegarder;
	private Button sauvegarder_sous;
	private ChoiceBox<String> cb_mois;
	private Label lbl_mois;
	private Label lbl_annee;
	private ChoiceBox<String> cb_annee;
	private Label lbl_solde;
	
	/**
	 * Constructeur de la classe.
	 * Instancie les différents éléments de la barre.
	 */
	public BarOutil() {
		super();
		ajouterItem("btnNouveau", "Creer un nouveau fichier").setOnAction(e -> ctrl.nouveau());
		ajouterItem("btnOuvrir", "Ouvre un fichier").setOnAction(e -> ctrl.charger());
		ajouterSeparateur();
		sauvegarder = ajouterItem("btnSauvegarder", "Sauvegargde votre fichier dans le fichier initial");
		sauvegarder.setOnAction(e -> ctrl.sauvegarder());
		sauvegarder.setDisable(true);
		sauvegarder_sous = ajouterItem("btnSauvegarderSous", "Sauvegarde votre fichier à l'emplacement choisi");
		sauvegarder_sous.setOnAction(e -> ctrl.sauvegarderSous());
		sauvegarder_sous.setDisable(true);
		ajouterSeparateur();
		ajouterItem("btnAjouterTransaction", "Ouvre l'assistant d'ajout de transaction").setOnAction(e -> ctrl.ajouterTransaction());
		ajouterItem("btnSupprimerTransaction", "Ouvre l'assiatant de suppression de transaction").setOnAction(e -> ctrl.supprimerTransaction());
		ajouterSeparateur();
		lbl_annee = new Label("Année: ");
		lbl_annee.setVisible(false);
		this.getItems().add(lbl_annee);
		cb_annee = new ChoiceBox<String>();
		cb_annee.setOnAction(e -> ctrl.changerAnnee(cb_annee.getValue()));
		cb_annee.setVisible(false);
		this.getItems().add(cb_annee);
		lbl_mois = new Label("Mois: ");
		lbl_mois.setVisible(false);
		this.getItems().add(lbl_mois);
		cb_mois = new ChoiceBox<String>();
		cb_mois.setOnAction(e -> ctrl.changerMois(cb_mois.getValue()));
		cb_mois.setVisible(false);
		this.getItems().add(cb_mois);
		Pane fill_empty_space = new Pane();
		HBox.setHgrow(fill_empty_space, Priority.ALWAYS);
		lbl_solde = new Label("Solde: 0 €");
		lbl_solde.setStyle("-fx-font-weight: bold");
		lbl_solde.setTextFill(Color.rgb(212, 7, 78));
		this.getItems().addAll(fill_empty_space, lbl_solde);
	}
	
	public void updateAnneeChoices(ArrayList<Integer> annee) {
		ArrayList<String> arr = new ArrayList<String>();
		if (annee != null) {
			for (Integer i : annee) {
				arr.add(i.toString());
			}
		}
		arr.add(0, "Toutes");
		cb_annee.setItems(FXCollections.observableArrayList(arr));
		cb_annee.getSelectionModel().selectFirst();
		cb_annee.setValue("Toutes");
		lbl_annee.setVisible(true);
		cb_annee.setVisible(true);
	}
	
	public void updateMoisChoices(ArrayList<Integer> mois) {
		ArrayList<String> arr = new ArrayList<String>();
		if (mois != null) {
			for (Integer i : mois) {
				arr.add(moisToString(i));
			}
		}
		arr.add(0, "Tous");
		cb_mois.setItems(FXCollections.observableArrayList(arr));
		cb_mois.getSelectionModel().selectFirst();
		cb_mois.setValue("Tous");
		lbl_mois.setVisible(true);
		cb_mois.setVisible(true);
	}
	
	private String moisToString(int mois) {
		switch (mois) {
		case 1:
			return "Janvier";
		case 2:
			return "Février";
		case 3:
			return "Mars";
		case 4:
			return "Avril";
		case 5:
			return "Mai";
		case 6:
			return "Juin";
		case 7:
			return "Juillet";
		case 8:
			return "Août";
		case 9:
			return "Septembre";
		case 10:
			return "Octobre";
		case 11:
			return "Novembre";
		case 12:
			return "Décembre";
		default:
			return null;
		}
	}
	
	public void setController(Controller control) {
		this.ctrl = control;
	}
	
	public void disableSauvegarder() {
		sauvegarder.setDisable(true);
	}
	
	public void enableSauvegarder() {
		sauvegarder.setDisable(false);
	}
	
	public void disableSauvegarderSous() {
		sauvegarder_sous.setDisable(true);
	}
	
	public void enableSauvegarderSous() {
		sauvegarder_sous.setDisable(false);
	}
	
	public void updateSolde(double s) {
		lbl_solde.setText("Solde: "+convertDecimal(s));
	}
	
	private String convertDecimal(double d) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(' ');
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(dfs);
		String s = df.format(d) + " €";
		return s;
	}
	
	private Button ajouterItem(String i, String t) {
		Button b = new Button();
		b.getStyleClass().add("btn");
		b.setId(i);
		b.getStylesheets().add("/fr/layce/V1_0/vue/style.css");
		b.setTooltip(new Tooltip(t));
		this.getItems().add(b);
		return b;
	}
	
	private void ajouterSeparateur() {
		this.getItems().add(new Separator());
	}
}
