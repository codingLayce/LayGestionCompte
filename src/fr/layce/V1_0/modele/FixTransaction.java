package fr.layce.V1_0.modele;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FixTransaction extends Transaction {
	private static final long serialVersionUID = -6928972141049410455L;
	
	private Fixed fix;
	
	// Prochain jour où la transaction sera active.
	private LocalDate jour;
	
	public FixTransaction(LocalDate date, Type type, String societe, double montant, double solde, Fixed fix, LocalDate jour) {
		super(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), type, societe, montant, solde);
		this.fix = fix;
		this.jour = jour;
	}

	public void setJour(LocalDate d) {
		jour = d;
	}
	
	public Fixed getFix() {
		return fix;
	}
	
	public LocalDate getJour() {
		return jour;
	}
}
