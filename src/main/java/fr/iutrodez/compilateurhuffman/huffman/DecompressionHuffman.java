package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.objets.ArbreHuffman;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import java.io.File;

import static fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire.getBytesADecompiler;
import static fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire.recupererBytesDansArbreHuffman;
import static fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier.getCheminFichierSource;
import static java.lang.System.out;

public class DecompressionHuffman {
    private static String cheminFichierADecompiler;
    private static String dossierDestination;
    private static String cheminFichierDecompile;
    private static String nomFichierDecompile;

    public static void demanderFichierADecompiler(String[] args) {
        boolean continuer = true;
        while (continuer) {
            try {
                out.print("""
                      Entrez le chemin du fichier à décompresser (URL) :
                      Exemple de chemin valide : "dossier1\\dossier2\\monFichier.bin"
                                                  dossier1/dossier2/monFichier.bin
                      ==>\s""");
                cheminFichierADecompiler = getCheminFichierSource(args);

                if (cheminFichierADecompiler.isEmpty()) {
                    throw new IOException("\nUn chemin de fichier valide doit être spécifié.");
                }

                ApplicationLigneCommande.afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichierADecompiler);
                ApplicationLigneCommande.afficherSeparateur();

                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    traiterFichierADecompresser(scanner);
                }
                continuer = demanderRecommencer();
            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier à décompresser !" + e.getMessage());
                continuer = demanderRecommencer();
            }
        }
    }

    private static void traiterFichierADecompresser(Scanner scanner) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();

        out.println("Où voulez-vous enregistrer le fichier une fois décompressé ? (Entrez le chemin complet du répertoire)");
        String cheminFichierDestination = OutilsGestionFichier.nettoyerChemin(OutilsGestionFichier.getCheminDestination(scanner));
        OutilsGestionFichier.verifierRepertoireValide(scanner, cheminFichierDestination);
        dossierDestination = cheminFichierDestination;

        out.println("Sous quel nom voulez-vous enregistrer le fichier une fois décompressé ? (Entrez le nom du fichier)");
        nomFichierDecompile = OutilsGestionFichier.getNomFichierDestinationUnique(scanner, cheminFichierDestination);

        ApplicationLigneCommande.afficherSeparateur();
        long tempsDeCompression = System.currentTimeMillis();
        creerFichierDecompresse();
        ApplicationLigneCommande.afficherSeparateur();
        resumeDecompression(tempsDeCompression);
    }

    private static void creerFichierDecompresse() throws IOException {
        cheminFichierDecompile = dossierDestination + "\\" + nomFichierDecompile + ".txt";
        String cheminArbreHuffman = cheminFichierADecompiler.substring(0, cheminFichierADecompiler.lastIndexOf("\\")) + "\\arbreHuffman.txt";
        List<String> arbreHuffmanString = recupererBytesDansArbreHuffman(cheminArbreHuffman);
        String contenueDecompile = ArbreHuffman.decoderFichier(arbreHuffmanString, getBytesADecompiler(cheminFichierADecompiler));
        Files.write(Path.of(cheminFichierDecompile), contenueDecompile.getBytes());
        out.println("Le fichier a bien été décompressé et enregistré à l'emplacement suivant : " + cheminFichierDecompile);
    }

    private static void resumeDecompression(long tempsDeCompression) {
        File fichierOriginal = new File(cheminFichierADecompiler);
        File fichierDecompile = new File(cheminFichierDecompile);
        long tailleFichierOriginal = fichierOriginal.length();
        long tailleFichierDecompile = fichierDecompile.length();

        out.println("Taille du fichier compressé : " + tailleFichierOriginal + " octets");
        out.println("Taille du fichier décompressé : " + tailleFichierDecompile + " octets");

        double tauxCompression = ((double) tailleFichierOriginal / tailleFichierDecompile) * 100;
        out.println("Taux de décompression : " + String.format("%.2f", tauxCompression) + "%");

        long tempsDeDecompression = System.currentTimeMillis() - tempsDeCompression;
        out.println("Temps de décompression : " + tempsDeDecompression + " millisecondes");
    }

    private static boolean demanderRecommencer() {
        out.println("Voulez-vous recommencer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }
}
