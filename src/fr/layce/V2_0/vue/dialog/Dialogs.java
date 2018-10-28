package fr.layce.V2_0.vue.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Classe permettant un appel rapide aux simple dialogs.
 * Souvent utiliser pour afficher les erreurs, les informations et de simples confirmations.
 * @author Layce17
 */
public class Dialogs {
    /**
     * Affiche un dialog de warning.
     * @param title titre du dialog.
     * @param content texte du dialog. Chaque index est une ligne.
     */
    public static void warning(String title, String ... content){
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(getFullText(content));
        a.showAndWait();
    }

    /**
     * Affiche un dialog d'erreur.
     * @param title titre du dialog.
     * @param content texte du dialog. Chaque index est une ligne.
     */
    public static void error(String title, String ... content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(getFullText(content));
        a.showAndWait();
    }

    /**
     * Affiche un dialog d'information.
     * @param title titre du dialog.
     * @param content texte du dialog. Chaque index est une ligne.
     */
    public static void information(String title, String ... content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(getFullText(content));
        a.showAndWait();
    }

    /**
     * Affiche un dialog de confirmation avec les buttons 'YES' et 'NO'.
     * @param title titre du dialog.
     * @param content texte du dialog. Chaque index est une ligne.
     * @return l'entrée de l'utilisateur.
     */
    public static Optional<ButtonType> confirmYesNo(String title, String ... content){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(getFullText(content));
        return a.showAndWait();
    }

    private static String getFullText(String ... content){
        String text = "";
        for (String s : content) {
            text += s + System.getProperty("line.separator");
        }
        return text;
    }
}
