package fr.iutrodez.compresseurhuffman.outils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestStatistiquesCompresseur {

    @Test
    void resumeCompression() {
        // Créer des données de test
        String cheminFichierSource = "chemin/vers/fichier/source.txt";
        String cheminFichierDestination = "chemin/vers/fichier/destination.comp";
        long tempsCompression = System.currentTimeMillis();

        // Rediriger la sortie de System.out vers un flux de sortie différent
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Appeler la méthode à tester
        StatistiquesCompresseur.resumeCompression(
                cheminFichierSource, cheminFichierDestination, tempsCompression);

        // Récupérer la sortie capturée dans le flux de sortie
        String output = outputStream.toString();

        // Ajouter des assertions pour vérifier la sortie de la méthode
        // Par exemple, vérifier si la sortie contient les informations attendues
        assertTrue(output.contains("Taille du fichier original"));
        assertTrue(output.contains("Taille du fichier compressé"));
        assertTrue(output.contains("Taux de compression"));
        assertTrue(output.contains("Temps de compression"));

        // Restaurer la sortie standard de System.out
        System.setOut(System.out);
    }

    @Test
    void resumeDecompression() {
        // Créer des données de test
        String source = "chemin/vers/fichier/source.comp";
        String cheminFichierDestination = "chemin/vers/fichier/destination.txt";
        long tempsDecompression = System.currentTimeMillis();

        // Rediriger la sortie de System.out vers un flux de sortie différent
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Appeler la méthode à tester
        StatistiquesCompresseur.resumeDecompression(
                source, cheminFichierDestination, tempsDecompression);

        // Récupérer la sortie capturée dans le flux de sortie
        String output = outputStream.toString();

        // Ajouter des assertions pour vérifier la sortie de la méthode
        // Par exemple, vérifier si la sortie contient les informations attendues
        assertTrue(output.contains("Taille du fichier compressé"));
        assertTrue(output.contains("Taille du fichier décompressé"));
        assertTrue(output.contains("Taux de décompression"));
        assertTrue(output.contains("Temps de décompression"));

        // Restaurer la sortie standard de System.out
        System.setOut(System.out);
    }
}