/*
 * Pas de copyright, ni de droit d'auteur.
 * DecompressionHuffman.java                  27/05/2024
 */
package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionChemin;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;
import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;

import java.util.Scanner;
import java.io.IOException;

import static java.lang.System.out;

/**
 * Cette classe gère le processus de décompression des fichiers compressés avec l'algorithme de Huffman.
 * Elle permet à l'utilisateur de spécifier le fichier à décompresser, ainsi que l'emplacement et le nom du fichier décompressé.
 * Les méthodes de cette classe sont conçues pour être utilisées dans une application de ligne
 * de commande, offrant ainsi une interface simple pour les utilisateurs.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class DecompressionHuffman {

    /**
     * Demande à l'utilisateur de spécifier le fichier à décompresser et gère le processus de décompression.
     * @param args les arguments de la ligne de commande (optionnels)
     */
    public static void demanderFichierADecompresser(String[] args) {
        boolean continuer = true;
        while (continuer) {
            try {
                String cheminFichierADecompresser = promptCheminFichierADecompresser(args);

                if (ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous sélectionner ce fichier pour la décompression ? (oui/non)")) {
                    traiterFichierADecompresser(cheminFichierADecompresser);
                }
                continuer = ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous recommencer le processus de décompression ? (oui/non)");
                ApplicationLigneCommande.afficherSeparateur();
            } catch (IOException e) {
                out.println("Erreur: " + e.getMessage());
                continuer = ApplicationLigneCommande.demanderOuiOuNon("Une erreur est survenue. Voulez-vous recommencer ? (oui/non)");
            }
        }
    }

    private static String promptCheminFichierADecompresser(String[] args) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();
        out.print("""
            Entrez le chemin du fichier à décompresser :
            Exemple de chemin valide : "dossier1\\dossier2\\monFichier.bin"
                                        dossier1/dossier2/monFichier.bin
            ==>\s""");
        String cheminFichierSource = OutilsGestionChemin.getCheminFichierSource(args);
        ApplicationLigneCommande.afficherSeparateur();
        out.println("Chemin du fichier spécifié : " + cheminFichierSource);
        ApplicationLigneCommande.afficherSeparateur();

        OutilsGestionChemin.verifierCheminFichierSourceValide(cheminFichierSource, "bin");
        return cheminFichierSource;
    }

    /**
     * Traite le fichier à décompresser en demandant l'emplacement de destination et en générant le fichier décompressé.
     *
     * @param cheminFichierADecompresser le chemin du fichier à décompresser
     * @throws IOException si une erreur survient lors de la manipulation des fichiers
     */
    private static void traiterFichierADecompresser(String cheminFichierADecompresser) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ApplicationLigneCommande.afficherSeparateur();

        String cheminFichierDestination = obtenirCheminDestination();
        if (cheminFichierDestination == null) {
            return;
        }

        String nomFichierDecompresse = obtenirNomFichierDestination(scanner, cheminFichierDestination);
        if (nomFichierDecompresse == null) {
            return;
        }

        ApplicationLigneCommande.afficherSeparateur();
        String cheminCompletFichierDecompresse = cheminFichierDestination + "\\" + nomFichierDecompresse + ".txt";
        OutilsGestionFichier.creerFichierDecompresse(cheminCompletFichierDecompresse, cheminFichierADecompresser);

        ApplicationLigneCommande.afficherSeparateur();
        long tempsDebut = System.currentTimeMillis();
        StatistiquesCompilateur.resumeDecompression(cheminCompletFichierDecompresse, cheminFichierADecompresser, tempsDebut);
        ApplicationLigneCommande.afficherSeparateur();

    }

    private static String obtenirCheminDestination() {
        Scanner scanner = new Scanner(System.in);
        String cheminDestination;
        boolean continuer = true;

        while (continuer) {
            try {
                out.println("Où voulez-vous enregistrer le fichier une fois décompilé ? (Entrez le chemin complet du répertoire)");
                cheminDestination = OutilsGestionChemin.getCheminDestination(scanner);
                return OutilsGestionChemin.enleverGuillemet(cheminDestination);
            } catch (RuntimeException e) {
                out.println("Erreur : " + e.getMessage());
                if (!ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous continuer ? (oui/non)")) {
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
                out.println("Quel nom voulez-vous donner à votre fichier une fois décompilé ?");
                nomFichier = OutilsGestionChemin.getNomFichierDestinationUnique(scanner, cheminFichierDestination);
                continuer = false;
            } catch (RuntimeException e) {
                out.println("Erreur : " + e.getMessage() + " Veuillez essayer un autre nom.");
                if (!ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous proposer un nouveau nom ? (oui/non)")) {
                    out.println("Annulation de la décompression.");
                    ApplicationLigneCommande.afficherSeparateur();
                }
            }
        }
        return nomFichier;
    }
}