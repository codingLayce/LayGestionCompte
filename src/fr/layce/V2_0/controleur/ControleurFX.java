package fr.layce.V2_0.controleur;

import fr.layce.V2_0.modele.Compte;
import fr.layce.V2_0.modele.Transaction;
import fr.layce.V2_0.modele.exceptions.TransactionException;
import fr.layce.V2_0.vue.Fenetre;
import fr.layce.V2_0.vue.dialog.AssistantTransaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;

import java.io.File;
import java.io.IOException;
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

    private SimpleStringProperty statut;

    private ControleurFX(){
        this.compte = null;
        this.statut = new SimpleStringProperty("Initialisation");
        this.fenetre = Fenetre.getInstance();
        this.fenetre.setStatut(statut);
    }

    /**
     * Cr�er un nouveau fichier.
     */
    public void nouveauFichier(Event e){
        this.compte = Compte.getInstance();
        this.fenetre.compteCreated();
        this.fenetre.setData(FXCollections.observableArrayList(this.compte.getTransactions()));
        // TODO: cr�er un nouveau fichier.
    }

    /**
     * Ouvre un nouveau fichier.
     */
    public void ouvrirFichier(Event e){
        try {
            this.compte = Compte.getInstance();
            this.compte.ouvrirCompte(new File("test.json"));
            this.fenetre.compteCreated();
            this.fenetre.setData(FXCollections.observableArrayList(this.compte.getTransactions()));
        } catch (IOException ex){
            ex.printStackTrace();
            // TODO: afficher erreur.
        }
    }

    public void modifierTransaction(Transaction transaction){
        // TODO: modifier.
    }

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

    public void sauvegarderCompte(Event e){
        if (this.compte != null){
            try {
                this.compte.sauvegarderCompte(new File("test.json"));
            } catch (IOException ex) {
                ex.printStackTrace();
                // TODO: afficher erreur.
            }
        } else{
            // TODO: afficher compte null.
        }
    }

    /**
     * Ouvre l'assistant d'ajout de transaction et ajoute la transaction au compte.
     */
    public void ajouterTransaction(Event e){
        if (this.compte != null){
            Optional<Transaction> transaction = new AssistantTransaction("Assistant transaction - Ajouter").showAndWait();
            if (transaction.isPresent()){
                this.compte.ajouterTransaction(transaction.get());
                this.fenetre.setData(FXCollections.observableArrayList(this.compte.getTransactions()));
            } else {
                // TODO: afficher erreur.
            }
        } else {
            // TODO: afficher compte null.
        }
    }

    /**
     * Quitte l'application.
     * @param e
     */
    public void quitter(Event e) {
        // TODO: faire les tests de sauvegarde avant de quitter.
        System.exit(0);
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