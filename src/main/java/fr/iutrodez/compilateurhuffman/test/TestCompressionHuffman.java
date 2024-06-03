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

    /**
     * Méthode exécutée avant chaque test pour initialiser les fichiers temporaires.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la création des fichiers temporaires.
     */
    @BeforeEach
    void setUp() throws IOException {
        fichierSource = Files.createTempFile("source", ".txt");
        fichierDestination = Files.createTempFile("destination", ".bin");
    }

    /**
     * Méthode exécutée après chaque test pour supprimer les fichiers temporaires.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la suppression des fichiers temporaires.
     */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(fichierSource);
        Files.deleteIfExists(fichierDestination);
    }

    /**
     * Teste la méthode CompresserFichier de la classe CompressionHuffman.
     * Vérifie que la compression du fichier source est effectuée avec succès.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de l'écriture des données dans le fichier source.
     */
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

    /**
     * Teste la méthode GenererEtEnregistrerArbreHuffman de la classe CompressionHuffman.
     * Vérifie que la génération et l'enregistrement de l'arbre de Huffman sont effectués avec succès.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de l'écriture des données dans le fichier source.
     */
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
