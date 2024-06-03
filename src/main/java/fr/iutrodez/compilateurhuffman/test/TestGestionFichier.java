package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.outils.GestionFichier;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionFichier {

    @TempDir
    Path tempDir;

    private Path fichierTexte;
    private Path fichierBinaire;
    private Path fichierDestination;

    @BeforeEach
    void setUp() throws IOException {
        fichierTexte = tempDir.resolve("testFile.txt");
        fichierBinaire = tempDir.resolve("testFile.bin");
        fichierDestination = tempDir.resolve("destinationFile.bin");

        // Crée un fichier texte factice
        String contenuTexte = "Ligne 1\nLigne 2\nLigne 3";
        Files.write(fichierTexte, contenuTexte.getBytes());

        // Crée un fichier binaire factice
        byte[] contenuBinaire = {0x0F, 0x2A, (byte) 0xFF};
        Files.write(fichierBinaire, contenuBinaire);
    }

    @Test
    void testLireToutLeFichier() {
        String[] lignes = GestionFichier.lireToutLeFichier(fichierTexte.toString());
        assertArrayEquals(new String[]{"Ligne 1", "Ligne 2", "Ligne 3"}, lignes);
    }

    @Test
    void testEcrireArbreHuffman() throws IOException {
        String[] arbreHuffman = {"char=97;code=0", "char=98;code=10", "char=99;code=11"};
        GestionFichier.ecrireArbreHuffman(fichierTexte.toString(), arbreHuffman);

        String[] lignes = Files.readAllLines(fichierTexte).toArray(new String[0]);
        assertArrayEquals(arbreHuffman, lignes);
    }

    @Test
    void testEcrireChaineBinaireDansFichier() throws IOException {
        String chaineBinaire = "01101101";
        GestionFichier.ecrireChaineBinaireDansFichier(chaineBinaire, fichierBinaire.toString());

        byte[] contenuBinaire = Files.readAllBytes(fichierBinaire);
        // Vérifie que la longueur des bits est correcte
        assertEquals(chaineBinaire.length(), contenuBinaire[0]);
    }

    @Test
    void testLireFichierBinaireEnChaine() throws IOException {
        // Prépare un fichier binaire avec une chaîne binaire connue
        String chaineBinaire = "01101101";
        GestionFichier.ecrireChaineBinaireDansFichier(chaineBinaire, fichierBinaire.toString());

        String contenuLu = GestionFichier.lireFichierBinaire(fichierBinaire.toString());
        assertEquals(chaineBinaire, contenuLu);
    }

    @Test
    void testRecupererOctets() throws IOException {
        byte[] contenuAttendu = {0x0F, 0x2A, (byte) 0xFF};
        byte[] contenuLu = GestionFichier.recupererOctets(fichierBinaire.toString());
        assertArrayEquals(contenuAttendu, contenuLu);
    }

    @Test
    void testEcrireFichierDestination() throws IOException {
        byte[] contenu = {0x0A, 0x1B, 0x2C};
        GestionFichier.ecrireFichierDestination(contenu, fichierDestination.toString());

        byte[] contenuLu = Files.readAllBytes(fichierDestination);
        assertArrayEquals(contenu, contenuLu);
    }
}

