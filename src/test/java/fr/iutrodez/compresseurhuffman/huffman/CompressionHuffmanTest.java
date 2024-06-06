package fr.iutrodez.compresseurhuffman.huffman;

import fr.iutrodez.compresseurhuffman.objets.Noeud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CompressionHuffmanTest {

    private CompressionHuffman compressionHuffmanTest;

    @BeforeEach
    void setUp() {
        compressionHuffmanTest = new CompressionHuffman("source.txt", "destination.txt");
    }

    @Test
    void testGetFrequenceDesOctets() {
        byte[] octets = { 'a', 'b', 'a', 'a', 'b', 'c' };
        Map<Byte, Integer> frequencies = compressionHuffmanTest.getFrequenceDesOctets(octets);

        assertEquals(3, frequencies.get((byte) 'a'));
        assertEquals(2, frequencies.get((byte) 'b'));
        assertEquals(1, frequencies.get((byte) 'c'));
    }

    @Test
    void testConstruireArbreAvecOccurences() {
        Map<Byte, Integer> occurrences = new HashMap<>();
        occurrences.put((byte) 'a', 5);
        occurrences.put((byte) 'b', 9);
        occurrences.put((byte) 'c', 12);
        occurrences.put((byte) 'd', 13);
        occurrences.put((byte) 'e', 16);
        occurrences.put((byte) 'f', 45);

        Noeud root = compressionHuffmanTest.construireArbreAvecOccurences(occurrences);

        assertNotNull(root);
        assertEquals(100, root.getFrequence()); // Total frequency
    }

    @Test
    void testGenererCodesHuffman() {
        Map<Byte, Integer> occurrences = new HashMap<>();
        occurrences.put((byte) 'a', 5);
        occurrences.put((byte) 'b', 9);
        occurrences.put((byte) 'c', 12);
        occurrences.put((byte) 'd', 13);
        occurrences.put((byte) 'e', 16);
        occurrences.put((byte) 'f', 45);

        Map<Byte, String> codes = compressionHuffmanTest.genererCodesHuffman(occurrences);

        assertNotNull(codes);
        assertFalse(codes.isEmpty());
    }

    @Test
    void testConvertirOctetsEnCodeHuffman() {
        byte[] octets = { 'a', 'b', 'a', 'a', 'b', 'c' };
        Map<Byte, String> huffmanCodes = new HashMap<>();
        huffmanCodes.put((byte) 'a', "00");
        huffmanCodes.put((byte) 'b', "01");
        huffmanCodes.put((byte) 'c', "10");

        String result = compressionHuffmanTest.convertirOctetsEnCodeHuffman(octets, huffmanCodes);

        assertEquals("00000100010010", result);
    }
}
