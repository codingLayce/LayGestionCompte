package fr.layce.V2_0.modele;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.layce.V2_0.modele.exceptions.TransactionException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Repr�sente le compte de l'utilisateur.
 * Un compte est une agr�gation de transactions.
 * @author Layce17
 */
public class Compte {
    private static Compte instance;

    private LinkedList<Transaction> transactions;

    private Compte(){
        transactions = new LinkedList<>();
    }

    /**
     * Ajoute une transaction au compte.
     * @param transaction � ajouter.
     */
    public void ajouterTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }

    /**
     * Retire une transaction du compte.
     * @param transaction � retirer.
     */
    public void retirerTransaction(Transaction transaction) throws TransactionException {
        if (this.transactions.contains(transaction)){
            this.transactions.remove(transaction);
        } else {
            throw new TransactionException("La transaction selectionne ne fait pas partie du compte");
        }
    }

    /**
     * Sauvegarde les transactions en json.
     * @param f fichier o� sauvegarder.
     * @throws IOException exception en cas d'erreur d'ouverture du fichier.
     */
    public void sauvegarderCompte(File f) throws IOException {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        PrintWriter writer = new PrintWriter(f, StandardCharsets.UTF_8);
        for (Transaction t : this.transactions){
            writer.println(gson.toJson(t));
        }
        writer.close();
    }

    /**
     * Ouvre un compte d�j� existant.
     * @param f fichier � ouvrir.
     * @throws IOException exception en cas d'erreur d'ouverture du fichier.
     */
    public void ouvrirCompte(File f) throws IOException{
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line;
        while ((line = reader.readLine()) != null){
            Transaction t = gson.fromJson(line, Transaction.class);
            this.transactions.add(t);
        }
        reader.close();
    }

    /* GETTERS & SETTERS */
    public LinkedList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Impl�mentation du patron singleton.
     * @return l'instance unique de Compte.
     */
    public static Compte getInstance(){
        if (instance == null)
            instance = new Compte();
        return instance;
    }

    /**
     * Calcul le solde pour une transaction donn�e.
     * @param transaction pour laquel il faut calculer le solde.
     * @return le solde au moment de la transaction (apr�s celle-ci).
     */
    String getSolde(Transaction transaction){
        double solde = 0;
        this.transactions.sort(Comparator.naturalOrder());

        int i = 0;
        while ((i < this.transactions.size()) && (this.transactions.get(i).compareTo(transaction) <= 0)){
            solde += this.transactions.get(i).getMontant();
            i++;
        }
        return String.valueOf(solde);
    }
}
