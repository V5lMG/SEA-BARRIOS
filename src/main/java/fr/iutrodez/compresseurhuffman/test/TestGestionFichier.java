package fr.iutrodez.compresseurhuffman.test;

import fr.iutrodez.compresseurhuffman.outils.GestionFichier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static fr.iutrodez.compresseurhuffman.outils.GestionFichier.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TestGestionFichier {

    @TempDir
    Path tempDir;

    private Path fichierTexte;
    private Path fichierBinaire;
    private Path fichierDestination;
    /**
     * Configure l'environnement de test avant chaque test.
     * Crée des fichiers temporaires texte et binaire avec des contenus factices.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
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

    /**
     * Teste la méthode lireToutLeFichier de la classe GestionFichier.
     * Vérifie que le contenu du fichier texte est correctement lu.
     */
    @Test
    void testLireToutLeFichier() {
        String[] lignes = lireFichierArbreHuffman(fichierTexte.toString());
        assertArrayEquals(new String[]{"Ligne 1", "Ligne 2", "Ligne 3"}, lignes);
    }

    /**
     * Teste la méthode ecrireArbreHuffman de la classe GestionFichier.
     * Vérifie que l'arbre de Huffman est correctement écrit dans le fichier texte.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testEcrireArbreHuffman() throws IOException {
        String[] arbreHuffman = {"char=97;code=0", "char=98;code=10", "char=99;code=11"};
        ecrireArbreHuffman(fichierTexte.toString(), arbreHuffman);

        String[] lignes = Files.readAllLines(fichierTexte).toArray(new String[0]);
        assertArrayEquals(arbreHuffman, lignes);
    }

    /**
     * Teste la méthode ecrireChaineBinaireDansFichier de la classe GestionFichier.
     * Vérifie que la chaîne binaire est correctement écrite dans le fichier binaire.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testEcrireChaineBinaireDansFichier() throws IOException {
        String chaineBinaire = "01101101";
        ecrireChaineBinaireDansFichier(chaineBinaire, fichierBinaire.toString());

        byte[] contenuBinaire = Files.readAllBytes(fichierBinaire);
        // Vérifie que la longueur des bits est correcte
        assertEquals(chaineBinaire.length(), contenuBinaire[0]);
    }

    /**
     * Teste la méthode lireFichierBinaire de la classe GestionFichier.
     * Vérifie que le contenu binaire du fichier est correctement lu sous forme de chaîne binaire.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testLireFichierBinaireEnChaine() throws IOException {
        // Prépare un fichier binaire avec une chaîne binaire connue
        String chaineBinaire = "01101101";
        ecrireChaineBinaireDansFichier(chaineBinaire, fichierBinaire.toString());

        String contenuLu = GestionFichier.lireFichierBinaire(fichierBinaire.toString());
        assertEquals(chaineBinaire, contenuLu);
    }

    /**
     * Teste la méthode recupererOctets de la classe GestionFichier.
     * Vérifie que le contenu binaire du fichier est correctement lu en tant que tableau d'octets.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testRecupererOctets() throws IOException {
        byte[] contenuAttendu = {0x0F, 0x2A, (byte) 0xFF};
        byte[] contenuLu = recupererOctets(fichierBinaire.toString());
        assertArrayEquals(contenuAttendu, contenuLu);
    }

    /**
     * Teste la méthode ecrireFichierDestination de la classe GestionFichier.
     * Vérifie que le contenu binaire est correctement écrit dans le fichier de destination.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Test
    void testEcrireFichierDestination() throws IOException {
        byte[] contenu = {0x0A, 0x1B, 0x2C};
        ecrireFichierDestination(contenu, fichierDestination.toString());

        byte[] contenuLu = Files.readAllBytes(fichierDestination);
        assertArrayEquals(contenu, contenuLu);
    }
}

