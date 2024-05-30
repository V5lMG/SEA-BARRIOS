package fr.iutrodez.compilateurhuffman.outils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class OutilsGestionChemin {
    /**
     * Obtient le chemin du fichier à partir des arguments de la ligne de commande ou de l'entrée utilisateur.
     *
     * @param args Les arguments de la ligne de commande.
     * @return Le chemin du fichier nettoyé.
     * @throws IOException En cas d'erreur de lecture de la ligne de commande.
     */
    public static String getCheminFichierSource(String[] args) throws IOException {
        if (args.length > 0) {
            return enleverGuillemet(args[0]);
        } else {
            BufferedReader lecteurConsole = new BufferedReader(new InputStreamReader(System.in));
            return enleverGuillemet(lecteurConsole.readLine());
        }
    }

    /**
     * Nettoie le chemin du fichier en supprimant les guillemets entourant le chemin si présents.
     *
     * @param chemin Le chemin du fichier.
     * @return Le chemin du fichier nettoyé.
     */
    public static String enleverGuillemet(String chemin) {
        if (chemin.startsWith("\"") && chemin.endsWith("\"")) {
            return chemin.substring(1, chemin.length() - 1);
        }
        return chemin;
    }

    /**
     * Obtient un nom de fichier unique dans le répertoire de destination pour éviter les conflits.
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @param cheminFichierDestination Le chemin du répertoire de destination.
     * @return Un nom de fichier unique.
     * @throws RuntimeException Si un fichier ou dossier avec le même nom existe déjà.
     */
    public static String getNomFichierDestinationUnique(Scanner scanner, String cheminFichierDestination) {
        String nomFichier = getNomFichierDestination(scanner);
        File fichier = new File(cheminFichierDestination, nomFichier);
        if (fichier.exists()) {
            throw new RuntimeException("Un fichier avec le nom \"" + nomFichier + "\" existe déjà dans le répertoire. Veuillez choisir un autre nom.");
        }
        return nomFichier;
    }

    /**
     * Permet à l'utilisateur de saisir un nom de fichier pour le fichier décompressé.
     * Vérifie la validité du nom de fichier en s'assurant qu'il ne contient pas de caractères spéciaux.
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @return Un nom de fichier valide.
     * @throws RuntimeException Si le nom de fichier est invalide.
     */
    private static String getNomFichierDestination(Scanner scanner) {
        String nomFichier = scanner.nextLine();
        if (!nomFichier.matches("[a-zA-Z0-9._-]+")) {
            throw new RuntimeException("Nom de fichier invalide. Uniquement les caractères alphanumériques, points, tirets bas et tirets sont autorisés.");
        }
        return nomFichier;
    }

    /**
     * TODO
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @return Le chemin du répertoire de destination validé.
     * @throws RuntimeException Si le répertoire est invalide.
     */
    public static String getCheminDestination(Scanner scanner) {
        String repertoire = scanner.nextLine();
        File repertoireFile = new File(repertoire);
        if (!repertoireFile.exists() || !repertoireFile.isDirectory()) {
            throw new RuntimeException("Le chemin spécifié \"" + repertoire + "\" n'existe pas ou n'est pas un répertoire.");
        } else if (!repertoireFile.canWrite()) {
            throw new RuntimeException("Vous n'avez pas les permissions nécessaires pour écrire dans le répertoire \"" + repertoire + "\".");
        } else if (!repertoireFile.canWrite()) {
            throw new RuntimeException("Vous n'avez pas les permissions d'écriture sur le répertoire \"" + repertoire + "\".");
        }
        return repertoire;
    }

    public static void verifierCheminFichierSourceValide(String cheminFichierSource, String type) throws IOException {
        if (type.equals("txt")) {
            if (cheminFichierSource.isEmpty()) {
                throw new IOException("Le chemin du fichier à compresser ne peut pas être vide.");
            }
            if (!cheminFichierSource.endsWith(".txt")) {
                throw new IOException("Veuillez spécifier un fichier .txt.");
            }
        } else if (type.equals("bin")) {
            if (cheminFichierSource.isEmpty()) {
                throw new IOException("Le chemin du fichier à décompresser ne peut pas être vide.");
            }
            if (!cheminFichierSource.endsWith(".bin")) {
                throw new IOException("Le fichier spécifié doit être un fichier .bin pour la décompression.");
            }
        }
    }
}
