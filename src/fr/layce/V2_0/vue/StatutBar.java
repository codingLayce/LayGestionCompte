package fr.layce.V2_0.vue;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Représente une barre d'état.
 * Elle est souvent placée en bas de la fenêtre.
 * @author Layce17
 */
class StatutBar extends HBox {
    private Label lbl_statut;

    StatutBar(){
        super();
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.lbl_statut = new Label();
        this.getChildren().add(this.lbl_statut);
    }

    /**
     * Définie la propriété que va observer le label.
     * @param property à observer.
     */
    void setStatutProperty(StringProperty property){
        this.lbl_statut.textProperty().bind(property);
    }
}
