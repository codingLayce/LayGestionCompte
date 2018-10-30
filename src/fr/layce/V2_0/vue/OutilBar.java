package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * Représente une bar d'outil.
 * Souvent placée en dessus de la barre de menu.
 *
 * @author Layce17
 */
public class OutilBar extends ToolBar {
  private ControleurFX ctrl;

  private Button sauvegarder;
  private Button sauvegarderSous;
  private Button ajouterTransaction;

  private Label lbl_solde;

  OutilBar() {
    super();
    ajouterItem("btnNouveau", "Créer un nouveau compte").setOnAction(e -> this.ctrl.nouveauFichier());
    ajouterItem("btnOuvrir", "Ouvre un compte existant").setOnAction(e -> this.ctrl.ouvrirFichier());
    ajouterSeparateur();
    this.sauvegarder = ajouterItem("btnSauvegarder", "Sauvegarde le compte courant");
    this.sauvegarder.setOnAction(e -> this.ctrl.sauvegarderCompte());
    this.sauvegarder.setDisable(true);
    this.sauvegarderSous = ajouterItem("btnSauvegarderSous", "Sauvegarde le compte courant à l'emplacement voulu");
    this.sauvegarderSous.setOnAction(e -> this.ctrl.sauvegarderSousCompte());
    this.sauvegarderSous.setDisable(true);
    ajouterSeparateur();
    this.ajouterTransaction = ajouterItem("btnAjouterTransaction", "Ouvre l'assistant d'ajout de transaction");
    this.ajouterTransaction.setOnAction(e -> this.ctrl.ajouterTransaction());
    this.ajouterTransaction.setDisable(true);

    Pane fill_empty_space = new Pane();
    HBox.setHgrow(fill_empty_space, Priority.ALWAYS);
    lbl_solde = new Label("Solde: 0 €");
    lbl_solde.setStyle("-fx-font-weight: bold");
    lbl_solde.setTextFill(Color.rgb(212, 7, 78));
    this.getItems().addAll(fill_empty_space, lbl_solde);
  }

  private Button ajouterItem(String i, String t) {
    Button b = new Button();
    b.getStyleClass().add("btn");
    b.setId(i);
    b.getStylesheets().add("/fr/layce/V2_0/vue/style.css");
    b.setTooltip(new Tooltip(t));
    this.getItems().add(b);
    return b;
  }

  private void ajouterSeparateur() {
    this.getItems().add(new Separator());
  }

  /* GETTERS & SETTERS */

  void setProperties(SimpleBooleanProperty sauvegarder, SimpleBooleanProperty sauvegarderSous) {
    this.sauvegarder.disableProperty().bind(sauvegarder);
    this.sauvegarderSous.disableProperty().bind(sauvegarderSous);
  }

  void setSoldeProperty(SimpleStringProperty solde){
    this.lbl_solde.textProperty().bind(solde);
  }
  public void setEditionDisable(boolean b) { this.ajouterTransaction.setDisable(b); }
  void setControleur(ControleurFX ctrl) {
    this.ctrl = ctrl;
  }
}
