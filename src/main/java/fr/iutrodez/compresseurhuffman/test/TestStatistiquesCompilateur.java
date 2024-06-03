package fr.iutrodez.compresseurhuffman.test;

import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestStatistiquesCompilateur {

    /**
     * Teste la méthode resumeCompression de la classe StatistiquesCompilateur.
     * Vérifie que les statistiques de compression sont correctement affichées.
     */
    @Test
    void testResumeCompression() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        long tempsCompression = System.currentTimeMillis();
        StatistiquesCompilateur.resumeCompression("fichier_source.txt", "fichier_compressé.bin", tempsCompression);

        String expectedOutput = "Taille du fichier original : 100 octets\n" +
                "Taille du fichier compressé : 50 octets\n" +
                "Taux de compression : 50.00 %\n" +
                "Temps de décompression : 0 millisecondes\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Teste la méthode resumeDecompression de la classe StatistiquesCompilateur.
     * Vérifie que les statistiques de décompression sont correctement affichées.
     */
    @Test
    void testResumeDecompression() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        long tempsDecompression = System.currentTimeMillis();
        StatistiquesCompilateur.resumeDecompression("fichier_compressé.bin", "fichier_décompressé.txt", tempsDecompression);

        String expectedOutput = "Taille du fichier compressé : 50 octets\n" +
                "Taille du fichier décompressé : 100 octets\n" +
                "Taux de décompression : 200.00 %\n" +
                "Temps de décompression : 0 millisecondes\n";

        assertEquals(expectedOutput, outContent.toString());
    }
}