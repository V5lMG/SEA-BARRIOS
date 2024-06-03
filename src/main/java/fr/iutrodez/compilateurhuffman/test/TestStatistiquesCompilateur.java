package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestStatistiquesCompilateur {

    @Test
    void testResumeCompression() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        StatistiquesCompilateur.resumeCompression("/chemin/vers/fichierSource.txt",
                "/chemin/vers/fichierDestination.bin");

        String expectedOutput = "Taille du fichier original : 1234 octets\n" +
                "Taille du fichier compressé : 567 octets\n" +
                "Taux de compression : 45.99 %\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testResumeDecompression() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        long tempsDecompression = System.currentTimeMillis();
        StatistiquesCompilateur.resumeDecompression("/chemin/vers/fichierCompresse.bin",
                "/chemin/vers/fichierDestination.txt",
                tempsDecompression);

        String expectedOutput = "Taille du fichier compressé : 567 octets\n" +
                "Taille du fichier décompressé : 1234 octets\n" +
                "Taux de décompression : 45.99 %\n" +
                "Temps de décompression : 1000 millisecondes\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
