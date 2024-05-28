/*
 * Pas de copyright, ni de droit d'auteur.
 * DecompressionHuffman.java                  27/05/2024
 */
package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
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
                out.print("""
                      Entrez le chemin du fichier à décompresser (URL) :
                      Exemple de chemin valide : "dossier1\\dossier2\\monFichier.bin"
                                                  dossier1/dossier2/monFichier.bin
                      ==>\s""");
                String cheminFichierADecompresser = OutilsGestionFichier.getCheminFichierSource(args);

                if (cheminFichierADecompresser.isEmpty()) {
                    throw new IOException("\nLe chemin du fichier à décompresser ne peut pas être vide.");
                }

                if (!cheminFichierADecompresser.endsWith(".bin")) {
                    throw new IOException("\nLe fichier à décompresser ne peut pas être de ce format. " +
                                          "Veuillez spécifier un fichier .bin.");
                }

                ApplicationLigneCommande.afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichierADecompresser);
                ApplicationLigneCommande.afficherSeparateur();

                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    traiterFichierADecompresser(scanner, cheminFichierADecompresser);
                } else if (!choix.equalsIgnoreCase("oui") || !choix.equalsIgnoreCase("non")){
                    out.println("Réponse invalide. Veuillez répondre par 'oui' ou 'non'.");
                }
                continuer = demanderRecommencer();
            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier à décompresser : " + e.getMessage());
                continuer = demanderRecommencer();
            }
        }
    }

    /**
     * Traite le fichier à décompresser en demandant l'emplacement de destination et en générant le fichier décompressé.
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     * @param cheminFichierADecompresser le chemin du fichier à décompresser
     * @throws IOException si une erreur survient lors de la manipulation des fichiers
     */
    private static void traiterFichierADecompresser(Scanner scanner, String cheminFichierADecompresser) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();

        out.println("Où voulez-vous enregistrer le fichier une fois décompressé ? (Entrez le chemin complet du répertoire)");
        String cheminDestination = OutilsGestionFichier.nettoyerChemin(OutilsGestionFichier.getCheminDestination(scanner));
        OutilsGestionFichier.verifierRepertoireValide(cheminDestination);

        out.println("Sous quel nom voulez-vous enregistrer le fichier une fois décompressé ? (Entrez le nom du fichier)");
        String nomFichierDecomprime = OutilsGestionFichier.getNomFichierDestinationUnique(scanner, cheminDestination);
        String cheminFichierDecompresse = cheminDestination + "\\" +  nomFichierDecomprime + ".txt";

        ApplicationLigneCommande.afficherSeparateur();
        long tempsDeCompression = System.currentTimeMillis();
        OutilsGestionFichier.creerFichierDecompresse(cheminFichierDecompresse, cheminFichierADecompresser);
        StatistiquesCompilateur.resumeDecompression(cheminFichierDecompresse, cheminFichierADecompresser, tempsDeCompression);
        ApplicationLigneCommande.afficherSeparateur();

    }

    /**
     * Demande à l'utilisateur s'il veut recommencer le processus de décompression.
     * @return true si l'utilisateur veut recommencer, sinon false
     */
    private static boolean demanderRecommencer() {
        out.println("Voulez-vous recommencer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }
}