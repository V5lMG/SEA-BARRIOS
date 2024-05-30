/*
 * Pas de copyright, ni de droit d'auteur.
 * OutilsGestionBinaire.java               27/05/2024
 */
package fr.iutrodez.compilateurhuffman.outils;

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
     * @param codesHuffman la map des codes Huffman pour chaque caractère.
     * @return une chaîne de caractères représentant la chaîne de caractères encodée en binaire.
     */
    public static String convertirCaractereEnBinaire(String contenu, Map<Character, String> codesHuffman) {
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
        int longueurOriginale = binaire.length();
        int padding = 8 - (longueurOriginale % 8);
        if (padding != 8) {
            binaire += "0".repeat(padding);
        }

        // Stocker le padding dans les derniers bits ou via un en-tête
        String header = String.format("%8s", Integer.toBinaryString(padding)).replace(' ', '0');
        binaire = header + binaire; // Ajouter l'en-tête au début

        int longueur = binaire.length();
        byte[] bytes = new byte[longueur / 8];
        for (int i = 0; i < longueur; i += 8) {
            String octet = binaire.substring(i, i + 8);
            bytes[i / 8] = (byte) Integer.parseInt(octet, 2);
        }
        return bytes;
    }

    /* *============================================================*
     *
     *                          DÉCODAGE
     *
     * *============================================================*
     */
    public static List<String> getBytesDansArbreHuffman(String cheminArbreHuffman) throws IOException {
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

    public static String getBytesADecompresser(String cheminFichierADecompiler) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(cheminFichierADecompiler));
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : fileBytes) {
            binaryStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        // Supprimer l'en-tête ou le traiter ici pour ajuster le padding
        int padding = Integer.parseInt(binaryStringBuilder.substring(0, 8), 2);
        binaryStringBuilder.delete(0, 8); // Supprimer l'en-tête
        binaryStringBuilder.setLength(binaryStringBuilder.length() - padding); // Ajuster la fin selon le padding

        return binaryStringBuilder.toString();
    }

    /**
     * Décode un tableau d'octets en une chaîne de caractères en utilisant la map Huffman fournie.
     *
     * @param huffmanMap la map des codes Huffman et des caractères correspondants.
     * @param bytesADecompiler le tableau d'octets à décoder.
     * @return la chaîne de caractères décodée.
     */
    public static String decompresserBytes(Map<String, Character> huffmanMap, String bytesADecompiler) {
        StringBuilder decodedString = new StringBuilder();
        StringBuilder currentBits = new StringBuilder();

        for (char bit : bytesADecompiler.toCharArray()) {
            currentBits.append(bit);
            if (huffmanMap.containsKey(currentBits.toString())) {
                decodedString.append(huffmanMap.get(currentBits.toString()));
                currentBits.setLength(0); // Réinitialiser les bits actuels
            }
        }

        return decodedString.toString();
    }
}
