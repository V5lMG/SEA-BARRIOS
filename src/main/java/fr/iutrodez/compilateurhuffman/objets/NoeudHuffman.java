/*
 * Pas de copyright, ni de droit d'auteur.
 * NoeudHuffman.java                       27/05/2024
 */
package fr.iutrodez.compilateurhuffman.objets;

/**
 * Représente un nœud dans un arbre de Huffman utilisé pour
 * la compression et la décompression de données.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class NoeudHuffman {
    /** Fréquence du caractère */
    private final double frequence;

    /** Le fils gauche de ce nœud Huffman. */
    private NoeudHuffman gauche;

    /** Le fils droit de ce nœud Huffman. */
    private NoeudHuffman droite;

    /** Le code de Huffman associé à ce nœud. */
    private String codeHuffman;

    /** Le caractère représenté par ce nœud. */
    private final char caractere;

    /**
     * Crée un nœud de Huffman avec un caractère et sa fréquence d'apparition.
     *
     * @param caractere le caractère.
     * @param frequence la fréquence d'apparition du caractère.
     */
    public NoeudHuffman(char caractere, double frequence) {
        this.caractere = caractere;
        this.frequence = frequence;
    }

    /**
     * Nœud de Huffman sans caractère spécifique (utilisé pour les nœuds internes).
     * Ce constructeur est utilisé pour créer des nœuds parents dans l'arbre de Huffman. Contrairement au
     * constructeur qui prend un caractère et une fréquence, celui-ci ne représente pas une feuille mais
     * un nœud interne qui est le résultat de la combinaison des deux nœuds enfants (gauche et droite).
     * Le caractère est défini à '\0' car le nœud n'est pas une feuille et ne correspond à aucun caractère
     * spécifique, tandis que la fréquence est la somme des fréquences des nœuds enfants, reflétant ainsi
     * la fréquence combinée de tous les caractères dans les sous-arbres gauche et droit.
     *
     * @param gauche le nœud gauche, représentant la branche de fréquence inférieure.
     * @param droite le nœud droit, représentant la branche de fréquence supérieure.
     */
    public NoeudHuffman(NoeudHuffman gauche, NoeudHuffman droite) {
        this.caractere = '\0';
        this.frequence = gauche.getFrequence() + droite.getFrequence();
        this.gauche = gauche;
        this.droite = droite;
    }

    /**
     * Accès au caractère de ce nœud.
     *
     * @return Le caractère associé à ce nœud.
     */
    public char getCaractere() {
        return caractere;
    }

    /**
     * Accès à la fréquence de ce nœud.
     *
     * @return La fréquence du nœud.
     */
    public double getFrequence() {
        return frequence;
    }

    /**
     * Accès du nœud gauche.
     *
     * @return Le nœud gauche.
     */
    public NoeudHuffman getGauche() {
        return gauche;
    }

    /**
     * Accès du nœud droit.
     *
     * @return Le nœud droit.
     */
    public NoeudHuffman getDroite() {
        return droite;
    }

    /**
     * Accès du code Huffman.
     *
     * @return Le code Huffman du nœud.
     */
    public String getCodeHuffman() {
        return codeHuffman;
    }

    /**
     * Attribue un code Huffman.
     *
     * @param codeHuffman Le nouveau code à attribuer.
     */
    public void setCodeHuffman(String codeHuffman) {
        this.codeHuffman = codeHuffman;
    }

    /**
     * Vérifie si le nœud est une feuille.
     *
     * @return true si le nœud est une feuille, sinon false.
     */
    public boolean estFeuille() {
        return gauche == null && droite == null;
    }

    /**
     * Renvoie une représentation textuelle du nœud, incluant
     * son code Huffman, son encodage binaire UTF-8,
     * et son caractère.
     *
     * @return La représentation du nœud.
     */
    @Override
    public String toString() {
        return "codeHuffman = " + getCodeHuffman() + " ; encode = " + getUTF8Binary(getCaractere()) + " ; symbole = " + getCaractere();
    }

    /**
     * Convertit un caractère en sa représentation binaire UTF-8. Cette méthode prend un caractère
     * et le convertit en une séquence de bytes selon l'encodage UTF-8, puis transforme ces bytes
     * en une chaîne de caractères représentant leur valeur binaire.
     * Chaque byte est converti en un string de 8 bits, où des zéros sont ajoutés à gauche si nécessaire
     * pour garantir que chaque byte soit représenté par exactement 8 bits. Cette représentation est
     * utile pour des visualisations de bas niveau des données ou pour des opérations de traitement
     * de texte qui nécessitent une connaissance précise de la structure binaire des caractères.
     *
     * @param caractere Le caractère à convertir en binaire.
     * @return La chaîne de caractères de la représentation binaire UTF-8 du caractère.
     */
    public static String getUTF8Binary(char caractere) {
        /*
         * Convertit le caractère en un tableau de bytes selon l'encodage UTF-8.
         * L'encodage UTF-8 permet de représenter tous les caractères du standard Unicode
         * en utilisant un à quatre octets (bytes), ce qui le rend compatible avec les formats
         * de texte multi-langues et est largement utilisé dans les systèmes informatiques modernes.
         * https://docs.oracle.com/javase/8/docs/api/java/nio/charset/StandardCharsets.html#UTF_8
         */
        byte[] utf8Bytes = String.valueOf(caractere).getBytes(java.nio.charset.StandardCharsets.UTF_8);

        StringBuilder binaryRepresentation = new StringBuilder();
        for (byte b : utf8Bytes) {
            /*
             * Utilisation de format pour ajouter des zéros à gauche et garantir une représentation binaire sur 8 bits.
             * String.format("%8s", ...) : Formate la chaîne pour garantir une longueur de 8 caractères,
             * en ajoutant des espaces à gauche si nécessaire.
             *
             * Utilisation de toBinaryString pour convertir un entier en sa représentation binaire.
             * Integer.toBinaryString(int i) : Convertit le nombre entier 'i' en chaîne de caractères représentant
             * sa forme binaire. Lien vers la documentation officielle Java :
             * https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#toBinaryString-int-
             *
             * replace(' ', '0') : Remplace les espaces ajoutés par String.format pour assurer que chaque byte
             * est représenté par exactement 8 bits en binaire.
             *
             * append() : Ajoute la représentation binaire du byte actuel à la fin du StringBuilder,
             * permettant de construire une chaîne complète de tous les bytes représentant le caractère en UTF-8.
             */
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryRepresentation.append(binary);
        }
        return binaryRepresentation.toString();
    }
}
