package fr.layce.V2_0.controleur;

import fr.layce.V2_0.modele.Compte;
import fr.layce.V2_0.modele.Transaction;
import fr.layce.V2_0.modele.exceptions.TransactionException;
import fr.layce.V2_0.vue.Fenetre;
import fr.layce.V2_0.vue.dialog.AssistantTransaction;
import fr.layce.V2_0.vue.dialog.Dialogs;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Représente le controleur du programme.
 * C'est lui qui va faire les changements sur le modèle en fonction des actions de l'utilisateur sur la bue.
 * @author Layce17
 */
public class ControleurFX {
  private static ControleurFX instance;

  private Compte compte;
  private Fenetre fenetre;

  private SimpleStringProperty statut;
  private SimpleBooleanProperty disableSauvegarder;
  private SimpleBooleanProperty disableSauvegarderSous;

  private ControleurFX(){
    this.compte = null;
    this.fenetre = Fenetre.getInstance();

    this.statut = new SimpleStringProperty("chargement OK");
    this.disableSauvegarder = new SimpleBooleanProperty(true);
    this.disableSauvegarderSous = new SimpleBooleanProperty(true);

    this.fenetre.setProperties(this.statut, this.disableSauvegarder, this.disableSauvegarderSous);
  }

  /**
   * Créer un nouveau fichier.
   * Appelée au click dans le menu et la barre d'outils.
   */
  public void nouveauFichier(){
    if (this.compte != null) {
      if (this.compte.hasBeenModified()){
        Optional<ButtonType> result = Dialogs.confirmYesNo("Sauvegarder compte",
                "Des modifications ont été faites sur votre compte.", "Voulez-vous les sauvegarder ?");
        if (result.isPresent()){
          if (result.get() == ButtonType.YES){
            // TODO: Sauvegarder compte.
          }
        } else {
          Dialogs.error("Sauvegarder compte", "Vous devez sélectionner une réponse.");
          return;
        }
      }
    }

    this.statut.setValue("Création du compte...");

    this.compte = Compte.getInstance();

    this.fenetre.compteCreated();
    this.fenetre.setData(FXCollections.observableArrayList(this.compte.getTransactions()));

    this.disableSauvegarderSous.setValue(false);

    this.statut.setValue("Création du compte OK");
  }

  /**
   * Ouvre un nouveau fichier.
   * Appelée au click dans le menu et la barre d'outils.
   */
  public void ouvrirFichier(){
    this.statut.setValue("Ouverture d'un compte...");

    FileChooser fc = new FileChooser();
    fc.setTitle("Sélectionner un fichier");
    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers gestion de compte", "*.gtc"));
    File chosenFile = fc.showOpenDialog(null);
    if (chosenFile != null){
      try {
        this.compte = Compte.getInstance();
        this.compte.ouvrirCompte(chosenFile);

        this.fenetre.compteCreated();
        this.fenetre.setData(FXCollections.observableArrayList(this.compte.getTransactions()));

        this.statut.setValue("Compte ouvert");
      } catch (IOException ex){
        Dialogs.errorMessage("Ouverture de compte", ex);
        this.statut.setValue("Impossible d'ouvrir le compte");
      }
    } else {
      Dialogs.warning("Ouverture de compte", "Aucun fichier sélectionné.");
      this.statut.setValue("Aucun fichier sélectionné");
    }
  }

  /**
   * Affiche l'assistant de transaction pour modifier la transaction.
   * Appelée à la sélection de l'option modifier en click droit sur la transaction.
   * @param transaction à modifier.
   */
  public void modifierTransaction(Transaction transaction){
    // TODO: modifier.
  }

  /**
   * Supprime une transaction du compte.
   * Appelée à la sélection de l'option supprimer en clic droit sur la transaction.
   * @param transaction à supprimer.
   */
  public void supprimerTransaction(Transaction transaction){
    if (this.compte != null){
      try {
        this.compte.retirerTransaction(transaction);
        Fenetre.getInstance().setData(FXCollections.observableArrayList(this.compte.getTransactions()));
        // TODO: afficher suppression OK.
      } catch (TransactionException e) {
        e.printStackTrace();
        // TODO: afficher erreur.
      }
    } else {
      // TODO: afficher compte null.
    }
  }

  public void sauvegarderCompte(){
    this.statut.setValue("sauvegarde du compte...");

    if (this.compte != null){
      try {
        this.compte.sauvegarderCompte(new File("test.json"));

        this.disableSauvegarder.setValue(true);
        this.statut.setValue("compte sauvegarder OK");
      } catch (IOException ex) {
        Dialogs.errorMessage("Sauvegarder votre compte", ex);
      }
    } else{
      Dialogs.error("Sauvegarder votre compte", "Vous ne pouvez pas sauvegarder votre compte.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec de la sauvegarde du compte");
    }
  }

  /**
   * Sauvegarde le compte à l'endroit choisi par l'utilisateur.
   * Appelée au clic sur les boutons de sauvegarde-sous ou quand c'est la première sauvegarde.
   */
  public void sauvegarderSousCompte(){
    this.statut.setValue("sauvegarde du compte...");

    if (this.compte != null){
      FileChooser fc = new FileChooser();
      fc.setTitle("Sauvegarder votre compte");
      fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers gestion de compte", "*.gtc"));
      File chosenFile = fc.showOpenDialog(null);
      if (chosenFile != null){
        try {
          this.compte.sauvegarderCompte(chosenFile);

          this.disableSauvegarder.setValue(true);
          this.statut.setValue("compte sauvegarder OK");
        } catch (IOException e) {
          Dialogs.errorMessage("Sauvegarder votre compte", e);
          this.statut.setValue("Sauvegarde du compte erreur");
        }
      }
    } else {
      Dialogs.error("Sauvegarder votre compte", "Vous ne pouvez pas sauvegarder votre compte.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec de la sauvegarde du compte");
    }
  }

  /**
   * Ouvre l'assistant d'ajout de transaction et ajoute la transaction au compte.
   * Appelée au click dans le menu et la barre d'outils.
   */
  public void ajouterTransaction(){
    this.statut.setValue("Ajout d'une transaction...");

    if (this.compte != null){
      Optional<Transaction> transaction = new AssistantTransaction("Assistant d'ajout de transaction").showAndWait();
      if (transaction.isPresent()){
        this.compte.ajouterTransaction(transaction.get());
        this.fenetre.setData(FXCollections.observableArrayList(this.compte.getTransactions()));
        this.statut.setValue("ajout d'une transacition OK");
      }
    } else {
      Dialogs.error("Assiatnt d'ajout de transaction", "Vous ne pouvez pas ajouter de transaction.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec d'ajout d'une transaction");
    }
  }

  /**
   * Quitte l'application.
   * Appelée au click dans le menu ou au click de la croix rouge.
   */
  public void quitter() {
    // TODO: faire les tests de sauvegarde avant de quitter.
    System.exit(0);
  }

  /* GETTERS & SETTERS */

  /**
   * Implémentation du patron singleton.
   * @return l'unique instance du controleur.
   */
  public static ControleurFX getInstance(){
    if (instance == null)
      instance = new ControleurFX();
    return instance;
  }
}
