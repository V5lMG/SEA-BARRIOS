package fr.iutrodez.testJoric;

import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutilsGestionBinaireTest {

    private static Map<Character, String> codesHuffman;
    private static Map<String, Character> huffmanMap;

    @BeforeAll
    public static void setUp() {
        // Initialiser les maps pour les tests
        codesHuffman = new HashMap<>();
        codesHuffman.put('a', "00");
        codesHuffman.put('b', "01");
        codesHuffman.put('c', "10");
        codesHuffman.put('d', "11");
        ArbreHuffman.setCodesHuffman(codesHuffman); // Suppose que ArbreHuffman a une méthode statique setCodesHuffman

        huffmanMap = new HashMap<>();
        huffmanMap.put("00", 'a');
        huffmanMap.put("01", 'b');
        huffmanMap.put("10", 'c');
        huffmanMap.put("11", 'd');
    }

    @Test
    public void testConvertirCaractereEnBinaire() {
        String contenu = "abcd";
        String expected = "00011011";
        String result = OutilsGestionBinaire.convertirCaractereEnBinaire(contenu);
        assertEquals(expected, result);
    }

    @Test
    public void testConvertirBinaireEnBytes() {
        String binaire = "00011011";
        byte[] expected = { (byte) 0b00011011 };
        byte[] result = OutilsGestionBinaire.convertirBinaireEnBytes(binaire);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testRecupererBytesDansArbreHuffman() throws IOException {
        // Créer un fichier temporaire pour les tests
        Path path = Files.createTempFile("huffmanCodes", ".txt");
        Files.write(path, List.of(
                "codeHuffman = 00 ; encode = 0 ; symbole = a",
                "codeHuffman = 01 ; encode = 1 ; symbole = b"
        ));

        List<String> expected = List.of(
                "codeHuffman = 00 ; encode = 0 ; symbole = a",
                "codeHuffman = 01 ; encode = 1 ; symbole = b"
        );
        List<String> result = OutilsGestionBinaire.recupererBytesDansArbreHuffman(path.toString());
        assertEquals(expected, result);

        // Supprimer le fichier temporaire
        Files.deleteIfExists(path);
    }

    @Test
    public void testGetBytesADecompresser() throws IOException {
        // Créer un fichier temporaire pour les tests
        Path path = Files.createTempFile("binaryFile", ".bin");
        Files.write(path, new byte[]{ (byte) 0b00011011 });

        String expected = "00011011";
        String result = OutilsGestionBinaire.getBytesADecompresser(path.toString());
        assertEquals(expected, result);

        // Supprimer le fichier temporaire
        Files.deleteIfExists(path);
    }

    @Test
    public void testDecoderBytes() {
        String bytesADecompiler = "00011011";
        String expected = "abcd";
        String result = OutilsGestionBinaire.decoderBytes(huffmanMap, bytesADecompiler);
        assertEquals(expected, result);
    }
}