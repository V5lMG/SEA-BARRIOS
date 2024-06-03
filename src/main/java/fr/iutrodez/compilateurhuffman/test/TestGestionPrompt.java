package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.outils.GestionPrompt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionPrompt {

    private GestionPrompt gestionPrompt;

    @TempDir
    Path tempDir;

    /**
     * Initialise l'instance de GestionPrompt avant chaque test.
     */
    @BeforeEach
    void setUp() {
        gestionPrompt = new GestionPrompt();
    }

    /**
     * Teste la méthode getFichierSource de la classe GestionPrompt.
     * Vérifie que le chemin du fichier source est correctement récupéré à partir de l'entrée utilisateur.
     */
    @Test
    void testGetFichierSource() {
        String input = "dossier1\\dossier2\\monFichier.txt\noui\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String cheminFichier = tempDir.resolve("monFichier.txt").toString();
        assertDoesNotThrow(() -> Files.createFile(Path.of(cheminFichier)));

        String resultat = gestionPrompt.getFichierSource("txt");
        assertEquals(cheminFichier, resultat);
    }

    /**
     * Teste la méthode getContenuFichier de la classe GestionPrompt.
     * Vérifie que le contenu du fichier est correctement lu.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la lecture du fichier.
     */
    @Test
    void testGetContenuFichier() throws IOException {
        String contenu = "Ligne 1\nLigne 2\nLigne 3";
        Path fichier = tempDir.resolve("testFile.txt");
        Files.write(fichier, contenu.getBytes());

        String contenuLu = gestionPrompt.getContenuFichier(fichier.toString());
        assertEquals(contenu + "\n", contenuLu);
    }

    /**
     * Teste la méthode getFichierDestination de la classe GestionPrompt.
     * Vérifie que le chemin du fichier destination est correctement récupéré à partir de l'entrée utilisateur.
     */
    @Test
    void testGetFichierDestination() {
        String input = tempDir.toString() + "\nmonFichierDestination\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String resultat = gestionPrompt.getFichierDestination("txt");
        String cheminAttendu = tempDir.resolve("monFichierDestination.txt").toString();
        assertEquals(cheminAttendu, resultat);
    }


    /**
     * Teste la méthode creerDossierPourCompilation de la classe GestionPrompt.
     * Vérifie que le dossier pour la compilation est créé avec succès.
     */
    @Test
    void testCreerDossierPourCompilation() {
        String cheminDossier = tempDir.resolve("nouveauDossier").toString();
        assertDoesNotThrow(() -> GestionPrompt.creerDossierPourCompilation(cheminDossier));
        assertTrue(Files.exists(Path.of(cheminDossier)));
    }

    /**
     * Teste la méthode demanderOuiOuNon de la classe GestionPrompt.
     * Vérifie que la réponse de l'utilisateur est correctement traitée.
     */
    @Test
    void testDemanderOuiOuNon() {
        String input = "oui\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean reponse = gestionPrompt.demanderOuiOuNon("Voulez-vous continuer?");
        assertTrue(reponse);

        input = "non\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        reponse = gestionPrompt.demanderOuiOuNon("Voulez-vous continuer?");
        assertFalse(reponse);
    }
}
