package fr.layce.V1_0.vue.dialog;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import fr.layce.V1_0.modele.FixTransaction;
import fr.layce.V1_0.modele.Fixed;
import fr.layce.V1_0.modele.Transaction;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AssistantFixTransaction extends Dialog<FixTransaction> {
	private Transaction trans;
	
	private GridPane gp_layout;
	private DatePicker dp_jour;
	private Button btn_ok;
	
	private ChoiceBox<Fixed> cb_fix;
	
	public AssistantFixTransaction(String t, Transaction trans) {
		super();
		this.trans = trans;
		
		setTitle(t);
		setHeaderText(null);
		
		initComponent();
		initButtons();
		initListeners();
		placement();
		initResult();
	}
	
	private void initComponent() {
		gp_layout = new GridPane();
		gp_layout.setHgap(5);
		gp_layout.setVgap(5);
		gp_layout.setPadding(new Insets(10, 20, 10, 20));
		
		cb_fix = new ChoiceBox<Fixed>(FXCollections.observableArrayList(Fixed.JOURNALIER, Fixed.QUOTIDIEN, Fixed.HEBDOMADAIRE, Fixed.ANNUEL));
		cb_fix.getSelectionModel().selectFirst();
		
		dp_jour = new DatePicker();
	}
	
	private void initButtons() {
		this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		btn_ok = (Button) getDialogPane().lookupButton(ButtonType.OK);
		btn_ok.setDisable(true);
	}
	
	private void initListeners() {
		dp_jour.valueProperty().addListener((ob, old, nv) -> {
			btn_ok.setDisable(disableButton());
		});
		
		cb_fix.valueProperty().addListener((ob, old, nv) -> {
			btn_ok.setDisable(disableButton());
		});
	}
	
	private void placement() {
		gp_layout.add(new Label("Mode de fixation :"), 0, 0);
		gp_layout.add(cb_fix, 1, 0);
		gp_layout.add(new Label("Premier jour actif :"), 0, 1);
		gp_layout.add(dp_jour, 1, 1);
		
		this.getDialogPane().setContent(gp_layout);
	}
	
	private void initResult() {
		setResultConverter(e -> {
			if (e == ButtonType.OK) {
				LocalDate date = Date.from(dp_jour.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				return new FixTransaction(trans.date(), trans.type(), trans.getSociete(), trans.getMontantNumber(), trans.solde(), cb_fix.getValue(), date);
			} else {
				return null;
			}
		});
	}
	
	private boolean disableButton() {
		if (cb_fix.getValue() != null && dp_jour.getValue() != null) {
			return false;
		} else
			return true;
	}
}
