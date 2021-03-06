package fr.layce.V2_0.controleur;

import fr.layce.V2_0.modele.Compte;
import fr.layce.V2_0.modele.Transaction;
import fr.layce.V2_0.modele.Utils;
import fr.layce.V2_0.modele.config.Config;
import fr.layce.V2_0.modele.config.StringConfig;
import fr.layce.V2_0.modele.exceptions.TransactionException;
import fr.layce.V2_0.vue.Fenetre;
import fr.layce.V2_0.vue.dialog.AssistantTransaction;
import fr.layce.V2_0.vue.dialog.Dialogs;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.Optional;

/**
 * Repr�sente le controleur du programme.
 * C'est lui qui va faire les changements sur le mod�le en fonction des actions de l'utilisateur sur la bue.
 * @author Layce17
 */
public class ControleurFX {
  private static ControleurFX instance;

  private Compte compte;
  private Fenetre fenetre;
  private Config config;

  private SimpleStringProperty statut;
  private SimpleBooleanProperty disableSauvegarder;
  private SimpleBooleanProperty disableSauvegarderSous;

  private ControleurFX(){
    try {
      Config.create();
      this.config = Config.getInstance();
    } catch (IOException e) {
      Dialogs.errorMessage("Chargement des configurations", e);
    }

    this.compte = null;
    this.fenetre = Fenetre.getInstance();

    this.statut = new SimpleStringProperty("chargement OK");
    this.disableSauvegarder = new SimpleBooleanProperty(true);
    this.disableSauvegarderSous = new SimpleBooleanProperty(true);

    this.fenetre.setProperties(this.statut, this.disableSauvegarder, this.disableSauvegarderSous);
  }

  /**
   * Cr�er un nouveau fichier.
   * Appel�e au click dans le menu et la barre d'outils.
   */
  public void nouveauFichier(){
    verifSauvegarde();

    this.statut.setValue("Cr�ation du compte...");

    Compte.create();
    this.compte = Compte.getInstance();

    this.fenetre.compteCreated();
    this.fenetre.setData(this.compte.getTransactions());
    this.fenetre.getMenuBar().setEditionDisable(false);
    this.fenetre.getOutilBar().setEditionDisable(false);
    this.fenetre.setSoldeProperty(this.compte.getSoldeActuel());

    this.disableSauvegarderSous.setValue(false);
    this.disableSauvegarder.setValue(true);

    this.statut.setValue("Cr�ation du compte OK");
  }

  /**
   * Ouvre un nouveau fichier.
   * Appel�e au click dans le menu et la barre d'outils.
   */
  public void ouvrirFichier(){
    verifSauvegarde();

    this.statut.setValue("Ouverture d'un compte...");

    FileChooser fc = new FileChooser();
    fc.setTitle("S�lectionner un fichier");
    initDefaultDirectory(fc);
    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers gestion de compte", "*.gtc"));
    File chosenFile = fc.showOpenDialog(null);
    if (chosenFile != null){
      try {
        Compte.create();
        this.compte = Compte.getInstance();
        this.compte.ouvrirCompte(chosenFile);

        this.fenetre.compteCreated();
        this.fenetre.setData(this.compte.getTransactions());
        this.fenetre.getMenuBar().setEditionDisable(false);
        this.fenetre.getOutilBar().setEditionDisable(false);
        this.fenetre.getOutilBar().setDateItems(this.compte.getAnneesExistantes(), this.compte.getMoisExistants());
        this.fenetre.setSoldeProperty(this.compte.getSoldeActuel());

        this.disableSauvegarderSous.setValue(false);
        this.disableSauvegarder.setValue(true);

        this.statut.setValue("Compte ouvert");
        this.config.getProperty("LastDirectory").setConfig(chosenFile.getParent());
      } catch (IOException ex){
        Dialogs.errorMessage("Ouverture de compte", ex);
        this.statut.setValue("Impossible d'ouvrir le compte");
      }
    } else {
      this.statut.setValue("");
    }
  }

  /**
   * Affiche l'assistant de transaction pour modifier la transaction.
   * Appel�e � la s�lection de l'option modifier en click droit sur la transaction.
   * @param transaction � modifier.
   */
  public void modifierTransaction(Transaction transaction){
    this.statut.setValue("modification d'une transaction...");

    if (this.compte != null){
      AssistantTransaction dialog = new AssistantTransaction("Assistant de modification de transaction", transaction);
      Optional<Transaction> result = dialog.showAndWait();
      if (result.isPresent()){
        try {
          this.compte.modifierTransaction(transaction, result.get());

          this.fenetre.setData(this.compte.getTransactions());
          this.fenetre.getOutilBar().setDateItems(this.compte.getAnneesExistantes(), this.compte.getMoisExistants());
          this.disableSauvegarder.setValue(false);
          this.statut.setValue("modification d'une transaction OK");
        } catch (TransactionException e) {
          Dialogs.errorMessage("Modification d'une transaction", e);
          this.statut.setValue("echec de modification d'une transaction");
        }
      } else {
        this.statut.setValue("");
      }
    } else {
      Dialogs.error("Modification d'une transaction", "Vous ne pouvez pas modifier cette transaction.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec de modification d'une transaction");
    }
  }

