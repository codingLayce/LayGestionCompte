package fr.layce.V1_0.vue;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Classe représentant la barre d'état de l'application.
 * @author Layce
 */
public class BarEtat extends HBox {
	private Label lbl_etat;
	
	/**
	 * Constructeur de la barre d'état.
	 * Instancie les différents éléments.
	 */
	public BarEtat() {
		super();
		this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		this.lbl_etat = new Label();
		this.getChildren().add(this.lbl_etat);
	}
	
	/**
	 * Fonction changeant la valeur du label de statut.
	 * @param statut nouveau texte de statut
	 */
	public void etat(String statut) {
		this.lbl_etat.setText(statut);
	}
}
