package fr.iutrodez.compilateurhuffman;

import fr.iutrodez.compilateurhuffman.huffman.CompressionHuffman;
import fr.iutrodez.compilateurhuffman.huffman.DecompressionHuffman;
import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;

import java.io.IOException;

import static java.lang.System.out;

/**
 * Classe destinée à gérer et interpréter les arguments de ligne de commande pour l'application de compression Huffman.
 */
public class ArgumentLigneCommande {

    /**
     * Traite les arguments de ligne de commande fournis à l'application.
     * Détermine l'action appropriée basée sur le premier argument et appelle les méthodes concernées.
     *
     * @param args les arguments de ligne de commande passés à l'application principale.
     */
    public static void gererArgumentsEnLigneCommande(String[] args) {

        String commande = args[0].toLowerCase();
        ApplicationLigneCommande.afficherSeparateur();

        switch (commande) {
            case "compresser":
                if (args.length < 4) {
                    out.println("Arguments manquants. Utilisation : " +
                            "compresser <chemin_fichier_a_compresser> <nom_dossier_destination> <nom_fichier>");
                } else {
                    compresserFichier(args[1], args[2], args[3]);
                }
                break;
            case "decompresser":
                if (args.length < 4) {
                    out.println("Arguments manquants. Utilisation : " +
                            "decompresser <chemin_fichier_a_decompresser> <nom_dossier_destination> <nom_fichier>");
                } else {
                    decompresserFichier(args[1], args[2], args[3]);
                }
                break;
            case "help":
                afficherAide();
                break;
            default:
                out.println("Commande invalide. Utilisez 'help' pour obtenir des informations sur l'utilisation.");
                break;
        }

        ApplicationLigneCommande.afficherSeparateur();
    }

    private static void compresserFichier(String cheminFichierSource, String cheminDossierDestination, String nomFichierCompresse) {
        out.println("Compression du fichier : " + cheminFichierSource);
        cheminDossierDestination =      cheminDossierDestination + "\\" + nomFichierCompresse;
        String cheminCompletFichierDestination = cheminDossierDestination + "\\" + nomFichierCompresse + ".bin";

        CompressionHuffman compresser = new CompressionHuffman(cheminFichierSource, cheminCompletFichierDestination);
        try {
            compresser.compresserFichier();

            ApplicationLigneCommande.afficherSeparateur();
            StatistiquesCompilateur.resumeCompression(cheminFichierSource, cheminCompletFichierDestination);
            ApplicationLigneCommande.afficherSeparateur();
            out.println();
        } catch (IOException erreur) {
            out.println("Erreur lors de la compression du fichier : " + erreur.getMessage());
        }
    }

    private static void decompresserFichier(String cheminFichierSource, String cheminDossierDestination, String nomFichierDecompresse) {
        out.println("Décompression du fichier : " + cheminFichierSource);
        String cheminCompletFichierDestination = cheminDossierDestination + "\\" + nomFichierDecompresse + ".txt";

        DecompressionHuffman decompresser = new DecompressionHuffman(cheminFichierSource, cheminCompletFichierDestination);
        try {
            long tempsDecompression = System.currentTimeMillis();
            decompresser.decompresserFichier();

            ApplicationLigneCommande.afficherSeparateur();
            StatistiquesCompilateur.resumeDecompression(cheminFichierSource, cheminCompletFichierDestination, tempsDecompression);
            ApplicationLigneCommande.afficherSeparateur();
            out.println();
        } catch (IOException erreur) {
            out.println("Erreur lors de la décompression du fichier : " + erreur.getMessage());
        }
    }

    private static void afficherAide() {
        out.println("Utilisation des commandes de l'application de compression Huffman :");
        out.println("----  compresser <chemin_fichier_a_compresser> <nom_dossier_destination> <nom_fichier>  ----");
        out.println("     Compresse le fichier spécifié et enregistre le résultat dans le dossier de destination sous le nouveau nom de fichier.");
        out.println("----  decompresser <chemin_fichier_a_decompresser> <nom_dossier_destination> <nom_fichier>   ----");
        out.println("     Décompresse le fichier spécifié et enregistre le résultat dans le dossier de destination sous le nouveau nom de fichier.");
        out.println("--------  help   --------");
        out.println("     Affiche ce message d'aide, expliquant comment utiliser toutes les commandes disponibles.");
    }
}
