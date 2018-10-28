package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

/**
 * Représente une bar d'outil.
 * Souvent placée en dessus de la barre de menu.
 * @author Layce17
 */
class OutilBar extends ToolBar {
    private ControleurFX ctrl;

    OutilBar(){
        super();
        ajouterItem("btnNouveau", "Créer un nouveau compte").setOnAction(e -> this.ctrl.nouveauFichier());
        ajouterItem("btnOuvrir", "Ouvre un compte existant").setOnAction(e -> this.ctrl.ouvrirFichier());
        ajouterSeparateur();
        ajouterItem("btnSauvegarder", "Sauvegarde le compte courant").setOnAction(e -> this.ctrl.sauvegarderCompte());
        ajouterItem("btnSauvegarderSous", "Sauvegarde le compte courant à l'emplacement voulu");
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
    public void setControleur(ControleurFX ctrl){
        this.ctrl = ctrl;
    }
}
