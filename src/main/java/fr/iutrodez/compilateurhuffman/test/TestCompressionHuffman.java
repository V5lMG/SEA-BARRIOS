package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.huffman.CompressionHuffman;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCompressionHuffman {

    private Path fichierSource;
    private Path fichierDestination;

    @BeforeEach
    void setUp() throws IOException {
        fichierSource = Files.createTempFile("source", ".txt");
        fichierDestination = Files.createTempFile("destination", ".bin");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(fichierSource);
        Files.deleteIfExists(fichierDestination);
    }

    @Test
    void testCompresserFichier() throws IOException {
        String contenu = "aaaabcc";
        Files.write(fichierSource, contenu.getBytes());

        CompressionHuffman compressionHuffman = new CompressionHuffman(
                fichierSource.toString(), fichierDestination.toString());
        compressionHuffman.compresserFichier();

        // Vérifiez que le fichier de destination a été créé et n'est pas vide
        assertTrue(Files.exists(fichierDestination));
        assertTrue(Files.size(fichierDestination) > 0);
    }

    @Test
    void testGenererEtEnregistrerArbreHuffman() throws IOException {
        String contenu = "aaaabcc";
        Files.write(fichierSource, contenu.getBytes());

        CompressionHuffman compressionHuffman = new CompressionHuffman(
                fichierSource.toString(), fichierDestination.toString());
        compressionHuffman.genererEtEnregistrerArbreHuffman();

        // Vérifiez que le fichier de destination a été créé et n'est pas vide
        assertTrue(Files.exists(fichierDestination));
        assertTrue(Files.size(fichierDestination) > 0);
    }
}
