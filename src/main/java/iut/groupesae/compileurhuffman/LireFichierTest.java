package iut.groupesae.compileurhuffman;

import iut.groupesae.compileurhuffman.LireFichier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class LireFichierTest {

    private LireFichier lireFichier;

    @BeforeEach
    public void creerFichier() {
        lireFichier = new LireFichier("test.txt");
    }

    @Test
    public void testLireEtAfficherLigne() throws IOException {
        // Créer un fichier pour le test
        File fichier = new File("test.txt");

        // Ecrire du contenu dans le fichier
        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("TestLigne1\n");
            writer.write("TestLigne2\n");
        }
    }
    
    @Test
    public void testGetCheminFichier() {
        // Vérifier que le chemin du fichier est correctement retourné
        String cheminFichier = lireFichier.getCheminFichier();
        assertEquals("test.txt", cheminFichier);
    }
    
    @Test
    public void testGetLireFichier() {
    	String chemin;
    	chemin = fichier.getAbsolutePath();
        lireFichier.setCheminFichier(fichier.getAbsolutePath());
        lireFichier.LireEtAfficherLigne();

        String contenuFichier = new String(Files.readAllBytes(fichier));
        assertEquals(contenuFichier, "TestLigne1\nTestLigne2\n");
    }

    @Test
    public void testContenu() {
        // Vérifier que le contenu du fichier est affiché
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();
            assertTrue(ligne.length() > 0);
        }
    }
}
