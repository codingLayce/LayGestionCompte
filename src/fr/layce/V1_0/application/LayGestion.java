package fr.layce.V1_0.application;

import java.io.FileNotFoundException;
import java.io.IOException;

import fr.layce.V1_0.controller.Controller;
import fr.layce.V1_0.modele.Config;
import fr.layce.V1_0.modele.Gestion;
import fr.layce.V1_0.vue.Fenetre;
import fr.layce.V1_0.vue.dialog.Dialogs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LayGestion extends Application{
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Gestion g = new Gestion();
		Fenetre fen = new Fenetre(g);
		Controller ctrl = new Controller(g, fen);
		fen.controller(ctrl);
		stage.setScene(new Scene(fen));
		stage.getIcons().addAll(new Image(getClass().getClassLoader().getResourceAsStream("fr/layce/V1_0/vue/icon/logo.png")));
		stage.setWidth(950); stage.setHeight(650);
		stage.show();
		try {
			Config.init();
		} catch (FileNotFoundException e) {
			Dialogs.error("Erreur - Configuration", "Impossible d'ouvrir/créer le fichier config.ini. Veuillez vérifier vos droits sur cette machine.");
		} catch (IOException e) {
			Dialogs.error("Erreur - Configuration", "Impossible d'écrire dans le fichier config.ini. Le fichier peut être corrompu.");
		}
		stage.setTitle("LayGestion Comptes - V_" + Config.getConfigs().get("version"));
		Dialogs.information("Version - Information", "Vous utlisez une version alpha du logiciel.", "Certaines fonctionnalités peuvent ne pas fonctionner correctement.", "", "Merci de votre compréhension");
		stage.setOnCloseRequest(e -> {
			ctrl.quitter();
		});
	}
}
