package fr.iutrodez.compilateurhuffman.testJoric;

import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatistiquesCompilateurTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testResumeCompression() {
        // Given
        String cheminFichierCompresse = "testCompresse.txt";
        String cheminFichierSource = "testSource.txt";

        // Créez des fichiers de test avec des tailles spécifiques
        createTestFile(cheminFichierSource, 1000); // 1000 octets
        createTestFile(cheminFichierCompresse, 500); // 500 octets

        // When
        StatistiquesCompilateur.resumeCompression(cheminFichierCompresse, cheminFichierSource);

        // Then
        String expectedOutput = "Taille du fichier original : 1000 octets\n" +
                "Taille du fichier compressé : 500 octets\n" +
                "Taux de compression : 50.00 %\n";
        assertEquals(expectedOutput, outContent.toString());

        // Supprimez les fichiers de test
        deleteTestFile(cheminFichierSource);
        deleteTestFile(cheminFichierCompresse);
    }

    @Test
    public void testResumeDecompression() {
        // Given
        String cheminFichierDecompresse = "testDecompresse.txt";
        String cheminFichierADecompresser = "testCompresse.txt";

        // Créez des fichiers de test avec des tailles spécifiques
        createTestFile(cheminFichierADecompresser, 500); // 500 octets
        createTestFile(cheminFichierDecompresse, 1000); // 1000 octets

        long tempsDeCompression = System.currentTimeMillis() - 1000; // Simuler un temps de compression

        // When
        StatistiquesCompilateur.resumeDecompression(cheminFichierDecompresse, cheminFichierADecompresser, tempsDeCompression);

        // Then
        String expectedOutput = "Taille du fichier compressé : 500 octets\n" +
                "Taille du fichier décompressé : 1000 octets\n" +
                "Taux de décompression : 50.00 %\n" +
                "Temps de décompression : 1000 millisecondes\n";
        String actualOutput = outContent.toString();

        // Extraire les parties dynamiques de la sortie
        String[] actualLines = actualOutput.split("\n");
        String[] expectedLines = expectedOutput.split("\n");

        assertEquals(expectedLines[0], actualLines[0]); // Taille du fichier compressé
        assertEquals(expectedLines[1], actualLines[1]); // Taille du fichier décompressé
        assertEquals(expectedLines[2], actualLines[2]); // Taux de décompression
        assertEquals(3, actualLines.length); // Vérifiez qu'il n'y a que 3 lignes

        // Supprimez les fichiers de test
        deleteTestFile(cheminFichierDecompresse);
        deleteTestFile(cheminFichierADecompresser);
    }

    // Méthode pour créer un fichier de test avec une taille spécifique
    private void createTestFile(String filePath, int sizeInBytes) {
        try {
            File file = new File(filePath);
            file.createNewFile();
            try (PrintStream out = new PrintStream(file)) {
                for (int i = 0; i < sizeInBytes; i++) {
                    out.print('a');
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un fichier de test
    private void deleteTestFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}