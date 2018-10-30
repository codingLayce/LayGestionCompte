package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
  private Button supprimerTransaction;

  private ChoiceBox<String> cb_annee;
  private ChoiceBox<String> cb_mois;

  private Label lbl_solde;

  OutilBar() {
    super();
    ajouterBouton("btnNouveau", "Créer un nouveau compte").setOnAction(e -> this.ctrl.nouveauFichier());
    ajouterBouton("btnOuvrir", "Ouvre un compte existant").setOnAction(e -> this.ctrl.ouvrirFichier());

    ajouterSeparateur();
    this.sauvegarder = ajouterBouton("btnSauvegarder", "Sauvegarde le compte courant");
    this.sauvegarder.setOnAction(e -> this.ctrl.sauvegarderCompte());
    this.sauvegarder.setDisable(true);
    this.sauvegarderSous = ajouterBouton("btnSauvegarderSous", "Sauvegarde le compte courant à l'emplacement voulu");
    this.sauvegarderSous.setOnAction(e -> this.ctrl.sauvegarderSousCompte());
    this.sauvegarderSous.setDisable(true);

    ajouterSeparateur();
    this.ajouterTransaction = ajouterBouton("btnAjouterTransaction", "Ouvre l'assistant d'ajout de transaction");
    this.ajouterTransaction.setOnAction(e -> this.ctrl.ajouterTransaction());
    this.ajouterTransaction.setDisable(true);
    this.supprimerTransaction = ajouterBouton("btnSupprimerTransaction", "Supprime la transaction sélectionné");
    this.supprimerTransaction.setOnAction(e -> this.ctrl.supprimerTransaction(Fenetre.getInstance().getSelectedTransaction()));
    this.supprimerTransaction.setDisable(true);

    ajouterSeparateur();
    this.cb_annee = ajouteChoice("Année: ");
    this.cb_annee.getItems().add("Toutes");
    this.cb_annee.getSelectionModel().selectFirst();
    this.cb_annee.setDisable(true);
    this.cb_mois = ajouteChoice("Mois: ");
    this.cb_mois.getItems().add("Tous");
    this.cb_mois.getSelectionModel().selectFirst();
    this.cb_mois.setDisable(true);
    this.cb_annee.setOnAction(e -> this.ctrl.updateDates(this.cb_annee.getSelectionModel().getSelectedItem(), this.cb_mois.getSelectionModel().getSelectedItem()));
    this.cb_mois.setOnAction(e -> this.ctrl.updateDates(this.cb_annee.getSelectionModel().getSelectedItem(), this.cb_mois.getSelectionModel().getSelectedItem()));

    Pane fill_empty_space = new Pane();
    HBox.setHgrow(fill_empty_space, Priority.ALWAYS);
    lbl_solde = new Label("Solde: 0 €");
    lbl_solde.setStyle("-fx-font-weight: bold");
    lbl_solde.setTextFill(Color.rgb(212, 7, 78));
    this.getItems().addAll(fill_empty_space, lbl_solde);
  }

  private Button ajouterBouton(String i, String t) {
    Button b = new Button();
    b.getStyleClass().add("btn");
    b.setId(i);
    b.getStylesheets().add("/fr/layce/V2_0/vue/style.css");
    b.setTooltip(new Tooltip(t));
    this.getItems().add(b);
    return b;
  }

  private ChoiceBox<String> ajouteChoice(String label){
    Label lbl = new Label(label);
    ChoiceBox<String> cb = new ChoiceBox<>();
    HBox hb = new HBox();
    hb.getChildren().addAll(lbl, cb);
    this.getItems().add(hb);
    return cb;
  }

  private void ajouterSeparateur() {
    this.getItems().add(new Separator());
  }

  /* GETTERS & SETTERS */
  void setProperties(SimpleBooleanProperty sauvegarder, SimpleBooleanProperty sauvegarderSous) {
    this.sauvegarder.disableProperty().bind(sauvegarder);
    this.sauvegarderSous.disableProperty().bind(sauvegarderSous);
  }
  public void setEditionDisable(boolean b) {
    this.ajouterTransaction.setDisable(b);
    this.supprimerTransaction.setDisable(b);
    this.cb_annee.setDisable(b);
    this.cb_mois.setDisable(b);
  }

  public void setDateItems(ObservableList<String> annees, ObservableList<String> mois){
    annees.add(0, "Toutes");
    mois.add(0, "Tous");
    this.cb_annee.setItems(annees);
    this.cb_mois.setItems(mois);
    this.cb_annee.getSelectionModel().selectFirst();
    this.cb_mois.getSelectionModel().selectFirst();
  }

  void setSoldeProperty(SimpleStringProperty solde){
    this.lbl_solde.textProperty().bind(solde);
  }
  void setControleur(ControleurFX ctrl) {
    this.ctrl = ctrl;
  }
}
