package fr.layce.V1_0.vue;

import fr.layce.V1_0.controller.Controller;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Classe représentant la barre de menu de l'application.
 * @author Layce
 */
public class BarMenu extends MenuBar {
	private Controller ctrl;
	private MenuItem sauvegarder;
	private MenuItem sauvegarder_sous;
	
	/**
	 * Constructeur de la classe.
	 * Instancie les différents menus et items.
	 */
	public BarMenu() {
		super();
		Menu m = ajouteMenu("Fichier");
		MenuItem mi = ajouteItem("Nouveau", m, "nouveau.png");
		mi.setOnAction(e -> ctrl.nouveau());
		mi.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		mi = ajouteItem("Ouvrir", m, "ouvrir.png");
		mi.setOnAction(e -> ctrl.charger());
		mi.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		ajouteSeparateur(m);
		sauvegarder = ajouteItem("Sauvegarder", m, "sauvegarder.png");
		sauvegarder.setOnAction(e -> ctrl.sauvegarder());
		sauvegarder.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		sauvegarder.setDisable(true);
		sauvegarder_sous = ajouteItem("Sauvegarder-Sous", m, "sauvegarderSous.png");
		sauvegarder_sous.setOnAction(e -> ctrl.sauvegarderSous());
		sauvegarder_sous.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		sauvegarder_sous.setDisable(true);
		ajouteSeparateur(m);
		mi = ajouteItem("Fermer", m);
		mi.setOnAction(e -> ctrl.quitter());
		mi.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		
		m = ajouteMenu("Edition");
		ajouteItem("Ajouter Transaction", m, "ajouter.png").setOnAction(e -> ctrl.ajouterTransaction());
		ajouteItem("Supprimer Transaction", m, "supprimer.png").setOnAction(e -> ctrl.supprimerTransaction());
		
		m = ajouteMenu("A propos");
		ajouteItem("Notes de version", m).setOnAction(e -> ctrl.notesVersion());
	}
	
	private Menu ajouteMenu(String n) {
		Menu m = new Menu(n);
		getMenus().add(m);
		return m;
	}
	
	private MenuItem ajouteItem(String n, Menu m) {
		MenuItem it = new MenuItem(n);
		m.getItems().add(it);
		return it;
	}
	
	private MenuItem ajouteItem(String n, Menu m, String i) {
		MenuItem it = new MenuItem(n);
		setIcon(it, i);
		m.getItems().add(it);
		return it;
	}
	
	private void ajouteSeparateur(Menu m) {
		m.getItems().add(new SeparatorMenuItem());
	}
	
	private void setIcon(MenuItem m, String i) {
		Image img = new Image(getClass().getResourceAsStream("icon/"+i));
		ImageView icon = new ImageView(img);
		icon.setFitWidth(15);
		icon.setFitHeight(15);
		m.setGraphic(icon);
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
	
	/* SETTERS */
	/**
	 * Défini le controller à utiliser.
	 * @param control controller de la barre de menu
	 */
	public void setController(Controller control) {
		this.ctrl = control;
	}
}
