package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

/**
 * Représente une bar d'outil.
 * Souvent placée en dessus de la barre de menu.
 * @author Layce17
 */
public class OutilBar extends ToolBar {
    private ControleurFX ctrl;

    private Button sauvegarder;
    private Button sauvegarderSous;

    OutilBar(){
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
        ajouterItem("btnAjouterTransaction", "Ouvre l'assistant d'ajout de transaction").setOnAction(e -> this.ctrl.ajouterTransaction());
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
    void setControleur(ControleurFX ctrl){
        this.ctrl = ctrl;
    }void setProperties(SimpleBooleanProperty sauvegarder, SimpleBooleanProperty sauvegarderSous){
        this.sauvegarder.disableProperty().bind(sauvegarder);
        this.sauvegarderSous.disableProperty().bind(sauvegarderSous);
    }
    public void setSauvegarderDisable(boolean b) { this.sauvegarder.setDisable(b); }
    public void setSauvegarderSousDisable(boolean b) { this.sauvegarderSous.setDisable(b); }
}
