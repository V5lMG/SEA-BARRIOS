package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;
import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;

import java.util.Scanner;
import java.util.Map;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;

import static java.lang.System.out;

public class CompressionHuffman {
    private static String cheminFichierACompiler;
    private static String contenu;
    private static String dossierDestination;
    private static String nomFichierCompile;
    private static ArbreHuffman arbre;

    /**
     * Demande à l'utilisateur de spécifier le fichier à compiler.
     * @param args les arguments de la ligne de commande (optionnelle)
     */
    public static void demanderFichierACompiler(String[] args) {
        boolean continuer = true;
        while (continuer) {
            try {
                out.print("""
                      Entrez le chemin de votre fichier (URL) :
                      Exemple de chemin valide : "dossier1\\dossier2\\monFichier.txt"
                                                  dossier1/dossier2/monFichier.txt
                      ==>\s""");
                cheminFichierACompiler = OutilsGestionFichier.getCheminFichierSource(args);
                // Vérification du chemin de fichier
                if ( cheminFichierACompiler.isEmpty()) {
                    throw new IOException("\nUn chemin de fichier doit être spécifié");
                }
                ApplicationLigneCommande.afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichierACompiler);
                ApplicationLigneCommande.afficherSeparateur();

                contenu = OutilsGestionFichier.getContenuFichier(cheminFichierACompiler);
                out.println("Contenu du fichier :\n" + contenu);

                ApplicationLigneCommande.afficherSeparateur();
                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    traiterFichierACompiler(scanner);
                }
                continuer = demanderRecommencer();
            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier à compiler !" + e.getMessage());

                continuer = demanderRecommencer();
            }
        }
    }

    /**
     * Traite le fichier à compiler en demandant l'emplacement de destination et en générant le fichier compilé.
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     * @throws IOException si une erreur survient lors de la manipulation des fichiers
     */
    private static void traiterFichierACompiler(Scanner scanner) throws IOException {
        afficherCaracteres(contenu);
        ApplicationLigneCommande.afficherSeparateur();

        out.println("Où voulez-vous enregistrer le fichier une fois compilé ? (Entrez le chemin complet du répertoire)");
        String cheminFichierDestination = OutilsGestionFichier.nettoyerChemin(OutilsGestionFichier.getCheminDestination(scanner));
        OutilsGestionFichier.verifierRepertoireValide(scanner, cheminFichierDestination);

        out.println("Sous quel nom voulez-vous enregistrer le fichier une fois compilé ? (Entrez le nom du fichier)");
        nomFichierCompile = OutilsGestionFichier.getNomFichierDestinationUnique(scanner, cheminFichierDestination);

        dossierDestination = cheminFichierDestination + "\\" + nomFichierCompile;
        ApplicationLigneCommande.afficherSeparateur();
        creerDossierCompilation();
        //ApplicationLigneCommande.afficherSeparateur(); TODO : tester si il est a ça place
        creerArbreHuffman();
        creerFichierCompile();
        ApplicationLigneCommande.afficherSeparateur();
    }

    /**
     * Affiche les caractères et leurs fréquences pour le fichier spécifié.
     * @param contenu le contenu à analyser
     */
    private static void afficherCaracteres(String contenu) {
        int[][] occurrences = OutilsGestionFichier.getOccurrencesOrdonnee(contenu);
        Map<Character, Double> frequenceMap = OutilsGestionFichier.getFrequences(contenu);

        for (int[] occurrence : occurrences) {
            char caractere = (char) occurrence[0];
            int nombreOccurrences = occurrence[1];
            double frequence = frequenceMap.get(caractere);
            out.println("Caractère: " + caractere + "; Occurrences: " + nombreOccurrences + "; Fréquence: " + (frequence * 100) + "%");
        }
    }

    /**
     * Crée le répertoire de compilation si celui-ci n'existe pas.
     */
    private static void creerDossierCompilation() {
        File dossier = new File(dossierDestination);
        if (!dossier.exists()) {
            boolean result = dossier.mkdirs();
            if (!result) {
                throw new RuntimeException("Erreur : Impossible de créer le répertoire " + dossierDestination);
            }
        }
    }

    /**
     * Crée l'arbre de Huffman à partir du fichier spécifié et écrit sa représentation dans un fichier.
     * @throws IOException si une erreur survient lors de l'écriture du fichier
     */
    private static void creerArbreHuffman() throws IOException {
        try (BufferedWriter writerArbre = new BufferedWriter(new FileWriter(dossierDestination + "\\arbreHuffman.txt"))) {
            arbre = new ArbreHuffman(cheminFichierACompiler);
            arbre.trierArbre(writerArbre);
        }
        out.println("L'arbre d'Huffman a bien été créé.");
    }

    /**
     * Encode le contenu du fichier à compiler en utilisant l'arbre de Huffman
     * et écrit le résultat dans un fichier binaire.
     * @throws IOException si une erreur survient lors de l'écriture du fichier
     */
    private static void creerFichierCompile() throws IOException {
        byte[] bytes = arbre.encoderFichier(contenu);
        String cheminFichierCompile = dossierDestination + "\\" + nomFichierCompile + ".bin";
        try (FileOutputStream ouvreurFichier = new FileOutputStream(cheminFichierCompile)) {
            ouvreurFichier.write(bytes);
        }
        out.println("Le fichier fourni a bien été compilé.");
        resumeCompression(cheminFichierCompile);
    }

    /**
     * Affiche un résumé de la compression en indiquant la taille des fichiers et le taux de compression.
     */
    private static void resumeCompression(String cheminFichierCompile) {
        File fichierOriginal = new File(cheminFichierACompiler);
        File fichierCompile = new File(cheminFichierCompile);
        long tailleFichierOriginal = fichierOriginal.length();
        long tailleFichierCompile = fichierCompile.length();

        out.println("Taille du fichier original : " + tailleFichierOriginal + " octets");
        out.println("Taille du fichier compilé : " + tailleFichierCompile + " octets");

        double tauxCompression = ((double) tailleFichierCompile / tailleFichierOriginal) * 100;
        out.println("Taux de compression : " + String.format("%.2f", tauxCompression) + "%");
    }

    /**
     * Demande à l'utilisateur s'il veut réessayer.
     * @return true si l'utilisateur veut réessayer, sinon false
     */
    private static boolean demanderRecommencer() {
        out.println("Voulez-vous recommencer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }
}
