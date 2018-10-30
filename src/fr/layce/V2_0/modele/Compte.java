package fr.layce.V2_0.modele;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.layce.V2_0.modele.exceptions.TransactionException;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Repr�sente le compte de l'utilisateur.
 * Un compte est une agr�gation de transactions.
 *
 * @author Layce17
 */
public class Compte {
  private static Compte instance;

  private LinkedList<Transaction> transactions;
  private boolean modified;
  private File openFile;

  private SimpleStringProperty soldeActuel;

  private Compte() {
    this.transactions = new LinkedList<>();
    this.modified = false;
    this.openFile = null;
    this.soldeActuel = new SimpleStringProperty();
  }

  /**
   * Ajoute une transaction au compte.
   *
   * @param transaction � ajouter.
   */
  public void ajouterTransaction(Transaction transaction) {
    this.transactions.add(transaction);
    this.modified = true;
    updateSoldeActuel();
  }

  /**
   * Retire une transaction du compte.
   *
   * @param transaction � retirer.
   */
  public void retirerTransaction(Transaction transaction) throws TransactionException {
    if (this.transactions.contains(transaction)) {
      this.transactions.remove(transaction);
      this.modified = true;
      updateSoldeActuel();
    } else {
      throw new TransactionException("La transaction selectionne ne fait pas partie du compte");
    }
  }

  /**
   * Sauvegarde les transactions en json.
   *
   * @param f fichier o� sauvegarder.
   * @throws IOException exception en cas d'erreur d'ouverture du fichier.
   */
  public void sauvegarderCompte(File f) throws IOException {
    final GsonBuilder builder = new GsonBuilder();
    final Gson gson = builder.create();
    PrintWriter writer = new PrintWriter(f);
    for (Transaction t : this.transactions) {
      writer.println(gson.toJson(t));
    }
    writer.close();
    this.modified = false;
  }

  /**
   * Ouvre un compte d�j� existant.
   *
   * @param f fichier � ouvrir.
   * @throws IOException exception en cas d'erreur d'ouverture du fichier.
   */
  public void ouvrirCompte(File f) throws IOException {
    final GsonBuilder builder = new GsonBuilder();
    final Gson gson = builder.create();
    BufferedReader reader = new BufferedReader(new FileReader(f));
    String line;
    while ((line = reader.readLine()) != null) {
      Transaction t = gson.fromJson(line, Transaction.class);
      this.transactions.add(t);
    }
    reader.close();
    this.openFile = f;
    updateSoldeActuel();
  }

  public void modifierTransaction(Transaction ancienne, Transaction nouvelle) throws TransactionException {
    if (this.transactions.contains(ancienne)) {
      this.transactions.remove(ancienne);
      this.transactions.add(nouvelle);
      updateSoldeActuel();
    } else {
      throw new TransactionException("La transaction selectionne ne fait pas partie du compte");
    }
  }

  public static void create() {
    instance = new Compte();
  }

  /* GETTERS & SETTERS */
  public boolean hasBeenModified() {
    return this.modified;
  }

  /**
   * Impl�mentation du patron singleton.
   *
   * @return l'instance unique de Compte.
   */
  public static Compte getInstance() {
    if (instance == null)
      instance = new Compte();
    return instance;
  }

  /**
   * Calcul le solde pour une transaction donn�e.
   *
   * @param transaction pour laquel il faut calculer le solde.
   * @return le solde au moment de la transaction (apr�s celle-ci).
   */
  double getSolde(Transaction transaction) {
    double solde = 0;
    this.transactions.sort(Comparator.naturalOrder());

    int i = 0;
    while ((i < this.transactions.size()) && (this.transactions.get(i).compareTo(transaction) <= 0)) {
      solde += this.transactions.get(i).getMontant();
      i++;
    }

    return Utils.arrondi(solde);
  }

  /**
   * Calcul le solde actuel du compte.
   */
  private void updateSoldeActuel(){
    double solde = 0;
     this.transactions.sort(Comparator.naturalOrder());

     for (Transaction t : this.transactions){
       solde += t.getMontant();
     }
     this.soldeActuel.set("Solde: " + String.format("%,.2f �", Utils.arrondi(solde)));
  }

  public SimpleStringProperty getSoldeActuel(){
    return this.soldeActuel;
  }
  public LinkedList<Transaction> getTransactions() {
    return this.transactions;
  }
  public File getOpenFile() {
    return this.openFile;
  }
}
