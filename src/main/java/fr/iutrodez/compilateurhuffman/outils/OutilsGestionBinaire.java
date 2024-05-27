package fr.iutrodez.compilateurhuffman.outils;

import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutilsGestionBinaire {

    /* *============================================================*
     *
     *                          ENCODAGE
     *
     * *============================================================*
     */
    /**
     * Encode une chaîne de caractères en utilisant les codes de l'arbre Huffman.
     *
     * @param contenu la chaîne de caractères à encoder.
     * @return une chaîne de caractères représentant la chaîne de caractères encodée en binaire.
     */
    public static String convertirCaractereEnBinaire(String contenu) {
        Map<Character, String> codesHuffman = ArbreHuffman.getCodesHuffman();
        StringBuilder contenuEncode = new StringBuilder();

        for (char caractere : contenu.toCharArray()) {
            contenuEncode.append(codesHuffman.get(caractere));
        }

        return contenuEncode.toString();
    }

    /**
     * Convertit une chaîne de caractères binaires en un tableau d'octets.
     *
     * @param binaire la chaîne de caractères binaires à convertir.
     * @return un tableau d'octets représentant la chaîne de caractères binaires convertie.
     */
    public static byte[] convertirBinaireEnBytes(String binaire) {
        // Compléter la chaîne binaire avec des zéros pour la rendre de longueur multiple de 8.
        int longueur = binaire.length();
        int padding = 8 - (longueur % 8);
        if (padding != 8) {
            binaire += "0".repeat(padding);
        }

        // Calculer la nouvelle longueur après ajout des zéros.
        longueur = binaire.length();
        byte[] bytes = new byte[longueur / 8];
        for (int i = 0; i < longueur; i += 8) {
            String octet = binaire.substring(i, i + 8);
            bytes[i / 8] = (byte) Integer.parseInt(octet, 2);
        }
        return bytes;
    }

    public static List<String> recupererBytesDansArbreHuffman(String cheminArbreHuffman) throws IOException {
        List<String> huffmanCodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminArbreHuffman))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ; ");
                if (parts.length == 3) {
                    String codeHuffman = parts[0].split(" = ")[1];
                    String encode = parts[1].split(" = ")[1];

                    // Formater correctement le symbole de retour a la ligne
                    String symbolePart = parts[2].split(" = ").length > 1 ? parts[2].split(" = ")[1] : "\n";

                    char symbole = symbolePart.charAt(0);
                    String huffmanCodeEntry = "codeHuffman = " + codeHuffman + " ; encode = " + encode + " ; symbole = " + symbole;
                    huffmanCodes.add(huffmanCodeEntry);
                }
            }
        }
        return huffmanCodes;
    }

    public static String getBytesADecompiler(String cheminFichierADecompiler) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(cheminFichierADecompiler));
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : fileBytes) {
            binaryStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return binaryStringBuilder.toString();
    }
}
