package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import fr.layce.V2_0.modele.Transaction;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Représente une fenêtre.
 * Elle va contenir tous les éléments graphique de la vue.
 * @author Layce17
 */
public class Fenetre extends BorderPane {
    private static Fenetre instance;

    private MenuBar menu_bar;
    private OutilBar outil_bar;
    private StatutBar statut_bar;
    private InformationsPane informations_pane;

    private Fenetre(){
        super();

        initTop();
        initStatut();
        this.setCenter(new Label("Pas de compte ouvert."));
        initInformationsPane();
    }

    private void initTop(){
        this.menu_bar = new MenuBar();
        this.outil_bar = new OutilBar();
        VBox vb = new VBox();
        vb.getChildren().addAll(this.menu_bar, this.outil_bar);
        this.setTop(vb);
    }

    private void initStatut(){
        this.statut_bar = new StatutBar();
        this.setBottom(this.statut_bar);

    }

    private void initInformationsPane(){
        this.informations_pane = new InformationsPane();
    }

    public void compteCreated(){
        this.setCenter(this.informations_pane);
    }

    /* GETTERS & SETTERS */

    /**
     * Implémentation du patron singleton.
     * @return l'instance unique de la fenetre.
     */
    public static Fenetre getInstance(){
        if (instance == null)
            instance = new Fenetre();
        return instance;
    }

    /**
     * Défini la propriété que la barre d'état va observer.
     * @param property à oberserver.
     */
    public void setStatut(StringProperty property){
        this.statut_bar.setStatutProperty(property);
    }

    /**
     * Défini le controleur sur tous les parties de la vue nécessitants une redirection d'un event vers le controleur.
     * @param ctrl controleur qui fait la logique du programme.
     */
    public void setControleur(ControleurFX ctrl){
        this.menu_bar.setControleur(ctrl);
        this.outil_bar.setControleur(ctrl);
        this.informations_pane.setControleur(ctrl);
    }

    /**
     * Update le tableau des transactions.
     * @param transactions mises à jour.
     */
    public void setData(ObservableList<Transaction> transactions){
        this.informations_pane.setData(transactions);
    }
}
