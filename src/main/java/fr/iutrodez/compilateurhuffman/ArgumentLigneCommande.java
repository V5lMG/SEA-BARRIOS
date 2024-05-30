package fr.iutrodez.compilateurhuffman;

import fr.iutrodez.compilateurhuffman.outils.OutilsAnalyseFichier;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;
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
            case "construire":
                if (args.length < 3) {
                    out.println("Arguments manquants. Utilisation : construire <chemin_fichier_base_arbre> <nom_dossier_destination>");
                } else {
                    construireArbreHuffman(args[1], args[2]);
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
        try {
            cheminDossierDestination =      cheminDossierDestination + "\\" + nomFichierCompresse;
            String cheminFichierCompresse = cheminDossierDestination + "\\" + nomFichierCompresse + ".bin";

            String contenu = OutilsAnalyseFichier.getContenuFichier(cheminFichierSource);

            OutilsGestionFichier.creerDossierPourCompilation(cheminDossierDestination);
            OutilsGestionFichier.creerFichierArbreHuffman(cheminFichierSource, cheminDossierDestination);
            OutilsGestionFichier.creerFichierCompresse(cheminFichierCompresse, contenu);

            ApplicationLigneCommande.afficherSeparateur();
            StatistiquesCompilateur.resumeCompression(cheminFichierCompresse, cheminFichierSource);
        } catch (IOException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }

    private static void decompresserFichier(String cheminFichierADecompresser, String cheminDossierDestination, String nomFichierDecompresse) {
        out.println("Décompression du fichier : " + cheminFichierADecompresser);
        try {
            String cheminCompletFichierDecompresse = cheminDossierDestination + "\\" + nomFichierDecompresse + ".txt";
            OutilsGestionFichier.creerFichierDecompresse(cheminCompletFichierDecompresse, cheminFichierADecompresser);

            ApplicationLigneCommande.afficherSeparateur();
            long tempsDebut = System.currentTimeMillis();
            StatistiquesCompilateur.resumeDecompression(cheminCompletFichierDecompresse, cheminFichierADecompresser, tempsDebut);
        } catch (IOException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }

    private static void construireArbreHuffman(String cheminFichierPourArbre, String cheminDossierDestination) {
        out.println("Création de l'arbre : " + cheminDossierDestination + "\\arbreHuffman.txt");
        try {
            OutilsGestionFichier.creerFichierArbreHuffman(cheminFichierPourArbre, cheminDossierDestination);
        } catch (IOException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }

    private static void afficherAide() {
        out.println("Utilisation des commandes de l'application de compression Huffman :");
        out.println("----  compresser <chemin_fichier_a_compresser> <nom_dossier_destination> <nom_fichier>  ----");
        out.println("     Compresse le fichier spécifié et enregistre le résultat dans le dossier de destination sous le nouveau nom de fichier.");
        out.println("----  decompresser <chemin_fichier_a_decompresser> <nom_dossier_destination> <nom_fichier>   ----");
        out.println("     Décompresse le fichier spécifié et enregistre le résultat dans le dossier de destination sous le nouveau nom de fichier.");
        out.println("----  construire <chemin_fichier_base_arbre> <nom_dossier_destination>   ----");
        out.println("     Construit l'arbre de Huffman à partir du fichier spécifié et enregistre la structure dans le dossier de destination.");
        out.println("--------  help   --------");
        out.println("     Affiche ce message d'aide, expliquant comment utiliser toutes les commandes disponibles.");
    }

}
