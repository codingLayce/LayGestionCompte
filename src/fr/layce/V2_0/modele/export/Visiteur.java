package fr.layce.V2_0.modele.export;

import fr.layce.V2_0.modele.Transaction;

import java.io.IOException;

public interface Visiteur {
    void visite(Transaction transaction) throws IOException;
}
