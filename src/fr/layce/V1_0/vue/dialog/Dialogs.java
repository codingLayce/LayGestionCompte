package fr.layce.V1_0.vue.dialog;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Dialogs {
	public static void warning(String title, String... content) {
		Alert a = new Alert(AlertType.WARNING);
		a.setTitle(title);
		a.setHeaderText(null);
		String text = "";
		for (String s : content) {
			text += s + System.getProperty("line.separator");
		}
		a.setContentText(text);
		a.showAndWait();
	}
	
	public static void error(String title, String... content) {
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle(title);
		a.setHeaderText(null);
		String text = "";
		for (String s : content) {
			text += s + System.getProperty("line.separator");
		}
		a.setContentText(text);
		a.showAndWait();
	}
	
	public static void information(String title, String... content) {
		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle(title);
		a.setHeaderText(null);
		String text = "";
		for (String s : content) {
			text += s + System.getProperty("line.separator");
		}
		a.setContentText(text);
		a.showAndWait();
	}
	
	public static void information(String title, ArrayList<String> content) {
		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle(title);
		a.setHeaderText(null);
		String text = "";
		for (String s : content) {
			text += s + System.getProperty("line.separator");
		}
		a.setContentText(text);
		a.showAndWait();
	}
}
