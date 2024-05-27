package fr.iutrodez.compilateurhuffman.outils;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire.getBytesADecompresser;
import static fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire.recupererBytesDansArbreHuffman;
import static java.lang.System.out;

/**
 * Cette classe fournit des méthodes pour la gestion des fichiers.
 */
public class OutilsGestionFichier {
    private static ArbreHuffman arbre;

    /**
     * Obtient le chemin du fichier à partir des arguments de la ligne de commande ou de l'entrée utilisateur.
     *
     * @param args Les arguments de la ligne de commande.
     * @return Le chemin du fichier nettoyé.
     * @throws IOException En cas d'erreur de lecture de la ligne de commande.
     */
    public static String getCheminFichierSource(String[] args) throws IOException {
        if (args.length > 0) {
            return nettoyerChemin(args[0]);
        } else {
            BufferedReader lecteurConsole = new BufferedReader(new InputStreamReader(System.in));
            return nettoyerChemin(lecteurConsole.readLine());
        }
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
        while (fichier.exists()) {
            throw new RuntimeException("Un fichier ou dossier avec ce nom existe déjà. Veuillez entrer un nom différent.");
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
        String nomFichier = "";
        boolean isNomFichierValide = false;
        while (!isNomFichierValide) {
            nomFichier = scanner.nextLine();
            if (!nomFichier.isEmpty() && nomFichier.matches("[a-zA-Z0-9._-]+")) { // regex pour les caracteres invalides
                isNomFichierValide = true;
            } else {
                throw new RuntimeException("Nom de fichier invalide. Assurez-vous d'entrer un nom de fichier valide sans caractères spéciaux autres que '.', '_', ou '-'.");
            }
        }
        return nomFichier;
    }

    /**
     * Permet à l'utilisateur de saisir un chemin de répertoire valide pour la décompression.
     * Vérifie que le chemin existe et qu'il s'agit bien d'un répertoire.
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @return Le chemin du répertoire de destination validé.
     * @throws RuntimeException Si le répertoire est invalide.
     */
    public static String getCheminDestination(Scanner scanner) {
        String repertoire = "";
        boolean isRepertoireValide = false;
        while (!isRepertoireValide) {
            repertoire = scanner.nextLine();
            File repertoireFile = new File(repertoire);
            if (repertoireFile.exists() && repertoireFile.isDirectory()) {
                isRepertoireValide = true;
            } else {
                throw new RuntimeException("Répertoire invalide. Assurez-vous d'entrer un chemin de répertoire valide.");
            }
        }
        return repertoire;
    }

    /**
     * Nettoie le chemin du fichier en supprimant les guillemets entourant le chemin si présents.
     *
     * @param chemin Le chemin du fichier.
     * @return Le chemin du fichier nettoyé.
     */
    public static String nettoyerChemin(String chemin) {
        if (chemin.startsWith("\"") && chemin.endsWith("\"")) {
            return chemin.substring(1, chemin.length() - 1);
        }
        return chemin;
    }

    /**
     * Vérifie que le répertoire de destination spécifié par l'utilisateur est valide et accessible en écriture.
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     * @param cheminFichierDestination Le chemin du répertoire de destination.
     * @throws RuntimeException Si le répertoire est invalide ou inaccessible en écriture.
     */
    public static void verifierRepertoireValide(Scanner scanner, String cheminFichierDestination) {
        File repertoireFile = new File(cheminFichierDestination);
        while (!repertoireFile.exists() || !repertoireFile.isDirectory() || !repertoireFile.canWrite()) {
            throw new RuntimeException("Répertoire invalide ou vous n'avez pas les droits d'écriture. Assurez-vous d'entrer un chemin de répertoire valide et accessible en écriture.");
        }
    }

    /**
     * Crée le répertoire de compilation s'il n'existe pas.
     *
     * @param cheminDossierDestination Le chemin du répertoire de destination.
     * @throws IOException Si le répertoire ne peut pas être créé.
     */
    public static void creerDossierPourCompilation(String cheminDossierDestination) throws IOException {
        File dossier = new File(cheminDossierDestination);

        if (!dossier.exists()) {
            boolean result = dossier.mkdirs();
            if (!result) {
                throw new IOException("Impossible de créer le répertoire de destination. Permission non accordée.");
            }
        } else {
            throw new IOException("Le répertoire de destination pour la compression existe déjà.");
        }
    }

    /**
     * Crée l'arbre de Huffman à partir du fichier spécifié et écrit sa représentation dans un fichier.
     *
     * @param cheminFichierSource Le chemin du fichier source.
     * @param cheminDossierDestination Le chemin du répertoire de destination.
     * @throws IOException Si une erreur survient lors de l'écriture du fichier.
     */
    public static void creerFichierArbreHuffman(String cheminFichierSource, String cheminDossierDestination) throws IOException {
        try (BufferedWriter writerArbre = new BufferedWriter(new FileWriter(cheminDossierDestination  + "\\arbreHuffman.txt"))) {
            arbre = new ArbreHuffman(cheminFichierSource);
            arbre.trierArbre(writerArbre);
        }
        ApplicationLigneCommande.afficherSeparateur();
        out.println("L'arbre de Huffman a bien été créé.");
    }

    /**
     * Encode le contenu du fichier à compresser en utilisant l'arbre de Huffman
     * et écrit le résultat dans un fichier binaire.
     *
     * @param cheminFichierCompresse Le chemin du fichier compressé.
     * @param contenu Le contenu à compresser.
     * @throws IOException Si une erreur survient lors de l'écriture du fichier.
     */
    public static void creerFichierCompresse(String cheminFichierCompresse, String contenu) throws IOException {
        byte[] bytes = arbre.encoderFichier(contenu);
        try (FileOutputStream ouvreurFichier = new FileOutputStream(cheminFichierCompresse)) {
            ouvreurFichier.write(bytes);
        }
        out.println("Le fichier a été compressé avec succès.");
    }

    /**
     * Décompresse le fichier spécifié en utilisant l'arbre de Huffman et écrit le résultat dans un fichier texte.
     *
     * @param cheminFichierDecompresse Le chemin du fichier décompressé.
     * @param cheminFichierADecompresser Le chemin du fichier à décompresser.
     * @throws IOException Si une erreur survient lors de l'écriture du fichier.
     */
    public static void creerFichierDecompresse(String cheminFichierDecompresse, String cheminFichierADecompresser) throws IOException {
        String cheminArbreHuffman = cheminFichierADecompresser.substring(0, cheminFichierADecompresser.lastIndexOf("\\")) + "\\arbreHuffman.txt";
        List<String> arbreHuffmanString = recupererBytesDansArbreHuffman(cheminArbreHuffman);
        String contenueDecompresse = ArbreHuffman.decoderFichier(arbreHuffmanString, getBytesADecompresser(cheminFichierADecompresser));
        Files.write(Path.of(cheminFichierDecompresse), contenueDecompresse.getBytes());
        out.println("Le fichier a bien été décompressé et enregistré à l'emplacement suivant : " + cheminFichierDecompresse);
    }
}
