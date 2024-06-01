package fr.iutrodez.compilateurhuffman.outils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GestionUrl {
    /**
     * TODO
     *
     * @param chemin Le chemin du fichier.
     * @return Le chemin du fichier nettoyé.
     */
    public String enleverGuillemet(String chemin) {
        if (chemin.startsWith("\"") && chemin.endsWith("\"")) {
            return chemin.substring(1, chemin.length() - 1);
        } else if (chemin.startsWith("\"")) {
            return chemin.substring(1);
        } else if (chemin.endsWith("\"")) {
            return chemin.substring(0, chemin.length() - 1);
        }
        return chemin;
    }

    /**
     * TODO
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @param cheminFichierDestination Le chemin du répertoire de destination.
     * @return Un nom de fichier unique.
     * @throws RuntimeException Si un fichier ou dossier avec le même nom existe déjà.
     */
    public String getNomFichierDestinationUnique(Scanner scanner, String cheminFichierDestination) {
        String nomFichier = getNomFichierDestination(scanner);
        File fichier = new File(cheminFichierDestination, nomFichier);
        if (fichier.exists()) {
            throw new RuntimeException("Un fichier avec le nom \"" + nomFichier + "\" existe déjà dans le répertoire. Veuillez choisir un autre nom.");
        }
        return nomFichier;
    }

    /**
     * TODO
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @return Un nom de fichier valide.
     * @throws RuntimeException Si le nom de fichier est invalide.
     */
    private String getNomFichierDestination(Scanner scanner) {
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
    public String getCheminDestination(Scanner scanner) {
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

    /**
     * TODO
     *
     * @param cheminFichierSource
     * @param type
     * @throws IOException
     */
    public void verifierCheminFichierSourceValide(String cheminFichierSource, String type) throws IOException {
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
