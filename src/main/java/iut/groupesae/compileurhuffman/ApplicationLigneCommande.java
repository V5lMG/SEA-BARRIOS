package iut.groupesae.compileurhuffman;

import static java.lang.System.out;

/**
 * Cette classe représente l'application en ligne de commande pour utiliser le compilateur Huffman.
 * Elle permet de sélectionner un fichier à compiler, de spécifier un emplacement de destination
 * et de générer le fichier compilé et l'arbre de Huffman correspondant.
 * TODO : gérer les arguments en lignes de commandes
 * TODO : empecher les fichiers vide
 * TODO : empecher les fichiers texte
 */
public class ApplicationLigneCommande {
    /**
     * Point d'entrée principal de l'application en ligne de commande.
     * @param args les arguments de la ligne de commande (optionnelle)
     */
    public static void main(String[] args) {
        afficherSeparateur();
        out.println("L'application est lancée.");

        boolean continuer = true;
        while (continuer) {
            afficherSeparateur();
            out.println("Choisissez une action :");
            out.println("1. Compression de fichier");
            out.println("2. Décompression de fichier");
            out.println("3. Quitter l'application");
            afficherSeparateur();

            int choix = demanderChoixUtilisateur();

            switch (choix) {
                case 1:
                    CompressionHuffman.demanderFichierACompiler(args);
                    break;
                case 2:
                    DecompressionHuffman.demanderFichierADecompiler(args);
                    break;
                case 3:
                    continuer = false;
                    break;
                default:
                    out.println("Choix invalide. Veuillez saisir un numéro valide.");
            }
        }

        afficherSeparateur();
        out.println("Fin de l'application.");
        afficherSeparateur();
    }

    /**
     * Demande à l'utilisateur de saisir un choix valide.
     * @return le choix de l'utilisateur.
     */
    private static int demanderChoixUtilisateur() {
        out.print("Votre choix : ");
        String input = System.console().readLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            out.println("Veuillez saisir un numéro valide.");
            return demanderChoixUtilisateur();
        }
    }

    /**
     * Affiche un séparateur visuel.
     */
    public static void afficherSeparateur() {
        out.println("------------------------------------------------------------");
    }
}