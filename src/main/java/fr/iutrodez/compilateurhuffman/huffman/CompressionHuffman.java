/*
 * Pas de copyright, ni de droit d'auteur.
 * CompressionHuffman.java                  27/05/2024
 */
package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.outils.OutilsAnalyseFichier;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionChemin;
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
                String cheminFichierSource = promptCheminFichier(args);
                String contenu = OutilsAnalyseFichier.getContenuFichier(cheminFichierSource);
                afficherContenuFichier(contenu, cheminFichierSource);

                continuer = traiterCompressionSiConfirme(cheminFichierSource, contenu);
            } catch (Exception e) {
                out.println("Erreur: " + e.getMessage());
                continuer = ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous recommencer le procéssus de compression ? (oui/non)");
            }
        }
    }

    private static String promptCheminFichier(String[] args) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();
        out.print("""
            Entrez le chemin de votre fichier (URL) :
            Exemple de chemin valide : "dossier1\\dossier2\\monFichier.txt"
                                        dossier1/dossier2/monFichier.txt
            ==>\s""");
        String cheminFichierSource = OutilsGestionChemin.getCheminFichierSource(args);
        OutilsGestionChemin.verifierCheminFichierSourceValide(cheminFichierSource, "txt");
        return cheminFichierSource;
    }

    private static void afficherContenuFichier(String contenu, String cheminFichierSource) {
        ApplicationLigneCommande.afficherSeparateur();
        out.println("Chemin du fichier spécifié : " + cheminFichierSource);

        ApplicationLigneCommande.afficherSeparateur();
        out.println("Contenu du fichier :\n" + contenu);
        ApplicationLigneCommande.afficherSeparateur();
    }

    /**
     * Traite le fichier à compresser en demandant l'emplacement de destination et en générant le fichier compilé.
     *
     * @param cheminFichierSource le chemin du fichier source à compresser
     * @param contenu le contenu du fichier source
     */
    private static boolean traiterCompressionSiConfirme(String cheminFichierSource, String contenu) {
        Scanner scanner = new Scanner(System.in);
        if (ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous sélectionner ce fichier : " + cheminFichierSource + " ? (oui/non)")) {
            traiterFichierACompresser(scanner, cheminFichierSource, contenu);
            return false;
        }
        return ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous recommencer ? (oui/non)");
    }

    private static void traiterFichierACompresser(Scanner scanner, String cheminFichierSource, String contenu) {
        try {
            ApplicationLigneCommande.afficherSeparateur();
            afficherCaracteres(contenu);
            ApplicationLigneCommande.afficherSeparateur();

            String cheminFichierDestination = obtenirCheminDestination();
            String nomFichierCompresse = obtenirNomFichierDestination(scanner, cheminFichierDestination);
            String cheminDossierDestination = cheminFichierDestination + "\\" + nomFichierCompresse;
            String cheminFichierCompresse = cheminDossierDestination + "\\" + nomFichierCompresse + ".bin";

            ApplicationLigneCommande.afficherSeparateur();

            OutilsGestionFichier.creerDossierPourCompilation(cheminDossierDestination);
            OutilsGestionFichier.creerFichierArbreHuffman(cheminFichierSource, cheminDossierDestination);
            OutilsGestionFichier.creerFichierCompresse(cheminFichierCompresse, contenu);

            StatistiquesCompilateur.resumeCompression(cheminFichierCompresse, cheminFichierSource);
            ApplicationLigneCommande.afficherSeparateur();
            out.println("\n\n");
        } catch (IOException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }

    private static String obtenirCheminDestination() {
        Scanner scanner = new Scanner(System.in);
        String cheminDestination;
        boolean continuer = true;

        while (continuer) {
            try {
                out.println("Où voulez-vous enregistrer le fichier une fois compilé ? (Entrez le chemin complet du répertoire)");
                cheminDestination = OutilsGestionChemin.getCheminDestination(scanner);
                return OutilsGestionChemin.enleverGuillemet(cheminDestination);
            } catch (RuntimeException e) {
                out.println("Erreur : " + e.getMessage());
                if (!ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous continuer le procéssus de compression ? (oui/non)")) {
                    continuer = false;
                }
            }
        }
        return null;
    }

    private static String obtenirNomFichierDestination(Scanner scanner, String cheminFichierDestination) {
        String nomFichier = null;
        boolean continuer = true;

        while (continuer) {
            try {
                out.println("Quel nom voulez-vous donner à votre fichier une fois compilé ?");
                nomFichier = OutilsGestionChemin.getNomFichierDestinationUnique(scanner, cheminFichierDestination);
                continuer = false;
            } catch (RuntimeException e) {
                out.println("Erreur : " + e.getMessage() + " Veuillez essayer un autre nom.");
                continuer = ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous rentrer un autre nom ? (oui/non)");
            }
        }
        return nomFichier;
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
}
