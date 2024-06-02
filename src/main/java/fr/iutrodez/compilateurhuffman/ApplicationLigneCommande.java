/*
 * Pas de copyright, ni de droit d'auteur.
 * ApplicationLigneCommande.java               27/05/2024
 */
package fr.iutrodez.compilateurhuffman;

import fr.iutrodez.compilateurhuffman.huffman.CompressionHuffman;
import fr.iutrodez.compilateurhuffman.huffman.DecompressionHuffman;
import fr.iutrodez.compilateurhuffman.outils.GestionPrompt;
import fr.iutrodez.compilateurhuffman.outils.StatistiquesCompilateur;

import java.io.IOException;

import static java.lang.System.out;

/**
 * Classe principale permettant de gérer l'application de compression et de
 * décompression de fichiers en ligne de commande.
 * Elle fournit un point d'entrée pour l'interaction avec l'utilisateur
 * via la console. Elle permet à l'utilisateur de choisir entre la compression
 * et la décompression de fichiers texte.
 * L'application s'exécute jusqu'à ce que l'utilisateur choisisse de quitter.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class ApplicationLigneCommande {

    /**
     * Instance de {@link GestionPrompt} utilisée pour gérer
     * les opérations sur les fichiers.
     */
    public static GestionPrompt gestionnaireFichier = new GestionPrompt();

    /**
     * Point d'entrée principal de l'application de ligne de commande.
     * Cette méthode évalue les arguments de la ligne de commande
     * pour déterminer le mode de fonctionnement.
     * Si des arguments sont passés, elle tente de les traiter pour
     * des opérations de commande spécifiques.
     * Sinon, elle affiche un menu interactif pour permettre
     * à l'utilisateur de choisir entre différentes actions.
     *
     * @param args Les arguments de la ligne de commande (optionnel).
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                ArgumentLigneCommande.gererArgumentsEnLigneCommande(args);
            } else {
                menu();
            }
        } catch (Exception e) {
            out.println("Erreur durant l'exécution de l'application: "
                        + e.getMessage());
        }
    }

    /**
     * Affiche un menu interactif et permet à l'utilisateur de choisir
     * entre différentes options de traitement :
     * Compression de fichiers,
     * décompression de fichiers,
     * ou fermeture l'application.
     * Chaque choix est traité par appel de
     * la méthode correspondante (compression ou décompression).
     * Des validations et des messages d'erreur sont présentés
     * si un choix invalide est effectué.
     */
    private static void menu() {
        afficherSeparateur();
        out.println("L'application est lancée.");

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = demanderChoixUtilisateur();

            switch (choix) {
                case 1:
                    lancerCompression();
                    break;
                case 2:
                    lancerDecompression();
                    break;
                case 3:
                    continuer = false;
                    break;
                default:
                    out.println("Choix invalide. " +
                                "Veuillez saisir un numéro valide.");
                    break;
            }
        }
        afficherSeparateur();
        out.println("Fin de l'application.");
        afficherSeparateur();
    }

    /**
     * Lance le processus de compression d'un fichier texte en utilisant
     * l'algorithme de Huffman.
     * Le fichier source doit être un fichier texte (.txt) et
     * la destination sera un fichier binaire (.bin).
     * Affiche des informations de compression,
     * y compris des statistiques de réduction de taille.
     * Gère également les erreurs de fichier et
     * affiche les messages appropriés en cas d'erreur.
     */
    private static void lancerCompression() {
        String source = gestionnaireFichier.getFichierSource("txt");
        if (source == null) {
            return;
        }

        String destination = gestionnaireFichier.getFichierDestination("bin");
        if (destination == null) {
            return;
        }

        CompressionHuffman compresser =
                new CompressionHuffman(source, destination);

        try {
            compresser.compresserFichier();
            afficherSeparateur();
            StatistiquesCompilateur.resumeCompression(source, destination);
        } catch (IOException erreur) {
            out.println("Erreur lors de la compression du fichier : "
                        + erreur.getMessage());
        }
    }


    /**
     * Lance le processus de décompression d'un fichier binaire
     * compressé en utilisant l'algorithme de Huffman.
     * Le fichier source doit être un fichier binaire (.bin) et
     * la destination sera un fichier texte (.txt).
     * Affiche le résumé de la décompression,
     * y compris le temps pris pour décompresser.
     * Gère les erreurs de fichier et
     * affiche les messages appropriés en cas d'erreur.
     */
    private static void lancerDecompression() {
        String source = gestionnaireFichier.getFichierSource("bin");
        if (source == null) {
            return;
        }

        String destination = gestionnaireFichier.getFichierDestination("txt");
        if (destination == null) {
            return;
        }

        DecompressionHuffman decompresser =
                new DecompressionHuffman(source, destination);

        try {
            long tempsDecompression = System.currentTimeMillis();
            decompresser.decompresserFichier();

            afficherSeparateur();
            StatistiquesCompilateur.resumeDecompression(source,
                                                        destination,
                                                        tempsDecompression);
            afficherSeparateur();
        } catch (IOException erreur) {
            out.println("Erreur lors de la décompression du fichier : "
                        + erreur.getMessage());
        }
    }

    /**
     * Affiche un menu à l'utilisateur lui permettant de choisir entre
     * une compression de fichier,
     * une décompression de fichier
     * ou quitter l'application.
     */
    private static void afficherMenu() {
        afficherSeparateur();
        out.println("Choisissez une action :");
        out.println("1. Compression de fichier");
        out.println("2. Décompression de fichier");
        out.println("3. Quitter l'application");
        afficherSeparateur();
    }

    /**
     * Demande à l'utilisateur de saisir un choix valide.
     * Répète la demande jusqu'à ce qu'un choix valide soit saisi.
     * Gère les entrées non valides en affichant un message d'erreur
     * et en redemandant.
     *
     * @return le choix de l'utilisateur.
     */
    private static int demanderChoixUtilisateur() {
        out.print("Votre choix : ");
        /*
         * Lit une ligne de texte depuis la console.
         * Cette méthode attend que l'utilisateur saisisse une ligne de texte
         * et appuie sur la touche "Entrée". La ligne saisie est ensuite
         * renvoyée sous forme de chaîne de caractères.
         */
        String input = System.console().readLine();
        try {
            /*
             * Convertit la chaîne saisie par l'utilisateur en un entier.
             * Si la chaîne saisie est un entier valide,
             * cette méthode renvoie cet entier.
             * Si la chaîne saisie ne peut pas être interprétée
             * comme un entier valide, une exception
             * de type NumberFormatException est levée et
             * est capturée dans le bloc catch.
             */
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            out.println("Veuillez saisir un numéro valide.");
            return demanderChoixUtilisateur();
        }
    }

    /**
     * Affiche un séparateur horizontal pour
     * structurer visuellement l'interface en console.
     * Utilisé pour séparer des sections de texte dans les affichages console.
     */
    public static void afficherSeparateur() {
        out.println("--------------------------------------------------------");
    }
}