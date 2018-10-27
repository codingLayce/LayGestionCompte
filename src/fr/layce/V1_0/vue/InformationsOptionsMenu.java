package fr.layce.V1_0.vue;

import fr.layce.V1_0.controller.Controller;
import fr.layce.V1_0.modele.Transaction;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class InformationsOptionsMenu extends ContextMenu {
	
	public InformationsOptionsMenu(Transaction transfert, Controller c) {
		MenuItem modif = new MenuItem("Modifier");
		MenuItem suppr = new MenuItem("Supprimer");
		MenuItem fix = new MenuItem("Fixer");
		this.getItems().addAll(suppr);
		if (!transfert.estFix())
			this.getItems().addAll(modif, fix);
			
		modif.setOnAction(evt -> c.modifierTransaction(transfert));
		suppr.setOnAction(evt -> c.supprimerTransaction(transfert));
		fix.setOnAction(evt -> c.fixerTransaction(transfert));
	}
}
