/*
 * Pas de copyright, ni de droit d'auteur.
 * OutilsGestionFichier.java               27/05/2024
 */
package fr.iutrodez.compilateurhuffman.outils;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.System.out;

/**
 * Cette classe fournit des méthodes pour la gestion des fichiers.
 */
public class OutilsGestionFichier {
    private static ArbreHuffman arbre;

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
        List<String> arbreHuffmanString = OutilsGestionBinaire.getBytesDansArbreHuffman(cheminArbreHuffman);
        String contenueDecompresse = ArbreHuffman.decoderFichier(arbreHuffmanString, OutilsGestionBinaire.getBytesADecompresser(cheminFichierADecompresser));
        Files.write(Path.of(cheminFichierDecompresse), contenueDecompresse.getBytes());
        out.println("Le fichier a bien été décompressé et enregistré à l'emplacement suivant : " + cheminFichierDecompresse);
    }
}
