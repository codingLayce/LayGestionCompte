package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

/**
 * Repr�sente une barre de menu.
 * Souvent positionn� en haut de la fen�tre.
 *
 * @author Layce17
 */
public class MenuBar extends javafx.scene.control.MenuBar {
  private ControleurFX ctrl;

  private MenuItem sauvegarder;
  private MenuItem sauvegarderSous;

  private Menu edition;

  MenuBar() {
    super();

    Menu m = ajouteMenu("Fichier");
    MenuItem mi = ajouteItem("Nouveau", m, "nouveau.png");
    mi.setOnAction(e -> this.ctrl.nouveauFichier());
    mi.setAccelerator(KeyCombination.valueOf("ctrl + N"));
    mi = ajouteItem("Ouvrir", m, "ouvrir.png");
    mi.setOnAction(e -> this.ctrl.ouvrirFichier());
    mi.setAccelerator(KeyCombination.valueOf("ctrl + O"));
    ajouteSeparateur(m);
    this.sauvegarder = ajouteItem("Enregistrer", m, "sauvegarder.png");
    this.sauvegarder.setOnAction(e -> this.ctrl.sauvegarderCompte());
    this.sauvegarder.setAccelerator(KeyCombination.valueOf("ctrl + S"));
    this.sauvegarder.setDisable(true);
    this.sauvegarderSous = ajouteItem("Enregistrer-sous", m, "sauvegarderSous.png");
    this.sauvegarderSous.setOnAction(e -> this.ctrl.sauvegarderSousCompte());
    this.sauvegarderSous.setAccelerator(KeyCombination.valueOf("ctrl + shift + S"));
    this.sauvegarderSous.setDisable(true);
    ajouteSeparateur(m);
    mi = ajouteItem("Quitter", m);
    mi.setOnAction(e -> this.ctrl.quitter());
    mi.setAccelerator(KeyCombination.valueOf("ctrl + alt + Q"));

    this.edition = ajouteMenu("Edition");
    mi = ajouteItem("Ajouter transaction", this.edition, "ajouter.png");
    mi.setOnAction(e -> this.ctrl.ajouterTransaction());
    mi.setAccelerator(KeyCombination.valueOf("ctrl + shift + A"));
    mi = ajouteItem("Supprimer transaction", this.edition, "supprimer.png");
    mi.setOnAction(e -> this.ctrl.supprimerTransaction(Fenetre.getInstance().getSelectedTransaction()));
    mi.setAccelerator(KeyCombination.valueOf("ctrl + shift + D"));
    this.edition.setDisable(true);
  }

  /**
   * Ajoute un menu � la menu barre.
   *
   * @param n nom du menu.
   * @return le menu cr��.
   */
  private Menu ajouteMenu(String n) {
    Menu m = new Menu(n);
    getMenus().add(m);
    return m;
  }

  /**
   * Ajoute un item � un menu de la menu barre.
   *
   * @param n nom du menu item.
   * @param m menu auquel ajouter l'item.
   * @return le menu item cr��.
   */
  private MenuItem ajouteItem(String n, Menu m) {
    MenuItem it = new MenuItem(n);
    m.getItems().add(it);
    return it;
  }

  /**
   * Ajoute un item avec une icone � un menu de la menu barre.
   *
   * @param n nom du menu item.
   * @param m menu auquel ajouter l'item.
   * @param i nom de l'icone dans le package vue.icon.
   * @return le menu item cr��.
   */
  private MenuItem ajouteItem(String n, Menu m, String i) {
    MenuItem it = new MenuItem(n);
    setIcon(it, i);
    m.getItems().add(it);
    return it;
  }

  /**
   * Ajoute un s�parateur � un menu.
   *
   * @param m menu sur lequel ajouter un s�parateur.
   */
  private void ajouteSeparateur(Menu m) {
    m.getItems().add(new SeparatorMenuItem());
  }

  /**
   * Defini l'icone sur un menu item donn�.
   *
   * @param m menu item sur lequel on ajoute une icone.
   * @param i icone du package vue.icone � ajouter.
   */
  private void setIcon(MenuItem m, String i) {
    Image img = new Image(getClass().getResourceAsStream("icon/" + i));
    ImageView icon = new ImageView(img);
    icon.setFitWidth(15);
    icon.setFitHeight(15);
    m.setGraphic(icon);
  }

  /* GETTERS & SETTERS */
  void setProperties(SimpleBooleanProperty sauvegarder, SimpleBooleanProperty sauvegarderSous) {
    this.sauvegarder.disableProperty().bind(sauvegarder);
    this.sauvegarderSous.disableProperty().bind(sauvegarderSous);
  }

  public void setEditionDisable(boolean b) { this.edition.setDisable(b); }
  void setControleur(ControleurFX ctrl) {
    this.ctrl = ctrl;
  }
}
