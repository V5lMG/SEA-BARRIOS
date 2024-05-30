package fr.iutrodez.testJoric;

import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OutilsGestionBinaireTest {

    @BeforeAll
    public static void setUp() throws IOException {
        // Initialiser un arbre de Huffman pour les tests
        String cheminFichier = "test_file.txt";
        Files.write(Paths.get(cheminFichier), "aaaabbbccd".getBytes());
        new ArbreHuffman(cheminFichier);
    }

    @Test
    public void testConvertirCaractereEnBinaire() throws IOException {

        /*// Chaîne de test
        String contenu = "abc";
        String contenuEncode = OutilsGestionBinaire.convertirCaractereEnBinaire(contenu);

        // Récupérer les codes Huffman générés pour vérifier le résultat attendu
        Map<Character, String> codesHuffman = ArbreHuffman.getCodesHuffman();
        StringBuilder expectedEncode = new StringBuilder();
        for (char c : contenu.toCharArray()) {
            expectedEncode.append(codesHuffman.get(c));
        }
        assertEquals(expectedEncode.toString(), contenuEncode);
        */

        // TODO refaire
    }

    @Test
    public void testConvertirBinaireEnBytes() {
        String binaire = "101011100001";
        byte[] bytes = OutilsGestionBinaire.convertirBinaireEnBytes(binaire);

        // Résultat attendu : [10101110, 00010000] en binaire → [-82, 16] en décimal
        byte[] expectedBytes = new byte[] { (byte) 0xAE, 0x10 };
        assertArrayEquals(expectedBytes, bytes);
    }

    @Test
    public void testRecupererBytesDansArbreHuffman() throws IOException {
        // Créer un fichier temporaire
        String cheminArbreHuffman = "test_huffman.txt";
        String contenu = "codeHuffman = 1010 ; encode = a ; symbole = a\n"
                + "codeHuffman = 1110 ; encode = b ; symbole = b\n";
        Files.write(Paths.get(cheminArbreHuffman), contenu.getBytes());

        // Lire le fichier
        List<String> codes = OutilsGestionBinaire.getBytesDansArbreHuffman(cheminArbreHuffman);

        // Vérifier le résultat attendu
        assertEquals(2, codes.size());
        assertEquals("codeHuffman = 1010 ; encode = a ; symbole = a", codes.get(0));
        assertEquals("codeHuffman = 1110 ; encode = b ; symbole = b", codes.get(1));

        // Supprimer le fichier temporaire
        Files.delete(Paths.get(cheminArbreHuffman));
    }

    @Test
    public void testGetBytesADecompresser() throws IOException {
        // Créer un fichier temporaire
        String cheminFichierADecompiler = "test_file.bin";
        byte[] contenu = new byte[] { (byte) 0xAE, 0x10 }; // 10101110 00010000 en binaire
        Files.write(Paths.get(cheminFichierADecompiler), contenu);

        // Lire le fichier
        String binaire = OutilsGestionBinaire.getBytesADecompresser(cheminFichierADecompiler);

        // Vérifier le résultat attendu
        assertEquals("1010111000010000", binaire);

        // Supprimer le fichier temporaire
        Files.delete(Paths.get(cheminFichierADecompiler));
    }

    @Test
    public void testDecoderBytes() {
        // Initialiser la map Huffman
        Map<String, Character> huffmanMap = new HashMap<>();
        huffmanMap.put("1010", 'a');
        huffmanMap.put("1110", 'b');
        huffmanMap.put("0001", 'c');

        // Chaîne de test
        String bytesADecompiler = "101011100001";
        String decodedString = OutilsGestionBinaire.decompresserBytes(huffmanMap, bytesADecompiler);

        // Vérifier le résultat attendu
        assertEquals("abc", decodedString);
    }
}