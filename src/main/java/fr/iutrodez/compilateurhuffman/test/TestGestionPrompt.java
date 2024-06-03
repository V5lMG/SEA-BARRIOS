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

    @BeforeEach
    void setUp() {
        gestionPrompt = new GestionPrompt();
    }

    @Test
    void testGetFichierSource() {
        String input = "dossier1\\dossier2\\monFichier.txt\noui\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String cheminFichier = tempDir.resolve("monFichier.txt").toString();
        assertDoesNotThrow(() -> Files.createFile(Path.of(cheminFichier)));

        String resultat = gestionPrompt.getFichierSource("txt");
        assertEquals(cheminFichier, resultat);
    }

    @Test
    void testGetContenuFichier() throws IOException {
        String contenu = "Ligne 1\nLigne 2\nLigne 3";
        Path fichier = tempDir.resolve("testFile.txt");
        Files.write(fichier, contenu.getBytes());

        String contenuLu = gestionPrompt.getContenuFichier(fichier.toString());
        assertEquals(contenu + "\n", contenuLu);
    }

    @Test
    void testGetFichierDestination() {
        String input = tempDir.toString() + "\nmonFichierDestination\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String resultat = gestionPrompt.getFichierDestination("txt");
        String cheminAttendu = tempDir.resolve("monFichierDestination.txt").toString();
        assertEquals(cheminAttendu, resultat);
    }

    @Test
    void testCreerDossierPourCompilation() {
        String cheminDossier = tempDir.resolve("nouveauDossier").toString();
        assertDoesNotThrow(() -> GestionPrompt.creerDossierPourCompilation(cheminDossier));
        assertTrue(Files.exists(Path.of(cheminDossier)));
    }

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
