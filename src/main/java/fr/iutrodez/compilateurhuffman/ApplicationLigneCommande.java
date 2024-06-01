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

/* TODO LIST
 * TODO : limiter tout le code à 121 colonnes
 * TODO : revoir l'orienté objet
 * TODO : JavaDoc et explication code / revoir variable
 *
 * TODO : test UNITAIRE
 */

/**
 * Classe principale permettant de gérer l'application de compression et de décompression de fichiers
 * en ligne de commande. Elle fournit un point d'entrée pour l'interaction avec l'utilisateur via la console.
 * Elle permet à l'utilisateur de choisir entre la compression et la décompression de fichiers texte.
 * L'application s'exécute jusqu'à ce que l'utilisateur choisisse de quitter.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class ApplicationLigneCommande {

    public static GestionPrompt gestionnaireFichier = new GestionPrompt();

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                ArgumentLigneCommande.gererArgumentsEnLigneCommande(args);
            } else {
                menu();
            }
        } catch (Exception e) {
            out.println("Erreur durant l'exécution de l'application: " + e.getMessage());
        }
    }

    /**
     * Point d'entrée principal de l'application en ligne de commande.
     * Affiche un menu d'options à l'utilisateur et gère les choix de l'utilisateur.
     */
    public static void menu() {
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
                    out.println("Choix invalide. Veuillez saisir un numéro valide.");
                    break;
            }
        }
        afficherSeparateur();
        out.println("Fin de l'application.");
        afficherSeparateur();
    }

    /**
     * TODO
     */
    private static void lancerCompression() {
        String source = gestionnaireFichier.getFichierSource("txt");
        if (source != null) {
            String destination = gestionnaireFichier.getFichierDestination("bin");
            if (destination != null) {
                CompressionHuffman compresser = new CompressionHuffman(source, destination);
                try {
                    compresser.compresserFichier();

                    afficherSeparateur();
                    StatistiquesCompilateur.resumeCompression(source, destination);
                    afficherSeparateur();
                    out.println();
                } catch (IOException erreur) {
                    out.println("Erreur lors de la compression du fichier : " + erreur.getMessage());
                }
            }
        }
    }

    /**
     * TODO
     */
    private static void lancerDecompression() {
        String source = gestionnaireFichier.getFichierSource("bin");
        if (source != null) {
            String destination = gestionnaireFichier.getFichierDestination("txt");
            if (destination != null) {
                DecompressionHuffman decompresser = new DecompressionHuffman(source, destination);
                try {
                    long tempsDecompression = System.currentTimeMillis();
                    decompresser.decompresserFichier();

                    afficherSeparateur();
                    StatistiquesCompilateur.resumeDecompression(source, destination, tempsDecompression);
                    afficherSeparateur();
                    out.println();
                } catch (IOException erreur) {
                    out.println("Erreur lors de la décompression du fichier : " + erreur.getMessage());
                }
            }
        }
    }


    /**
     * TODO
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
             * Si la chaîne saisie est un entier valide, cette méthode renvoie cet entier.
             * Si la chaîne saisie ne peut pas être interprétée comme un entier valide,
             * une exception de type NumberFormatException est levée et capturée dans le bloc catch.
             */
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