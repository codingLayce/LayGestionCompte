package fr.layce.V2_0.application;

import fr.layce.V2_0.controleur.ControleurFX;
import fr.layce.V2_0.vue.Fenetre;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LayGestion extends Application{
    public static void main(String ...args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage){
        Fenetre fen = Fenetre.getInstance();
        ControleurFX ctrl = ControleurFX.getInstance();
        fen.setControleur(ctrl);
        stage.setScene(new Scene(fen));
        stage.setWidth(950); stage.setHeight(650);
        stage.setTitle("LayGestionCompte");
        stage.getIcons().addAll(new Image(getClass().getClassLoader().getResourceAsStream("fr/layce/V2_0/vue/icon/logo.png")));
        stage.show();
        stage.setOnCloseRequest(ctrl::quitter);
    }
}
