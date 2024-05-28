/*
 * Pas de copyright, ni de droit d'auteur.
 * ApplicationLigneCommande.java               27/05/2024
 */
package fr.iutrodez.compilateurhuffman;

import fr.iutrodez.compilateurhuffman.huffman.CompressionHuffman;
import fr.iutrodez.compilateurhuffman.huffman.DecompressionHuffman;

import static java.lang.System.out;

/* TODO LIST
 * TODO : gérer les arguments en lignes de commandes
 * TODO : faire une liste de toutes les méthodes du JDK utilisé et les expliquer
 * TODO : modifier les imports OutilsGestionFichier
 * TODO : description de Systeme.console().readLine()
 * TODO : enlever les map
 *
 * TODO : améliorer la gestion des exceptions
 * TODO : Faire une classe à part pour la récéption des erreurs
 *
 * TODO : limiter tout le code à 121 colonnes
 * TODO : refaire les statistiques de la décompression
 *
 * TODO : Problème avec inversion des 1 et 0 dans l'algo d'Huffman
 * TODO : Empêcher les caractères non UTF8
 * TODO : JavaDoc et Refactoring des trois Outils
 * TODO : vérifier la validité d'un chemin avant la fin des questions
 *
 * TODO : test
 *
 * TODO : bloqué réponse vide
 * TODO : ne pas compiler un fichier vide
 * TODO : enlever le caractère en trop a la fin de la décompression
 * TODO : trier correctement l'arbre d'Huffman
 * TODO : encoder Huffman sur 4 bits et pas 3 (ajout de zéro a la fin)
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

        boolean continuer = true;
        while (continuer) {
            afficherSeparateur();
            out.println("Choisissez une action :");
            out.println("1. Compression de fichier");
            out.println("2. Décompression de fichier");
            out.println("3. Quitter l'application");
            afficherSeparateur();

            int choix = demanderChoixUtilisateur();

            if (choix == 1) {
                CompressionHuffman.demanderFichierACompresser(args);
            } else if (choix == 2) {
                DecompressionHuffman.demanderFichierADecompresser(args);
            } else if (choix == 3) {
                continuer = false;
            } else {
                out.println("Choix invalide. Veuillez saisir un numéro valide.");
            }
        }

        afficherSeparateur();
        out.println("Fin de l'application.");
        afficherSeparateur();
    }

    /**
     * Demande à l'utilisateur de saisir un choix valide.
     *
     * @return le choix de l'utilisateur.
     */
    private static int demanderChoixUtilisateur() {
        out.print("Votre choix : ");
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