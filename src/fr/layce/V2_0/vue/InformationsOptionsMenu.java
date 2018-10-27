package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import fr.layce.V2_0.modele.Transaction;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

class InformationsOptionsMenu extends ContextMenu {
    InformationsOptionsMenu(Transaction transaction, ControleurFX ctrl){
        MenuItem modif = new MenuItem("Modifier");
        MenuItem suppr = new MenuItem("Supprimer");

        this.getItems().addAll(modif, suppr);

        modif.setOnAction(e -> ctrl.modifierTransaction(transaction));
        suppr.setOnAction(e -> ctrl.supprimerTransaction(transaction));
    }
}
