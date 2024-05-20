package test;

import iut.groupesae.compileurhuffman.GestionFichier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestionFichier {

    private final String CHEMIN_VALIDE = "test.txt";
    private final String CHEMIN_VALIDE_AVEC_GUILLEMETS = "\"test.txt\"";
    private final String CHEMIN_INEXISTANT = "chemin/vers/un/fichier/inexistant.txt";
    private final String CONTENUE_VALIDE = "azertyuiop : c'est la première ligne du clavier\nFrancais ! \n";

    @BeforeEach
    public void creerFichier() throws IOException {
        File fichier = new File("test.txt");

        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write(CONTENUE_VALIDE);
        }
    }

    @Test
    void testGetContenuFichier() {
        try {
            String contenu = GestionFichier.getContenuFichier(CHEMIN_VALIDE);
            assertEquals(CONTENUE_VALIDE, contenu, "Le contenu du fichier ne correspond pas.");
        } catch (IOException e) {
            fail("Une exception ne devrait pas être lancée pour un fichier valide.");
        }
    }

    @Test
    void testGetContenuFichierInexistant() {
        assertThrows(IOException.class, () -> GestionFichier.getContenuFichier(CHEMIN_INEXISTANT),
                "Une IOException devrait être lancée pour un fichier inexistant.");
    }

    @Test
    void testObtenirCheminFichierAvecArgsAvecGuillemets() {
        String[] args = {CHEMIN_VALIDE_AVEC_GUILLEMETS};
        assertDoesNotThrow(() -> {
            String cheminFichier = GestionFichier.getCheminFichierSource(args);
            assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
        });
    }

    @Test
    void testObtenirCheminFichierSansArgsAvecGuillemets() {
        // Simuler une saisie utilisateur depuis la console
        System.setIn(new java.io.ByteArrayInputStream(CHEMIN_VALIDE_AVEC_GUILLEMETS.getBytes()));

        assertDoesNotThrow(() -> {
            String cheminFichier = GestionFichier.getCheminFichierSource(new String[]{});
            assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
        });
    }

    @Test
    void testObtenirCheminFichierAvecArgs() {
        String[] args = {CHEMIN_VALIDE};
        assertDoesNotThrow(() -> {
            String cheminFichier = GestionFichier.getCheminFichierSource(args);
            assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
        });
    }

    @Test
    void testObtenirCheminFichierSansArgs() {
        String[] args = {};
        // Simuler une saisie utilisateur depuis la console
        System.setIn(new java.io.ByteArrayInputStream(CHEMIN_VALIDE.getBytes()));

        assertDoesNotThrow(() -> {
            String cheminFichier = GestionFichier.getCheminFichierSource(args);
            assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
        });
    }

    @Test
    void testGetFrequences() {
        String contenu = "aaabbbccc";
        Map<Character, Double> frequences = GestionFichier.getFrequences(contenu);

        assertEquals(3, frequences.size(), "La taille de la carte des fréquences est incorrecte.");

        assertTrue(frequences.containsKey('a'), "La carte des fréquences ne contient pas la clé 'a'.");
        assertEquals(0.3333333333333333, frequences.get('a'), "La fréquence de 'a' est incorrecte.");

        assertTrue(frequences.containsKey('b'), "La carte des fréquences ne contient pas la clé 'b'.");
        assertEquals(0.3333333333333333, frequences.get('b'), "La fréquence de 'b' est incorrecte.");

        assertTrue(frequences.containsKey('c'), "La carte des fréquences ne contient pas la clé 'c'.");
        assertEquals(0.3333333333333333, frequences.get('c'), "La fréquence de 'c' est incorrecte.");
    }

    @Test
    void testGetCheminDestination() {
        // Simuler une saisie utilisateur depuis la console
        System.setIn(new java.io.ByteArrayInputStream("chemin/vers/repertoire".getBytes()));
        Scanner scanner = new Scanner(System.in);

        String cheminDestination = GestionFichier.getCheminDestination(scanner);
        assertEquals("chemin/vers/repertoire", cheminDestination, "Le chemin de destination récupéré ne correspond pas.");
    }

    @Test
    void testNettoyerCheminSansGuillemets() {
        String cheminSansGuillemets = GestionFichier.nettoyerChemin(CHEMIN_VALIDE);
        assertEquals(CHEMIN_VALIDE, cheminSansGuillemets, "Le chemin nettoyé ne correspond pas.");
    }

    @Test
    void testNettoyerCheminAvecGuillemets() {
        String cheminAvecGuillemets = GestionFichier.nettoyerChemin(CHEMIN_VALIDE_AVEC_GUILLEMETS);
        assertEquals(CHEMIN_VALIDE, cheminAvecGuillemets, "Le chemin nettoyé ne correspond pas.");
    }
}
