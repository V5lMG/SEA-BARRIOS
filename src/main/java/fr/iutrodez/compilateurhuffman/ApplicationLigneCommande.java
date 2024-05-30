/*
 * Pas de copyright, ni de droit d'auteur.
 * ApplicationLigneCommande.java               27/05/2024
 */
package fr.iutrodez.compilateurhuffman;

import fr.iutrodez.compilateurhuffman.huffman.CreerArbreHuffman;
import fr.iutrodez.compilateurhuffman.huffman.CompressionHuffman;
import fr.iutrodez.compilateurhuffman.huffman.DecompressionHuffman;

import java.util.Scanner;

import static java.lang.System.out;

/* TODO LIST
 * TODO : limiter tout le code à 121 colonnes
 * TODO : revoir l'orienté objet
 * TODO : JavaDoc et explication code / revoir variable
 *
 * TODO : Empêcher les caractères non UTF8
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
    /**
     * Point d'entrée principal de l'application en ligne de commande.
     * Affiche un menu d'options à l'utilisateur et gère les choix de l'utilisateur.
     *
     * @param args les arguments de la ligne de commande (optionnels)
     */
    public static void main(String[] args) {
        afficherSeparateur();
        out.println("L'application est lancée.");

        if (args.length > 0) {
            ArgumentLigneCommande.gererArgumentsEnLigneCommande(args);
        } else {
            boolean continuer = true;
            while (continuer) {
                afficherMenu();
                int choix = demanderChoixUtilisateur();

                switch (choix) {
                    case 1:
                        CompressionHuffman.demanderFichierACompresser(args);
                        break;
                    case 2:
                        DecompressionHuffman.demanderFichierADecompresser(args);
                        break;
                    case 3:
                        CreerArbreHuffman.creerSeulementArbre(args);
                        break;
                    case 4:
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
    }

    private static void afficherMenu() {
        afficherSeparateur();
        out.println("Choisissez une action :");
        out.println("1. Compression de fichier");
        out.println("2. Décompression de fichier");
        out.println("3. Créer un arbre d'Huffman");
        out.println("4. Quitter l'application");
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

    public static boolean demanderOuiOuNon(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        String reponse = scanner.nextLine().trim().toLowerCase();
        while (!reponse.equals("oui") && !reponse.equals("non")) {
            System.out.println("Veuillez saisir une option valide (oui/non).");
            System.out.println(message);
            reponse = scanner.nextLine().trim().toLowerCase();
        }

        return reponse.equals("oui");
    }

    /**
     * Affiche un séparateur visuel.
     */
    public static void afficherSeparateur() {
        out.println("------------------------------------------------------------");
    }
}