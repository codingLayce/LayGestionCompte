package fr.layce.V1_0.vue;

import fr.layce.V1_0.controller.Controller;
import fr.layce.V1_0.modele.Transaction;
import fr.layce.V1_0.vue.dialog.Dialogs;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

/**
 * Classe représentant le tableau contenant les informations sur les transactions.
 * @author Layce
 */
public class Informations extends TableView<Transaction> {
	private Controller ctrl;
	private TableColumn<Transaction, String> clm_solde;
	
	/**
	 * Constructeur de classe.
	 * @param transferts liste des transactions
	 */
	public Informations(ObservableList<Transaction> transferts) {
		super();
		this.setItems(transferts);
		TableColumn<Transaction, String> tc = ajouteColonne("Date");
		tc.setCellValueFactory(new PropertyValueFactory<Transaction, String>("date"));
		fixListener(tc, "date");
		this.getColumns().add(tc);
		tc = ajouteColonne("Libelle");
		tc.setCellValueFactory(new PropertyValueFactory<Transaction, String>("societe"));
		fixListener(tc, "societe");
		this.getColumns().add(tc);
		tc = ajouteColonne("Type");
		tc.setCellValueFactory(new PropertyValueFactory<Transaction, String>("type"));
		fixListener(tc, "type");
		this.getColumns().add(tc);
		tc = ajouteColonne("Retrait");
		tc.setCellValueFactory(new PropertyValueFactory<Transaction, String>("retrait"));
		fixListener(tc, "retrait");
		this.getColumns().add(tc);
		tc = ajouteColonne("Depôt");
		tc.setCellValueFactory(new PropertyValueFactory<Transaction, String>("depot"));
		fixListener(tc, "depot");
		this.getColumns().add(tc);
		clm_solde = ajouteColonne("Solde");
		clm_solde.setCellValueFactory(new PropertyValueFactory<Transaction, String>("solde"));
		clm_solde.setCellFactory(column -> {
			return new TableCell<Transaction, String>(){
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						// Définission de la couleur du solde
						int current_index = indexProperty().getValue();
						double solde = column.getTableView().getItems().get(current_index).getSoldeNumber();
						if (solde > 0) {
							if (column.getTableView().getItems().get(current_index).estFix()) {
								setStyle("-fx-background-color: rgb(0,200,0); -fx-font-weight: bold; -fx-font-style: italic");
								setTextFill(Color.rgb(212, 7, 78));
								setTooltip(new Tooltip("Transaction fixée"));
							} else {
								setStyle("-fx-background-color: rgb(0,200,0); -fx-font-weight: bold;");
								setTextFill(Color.WHITE);
							}
						} else {
							if (column.getTableView().getItems().get(current_index).estFix()) {
								setStyle("-fx-background-color: red; -fx-font-weight: bold; -fx-font-style: italic");
								setTextFill(Color.rgb(212, 7, 78));
								setTooltip(new Tooltip("Transaction fixée"));
							} else {
								setStyle("-fx-background-color: rgb(0,200,0); -fx-font-weight: bold;");
								setTextFill(Color.WHITE);
							}
						}
						setText(column.getTableView().getItems().get(current_index).getSolde());
					}
				}
			};
		});
		this.getColumns().add(clm_solde);
		
		this.setOnMousePressed(e -> {
			if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
				Transaction current_t = getSelectionModel().getSelectedItem();
				if (!current_t.estFix())
					ctrl.modifierTransaction(current_t);
				else
					Dialogs.warning("Assistant de Transaction - Attention", "Impossible de modifier une transaction fixe.");
			} else if (e.isSecondaryButtonDown()) {
				if (getSelectionModel().getSelectedItem() != null)
					this.setContextMenu(new InformationsOptionsMenu(getSelectionModel().getSelectedItem(), ctrl));
			}
		});
	}
	
	private void fixListener(TableColumn<Transaction, String> tc, String type) {
		tc.setCellFactory(column -> {
			return new TableCell<Transaction, String>(){
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						int current_index = indexProperty().getValue();
						Transaction t = column.getTableView().getItems().get(current_index);
						if (t.estFix()) {
							setStyle("-fx-font-style: italic");
							setTextFill(Color.rgb(212, 7, 78));
							setTooltip(new Tooltip("Transaction fixée"));
						} else {
							setTextFill(Color.BLACK);
						}
						
						if (type == "date")
							setText(column.getTableView().getItems().get(current_index).getDate());
						else if (type == "societe")
							setText(column.getTableView().getItems().get(current_index).getSociete());
						else if (type == "type")
							setText(column.getTableView().getItems().get(current_index).getType());
						else if (type == "retrait")
							setText(column.getTableView().getItems().get(current_index).getRetrait());
						else if (type == "depot")
							setText(column.getTableView().getItems().get(current_index).getDepot());
					}
				}
			};
		});
	}
	
	public void setController(Controller c) {
		ctrl = c;
	}
	
	public void setData(ObservableList<Transaction> transferts) {
		this.setItems(transferts);
	}
	
	public Transaction getSelectedTransaction(){
		return getSelectionModel().getSelectedItem();
	}
	
	private TableColumn<Transaction, String> ajouteColonne(String s) {
		TableColumn<Transaction, String> tc = new TableColumn<Transaction, String>(s);
		tc.setMinWidth(150);
		return tc;
	}
}
