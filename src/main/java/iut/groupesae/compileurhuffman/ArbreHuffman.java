package iut.groupesae.compileurhuffman;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

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
            nouveauNoeud.gauche = feuilleMin1;
            nouveauNoeud.droite = feuilleMin2;

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

    /**
     * Enregistre l'arbre de Huffman dans un fichier spécifié.
     * @param cheminFichier le chemin vers le fichier source
     * @param nomFichierDestination le nom du fichier de destination
     */
    public static void saveArbreHuffman(String cheminFichier, String nomFichierDestination) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichierDestination))) {
            // Appel récursif pour générer le contenu de l'arbre de Huffman et écrire dans le fichier
            ArbreHuffman arbre = new ArbreHuffman(cheminFichier);
            ArbreHuffman.arbreHuffmanRecursive(arbre.getRacine(), "", writer);
        } catch (IOException e) {
            out.println("Erreur lors de l'enregistrement de l'arbre de Huffman dans le fichier.");
        }
    }

    public static void arbreHuffmanRecursive(NoeudHuffman racine, String code, BufferedWriter writer) throws IOException {
        if (racine == null) {
            return;
        }

        // Vérifie si le nœud courant est une feuille (pas de fils gauche et droit)
        if (racine.gauche == null && racine.droite == null) {
            // Écriture de la ligne dans le fichier
            writer.write("codeHuffman = " + code + " ; encode = " + getUTF8Binary(racine.caractere) + " ; symbole = " + racine.caractere + "\n");
            // Affichage dans la console
            out.println("codeHuffman = " + code + " ; encode = " + getUTF8Binary(racine.caractere) + " ; symbole = " + racine.caractere);
        }

        // Parcourt récursivement les nœuds fils gauche et droit
        arbreHuffmanRecursive(racine.gauche, code + "0", writer);
        arbreHuffmanRecursive(racine.droite, code + "1", writer);
    }

    private static String getUTF8Binary(char c) {
        // Il faut crée un tableau de bytes contenant l'encodage UTF-8 du caractère
        byte[] utf8Bytes = String.valueOf(c).getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // Convertit chaque byte en représentation binaire et concatène
        StringBuilder binaryRepresentation = new StringBuilder();
        for (byte b : utf8Bytes) {
            // Convertit le byte en représentation binaire avec padding à gauche de 0
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryRepresentation.append(binary);
        }

        return binaryRepresentation.toString();
    }







    /**
     * Affiche récursivement l'arbre de Huffman.
     * <p>
     * La récursivité est utilisée ici pour parcourir et afficher l'arbre de Huffman de manière efficace
     * et clean. Un arbre de Huffman est une structure de données récursive où chaque nœud peut avoir
     * deux enfants (gauche et droit), qui sont eux-mêmes des arbres de Huffman. En utilisant la récursivité,
     * nous pouvons parcourir chaque nœud de l'arbre de manière récursive, en commençant par la racine, puis
     * en explorant récursivement chaque sous-arbre gauche et droit. Cela nous permet de visiter chaque nœud
     * de l'arbre une fois et d'afficher son contenu.
     * </p>
     * <p>
     * Le paramètre "code" permet de stocker le code binaire associé au chemin parcouru depuis la racine
     * de l'arbre jusqu'au nœud courant. Pendant le parcours récursif de l'arbre de Huffman, chaque fois que nous
     * descendons à un nœud fils gauche, nous ajoutons un "0" au code, et chaque fois que nous descendons à un nœud
     * fils droit, nous ajoutons un "1" au code. Ainsi, le code est mis à jour à chaque étape de la
     * récursion pour refléter le chemin parcouru jusqu'au nœud actuel, permettant ainsi d'associer un code binaire
     * unique à chaque caractère représenté par les feuilles de l'arbre de Huffman.
     * </p>
     * @param racine la racine de l'arbre à parcourir
     * @param code le code binaire associé au nœud courant
     */
}
