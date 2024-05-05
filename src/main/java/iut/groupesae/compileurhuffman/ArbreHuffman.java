package iut.groupesae.compileurhuffman;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente un arbre de Huffman utilisé dans le processus de compression de données.
 */
public class ArbreHuffman {
    private final NoeudHuffman racine;

    /**
     * Constructeur de l'arbre de Huffman à partir du contenu d'un fichier.
     * @param cheminFichier le chemin vers le fichier dont on calcule les fréquences
     * @throws IOException en cas d'erreur d'entrée/sortie lors de la lecture du fichier
     */
    public ArbreHuffman(String cheminFichier) throws IOException {
        String contenu = GestionFichier.getContenuFichier(cheminFichier);
        Map<Character, Double> frequences = GestionFichier.getFrequences(contenu);
        this.racine = construireArbreHuffman(frequences);
    }

    /**
     * Construit l'arbre de Huffman à partir des fréquences des caractères.
     * <p>
     * L'algorithme de construction de l'arbre de Huffman crée un arbre binaire où chaque feuille
     * représente un caractère du fichier et chaque nœud interne représente une fusion de deux caractères
     * ou sous-arbres. L'arbre est construit en sélectionnant itérativement les deux feuilles (ou sous-arbres)
     * avec les fréquences les plus basses, en les fusionnant pour former un nouveau nœud parent dont la fréquence
     * est la somme des fréquences des nœuds fils. Ce processus se répète jusqu'à ce qu'il ne reste plus qu'un
     * seul nœud, qui devient la racine de l'arbre de Huffman.
     * </p>
     * @param frequences les fréquences des caractères dans le fichier
     * @return la racine de l'arbre de Huffman
     */
    private NoeudHuffman construireArbreHuffman(Map<Character, Double> frequences) {
        /*
         * Ici, nous avons utilisé plusieurs méthodes de la JRE Java.
         * Map : Interface utilisée pour stocker une collection de paires clé-valeur.
         * Entry : Interface interne de l'interface Map utilisée pour représenter une paire clé-valeur.
         * getKey() : Méthode de l'interface Map.Entry qui retourne la clé de l'entrée.
         * getValue() : Méthode de l'interface Map.Entry qui retourne la valeur de l'entrée.
         */
        List<NoeudHuffman> feuilles = new ArrayList<>();

        // Création des nœuds et des feuilles pour chaque caractère avec leur fréquence
        for (Map.Entry<Character, Double> entree : frequences.entrySet()) {
            feuilles.add(new NoeudHuffman(entree.getKey(), entree.getValue()));
        }

        // Construction de l'arbre Huffman
        while (feuilles.size() > 1) {
            NoeudHuffman feuilleMin1 = feuilles.get(0);
            NoeudHuffman feuilleMin2 = feuilles.get(1);

            for (int i = 2; i < feuilles.size(); i++) {
                if (feuilles.get(i).frequence < feuilleMin1.frequence) {
                    feuilleMin2 = feuilleMin1;
                    feuilleMin1 = feuilles.get(i);
                } else if (feuilles.get(i).frequence < feuilleMin2.frequence) {
                    feuilleMin2 = feuilles.get(i);
                }
            }

            // Création d'un nouveau nœud avec la somme des fréquences des nœuds d'avant
            NoeudHuffman nouveauNoeud  = new NoeudHuffman('\0', feuilleMin1.frequence + feuilleMin2.frequence);
            nouveauNoeud .gauche = feuilleMin1;
            nouveauNoeud .droite = feuilleMin2;

            feuilles.remove(feuilleMin1);
            feuilles.remove(feuilleMin2);
            feuilles.add(nouveauNoeud );
        }

        return feuilles.get(0);
    }

    /**
     * Getter de la racine de l'arbre de Huffman.
     * @return la racine de l'arbre de Huffman
     */
    public NoeudHuffman getRacine() {
        return this.racine;
    }
}
