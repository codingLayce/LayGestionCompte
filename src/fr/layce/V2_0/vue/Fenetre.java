package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import fr.layce.V2_0.modele.Transaction;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Représente une fenêtre.
 * Elle va contenir tous les éléments graphique de la vue.
 *
 * @author Layce17
 */
public class Fenetre extends BorderPane {
  private static Fenetre instance;

  private MenuBar menu_bar;
  private OutilBar outil_bar;
  private StatutBar statut_bar;
  private InformationsPane informations_pane;

  private Fenetre() {
    super();

    initTop();
    initStatut();
    this.setCenter(new Label("Pas de compte ouvert."));
    initInformationsPane();
  }

  private void initTop() {
    this.menu_bar = new MenuBar();
    this.outil_bar = new OutilBar();
    VBox vb = new VBox();
    vb.getChildren().addAll(this.menu_bar, this.outil_bar);
    this.setTop(vb);
  }

  private void initStatut() {
    this.statut_bar = new StatutBar();
    this.setBottom(this.statut_bar);

  }

  private void initInformationsPane() {
    this.informations_pane = new InformationsPane();
  }

  public void compteCreated() {
    this.setCenter(this.informations_pane);
  }

  /* GETTERS & SETTERS */

  /**
   * Implémentation du patron singleton.
   *
   * @return l'instance unique de la fenetre.
   */
  public static Fenetre getInstance() {
    if (instance == null)
      instance = new Fenetre();
    return instance;
  }

  /**
   * Défini les propriétés observable par la fenetre.
   *
   * @param statut          propriété de la barre d'état.
   * @param sauvegarder     propriété des boutons sauvegarder.
   * @param sauvegarderSous propriété des boutons sauvegarder-sous.
   */
  public void setProperties(SimpleStringProperty statut, SimpleBooleanProperty sauvegarder, SimpleBooleanProperty sauvegarderSous) {
    this.statut_bar.setStatutProperty(statut);
    this.menu_bar.setProperties(sauvegarder, sauvegarderSous);
    this.outil_bar.setProperties(sauvegarder, sauvegarderSous);
  }

  public void setSoldeProperty(SimpleStringProperty solde){
    this.outil_bar.setSoldeProperty(solde);
  }

  /**
   * Défini le controleur sur tous les parties de la vue nécessitants une redirection d'un event vers le controleur.
   *
   * @param ctrl controleur qui fait la logique du programme.
   */
  public void setControleur(ControleurFX ctrl) {
    this.menu_bar.setControleur(ctrl);
    this.outil_bar.setControleur(ctrl);
    this.informations_pane.setControleur(ctrl);
  }

  /**
   * Update le tableau des transactions.
   *
   * @param transactions mises à jour.
   */
  public void setData(List<Transaction> transactions) {
    this.informations_pane.setData(FXCollections.observableArrayList(transactions));
  }

  public MenuBar getMenuBar() {
    return this.menu_bar;
  }

  public OutilBar getOutilBar() {
    return this.outil_bar;
  }
}
