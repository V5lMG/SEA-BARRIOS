package fr.iutrodez.test;

import fr.iutrodez.compilateurhuffman.outils.OutilsAnalyseFichier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestOutilsAnalyseFichier {

    private static String cheminFichierTest;
    private static String contenuTest;

    @BeforeAll
    public static void setUp() throws IOException {
        cheminFichierTest = "test.txt";
        contenuTest = "abacabad";
        Files.write(Paths.get(cheminFichierTest), contenuTest.getBytes());
    }

    @Test
    public void testGetContenuFichier() throws IOException {
        String contenuActuel = OutilsAnalyseFichier.getContenuFichier(cheminFichierTest);
        assertEquals(contenuTest + "\n", contenuActuel); // Parce que getContenuFichier ajoute une nouvelle ligne
    }

    @Test
    public void testGetOccurrencesOrdonnees() {
        String contenu = "abacabad";
        int[][] occurrencesAttendues = {
                {'a', 4},
                {'b', 2},
                {'c', 1},
                {'d', 1}
        };
        int[][] occurrencesActuelles = OutilsAnalyseFichier.getOccurrencesOrdonnee(contenu);
        assertArrayEquals(occurrencesAttendues, occurrencesActuelles);
    }

    @Test
    public void testGetFrequences() {
        String contenu = "abacabad";
        Map<Character, Double> frequencesAttendues = Map.of(
                'a', 4 / 8.0,
                'b', 2 / 8.0,
                'c', 1 / 8.0,
                'd', 1 / 8.0
        );
        Map<Character, Double> frequencesActuelles = OutilsAnalyseFichier.getFrequences(contenu);

        assertEquals(frequencesAttendues.size(), frequencesActuelles.size());
        for (Map.Entry<Character, Double> entree : frequencesAttendues.entrySet()) {
            assertEquals(entree.getValue(), frequencesActuelles.get(entree.getKey()), 0.0001);
        }
    }
}