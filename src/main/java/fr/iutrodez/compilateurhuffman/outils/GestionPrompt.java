package fr.iutrodez.compilateurhuffman.outils;

import fr.iutrodez.compilateurhuffman.ApplicationLigneCommande;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class GestionPrompt {

    GestionUrl gestionUrlFichier = new GestionUrl();

    /**
     * TODO
     *
     * @return Le chemin du fichier source validé.
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
                gestionUrlFichier.verifierCheminFichierSourceValide(chemin, extension);

                if (extension.equals("txt")) {
                    ApplicationLigneCommande.afficherSeparateur();
                    out.println("Contenu du fichier :\n" + getContenuFichier(chemin));
                }

                ApplicationLigneCommande.afficherSeparateur();

                if (demanderOuiOuNon("Voulez-vous sélectionner ce fichier : " + chemin + " ? (oui/non)")) {
                    continuer = false;
                } else if (!demanderOuiOuNon("Voulez-vous recommencer ? (oui/non)")) {
                    continuer = false;
                    chemin = null;
                }
            } catch (RuntimeException | IOException erreur) {
                out.println("Erreur : " + erreur.getMessage() + " Veuillez essayer un autre chemin.");
                if (!demanderOuiOuNon("Voulez-vous réessayer ? (oui/non)")) {
                    continuer = false;
                }
            }
        }

        return chemin;
    }

    /**
     * TODO
     *
     * @param cheminFichier le chemin d'accès au fichier texte
     * @return le contenu du fichier sous forme de chaîne de caractères
     */
    public String getContenuFichier(String cheminFichier) throws IOException {
        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
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
                throw new IOException("Vous ne pouvez pas entrer un fichier vide.");
            }
            return contenu.toString();
        }
    }

    /**
     * TODO
     *
     * @return Le chemin du répertoire de destination validé.
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
                    out.println("Où voulez-vous enregistrer le fichier une fois compilé ? (Entrez le chemin complet du répertoire)");
                } else {
                    out.println("Où voulez-vous enregistrer le fichier une fois décompilé ? (Entrez le chemin complet du répertoire)");
                }
                cheminDossierDestination = gestionUrlFichier.getCheminDestination(scanner);
                cheminDossierDestination = gestionUrlFichier.enleverGuillemet(cheminDossierDestination);
                continuer = false;
            } catch (RuntimeException erreur) {
                out.println("Erreur : " + erreur.getMessage());
                if (!demanderOuiOuNon("Voulez-vous redonner un chemin de dossier valide ? (oui/non)")) {
                    if (demanderOuiOuNon("Voulez-vous arrêter l'opération en cours et retourner au menu principal ? (oui/non)")) {
                        return null;
                    }
                }
            }
        }
        continuer = true;
        while (continuer) {
            try {
                if (extension.equals("bin")) {
                    out.println("Quel nom voulez-vous donner à votre fichier une fois compilé ?");
                    nomFichierDestination = gestionUrlFichier.getNomFichierDestinationUnique(scanner, cheminDossierDestination);
                    cheminDossierDestination = cheminDossierDestination + "\\" + nomFichierDestination;
                    creerDossierPourCompilation(cheminDossierDestination);
                } else {
                    out.println("Quel nom voulez-vous donner à votre fichier une fois décompilé ?");
                    nomFichierDestination = gestionUrlFichier.getNomFichierDestinationUnique(scanner, cheminDossierDestination);
                }
                continuer = false;
            } catch (RuntimeException | IOException erreur) {
                out.println("Erreur : " + erreur.getMessage() + " Veuillez essayer un autre nom.");
                if (!demanderOuiOuNon("Voulez-vous rentrer un autre nom ? (oui/non)")) {
                    if (demanderOuiOuNon("Voulez-vous arrêter l'opération en cours et retourner au menu principal ? (oui/non)")) {
                        return null;
                    }
                }
            }
        }
        ApplicationLigneCommande.afficherSeparateur();
        return cheminDossierDestination + "\\" + nomFichierDestination + "." + extension;
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
     * TODO
     *
     * @param message TODO
     * @return TODO
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
