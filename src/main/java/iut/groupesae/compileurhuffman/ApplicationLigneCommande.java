package iut.groupesae.compileurhuffman;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

public class ApplicationLigneCommande {

    public static void main(String[] args) {
        afficherSeparateur();
        out.println("L'application est lancée.");
        afficherSeparateur();

        boolean continuer = true;
        while (continuer) {
            String cheminFichier = null;
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

    private static boolean demanderReessayer() {
        out.println("Voulez-vous réessayer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }

    private static void afficherSeparateur() {
        out.println("------------------------------------------------------------");
    }

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
}
