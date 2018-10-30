package fr.layce.V2_0.vue;

import fr.layce.V2_0.controleur.ControleurFX;
import fr.layce.V2_0.modele.Transaction;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

class InformationsPane extends TableView<Transaction> {
  private ControleurFX ctrl;

  InformationsPane() {
    super();
    TableColumn<Transaction, String> tc = ajouteColonne("Date");
    tc.setCellValueFactory(new PropertyValueFactory<>("date"));
    this.getColumns().add(tc);
    tc = ajouteColonne("Libelle");
    tc.setCellValueFactory(new PropertyValueFactory<>("libelle"));
    this.getColumns().add(tc);
    tc = ajouteColonne("Type");
    tc.setCellValueFactory(new PropertyValueFactory<>("type"));
    this.getColumns().add(tc);
    tc = ajouteColonne("Débit");
    tc.setCellValueFactory(new PropertyValueFactory<>("debit"));
    this.getColumns().add(tc);
    tc = ajouteColonne("Crédit");
    tc.setCellValueFactory(new PropertyValueFactory<>("credit"));
    this.getColumns().add(tc);
    tc = ajouteColonne("Solde");
    tc.setCellValueFactory(new PropertyValueFactory<>("solde"));
    tc.setCellFactory(col -> new TableCell<Transaction, String>(){
      @Override
      protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        if (item == null || empty) {
          setText(null);
          setStyle("");
        } else {
          // Définission de la couleur du solde
          int current_index = indexProperty().getValue();
          double solde = col.getTableView().getItems().get(current_index).getSoldeValue();
          if (solde > 0) {
              setStyle("-fx-background-color: rgb(0,200,0); -fx-font-weight: bold;");
              setTextFill(Color.WHITE);
          } else {
              setStyle("-fx-background-color: rgb(0,200,0); -fx-font-weight: bold;");
              setTextFill(Color.WHITE);
          }
          setText(col.getTableView().getItems().get(current_index).getSolde());
        }
      }
    });

    this.getColumns().add(tc);

    this.setOnMousePressed(e -> {
      if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
        Transaction current_t = getSelectionModel().getSelectedItem();
        ctrl.modifierTransaction(current_t);
      } else if (e.isSecondaryButtonDown()) {
        if (getSelectionModel().getSelectedItem() != null)
          this.setContextMenu(new InformationsOptionsMenu(getSelectionModel().getSelectedItem(), ctrl));
      }
    });
  }

  public void setData(ObservableList<Transaction> transferts) {
    this.setItems(transferts);
  }

  private TableColumn<Transaction, String> ajouteColonne(String nom) {
    TableColumn<Transaction, String> tc = new TableColumn<>(nom);
    tc.setMinWidth(150);
    return tc;
  }

  /* GETTERS & SETTERS */
  public void setControleur(ControleurFX ctrl) {
    this.ctrl = ctrl;
  }
}
