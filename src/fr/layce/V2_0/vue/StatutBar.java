package fr.layce.V2_0.vue;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Repr�sente une barre d'�tat.
 * Elle est souvent plac�e en bas de la fen�tre.
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
     * D�finie la propri�t� que va observer le label.
     * @param property � observer.
     */
    void setStatutProperty(StringProperty property){
        this.lbl_statut.textProperty().bind(property);
    }
}
