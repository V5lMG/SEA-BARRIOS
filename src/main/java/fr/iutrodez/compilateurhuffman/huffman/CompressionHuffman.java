/*
 * Pas de copyright, ni de droit d'auteur.
 * CompressionHuffman.java                  27/05/2024
 */
package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.outils.OutilsAnalyseFichier;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;
import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;

import java.util.Scanner;
import java.util.Map;
import java.io.IOException;

import static java.lang.System.out;

/**
 * Cette classe gère le processus de compression des fichiers avec l'algorithme de Huffman.
 * Les fonctionnalités principales de cette classe incluent la demande de fichier à compresser
 * à l'utilisateur, le nom du fichier, ainsi que la destination une fois compilé.
 * Les méthodes de cette classe sont conçues pour être utilisées dans une application de ligne
 * de commande, offrant ainsi une interface simple pour les utilisateurs.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class CompressionHuffman {
    /**
     * Demande à l'utilisateur de spécifier le fichier à compresser.
     *
     * @param args les arguments de la ligne de commande (optionnelle)
     */
    public static void demanderFichierACompresser(String[] args) {
        boolean continuer = true;
        while (continuer) {
            try {
                out.print("""
                   Entrez le chemin de votre fichier (URL) :
                   Exemple de chemin valide : "dossier1\\dossier2\\monFichier.txt"
                                               dossier1/dossier2/monFichier.txt
                   ==>\s""");
                String cheminFichierSource = OutilsGestionFichier.getCheminFichierSource(args);

                if (cheminFichierSource.isEmpty()) {
                    throw new IOException("\nLe chemin du fichier à compresser ne peut pas être vide.");
                }

                ApplicationLigneCommande.afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichierSource);
                ApplicationLigneCommande.afficherSeparateur();

                String contenu = OutilsAnalyseFichier.getContenuFichier(cheminFichierSource);
                out.println("Contenu du fichier :\n" + contenu);

                ApplicationLigneCommande.afficherSeparateur();
                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    traiterFichierAcompresser(scanner, cheminFichierSource, contenu);
                }else if (!choix.equalsIgnoreCase("oui") || !choix.equalsIgnoreCase("non")){
                    out.println("Réponse invalide. Veuillez répondre par 'oui' ou 'non'.");
                }
                continuer = demanderRecommencer();
            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier à compresser : " + e.getMessage());
            }
        }
    }

    /**
     * Traite le fichier à compresser en demandant l'emplacement de destination et en générant le fichier compilé.
     *
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     * @param cheminFichierSource le chemin du fichier source à compresser
     * @param contenu le contenu du fichier source
     * @throws IOException si une erreur survient lors de la manipulation des fichiers
     */
    private static void traiterFichierAcompresser(Scanner scanner, String cheminFichierSource, String contenu) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();
        afficherCaracteres(contenu);
        ApplicationLigneCommande.afficherSeparateur();

        out.println("Où voulez-vous enregistrer le fichier une fois compilé ? (Entrez le chemin complet du répertoire)");
        String cheminFichierDestination = OutilsGestionFichier.nettoyerChemin(OutilsGestionFichier.getCheminDestination(scanner));
        OutilsGestionFichier.verifierRepertoireValide(cheminFichierDestination);

        out.println("Sous quel nom voulez-vous enregistrer le fichier une fois compilé ? (Entrez le nom du fichier)");
        String nomFichierCompresse = OutilsGestionFichier.getNomFichierDestinationUnique(scanner, cheminFichierDestination);
        String cheminDossierDestination = cheminFichierDestination + "\\" + nomFichierCompresse;
        String cheminFichierCompresse = cheminDossierDestination + "\\" + nomFichierCompresse + ".bin";

        ApplicationLigneCommande.afficherSeparateur();
        OutilsGestionFichier.creerDossierPourCompilation(cheminDossierDestination);
        OutilsGestionFichier.creerFichierArbreHuffman(cheminFichierSource, cheminDossierDestination);
        OutilsGestionFichier.creerFichierCompresse(cheminFichierCompresse, contenu);

        StatistiquesCompilateur.resumeCompression(cheminFichierCompresse, cheminFichierSource);
        ApplicationLigneCommande.afficherSeparateur();
    }

    /**
     * Affiche les caractères et leurs fréquences pour le fichier spécifié.
     *
     * @param contenu le contenu à analyser
     */
    private static void afficherCaracteres(String contenu) {
        int[][] occurrences = OutilsAnalyseFichier.getOccurrencesOrdonnee(contenu);

        /*
         * La Map<Character, Double> frequenceMap est utilisée pour stocker les fréquences des caractères
         * présents dans le contenu d'un fichier.
         *
         * - Chaque caractère est utilisé comme clé.
         * - Chaque fréquence est utilisée comme valeur associée à la clé correspondante.
         *
         * Par exemple, si le caractère 'a' apparaît dans le contenu avec une fréquence de 0.15,
         * alors il sera stocké dans la map de la manière suivante :
         *   clé : 'a', valeur : 0.15
         *
         * Cela permet de récupérer rapidement la fréquence d'un caractère donné lorsque nécessaire.
         */
        Map<Character, Double> frequenceMap = OutilsAnalyseFichier.getFrequences(contenu);

        for (int[] occurrence : occurrences) {
            char caractere = (char) occurrence[0];
            int nombreOccurrences = occurrence[1];
            double frequence = frequenceMap.get(caractere);
            out.println("Caractère: " + caractere + "; "
                        + "Occurrences: " + nombreOccurrences
                        + "; Fréquence: " + (frequence * 100) + "%");
        }
    }

    /**
     * Demande à l'utilisateur s'il veut réessayer.
     *
     * @return true si l'utilisateur veut réessayer, sinon false
     */
    private static boolean demanderRecommencer() {
        out.println("Voulez-vous recommencer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }
}
