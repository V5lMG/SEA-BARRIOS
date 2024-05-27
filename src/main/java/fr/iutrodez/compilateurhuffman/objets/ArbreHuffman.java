package fr.iutrodez.compilateurhuffman.objets;

import fr.iutrodez.compilateurhuffman.outils.OutilsAnalyseFichier;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;

import java.io.IOException;
import java.util.Map;
import java.io.BufferedWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * ArbreHuffman représente un arbre de Huffman utilisé pour la compression et la décompression des données.
 * Un arbre de Huffman est un arbre binaire utilisé pour construire un code de Huffman.
 * Ce code est couramment utilisé pour compresser des données.
 */
public class ArbreHuffman {

    /**
     * Nœud racine de l'arbre de Huffman.
     */
    private static NoeudHuffman racine;

    /**
     * Crée un arbre de Huffman à partir des fréquences des caractères.
     *
     * @param cheminFichier le chemin d'accès au fichier texte
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    public ArbreHuffman(String cheminFichier) throws IOException {
        String contenu = OutilsAnalyseFichier.getContenuFichier(cheminFichier);
        Map<Character, Double> frequences = OutilsAnalyseFichier.getFrequences(contenu);
        racine = construireArbreHuffman(frequences);
    }

    /**
     * Trie l'arbre de Huffman dans l'ordre décroissant des fréquences des caractères.
     *
     * @param writer le flux d'écriture vers lequel écrire le résultat
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    public void trierArbre(BufferedWriter writer) throws IOException {
        List<NoeudHuffman> feuilles = new ArrayList<>();
        collecterFeuilles(racine, feuilles);
        feuilles.sort(Comparator.comparing(NoeudHuffman::getFrequence).reversed());

        for (NoeudHuffman feuille : feuilles) {
            String ligne = feuille.toString() + "\n";
            writer.write(ligne);
            System.out.print(ligne);
        }
    }

    /**
     * Parcourt l'arbre de Huffman et collecte les feuilles dans une liste.
     *
     * @param racine   le nœud racine de l'arbre de Huffman
     * @param feuilles la liste dans laquelle les feuilles collectées sont stockées
     */
    private void collecterFeuilles(NoeudHuffman racine, List<NoeudHuffman> feuilles) {
        if (racine == null) {
            return;
        }

        if (racine.getGauche() == null && racine.getDroite() == null) {
            feuilles.add(racine);
        }

        collecterFeuilles(racine.getGauche(), feuilles);
        collecterFeuilles(racine.getDroite(), feuilles);
    }

    /**
     * Crée un arbre de Huffman à partir d'une mappe des fréquences des caractères.
     *
     * @param frequences une map associant chaque caractère à sa fréquence d'apparition
     * @return le nœud racine de l'arbre de Huffman construit
     */
    private NoeudHuffman construireArbreHuffman(Map<Character, Double> frequences) {
        List<NoeudHuffman> feuilles = new ArrayList<>();

        for (Map.Entry<Character, Double> entree : frequences.entrySet()) {
            feuilles.add(new NoeudHuffman(entree.getKey(), entree.getValue()));
        }

        while (feuilles.size() > 1) {
            NoeudHuffman noeudMin1 = feuilles.remove(0);
            NoeudHuffman noeudMin2 = feuilles.remove(0);

            NoeudHuffman nouveauNoeud = new NoeudHuffman('\0', noeudMin1.getFrequence() + noeudMin2.getFrequence());
            nouveauNoeud.setGauche(noeudMin1);
            nouveauNoeud.setDroite(noeudMin2);

            feuilles.add(nouveauNoeud);
            feuilles.sort(Comparator.comparingDouble(NoeudHuffman::getFrequence));
        }

        setCodesHuffman(feuilles.get(0), ""); // Appel de la méthode pour attribuer les codes Huffman
        return feuilles.get(0);
    }

    /**
     * Attribue un code Huffman à chaque nœud de l'arbre.
     *
     * @param noeud le nœud courant auquel il faut attribuer un code
     * @param code  le code Huffman préfixe du nœud
     */
    private void setCodesHuffman(NoeudHuffman noeud, String code) {
        if (noeud == null) {
            return;
        }

        noeud.setCodeHuffman(code);
        // TODO echanger gauche = 0 et droite = 1
        setCodesHuffman(noeud.getGauche(), code + "1");
        setCodesHuffman(noeud.getDroite(), code + "0");
    }

    /**
     * Renvoie une map associant chaque caractère à son code Huffman
     *
     * @return une map des codes Huffman.
     */
    public static Map<Character, String> getCodesHuffman() {
        Map<Character, String> codesHuffman = new HashMap<>();
        collecterCodesHuffman(racine, codesHuffman);
        return codesHuffman;
    }

    /**
     * Parcourt l'arbre de Huffman et collecte les codes Huffman de chaque nœud dans une map.
     *
     * @param noeud        le nœud courant à partir duquel collecter le code Huffman
     * @param codesHuffman la map dans laquelle stocker les codes Huffman collectés
     */
    private static void collecterCodesHuffman(NoeudHuffman noeud, Map<Character, String> codesHuffman) {
        if (noeud == null) {
            return;
        }

        if (noeud.getGauche() == null && noeud.getDroite() == null) {
            codesHuffman.put(noeud.getCaractere(), noeud.getCodeHuffman());
        }

        collecterCodesHuffman(noeud.getGauche(), codesHuffman);
        collecterCodesHuffman(noeud.getDroite(), codesHuffman);
    }

    /**
     * Encode une chaîne de caractères en binaire en utilisant les codes de l'arbre Huffman.
     *
     * @param contenu la chaîne de caractères à encoder.
     * @return un tableau d'octets représentant la chaîne de caractères encodée.
     */
    public byte[] encoderFichier(String contenu) {
        String contenuEncode = OutilsGestionBinaire.convertirCaractereEnBinaire(contenu);
        return OutilsGestionBinaire.convertirBinaireEnBytes(contenuEncode);
    }

    /* *============================================================*
     *
     *                          DÉCODAGE
     *
     * *============================================================*
     */
    /**
     * Décode un tableau d'octets en une chaîne de caractères en utilisant l'arbre Huffman fourni.
     *
     * @param arbreHuffman l'arbre de Huffman utilisé pour décoder.
     * @param bytesADecompiler le tableau d'octets à décoder.
     * @return la chaîne de caractères décodée.
     */
    public static String decoderFichier(List<String> arbreHuffman, String bytesADecompiler) {
        // Créer une map pour les codes Huffman et leurs symboles
        Map<String, Character> huffmanMap = new HashMap<>();
        for (String entry : arbreHuffman) {
            String[] parts = entry.split(" ; ");
            if (parts.length == 3) {
                String codeHuffman = parts[0].split(" = ")[1];
                char symbole = parts[2].split(" = ")[1].charAt(0);
                huffmanMap.put(codeHuffman, symbole);
            }
        }

        // Lire bytesADecompiler caractère par caractère et décoder
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