  /**
   * Supprime une transaction du compte.
   * Appel�e � la s�lection de l'option supprimer en clic droit sur la transaction.
   * @param transaction � supprimer.
   */
  public void supprimerTransaction(Transaction transaction){
    this.statut.setValue("suppression d'une transaction...");

    if (this.compte != null){
      try {
        this.compte.retirerTransaction(transaction);

        this.fenetre.setData(this.compte.getTransactions());
        this.fenetre.getOutilBar().setDateItems(this.compte.getAnneesExistantes(), this.compte.getMoisExistants());
        this.disableSauvegarder.setValue(false);
        this.statut.setValue("suppression d'une transaction OK");
      } catch (TransactionException e) {
        Dialogs.errorMessage("Suppression d'une transaction", e);
        this.statut.setValue("echec de suppression d'une transaction");
      }
    } else {
      Dialogs.error("Sauvegarder votre compte", "Vous ne pouvez pas supprimer de transaction.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec de suppression d'une transaction");
    }
  }

  /**
   * Sauvegarde le compte m�me endroit que pr�c�dement.
   * Appel�e au clic sur les boutons de sauvegarde.
   */
  public void sauvegarderCompte(){
    this.statut.setValue("sauvegarde du compte...");

    if (this.compte != null){
      if (this.compte.getOpenFile() != null) {
        try {
          this.compte.sauvegarderCompte(this.compte.getOpenFile());

          this.disableSauvegarder.setValue(true);
          this.statut.setValue("compte sauvegarder OK");
        } catch (IOException ex) {
          Dialogs.errorMessage("Sauvegarder votre compte", ex);
          this.statut.setValue("echec de la sauvegarde du compte");
        }
      } else{
        sauvegarderSousCompte();
      }
    } else{
      Dialogs.error("Sauvegarder votre compte", "Vous ne pouvez pas sauvegarder votre compte.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec de la sauvegarde du compte");
    }
  }

  /**
   * Sauvegarde le compte � l'endroit choisi par l'utilisateur.
   * Appel�e au clic sur les boutons de sauvegarde-sous ou quand c'est la premi�re sauvegarde.
   */
  public void sauvegarderSousCompte(){
    this.statut.setValue("sauvegarde du compte...");

    if (this.compte != null){
      FileChooser fc = new FileChooser();
      fc.setTitle("Sauvegarder votre compte");
      initDefaultDirectory(fc);
      fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers gestion de compte", "*.gtc"));
      File chosenFile = fc.showSaveDialog(null);
      if (chosenFile != null){
        try {
          this.compte.sauvegarderCompte(chosenFile);

          this.disableSauvegarder.setValue(true);
          this.statut.setValue("compte sauvegarder OK");

          this.config.getProperty("LastDirectory").setConfig(chosenFile.getParent());
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
   * Appel�e au click dans le menu et la barre d'outils.
   */
  public void ajouterTransaction(){
    this.statut.setValue("Ajout d'une transaction...");

    if (this.compte != null){
      Optional<Transaction> transaction = new AssistantTransaction("Assistant d'ajout de transaction").showAndWait();
      if (transaction.isPresent()){
        this.compte.ajouterTransaction(transaction.get());

        this.fenetre.setData(this.compte.getTransactions());
        this.fenetre.getOutilBar().setDateItems(this.compte.getAnneesExistantes(), this.compte.getMoisExistants());
        this.disableSauvegarder.setValue(false);
        this.statut.setValue("ajout d'une transacition OK");
      } else {
        this.statut.setValue("");
      }
    } else {
      Dialogs.error("Assitant d'ajout de transaction", "Vous ne pouvez pas ajouter de transaction.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec d'ajout d'une transaction");
    }
  }

  /**
   * Met � jour les informations en fonction des filtres de date.
   * Appel�e au click sur un des filtres mois ou ann�e.
   */
  public void updateDates(String year, String month){
    int annee = 0;
    Month mois = null;
    if (year != null && !year.equals("Toutes"))
      annee = Integer.valueOf(year);
    if (month != null && !month.equals("Tous"))
      mois = Utils.toMonth(month);

    this.statut.setValue("mise en place des filtres...");

    if (this.compte != null){
      this.fenetre.setData(this.compte.getTransactionsFiltrees(annee, mois));
      this.statut.setValue("filtes OK");
    } else {
      Dialogs.error("Mise en place des filtres", "Vous ne pouvez pas filtrer les transactions.", "Aucun compte n'est ouvert.");
      this.statut.setValue("echec de mise en place des filtres");
    }
  }

  /**
   * Quitte l'application.
   * Appel�e au click dans le menu ou au click de la croix rouge.
   */
  public void quitter() {
    verifSauvegarde();
    System.exit(0);
  }

  private void verifSauvegarde(){
    if (this.compte != null) {
      if (this.compte.hasBeenModified()){
        Optional<ButtonType> result = Dialogs.confirmYesNo("Sauvegarder compte",
                "Des modifications ont �t� faites sur votre compte.", "Voulez-vous les sauvegarder ?");
        if (result.isPresent()){
          if (result.get() == ButtonType.YES){
            sauvegarderCompte();
          }
        } else {
          Dialogs.error("Sauvegarder compte", "Vous devez s�lectionner une r�ponse.");
        }
      }
    }
  }

  private void initDefaultDirectory(FileChooser fc){
    try {
      if (this.config.getProperty("LastDirectory").getConfig() != null)
        fc.setInitialDirectory(new File((this.config.getProperty("LastDirectory")).getConfig()));
      else
        fc.setInitialDirectory(new File((this.config.getProperty("DefaultDirectory").getConfig())));
    } catch (IOException ex){
      Dialogs.errorMessage("R�cup�ration de la configuration", ex);
    }
  }

  /* GETTERS & SETTERS */

  /**
   * Impl�mentation du patron singleton.
   * @return l'unique instance du controleur.
   */
  public static ControleurFX getInstance(){
    if (instance == null)
      instance = new ControleurFX();
    return instance;
  }
}
