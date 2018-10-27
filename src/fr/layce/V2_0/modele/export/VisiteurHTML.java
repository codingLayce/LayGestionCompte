package fr.layce.V2_0.modele.export;

import fr.layce.V2_0.modele.Transaction;

import java.io.File;
import java.io.IOException;

public class VisiteurHTML implements Visiteur {
    private File file;

    public VisiteurHTML(){
        this.file = new File("source.html");
    }

    @Override
    public void visite(Transaction transaction) throws IOException {
        // TODO: conversion en HTML.
    }
}
