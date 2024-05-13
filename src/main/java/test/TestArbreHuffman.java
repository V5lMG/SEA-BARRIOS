package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import iut.groupesae.compileurhuffman.GestionFichier;
import iut.groupesae.compileurhuffman.objetcs.ArbreHuffman;
import iut.groupesae.compileurhuffman.objetcs.NoeudHuffman;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TestArbreHuffman {

    @Test
    public void testConstructionArbreHuffman() {
        String cheminFichier = "test.txt";

        ArbreHuffman arbreHuffman = null;
        try {
            arbreHuffman = new ArbreHuffman(cheminFichier);
        } catch (IOException e) {
            fail("Erreur lors de la construction de l'arbre de Huffman : " + e.getMessage());
        }

        assertNotNull(arbreHuffman);
    }

    @Test
    public void testTrierArbre() throws IOException {
        Map<Character, Double> frequences = new HashMap<>();
        frequences.put('a', 0.25);
        frequences.put('b', 0.50);
        frequences.put('c', 0.25);

        String cheminFichier = "test.txt";
        ArbreHuffman arbreHuffman = new ArbreHuffman(cheminFichier);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);

        arbreHuffman.trierArbre(writer);
        String expectedOutput = "codeHuffman = null ; encode = 01100001 ; symbole = a\n"
                + "codeHuffman = null ; encode = 01100010 ; symbole = b\n"
                + "codeHuffman = null ; encode = 01100011 ; symbole = c\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }
}
