package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import iut.groupesae.compileurhuffman.objetcs.NoeudHuffman;

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
        assertEquals('a', noeud.getCaractere());
        assertEquals(0.25, noeud.getFrequence(), 0.0001);
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
        assertNull(noeud.getCodeHuffman());
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

    /**
     * Teste la méthode getCodeHuffman.
     */
    @Test
    public void testGetCodeHuffman() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        assertNull(noeud.getCodeHuffman());

        noeud.setCodeHuffman("101");
        assertEquals("101", noeud.getCodeHuffman());
    }

    /**
     * Teste la méthode setCodeHuffman.
     */
    @Test
    public void testSetCodeHuffman() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        assertNull(noeud.getCodeHuffman());

        noeud.setCodeHuffman("101");
        assertEquals("101", noeud.getCodeHuffman());
    }

    @Test
    public void testSetAndGetCodeHuffman() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        assertNull(noeud.getCodeHuffman());

        String codeHuffman = "0101";
        noeud.setCodeHuffman(codeHuffman);
        assertEquals(codeHuffman, noeud.getCodeHuffman());
    }

    @Test
    public void testGetUTF8Binary() {
        char caractere = 'a';
        String expectedBinary = "01100001";
        assertEquals(expectedBinary, NoeudHuffman.getUTF8Binary(caractere));
    }

    @Test
    public void testToString() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        noeud.setCodeHuffman("0101");
        String expectedString = "codeHuffman = 0101 ; encode = 01100001 ; symbole = a";
        assertEquals(expectedString, noeud.toString());
    }

    @Test
    public void testSetAndGetGauche() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        assertNull(noeud.getGauche());

        NoeudHuffman gauche = new NoeudHuffman('b', 0.5);
        noeud.setGauche(gauche);
        assertEquals(gauche, noeud.getGauche());
    }

    @Test
    public void testSetAndGetDroite() {
        NoeudHuffman noeud = new NoeudHuffman('a', 0.25);
        assertNull(noeud.getDroite());

        NoeudHuffman droite = new NoeudHuffman('c', 0.75);
        noeud.setDroite(droite);
        assertEquals(droite, noeud.getDroite());
    }
}
