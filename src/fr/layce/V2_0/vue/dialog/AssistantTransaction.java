package fr.layce.V2_0.vue.dialog;

import fr.layce.V2_0.modele.Transaction;
import fr.layce.V2_0.modele.Transaction.Type;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Représente le dialog d'assistant à la création/modification d'une transaction.
 * Créée à l'ajout et la modification d'une transaction.
 *
 * @author Layce17
 */
public class AssistantTransaction extends Dialog<Transaction> {
  private GridPane grid;

  private DatePicker dp;
  private TextField tf_lbl;
  private TextField tf_mon;
  private ChoiceBox<Transaction.Type> cb;

  private Node btn_ok;

  /**
   * Constructeur pour un ajout.
   *
   * @param title titre du dialog.
   */
  public AssistantTransaction(String title) {
    super();

    this.setTitle(title);
    this.setHeaderText(null);

    init();
  }

  /**
   * Constructeur pour une modification.
   *
   * @param title       titre du dialog.
   * @param transaction à modifier.
   */
  public AssistantTransaction(String title, Transaction transaction) {
    super();
    this.setTitle(title);
    this.setHeaderText(null);

    init();
    initValues(transaction);
  }

  private void init() {
    initComp();
    placement();
    initButtons();
    initListeners();
    initResults();
  }

  private void initComp() {
    this.grid = new GridPane();
    this.grid.setHgap(10);
    this.grid.setVgap(10);
    this.grid.setPadding(new Insets(10, 20, 10, 20));

    this.dp = new DatePicker();

    this.cb = new ChoiceBox<>(FXCollections.observableArrayList(Type.values()));
    this.cb.getSelectionModel().selectFirst();

    this.tf_lbl = new TextField();
    this.tf_lbl.setPromptText("ex: Courses");

    this.tf_mon = new TextField();
    this.tf_mon.setPromptText("ex: -150.45");
  }

  private void initButtons() {
    this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    this.btn_ok = this.getDialogPane().lookupButton(ButtonType.OK);
    this.btn_ok.setDisable(true);
  }

  private void placement() {
    this.grid.add(new Label("Date:"), 0, 0);
    this.grid.add(this.dp, 1, 0);
    this.grid.add(new Label("Libelle:"), 0, 1);
    this.grid.add(this.tf_lbl, 1, 1);
    this.grid.add(new Label("Type:"), 0, 2);
    this.grid.add(this.cb, 1, 2);
    this.grid.add(new Label("Montant:"), 0, 3);
    this.grid.add(this.tf_mon, 1, 3);

    this.getDialogPane().setContent(this.grid);
  }

  private void initListeners() {
    this.dp.valueProperty().addListener((ob, old, nv) -> {
      if (!this.tf_mon.getText().isEmpty() && !this.tf_lbl.getText().isEmpty()) {
        this.btn_ok.setDisable(false);
      }
    });

    this.tf_mon.textProperty().addListener((ob, old, nv) -> {
      if (this.dp.getValue() != null && !this.tf_lbl.getText().isEmpty()) {
        this.btn_ok.setDisable(false);
      }
    });

    this.tf_lbl.textProperty().addListener((ob, old, nv) -> {
      if (!this.tf_mon.getText().isEmpty() && this.dp.getValue() != null) {
        this.btn_ok.setDisable(false);
      }
    });

    this.btn_ok.addEventFilter(ActionEvent.ACTION, e -> {
      try {
        Double.valueOf(this.tf_mon.getText());
      } catch (Exception ex) {
        System.err.println(ex.getLocalizedMessage());
        // TODO: afficher erreur.
        e.consume();
      }
    });

    this.tf_mon.textProperty().addListener((ob, old, nv) -> {
      if (!nv.matches("(-?(\\d)+(\\.)?(\\d)*)")) {
        tf_mon.setText(nv.replaceAll("[^\\d.]", ""));
        StringBuilder st = new StringBuilder(nv);
        boolean point = false;
        for (int i = 0; i < st.length(); i++) {
          if (st.charAt(i) == '.') {
            if (!point)
              point = true;
            else
              st.deleteCharAt(i);
          } else
            point = false;
        }
        nv = st.toString();
      }
    });
  }

  private void initValues(Transaction transaction) {
    this.cb.setValue(transaction.getType());
    this.dp.setValue(transaction.getDateTime().toLocalDate());
    this.tf_lbl.setText(transaction.getLibelle());
    this.tf_mon.setText(String.valueOf(transaction.getMontant()));
  }

  private void initResults() {
    this.setResultConverter(e -> {
      if (e == ButtonType.OK) {
        Type type = this.cb.getValue();
        String libelle = this.tf_lbl.getText();
        double montant = Double.valueOf(this.tf_mon.getText());
        LocalDateTime date = LocalDateTime.from(this.dp.getValue().atTime(LocalTime.now()));
        return new Transaction(date, montant, type, libelle);
      } else {
        return null;
      }
    });
  }
}
