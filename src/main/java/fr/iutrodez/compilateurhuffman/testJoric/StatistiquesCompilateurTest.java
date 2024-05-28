package fr.iutrodez.compilateurhuffman.testJoric;

import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatistiquesCompilateurTest {

    @TempDir
    Path tempDir;

    private Path fichierOriginal;
    private Path fichierCompresse;
    private Path fichierDecompresse;

    @BeforeEach
    public void setUp() throws IOException {
        fichierOriginal = Files.createFile(tempDir.resolve("original.txt"));
        fichierCompresse = Files.createFile(tempDir.resolve("compressed.txt"));
        fichierDecompresse = Files.createFile(tempDir.resolve("decompressed.txt"));

        // Écrire des données dans les fichiers pour simuler différentes tailles
        Files.write(fichierOriginal, "Ceci est un texte original".getBytes());
        Files.write(fichierCompresse, "Texte compressé".getBytes());
        Files.write(fichierDecompresse, "Ceci est un texte original".getBytes());
    }

    @Test
    public void testResumeCompression() {
        // Rediriger la sortie standard pour capturer les impressions
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            StatistiquesCompilateur.resumeCompression(fichierCompresse.toString(), fichierOriginal.toString());

            String output = outContent.toString();

            assertTrue(output.contains("Taille du fichier original : " + fichierOriginal.toFile().length() + " octets"));
            assertTrue(output.contains("Taille du fichier compressé : " + fichierCompresse.toFile().length() + " octets"));
            double tauxCompression = (double) fichierCompresse.toFile().length() / fichierOriginal.toFile().length() * 100;
            assertTrue(output.contains(String.format("Taux de compression : %.2f %%", tauxCompression)));
        } finally {
            // Réinitialiser la sortie standard
            System.setOut(originalOut);
        }
    }

    @Test
    public void testResumeDecompression() {
        // Rediriger la sortie standard pour capturer les impressions
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            long tempsInitial = System.currentTimeMillis();
            StatistiquesCompilateur.resumeDecompression(fichierDecompresse.toString(), fichierCompresse.toString(), tempsInitial);

            String output = outContent.toString();

            assertTrue(output.contains("Taille du fichier compressé : " + fichierCompresse.toFile().length() + " octets"));
            assertTrue(output.contains("Taille du fichier décompressé : " + fichierDecompresse.toFile().length() + " octets"));
            double tauxDecompression = 100.0 * fichierCompresse.toFile().length() / fichierDecompresse.toFile().length();
            assertTrue(output.contains(String.format("Taux de décompression : %.2f %%", tauxDecompression)));

            long tempsDeDecompression = System.currentTimeMillis() - tempsInitial;
            assertTrue(output.contains("Temps de décompression : " + tempsDeDecompression + " millisecondes"));
        } finally {
            // Réinitialiser la sortie standard
            System.setOut(originalOut);
        }
    }
}
