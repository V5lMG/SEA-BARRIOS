package iut.groupesae.compileurhuffman;

import iut.groupesae.compileurhuffman.objets.ArbreHuffman;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Cette classe représente l'application en ligne de commande pour utiliser le compilateur Huffman.
 */
public class ApplicationLigneCommande {
    private static String cheminFichierACompiler;
    private static String contenu;
    private static String cheminFichierDestination;
    private static String dossierDestination;
    private static String nomFichierCompile;
    private static ArbreHuffman arbre;

    public static void main(String[] args) {
        afficherSeparateur();
        out.println("L'application est lancée.");
        afficherSeparateur();

        demanderFichierACompiler(args);

        afficherSeparateur();
        out.println("Fin de l'application.");
        afficherSeparateur();
    }

    private static void demanderFichierACompiler(String[] args) {
        boolean continuer = true;
        while (continuer) {
            try {
                out.print("""
                      Entrez le chemin de votre fichier (URL) :
                      Exemple de chemin valide : "dossier1\\dossier2\\monFichier.txt"
                                                  dossier1/dossier2/monFichier.txt
                      ==>\s""");
                cheminFichierACompiler = GestionFichier.getCheminFichierACompiler(args);

                afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichierACompiler);
                afficherSeparateur();

                contenu = GestionFichier.getContenuFichier(cheminFichierACompiler);
                out.println("Contenu du fichier :\n" + contenu);

                afficherSeparateur();
                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    traiterFichierACompiler(scanner);
                    continuer = demanderRecommencer();
                } else {
                    continuer = demanderRecommencer();
                }
            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier à compiler !\nErreur :" + e);
                continuer = demanderRecommencer();
            }
        }
    }

    private static void traiterFichierACompiler(Scanner scanner) throws IOException {
        afficherCaracteres(contenu);
        afficherSeparateur();

        out.println("Où voulez-vous enregistrer le fichier une fois compilé ? (Entrez le chemin complet du répertoire)");
        cheminFichierDestination = GestionFichier.nettoyerChemin(GestionFichier.getCheminDestination(scanner));
        verifierRepertoireValide(scanner);

        out.println("Sous quel nom voulez-vous enregistrer le fichier une fois compilé ? (Entrez le nom du fichier)");
        nomFichierCompile = obtenirNomFichierUnique(scanner, cheminFichierDestination);

        dossierDestination = cheminFichierDestination + "\\" + nomFichierCompile;
        creerDossierCompilation();
        creerArbreHuffman();
        creerFichierCompile();
    }

    private static void verifierRepertoireValide(Scanner scanner) {
        File repertoireFile = new File(cheminFichierDestination);
        while (!repertoireFile.exists() || !repertoireFile.isDirectory() || !repertoireFile.canWrite()) {
            out.println("Répertoire invalide ou vous n'avez pas les droits d'écriture. Assurez-vous d'entrer un chemin de répertoire valide et accessible en écriture.");
            cheminFichierDestination = GestionFichier.nettoyerChemin(GestionFichier.getCheminDestination(scanner));
            repertoireFile = new File(cheminFichierDestination);
        }
    }

    private static String obtenirNomFichierUnique(Scanner scanner, String cheminFichierDestination) {
        String nomFichier = GestionFichier.getNomFichierCompile(scanner);
        File fichier = new File(cheminFichierDestination, nomFichier);
        while (fichier.exists()) {
            out.println("Un fichier ou dossier avec ce nom existe déjà. Veuillez entrer un nom différent.");
            nomFichier = GestionFichier.getNomFichierCompile(scanner);
            fichier = new File(cheminFichierDestination, nomFichier);
        }
        return nomFichier;
    }

    private static void creerDossierCompilation() {
        File dossier = new File(dossierDestination);
        if (!dossier.exists()) {
            boolean result = dossier.mkdirs();
            if (!result) {
                throw new RuntimeException("Erreur : Impossible de créer le répertoire " + dossierDestination);
            }
        }
    }

    private static void creerArbreHuffman() throws IOException {
        try (BufferedWriter writerArbre = new BufferedWriter(new FileWriter(dossierDestination + "\\arbreHuffman.txt"))) {
            arbre = new ArbreHuffman(cheminFichierACompiler);
            arbre.trierArbre(writerArbre);
        }
        out.println("L'arbre d'Huffman a bien été créé.");
    }

    private static void creerFichierCompile() throws IOException {
        byte[] bytes = arbre.encoderFichier(contenu);
        try (FileOutputStream fos = new FileOutputStream(dossierDestination + "\\" + nomFichierCompile + ".bin")) {
            fos.write(bytes);
        }
        out.println("Le fichier fourni a bien été compilé.");
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

    /**
     * Affiche un séparateur visuel.
     */
    private static void afficherSeparateur() {
        out.println("------------------------------------------------------------");
    }

    /**
     * Affiche les caractères et leurs fréquences pour le fichier spécifié.
     * @param contenu le contenu à analyser
     */
    private static void afficherCaracteres(String contenu) {
        int[][] occurrences = GestionFichier.ordonnerOccurrences(contenu);
        Map<Character, Double> frequenceMap = GestionFichier.getFrequences(contenu);

        for (int[] occurrence : occurrences) {
            char caractere = (char) occurrence[0];
            int nombreOccurrences = occurrence[1];
            double frequence = frequenceMap.get(caractere);
            out.println("Caractère: " + caractere + "; Occurrences: " + nombreOccurrences + "; Fréquence: " + (frequence * 100) + "%");
        }
    }
}
