package iut.groupesae.compileurhuffman;

/**
 * Représente un nœud dans l'arbre de Huffman.
 * Cette classe implémente l'interface Comparable pour permettre la comparaison
 * des nœuds de Huffman en fonction de leur fréquence lors de la construction de l'arbre.
 */
public class NoeudHuffman implements Comparable<NoeudHuffman> {
    public char caractere;
    public double frequence;
    public NoeudHuffman gauche;
    public NoeudHuffman droite;

    /**
     * Constructeur pour créer un nœud Huffman avec un caractère et une fréquence donnés.
     *
     * @param caractere  Le caractère associé au nœud.
     * @param frequence  La fréquence du caractère dans le texte.
     */
    public NoeudHuffman(char caractere, double frequence) {
        this.caractere = caractere;
        this.frequence = frequence;
        gauche = droite = null;
    }

    /**
     * Compare ce nœud à un autre nœud Huffman en fonction de leur fréquence.
     *
     * @param noeud  Le nœud Huffman à comparer.
     * @return       Une valeur négative si ce nœud a une fréquence inférieure, zéro si elle est égale
     *               ou une valeur positive si elle est supérieure à celle de l'autre nœud.
     */
    public int compareTo(NoeudHuffman noeud) {
        // Utiliser Double.compare pour comparer les doubles de manière sûre
        return Double.compare(this.frequence, noeud.frequence);
    }
}
