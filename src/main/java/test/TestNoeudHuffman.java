package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import iut.groupesae.compileurhuffman.NoeudHuffman;

/**
 * Classe de test pour la classe NoeudHuffman.
 */
public class TestNoeudHuffman {

    /**
     * Teste la création d'un nœud Huffman avec des valeurs spécifiques.
     */
    @Test
    public void testCreationNoeud() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        assertEquals('a', noeud.caractere);
        assertEquals(0.25, noeud.frequence, 0.0001);
        assertNull(noeud.gauche);
        assertNull(noeud.droite);
    }

    /**
     * Teste la comparaison de deux nœuds Huffman.
     */
    @Test
    public void testComparaisonNoeud() {
        NoeudHuffman noeud1 = new NoeudHuffman('a', 0.25);
        NoeudHuffman noeud2 = new NoeudHuffman('b', 0.50);
        NoeudHuffman noeud3 = new NoeudHuffman('c', 0.25);

        assertTrue(noeud2.compareTo(noeud1) > 0); // b a
        assertEquals(0, noeud1.compareTo(noeud3)); // a c
        assertTrue(noeud3.compareTo(noeud2) < 0); // c b
    }
}
