package fr.layce.V2_0.modele;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représente une transaction effectuée par l'utilisateur.
 * Une transaction est caractérisée par un type {@link fr.layce.V2_0.modele.Transaction.Type}, une date et un montant.
 * @author Layce17
 */
public class Transaction implements Comparable<Transaction> {
  public enum Type {
    CARTE("CARTE"),
    VIREMENT("VIREMENT"),
    ESPECE("ESPECE"),
    CHEQUE("CHEQUE");

    private SimpleStringProperty typeProperty;

    Type(String type) {
      this.typeProperty = new SimpleStringProperty(type);
    }

    public SimpleStringProperty getTypeProperty() {
      return this.typeProperty;
    }
  }

  private LocalDateTime date;
  private SimpleDoubleProperty montant;
  private Type type;
  private SimpleStringProperty libelle;

  public Transaction(LocalDateTime date, double montant, Type type, String libelle) {
    this.date = date;
    this.montant = new SimpleDoubleProperty(montant);
    this.type = type;
    this.libelle = new SimpleStringProperty(libelle);
  }

  @Override
  public int compareTo(Transaction o) {
    return this.date.compareTo(o.getDateTime());
  }

  /* GETTERS & SETTERS */
  public String getDate() {
    return this.date.format(DateTimeFormatter.ISO_DATE);
  }

  public LocalDateTime getDateTime() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public double getMontant() {
    return montant.get();
  }

  public String getDebit() {
    if (this.montant.get() < 0.0)
      return String.valueOf(this.montant.get());
    else
      return null;
  }

  public String getCredit() {
    if (this.montant.get() < 0.0)
      return null;
    else
      return String.valueOf(this.montant.get());
  }

  public String getSolde() {
    return Compte.getInstance().getSolde(this);
  }

  public SimpleDoubleProperty montantProperty() {
    return montant;
  }

  public void setMontant(double montant) {
    this.montant.set(montant);
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getLibelle() {
    return this.libelle.get();
  }

  public void setLibelle(String libelle) {
    this.libelle.set(libelle);
  }
}
