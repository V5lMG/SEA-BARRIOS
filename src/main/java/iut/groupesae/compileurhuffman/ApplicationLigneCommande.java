package iut.groupesae.compileurhuffman;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Cette classe représente l'application en ligne de commande pour utiliser le compilateur Huffman.
 */
public class ApplicationLigneCommande {

    /**
     * Méthode principale de l'application. Permet de lancer toutes les méthodes pour le fonctionnement en ligne de cmd.
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        afficherSeparateur();
        out.println("L'application est lancée.");
        afficherSeparateur();

        boolean continuer = true;
        while (continuer) {
            String cheminFichier;
            try {
                out.print("""
                      Entrez le chemin de votre fichier (URL) :
                      Exemple de chemin valide : "dossier1\\dossier2\\monFichier.txt"
                                                  dossier1/dossier2/monFichier.txt
                      ==>\s""");

                cheminFichier = GestionFichier.getCheminFichier(args);

                afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichier);
                afficherSeparateur();

                String contenu = GestionFichier.getContenuFichier(cheminFichier);
                out.println("Contenu du fichier :\n" + contenu);

                afficherSeparateur();
                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    afficherCaracteres(contenu);
                    afficherArbreHuffman(cheminFichier);
                    continuer = false;
                } else {
                    continuer = demanderReessayer();
                }

            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier !");
                continuer = demanderReessayer();
            }
        }

        afficherSeparateur();
        out.println("Fin de l'application.");
        afficherSeparateur();
    }

    /**
     * Demande à l'utilisateur s'il veut réessayer.
     * @return true si l'utilisateur veut réessayer, sinon false
     */
    private static boolean demanderReessayer() {
        out.println("Voulez-vous réessayer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }

    /**
     * Affiche un séparateur visuel.
     */
    private static void afficherSeparateur() {
        out.println("------------------------------------------------------------");
    }

    /**
     * Affiche les caractères et leurs fréquences pour le fichier spécifié.
     * @param contenu le contenu à analyser
     */
    private static void afficherCaracteres(String contenu) {
        int[][] occurrences = GestionFichier.ordonnerOccurrences(contenu);
        Map<Character, Double> frequenceMap = GestionFichier.getFrequences(contenu);

        for (int[] occurrence : occurrences) {
            char caractere = (char) occurrence[0];
            int nbOccurrences = occurrence[1];
            double frequence = frequenceMap.get(caractere);
            System.out.println("Caractère: " + caractere + ", Occurrences: " + nbOccurrences + ", Fréquence: " + (frequence * 100) + "%");
        }
    }

    /**
     * Affiche l'arbre de Huffman pour le fichier spécifié.
     * @param cheminFichier le chemin vers le fichier
     * @throws IOException en cas d'erreur de lecture du fichier
     */
    private static void afficherArbreHuffman(String cheminFichier) throws IOException {
        ArbreHuffman arbre = new ArbreHuffman(cheminFichier);
        System.out.println("Affichage de l'arbre de Huffman :");
        arbreHuffmanRecursive(arbre.getRacine(), "");
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
    private static void arbreHuffmanRecursive(NoeudHuffman racine, String code) {
        if (racine == null) {
            return;
        }

        // Vérifie si le nœud courant est une feuille (pas de fils gauche et droit)
        if (racine.gauche == null && racine.droite == null) {
            System.out.println(racine.caractere + " : " + code);
        }

        // Parcourt récursivement les nœuds fils gauche et droit
        arbreHuffmanRecursive(racine.gauche, code + "0");
        arbreHuffmanRecursive(racine.droite, code + "1");
    }
}
