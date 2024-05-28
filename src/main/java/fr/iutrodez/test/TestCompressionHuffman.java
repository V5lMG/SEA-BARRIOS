package fr.iutrodez.test;

import fr.iutrodez.compilateurhuffman.huffman.CompressionHuffman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCompressionHuffman {
    private final ByteArrayOutputStream sortieConsole = new ByteArrayOutputStream();
    private final String cheminFichierTest = "test.txt";
    private final String contenuTest = "abacabad";
    private final String cheminDossierDestination = "testDestination";

    @BeforeEach
    public void setUp() throws IOException {
        // Rediriger la sortie console
        System.setOut(new PrintStream(sortieConsole));

        // Écrire le contenu du fichier de test
        Files.write(Paths.get(cheminFichierTest), contenuTest.getBytes());

        // Créer le dossier de destination
        Files.createDirectories(Paths.get(cheminDossierDestination));
    }

    @Test
    public void testDemanderFichierACompresser() throws IOException {
        // Simuler l'entrée utilisateur pour les chemins de fichier et les choix
        String input = cheminFichierTest + "\noui\n" + cheminDossierDestination + "\ntestCompresse\nnon\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Appeler la méthode à tester
        CompressionHuffman.demanderFichierACompresser(new String[]{});

        // Vérifier si le fichier compressé a été créé
        assertTrue(Files.exists(Paths.get(cheminDossierDestination, "testCompresse", "testCompresse.bin")));
    }

    @Test
    public void testAfficherCaracteres() {
        // Appeler la méthode à tester avec le contenu de test
        CompressionHuffman.afficherCaracteres(contenuTest);

        // Vérifier la sortie console
        String sortie = sortieConsole.toString();
        assertTrue(sortie.contains("Caractère: a; Occurrences: 4; Fréquence: 50.0%"));
        assertTrue(sortie.contains("Caractère: b; Occurrences: 2; Fréquence: 25.0%"));
        assertTrue(sortie.contains("Caractère: c; Occurrences: 1; Fréquence: 12.5%"));
        assertTrue(sortie.contains("Caractère: d; Occurrences: 1; Fréquence: 12.5%"));
    }

    @Test
    public void testDemanderRecommencer() {
        // Simuler l'entrée utilisateur pour les choix
        String input = "oui\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Appeler la méthode à tester
        boolean recommencer = CompressionHuffman.demanderRecommencer();

        // Vérifier le résultat
        assertTrue(recommencer);
    }
}
}
