package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionChemin;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionFichier;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Classe utilisée pour créer un fichier représentant l'arbre de Huffman basé sur un fichier source.
 */
public class CreerArbreHuffman {

    /**
     * TODO
     */
    public static void creerSeulementArbre(String[] args) {
        String cheminFichierSource;
        boolean continuer = true;
        while (continuer) {
            try {
                cheminFichierSource = demanderFichierSource(args);
                String cheminDossierDestination = obtenirCheminDestination();
                creerArbre(cheminFichierSource, cheminDossierDestination);

                continuer = ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous recommencer ? (oui/non)");
            } catch (Exception e) {
                out.println("Erreur: " + e.getMessage());
                continuer = ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous recommencer le procéssus " +
                                                                      "de création de l'arbre d'Huffman ? (oui/non)");
            }
        }

    }

    private static String demanderFichierSource(String[] args) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();
        out.print("""
            Entrez le chemin de votre fichier (URL) :
            Exemple de chemin valide : "dossier1\\dossier2\\monFichier.txt"
                                        dossier1/dossier2/monFichier.txt
            ==>\s""");
        String cheminFichierSource = OutilsGestionChemin.getCheminFichierSource(args);
        OutilsGestionChemin.verifierCheminFichierSourceValide(cheminFichierSource, "txt");
        return cheminFichierSource;
    }

    private static String obtenirCheminDestination() {
        Scanner scanner = new Scanner(System.in);
        String cheminDestination;
        boolean continuer = true;

        while (continuer) {
            try {
                ApplicationLigneCommande.afficherSeparateur();
                out.println("Où voulez-vous enregistrer l'arbre d'Huffman ?");
                cheminDestination = OutilsGestionChemin.getCheminDestination(scanner);
                return OutilsGestionChemin.enleverGuillemet(cheminDestination);
            } catch (RuntimeException e) {
                out.println("Erreur : " + e.getMessage());
                if (!ApplicationLigneCommande.demanderOuiOuNon("Voulez-vous continuer le procéssus de création de l'arbre d'Huffman ? (oui/non)")) {
                    continuer = false;
                }
            }
        }
        return null;
    }

    /**
     * TODO
     *
     * @param cheminFichierSource Le chemin complet vers le fichier source utilisé pour générer l'arbre de Huffman.
     * @param cheminDossierDestination Le dossier où le fichier de l'arbre de Huffman sera enregistré.
     */
    private static void creerArbre(String cheminFichierSource, String cheminDossierDestination) {
        try {
            ApplicationLigneCommande.afficherSeparateur();
            OutilsGestionFichier.creerFichierArbreHuffman(cheminFichierSource, cheminDossierDestination);
        } catch (IOException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }
}
