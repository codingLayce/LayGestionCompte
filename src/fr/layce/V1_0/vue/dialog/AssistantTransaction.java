package fr.layce.V1_0.vue.dialog;

import java.time.ZoneId;
import java.util.Date;

import fr.layce.V1_0.modele.Gestion;
import fr.layce.V1_0.modele.Transaction;
import fr.layce.V1_0.modele.Type;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class AssistantTransaction extends Dialog<Transaction> {
	private GridPane g;
	
	private Gestion ges;
	
	private DatePicker dp;
	private ChoiceBox<String> cb;
	private TextField tf_soc;
	private TextField tf_mon;
	
	private Node nd;
	private Button btn_ok;
	
	private ToggleGroup tg;
	private RadioButton rb_depot;
	private RadioButton rb_retrait;
	
	public AssistantTransaction(String t, Gestion ges, Transaction trans) {
		super();
		this.ges = ges;
		setTitle(t);
		setHeaderText(null);
		
		initComp();
		initValues(trans);		
		placement();
		initButtons();
		initListeners();
		Platform.runLater(() -> dp.requestFocus());
		initResult();
	}
	
	public AssistantTransaction(String t, Gestion ges) {
		super();
		this.ges = ges;
		setTitle(t);
		setHeaderText(null);
		
		initComp();
		placement();
		initButtons();
		initListeners();
		Platform.runLater(() -> dp.requestFocus());
		initResult();
	}
	
	private void initComp() {
		g = new GridPane();
		g.setHgap(10);
		g.setVgap(10);
		g.setPadding(new Insets(10, 20, 10, 20));
		
		tg = new ToggleGroup();
		rb_depot = new RadioButton("Dépôt");
		rb_depot.setToggleGroup(tg);
		rb_depot.setSelected(true);
		rb_retrait = new RadioButton("Retrait");
		rb_retrait.setToggleGroup(tg);
		
		dp = new DatePicker();
		
		cb = new ChoiceBox<String>(FXCollections.observableArrayList("Espece", "Carte", "Virement", "Cheque"));
		cb.getSelectionModel().selectFirst();
		
		tf_soc = new TextField();
		tf_soc.setPromptText("ex: E-Leclerc");
		
		tf_mon = new TextField();
		tf_mon.setPromptText("ex: -150.45");
	}
	
	private void initValues(Transaction t) {
		if (t.getMontantNumber() > 0)
			rb_depot.setSelected(true);
		else
			rb_retrait.setSelected(true);
		
		dp.setValue(t.date());
		if (t.getType() == "ESPECE")
			cb.getSelectionModel().select("Espece");
		else if (t.getType() == "CHEQUE")
			cb.getSelectionModel().select("Cheque");
		else if (t.getType() == "VIREMENT")
			cb.getSelectionModel().select("Virement");
		else if (t.getType() == "CARTE")
			cb.getSelectionModel().select("Carte");
		
		tf_soc.setText(t.getSociete());
		tf_mon.setText(String.valueOf(Math.abs(t.getMontantNumber())));
	}
	
	private void placement() {
		g.add(rb_depot, 0, 0);
		g.add(rb_retrait, 1, 0);
		g.add(new Label("Date de Transaction * : "), 0, 1);
		g.add(dp, 1, 1);
		g.add(new Label("Type de Transaction * : "), 0, 2);
		g.add(cb, 1, 2);
		g.add(new Label("Libelle :"), 0, 3);
		g.add(tf_soc, 1, 3);
		g.add(new Label("Montant * :"), 0, 4);
		g.add(tf_mon, 1, 4);
		g.add(new Label("* champs obligatoires"), 0, 5);

		this.getDialogPane().setContent(g);
	}
	
	private void initButtons() {
		this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		btn_ok = (Button) getDialogPane().lookupButton(ButtonType.OK);
		nd = this.getDialogPane().lookupButton(ButtonType.OK);
		nd.setDisable(true);
	}
	
	private void initListeners() {
		dp.valueProperty().addListener((ob, old, nv) -> {
			if (cb.getValue() != null && !tf_mon.getText().isEmpty()) {
				nd.setDisable(false);
			}
		});
		
		tf_mon.textProperty().addListener((ob, old, nv) -> {
			if (cb.getValue() != null && dp.getValue() != null) {
				nd.setDisable(false);
			}
		});
		
		tf_soc.textProperty().addListener((ob, old, nv) -> {
			if (cb.getValue() != null && dp.getValue() != null) {
				nd.setDisable(false);
			}
		});
		
		tf_mon.textProperty().addListener((ob, old, nv) -> {
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
		
		cb.valueProperty().addListener((ob, old, nv) -> {
			if (cb.getValue() != null && dp.getValue() != null) {
				nd.setDisable(false);
			}
		});
		
		tg.selectedToggleProperty().addListener((ob, old, nv) -> {
			if (cb.getValue() != null && dp.getValue() != null) {
				nd.setDisable(false);
			}
		});
		
		btn_ok.addEventFilter(ActionEvent.ACTION, e -> {
			try {
				Double.valueOf(tf_mon.getText());
			} catch (Exception ex) {
				Dialogs.error("Erreur - Assistant d'ajout de transaction", "Montant invalide, veuillez vérifier votre syntax");
				e.consume();
			}
		});
	}
	
	private void initResult() {
		this.setResultConverter(e -> {
			if (e == ButtonType.OK) {
				Type t = null;
				if (cb.getValue() == "Espece")
					t = Type.ESPECE;
				else if (cb.getValue() == "Virement")
					t = Type.VIREMENT;
				else if (cb.getValue() == "Cheque")
					t = Type.CHEQUE;
				else if (cb.getValue() == "Carte")
					t = Type.CARTE;
				
				double montant = Double.valueOf(tf_mon.getText());
				if (tg.getSelectedToggle().equals(rb_retrait))
					montant *= -1;
				
				double solde = ges.solde() + montant;
				
				Date d = Date.from(dp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				return new Transaction(d, t, tf_soc.getText(), montant, solde);
			} else
				return null;
		});
	}
}	
