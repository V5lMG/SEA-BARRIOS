package fr.iutrodez.compilateurhuffman.testJoric;

import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutilsGestionBinaireTest {

    private static Map<Character, String> codesHuffman;
    private static Map<String, Character> huffmanMap;

    @BeforeAll
    public static void setUp() {
        // Configuration initiale des codes Huffman pour les tests
        codesHuffman = new HashMap<>();
        codesHuffman.put('a', "100");
        codesHuffman.put('b', "101");
        codesHuffman.put('c', "110");

        // Initialiser l'ArbreHuffman avec les codes
        ArbreHuffman.setCodesHuffman(codesHuffman);

        huffmanMap = new HashMap<>();
        huffmanMap.put("100", 'a');
        huffmanMap.put("101", 'b');
        huffmanMap.put("110", 'c');
    }

    @Test
    public void testConvertirCaractereEnBinaire() {
        String contenu = "abc";
        String contenuEncode = OutilsGestionBinaire.convertirCaractereEnBinaire(contenu);
        assertEquals("100101110", contenuEncode);
    }

    @Test
    public void testConvertirBinaireEnBytes() {
        String binaire = "100101110";
        byte[] expectedBytes = new byte[]{(byte) 0b10010111, (byte) 0b00000000}; // ajout de padding
        byte[] resultBytes = OutilsGestionBinaire.convertirBinaireEnBytes(binaire);
        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void testRecupererBytesDansArbreHuffman() throws IOException {
        String cheminArbreHuffman = "test_files/huffman_codes.txt";
        List<String> huffmanCodes = OutilsGestionBinaire.recupererBytesDansArbreHuffman(cheminArbreHuffman);

        // On s'attend à ce que les codes soient correctement récupérés.
        assertEquals(3, huffmanCodes.size());
        assertEquals("codeHuffman = 100 ; encode = a ; symbole = a", huffmanCodes.get(0));
        assertEquals("codeHuffman = 101 ; encode = b ; symbole = b", huffmanCodes.get(1));
        assertEquals("codeHuffman = 110 ; encode = c ; symbole = c", huffmanCodes.get(2));
    }

    @Test
    public void testGetBytesADecompresser() throws IOException {
        String cheminFichierADecompiler = "test_files/compressed.bin";
        byte[] fileBytes = Files.readAllBytes(Paths.get(cheminFichierADecompiler));
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : fileBytes) {
            binaryStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        String expectedBinaryString = binaryStringBuilder.toString();
        String resultBinaryString = OutilsGestionBinaire.getBytesADecompresser(cheminFichierADecompiler);
        assertEquals(expectedBinaryString, resultBinaryString);
    }

    @Test
    public void testDecoderBytes() {
        String bytesADecompiler = "100101110";
        String expectedDecodedString = "abc";
        String decodedString = OutilsGestionBinaire.decoderBytes(huffmanMap, bytesADecompiler);
        assertEquals(expectedDecodedString, decodedString);
    }
}
