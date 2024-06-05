package fr.iutrodez.compresseurhuffman.outils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionFichier {

    private static final String CHEMIN_FICHIER_TEST = "testFile.txt";
    private static final String CHEMIN_FICHIER_BINAIRE = "testFile.bin";
    private static final String CHEMIN_FICHIER_OCTETS = "testFileOctets.bin";

    @BeforeEach
    void setUp() throws IOException {
        // Crée des fichiers temporaires pour les tests
        Files.writeString(Path.of(CHEMIN_FICHIER_TEST), "ligneUne\nligneDeux\nligneTrois");
        Files.writeString(Path.of(CHEMIN_FICHIER_BINAIRE), "0110110001101001011001110110111001100101010101010110111001100101"); // "ligneUne" en binaire
        Files.write(Path.of(CHEMIN_FICHIER_OCTETS), new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    }

    @AfterEach
    void tearDown() throws IOException {
        // Supprime les fichiers temporaires après chaque test
        Files.deleteIfExists(Path.of(CHEMIN_FICHIER_TEST));
        Files.deleteIfExists(Path.of(CHEMIN_FICHIER_BINAIRE));
        Files.deleteIfExists(Path.of(CHEMIN_FICHIER_OCTETS));
    }

    @Test
    void lireFichierArbreHuffman() {
        String[] lignes = GestionFichier.lireFichierArbreHuffman(CHEMIN_FICHIER_TEST);
        assertArrayEquals(new String[]{"ligneUne", "ligneDeux", "ligneTrois"}, lignes);
    }

    @Test
    void ecrireArbreHuffman() throws IOException {
        String[] arbreHuffman = {"test1", "test2", "test3"};
        GestionFichier.ecrireArbreHuffman(CHEMIN_FICHIER_TEST, arbreHuffman);

        String[] lignes = Files.readAllLines(Path.of(CHEMIN_FICHIER_TEST)).toArray(new String[0]);
        assertArrayEquals(arbreHuffman, lignes);
    }

    @Test
    void ecrireChaineBinaireDansFichier() throws IOException {
        String chaineBinaire = "0110110001101001011001110110111001100101010101010110111001100101"; // "ligneUne" en binaire
        GestionFichier.ecrireChaineBinaireDansFichier(chaineBinaire, CHEMIN_FICHIER_BINAIRE);

        byte[] bytesFromFile = Files.readAllBytes(Path.of(CHEMIN_FICHIER_BINAIRE));
        assertEquals(chaineBinaire.length(), (bytesFromFile.length - 4) * 8);
    }

    @Test
    void lireFichierBinaire() throws IOException {
        String chaineBinaire = "0110110001101001011001110110111001100101010101010110111001100101"; // "ligneUne" en binaire
        GestionFichier.ecrireChaineBinaireDansFichier(chaineBinaire, CHEMIN_FICHIER_BINAIRE);

        String chaineLue = GestionFichier.lireFichierBinaire(CHEMIN_FICHIER_BINAIRE);
        assertEquals(chaineBinaire, chaineLue);
    }

    @Test
    void recupererOctets() throws IOException {
        byte[] octetsAttendus = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] octetsLus = GestionFichier.recupererOctets(CHEMIN_FICHIER_OCTETS);

        assertArrayEquals(octetsAttendus, octetsLus);
    }

    @Test
    void ecrireFichierDestination() throws IOException {
        byte[] donnees = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        GestionFichier.ecrireFichierDestination(donnees, CHEMIN_FICHIER_OCTETS);

        byte[] donneesLues = Files.readAllBytes(Path.of(CHEMIN_FICHIER_OCTETS));
        assertArrayEquals(donnees, donneesLues);
    }
}