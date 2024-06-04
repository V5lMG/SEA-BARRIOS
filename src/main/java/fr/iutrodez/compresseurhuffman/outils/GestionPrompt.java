/*
 * Pas de copyright, ni de droit d'auteur.
 * GestionPrompt.java                        27/05/2024
 */
package fr.iutrodez.compresseurhuffman.outils;

import fr.iutrodez.compresseurhuffman.ApplicationLigneCommande;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Classe de gestion des invites utilisateur pour les opérations
 * de compression et décompression de fichiers.
 *
 * @author V. Munier--Genie
 * @author R. Xaviertaborda
 * @author J. Seychelles
 * @author B. Thenieres
 * @version 1.0
 */
public class GestionPrompt {

    /** Gestionnaire d'URL pour les fichiers */
    private GestionUrl gestionUrlFichier = new GestionUrl();

    /**
     * Demande le chemin du fichier source à l'utilisateur.
     *
     * @param extension L'extension du fichier attendu.
     * @return Le chemin du fichier source.
     */
    public String getFichierSource(String extension) {
        Scanner scanner = new Scanner(System.in);
        String chemin = null;
        boolean continuer = true;

        while (continuer) {
            ApplicationLigneCommande.afficherSeparateur();
            out.printf("""
                   Entrez le chemin de votre fichier :
                   Exemple de chemin valide : "dossier1\\dossier2\\monFichier.%s
                                               dossier1/dossier2/monFichier.%s
                   ==>\s""", extension, extension);
            chemin = scanner.nextLine();
            try {
                chemin = gestionUrlFichier.enleverGuillemet(chemin);
                gestionUrlFichier.verifierCheminFichierSourceValide(chemin,
                        extension);

                if (extension.equals("txt")) {
                    ApplicationLigneCommande.afficherSeparateur();
                    out.println("Contenu du fichier :\n"
                                + getContenuFichier(chemin));
                }

                ApplicationLigneCommande.afficherSeparateur();

                if (demanderOuiOuNon("Voulez-vous sélectionner ce fichier : "
                                     + chemin + " ? (oui/non)")) {
                    continuer = false;
                } else if (!demanderOuiOuNon("Voulez-vous recommencer ? "
                                             + "(oui/non)")) {
                    continuer = false;
                    chemin = null;
                }
            } catch (RuntimeException | IOException erreur) {
                out.println("Erreur : " + erreur.getMessage()
                            + " Veuillez essayer un autre chemin.");

                if (!demanderOuiOuNon("Voulez-vous réessayer ? (oui/non)")) {
                    continuer = false;
                }
            }
        }

        return chemin;
    }

    /**
     * Lit le contenu d'un fichier texte.
     *
     * @param cheminFichier Le chemin du fichier à lire.
     * @return Le contenu du fichier.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public String getContenuFichier(String cheminFichier) throws IOException {
        try (BufferedReader lecteur = new BufferedReader(new FileReader(
                cheminFichier))) {
            StringBuilder contenu = new StringBuilder();
            String ligne;
            boolean estVide = true;
            while ((ligne = lecteur.readLine()) != null) {
                if (!ligne.trim().isEmpty()) {
                    estVide = false;
                }
                contenu.append(ligne).append("\n");
            }
            if (estVide) {
                throw new IOException("Vous ne pouvez pas entrer un fichier "
                                      + "vide.");
            }
            return contenu.toString();
        }
    }

    /**
     * Demande le chemin du fichier de destination à l'utilisateur.
     *
     * @param extension L'extension du fichier attendu.
     * @return Le chemin du fichier de destination.
     */
    public String getFichierDestination(String extension) {
        Scanner scanner = new Scanner(System.in);
        String cheminDossierDestination = "";
        String nomFichierDestination = "";

        ApplicationLigneCommande.afficherSeparateur();

        boolean continuer = true;
        while (continuer) {
            try {
                if (extension.equals("txt")) {
                    out.println("Où voulez-vous enregistrer le fichier une "
                            + "fois compilé ? (Entrez le chemin complet du "
                            + "répertoire)");
                } else {
                    out.println(  "Où voulez-vous enregistrer le fichier une "
                            + "fois décompilé ? (Entrez le chemin complet du "
                            + "répertoire)");
                }
                cheminDossierDestination = gestionUrlFichier
                                               .getCheminDestination(scanner);
                cheminDossierDestination = gestionUrlFichier
                                               .enleverGuillemet(
                                                       cheminDossierDestination
                                               );
                continuer = false;
            } catch (RuntimeException erreur) {
                out.println("Erreur : " + erreur.getMessage());
                if (!demanderOuiOuNon("Voulez-vous redonner un chemin de "
                        + "dossier valide ? (oui/non)")) {
                    if (demanderOuiOuNon("Voulez-vous arrêter l'opération en "
                            + "cours et retourner au menu principal ? "
                            + "(oui/non)")) {
                        return null;
                    }
                }
            }
        }
        continuer = true;
        while (continuer) {
            try {
                if (extension.equals("bin")) {
                    out.println("Quel nom voulez-vous donner à votre fichier "
                                + "une fois compilé ?");
                    nomFichierDestination = gestionUrlFichier
                                              .getNomFichierDestinationUnique(
                                                      scanner,
                                                      cheminDossierDestination
                                              );

                    cheminDossierDestination = cheminDossierDestination + "\\"
                                               + nomFichierDestination;
                    creerDossierPourCompilation(cheminDossierDestination);
                } else {
                    out.println("Quel nom voulez-vous donner à votre fichier "
                            + "une fois décompilé ?");
                    nomFichierDestination = gestionUrlFichier
                                                .getNomFichierDestinationUnique(
                                                        scanner,
                                                        cheminDossierDestination
                                                );
                }
                continuer = false;
            } catch (RuntimeException | IOException erreur) {
                out.println("Erreur : " + erreur.getMessage() +
                        " Veuillez essayer un autre nom.");
                if (!demanderOuiOuNon("Voulez-vous rentrer un autre nom ? "
                                      + "(oui/non)")) {
                    if (demanderOuiOuNon("Voulez-vous arrêter l'opération en "
                                         + "cours et retourner au menu "
                                         + "principal ? (oui/non)")) {
                        return null;
                    }
                }
            }
        }
        ApplicationLigneCommande.afficherSeparateur();
        return cheminDossierDestination + "\\"
               + nomFichierDestination + "."
               + extension;
    }

    /**
     * Crée un dossier pour la compilation si nécessaire.
     *
     * @param cheminDossierDestination Le chemin du dossier à créer.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public static void creerDossierPourCompilation(
                                                 String cheminDossierDestination
                                                   )
            throws IOException {

        File dossier = new File(cheminDossierDestination);

        if (!dossier.exists()) {
            boolean result = dossier.mkdirs();
            if (!result) {
                throw new IOException("Impossible de créer le répertoire de "
                        + "destination. Permission non accordée.");
            }
        } else {
            throw new IOException("Le répertoire de destination pour la "
                    + "compression existe déjà.");
        }
    }

    /**
     * Demande à l'utilisateur de répondre par oui ou non.
     *
     * @param message Le message a affiché à l'utilisateur.
     * @return true si l'utilisateur répond "oui", false sinon.
     */
    public boolean demanderOuiOuNon(String message) {
        Scanner scanner = new Scanner(System.in);
        out.println(message);
        String reponse = scanner.nextLine().trim().toLowerCase();
        while (!reponse.equals("oui") && !reponse.equals("non")) {
            out.println("Veuillez saisir une option valide (oui/non).");
            reponse = scanner.nextLine().trim().toLowerCase();
        }
        return reponse.equals("oui");
    }
}