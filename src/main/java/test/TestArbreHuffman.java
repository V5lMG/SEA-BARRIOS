package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import iut.groupesae.compileurhuffman.ArbreHuffman;

/**
 * Classe de test pour la classe ArbreHuffman.
 */
public class TestArbreHuffman {

    /**
     * Teste la construction de l'arbre de Huffman
     */
    @Test
    public void testConstructionArbreHuffman() {
        String cheminFichier = "test.txt"; // Chemin vers le fichier de test

        ArbreHuffman arbreHuffman = null;
        try {
            arbreHuffman = new ArbreHuffman(cheminFichier);
        } catch (IOException e) {
            fail("Erreur lors de la construction de l'arbre de Huffman : " + e.getMessage());
        }

        assertNotNull(arbreHuffman.getRacine());
    }
}
