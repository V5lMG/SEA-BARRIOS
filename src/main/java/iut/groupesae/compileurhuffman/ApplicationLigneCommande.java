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
                    afficherSeparateur();
                    afficherArbreHuffman(cheminFichier);

                    afficherSeparateur();
                    out.println("Où voulez-vous enregistrer le fichier ?");
                    String repertoire = scanner.nextLine();
                    out.println("Sous quel nom voulez-vous enregistrer le fichier ?");
                    String nomFichier = scanner.nextLine();
                    afficherSeparateur();

                    ArbreHuffman.saveArbreHuffman(cheminFichier, repertoire + "\\" + nomFichier);
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
        ArbreHuffman.arbreHuffmanRecursive(arbre.getRacine(), "", null);
    }
}
