package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.huffman.DecompressionHuffman;
import fr.iutrodez.compilateurhuffman.objets.Noeud;
import fr.iutrodez.compilateurhuffman.outils.GestionFichier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestDecompressionHuffman {

    @TempDir
    Path tempDir;

    private Path fichierSource;
    private Path fichierDestination;
    private Path fichierArbre;

    @BeforeEach
    void setUp() throws IOException {
        fichierSource = tempDir.resolve("fichierSource.huff");
        fichierDestination = tempDir.resolve("fichierDestination.txt");
        fichierArbre = tempDir.resolve("arbreHuffman.txt");

        // Crée un fichier compressé factice
        byte[] contenuCompressé = { /* Contenu compressé factice */ };
        Files.write(fichierSource, contenuCompressé);

        // Crée un fichier d'arbre Huffman factice
        String contenuArbre = "char=97;code=0\nchar=98;code=10\nchar=99;code=11";
        Files.write(fichierArbre, contenuArbre.getBytes());
    }

    @Test
    void testDecompresserFichier() throws IOException {
        DecompressionHuffman decompressionHuffman = new DecompressionHuffman(
                fichierSource.toString(), fichierDestination.toString());
        decompressionHuffman.decompresserFichier();

        // Vérifie que le fichier de destination a été créé et n'est pas vide
        assertTrue(Files.exists(fichierDestination));
        assertTrue(Files.size(fichierDestination) > 0);

        // Vérifie que le fichier source n'a pas été altéré
        assertTrue(Files.exists(fichierSource));
        assertTrue(Files.size(fichierSource) > 0);
    }


    @Test
    void testGenererTableDeCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DecompressionHuffman decompressionHuffman = new DecompressionHuffman(
                fichierSource.toString(), fichierDestination.toString());

        String[] arbre = {
                "char=98;code=10",
                "char=99;code=11"
        };

        // Utilisation de la réflexion pour accéder à la méthode privée
        Method method = DecompressionHuffman.class.getDeclaredMethod("genererTableDeCode", String[].class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Byte, String> tableDeCode = (Map<Byte, String>) method.invoke(decompressionHuffman, (Object) arbre);

        // Vérifie que la table de code est correcte
        assertEquals("10", tableDeCode.get((byte) 98));
        assertEquals("11", tableDeCode.get((byte) 99));
    }

    @Test
    void testDecoderChaineBinaireEnBytes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Noeud racine = new Noeud();
        Noeud noeudA = new Noeud();
        noeudA.setCaractere((byte) 97); // 'a' en ASCII
        Noeud noeudB = new Noeud();
        noeudB.setCaractere((byte) 98); // 'b' en ASCII
        Noeud noeudC = new Noeud();
        noeudC.setCaractere((byte) 99); // 'c' en ASCII

        racine.setGauche(noeudA);
        Noeud noeudTemp = new Noeud();
        noeudTemp.setGauche(noeudB);
        noeudTemp.setDroite(noeudC);
        racine.setDroite(noeudTemp);

        String codeBinaire = "011011";

        // Utilisation de la réflexion pour accéder à la méthode privée
        Method method = DecompressionHuffman.class.getDeclaredMethod("decoderChaineBinaireEnBytes", String.class, Noeud.class);
        method.setAccessible(true);
        byte[] result = (byte[]) method.invoke(null, codeBinaire, racine);

        // Vérifie que le résultat est correct
        assertArrayEquals(new byte[]{97, 98, 99}, result); // 'a', 'b', 'c' en ASCII
    }
}