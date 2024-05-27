/*
 * Pas de copyright, ni de droit d'auteur.
 * StatistiquesCompilateur.java               27/05/2024
 */
package fr.iutrodez.compilateurhuffman.outils;

import java.io.File;

import static java.lang.System.out;


/**
 * Classe permettant de résumer les statistiques de compression et de décompression.
 * Cette classe fournit des méthodes pour afficher les tailles de fichiers et les taux de compression et de décompression.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class StatistiquesCompilateur {
    /**
     * Affiche un résumé de la compression, incluant les tailles du fichier original et compressé, ainsi que le taux de compression.
     *
     * @param cheminFichierCompresse Le chemin du fichier compressé.
     * @param cheminFichierSource Le chemin du fichier original.
     */
    public static void resumeCompression(String cheminFichierCompresse, String cheminFichierSource) {
        File fichierOriginal = new File(cheminFichierSource);
        File fichierCompile = new File(cheminFichierCompresse);
        long tailleFichierOriginal = fichierOriginal.length();
        long tailleFichierCompile = fichierCompile.length();

        out.println("Taille du fichier original : " + tailleFichierOriginal + " octets");
        out.println("Taille du fichier compressé : " + tailleFichierCompile + " octets");

        double tauxCompression = ((double) tailleFichierCompile / tailleFichierOriginal) * 100;
        out.printf("Taux de compression : %.2f %%\n", tauxCompression);
    }

    /**
     * Affiche un résumé de la décompression, incluant les tailles du fichier compressé et décompressé,
     * le taux de décompression et le temps pris pour la décompression.
     *
     * @param cheminFichierDecompresse Le chemin du fichier décompressé.
     * @param cheminFichierADecompresser Le chemin du fichier compressé à décompresser.
     * @param tempsDeCompression Le temps initial en millisecondes avant la décompression pour calculer le temps total de décompression.
     */
    public static void resumeDecompression(String cheminFichierDecompresse, String cheminFichierADecompresser, long tempsDeCompression) {
        File fichierOriginal = new File(cheminFichierADecompresser);
        File fichierDecompile = new File(cheminFichierDecompresse);
        long tailleFichierOriginal = fichierOriginal.length();
        long tailleFichierDecompresse = fichierDecompile.length();

        double tauxDecompression = ((double) tailleFichierOriginal / tailleFichierDecompresse) * 100.0;

        out.println("Taille du fichier compressé : " + tailleFichierOriginal + " octets");
        out.println("Taille du fichier décompressé : " + tailleFichierDecompresse + " octets");
        out.printf("Taux de décompression : %.2f %%\n", tauxDecompression);

        long tempsDeDecompression = System.currentTimeMillis() - tempsDeCompression;
        out.println("Temps de décompression : " + tempsDeDecompression + " millisecondes");
    }
}