package fr.layce.V1_0.modele;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Classe représentant une transaction effectué.
 * @author Layce
 */
public class Transaction implements Serializable {
	protected static final long serialVersionUID = -1519244569340837901L;
	
	protected SimpleDateFormat fDate;
	protected Date date;
	protected Type type;
	protected String societe;
	protected double retrait;
	protected double depot;
	protected double solde;
	protected boolean est_fix;
	
	/**
	 * Constructeur de la classe.
	 * @param date à laquelle la transaction a eu lieu
	 * @param type de transaction {@link fr.layce.V1_0.modele.Type}
	 * @param societe vers ou de laquelle a eu lieu la transaction
	 * @param montant de la transaction (positif ou nul)
	 * @param solde du compte après la transaction
	 */
	public Transaction(Date date, Type type, String societe, double montant, double solde) {
		this.date = date;
		this.type = type;
		this.societe = societe;
		if (montant < 0) {
			this.retrait = Math.abs(montant);
			this.depot = 0;
		} else {
			this.depot = Math.abs(montant);
			this.retrait = 0;
		}
		this.solde = solde;
		this.fDate = new SimpleDateFormat("dd-MM-YYYY");
		this.est_fix = false;
	}
	
	protected String convertDecimal(double d) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(' ');
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(dfs);
		String s = df.format(d) + " €";
		return s;
	}
	
	/* SETTERS */
	public void setSolde(double d) {
		this.solde = d;
	}
	
	public void setDate(LocalDate d) {
		date = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public void fixer() {
		this.est_fix = true;
	}

	/* GETTERS */
	public String getDate() {
		return fDate.format(date);
	}

	public String getType() {
		return String.valueOf(type);
	}

	public String getSociete() {
		return societe;
	}

	public String getRetrait() {
		if (retrait != 0.0)
			return convertDecimal(retrait);
		else
			return "";
	}
	
	public String getDepot() {
		if (depot != 0.0)
			return convertDecimal(depot);
		else
			return "";
	}

	public String getSolde() {
		return convertDecimal(solde);
	}
	
	public LocalDate date() {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public double getMontantNumber() {
		if (retrait == 0.0)
			return depot;
		else
			return retrait * -1;
	}
	
	public double getSoldeNumber() {
		return solde;
	}
	
	public Type type() {
		return type;
	}
	
	public double solde() {
		return solde;
	}
	
	public boolean estFix() {
		return est_fix;
	}
}
