package iut.groupesae.compileurhuffman.objets;

import iut.groupesae.compileurhuffman.GestionFichier;

import java.io.*;
import java.util.*;

/**
 * ArbreHuffman représente un arbre de Huffman utilisé pour la compression et la décompression des données.
 * Un arbre de Huffman est un arbre binaire utilisé pour construire un code de Huffman.
 * Ce code est couramment utilisé pour compresser des données.
 */
public class ArbreHuffman {

    /**
     * Nœud racine de l'arbre de Huffman.
     */
    private final NoeudHuffman racine;

    /**
     * Crée un arbre de Huffman à partir des fréquences des caractères.
     *
     * @param cheminFichier le chemin d'accès au fichier texte
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    public ArbreHuffman(String cheminFichier) throws IOException {
        if (cheminFichier.endsWith("ArbreHuffman.txt")) {
            this.racine = reConstruireArbreHuffman(cheminFichier);
        } else {
            String contenu = GestionFichier.getContenuFichier(cheminFichier);
            Map<Character, Double> frequences = GestionFichier.getFrequences(contenu);
            this.racine = construireArbreHuffman(frequences);
        }
    }

    private NoeudHuffman reConstruireArbreHuffman(String cheminArbreHuffman) throws IOException {
        // todo
        return null;
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
            NoeudHuffman noeudMin1 = feuilles.removeFirst();
            NoeudHuffman noeudMin2 = feuilles.removeFirst();

            NoeudHuffman nouveauNoeud = new NoeudHuffman('\0', noeudMin1.getFrequence() + noeudMin2.getFrequence());
            nouveauNoeud.setGauche(noeudMin1);
            nouveauNoeud.setDroite(noeudMin2);

            feuilles.add(nouveauNoeud);
            feuilles.sort(Comparator.comparingDouble(NoeudHuffman::getFrequence));
        }

        setCodesHuffman(feuilles.getFirst(), ""); // Appel de la méthode pour attribuer les codes Huffman
        return feuilles.getFirst();
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

        setCodesHuffman(noeud.getGauche(), code + "0");
        setCodesHuffman(noeud.getDroite(), code + "1");
    }

    /**
     * Renvoie une map associant chaque caractère à son code Huffman
     *
     * @return une map des codes Huffman.
     */
    private Map<Character, String> getCodesHuffman() {
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
    private void collecterCodesHuffman(NoeudHuffman noeud, Map<Character, String> codesHuffman) {
        if (noeud == null) {
            return;
        }

        if (noeud.getGauche() == null && noeud.getDroite() == null) {
            codesHuffman.put(noeud.getCaractere(), noeud.getCodeHuffman());
        }

        collecterCodesHuffman(noeud.getGauche(), codesHuffman);
        collecterCodesHuffman(noeud.getDroite(), codesHuffman);
    }


    /* *============================================================*
     *
     *                          ENCODAGE
     *
     * *============================================================*
     */
    /**
     * Encode une chaîne de caractères en binaire en utilisant les codes de l'arbre Huffman.
     *
     * @param contenu la chaîne de caractères à encoder.
     * @return un tableau d'octets représentant la chaîne de caractères encodée.
     */
    public byte[] encoderFichier(String contenu) {
        String contenuEncode = encoderContenu(contenu);
        return convertirBinaireEnBytes(contenuEncode);
    }

    /**
     * Encode une chaîne de caractères en utilisant les codes de l'arbre Huffman.
     *
     * @param contenu la chaîne de caractères à encoder.
     * @return une chaîne de caractères représentant la chaîne de caractères encodée en binaire.
     */
    public String encoderContenu(String contenu) {
        Map<Character, String> codesHuffman = getCodesHuffman();
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
    private static byte[] convertirBinaireEnBytes(String binaire) {
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
    public String decoderFichier(ArbreHuffman arbreHuffman, byte[] bytesADecompiler) {
        // TODO decoder le fichier en remplacant les binaires récolté par l'encode en utf8 fornit dans l'arbre, ensuite retourner une string a l'aide de ce code utf8
        return null;
    }
}