package iut.groupesae.compileurhuffman;

/**
 * Représente un noeud dans l'arbre de Huffman.
 * Cette classe implémente l'interface Comparable pour permettre la comparaison
 * des noeud de Huffman en fonction de leur fréquence lors de la construction de l'arbre.
 */
public class NoeudHuffman implements Comparable<NoeudHuffman> {
    char caractere;
    double frequence;
    NoeudHuffman gauche, droite;

    /**
     * Constructeur pour créer un noeud Huffman avec un caractère et une fréquence donnés.
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
     * Compare ce nœud à un autre noeud Huffman en fonction de leur fréquence.
     *
     * @param noeud  Le nœud Huffman à comparer.
     * @return       Une valeur négative, zéro ou une valeur positive si ce noeud a une fréquence inférieure, égale ou supérieure à celle de l'autre nœud.
     */
    public int compareTo(NoeudHuffman noeud) {
        return Double.compare(this.frequence, noeud.frequence); // Utiliser Double.compare pour comparer les doubles de manière sûre
    }
}
