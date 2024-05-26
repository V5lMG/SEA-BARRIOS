package iut.groupesae.compileurhuffman;

import java.io.*;
import java.util.*;

import static java.lang.System.out;

/**
 * Cette classe fournit des méthodes pour la gestion des fichiers.
 */
public class GestionFichier {
    /* *============================================================*
     *
     *                          FICHIER
     *
     * *============================================================*
     */

    /* *============================================================*
     *
     *                          CHEMIN
     *
     * *============================================================*
     */

    /* *============================================================*
     *
     *                          ENCODAGE
     *
     * *============================================================*
     */

    /* *============================================================*
     *
     *                          DÉCODAGE
     *
     * *============================================================*
     */
    /**
     * Obtient le chemin du fichier à partir des arguments de la ligne de commande ou de l'entrée utilisateur.
     * @param args les arguments de la ligne de commande
     * @return le chemin du fichier nettoyé
     * @throws IOException en cas d'erreur d'entrée/sortie lors de la lecture de la ligne de commande
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
     * Cette méthode permet à l'utilisateur de saisir un chemin de répertoire
     * dans lequel le fichier compressé sera créé.
     * Elle vérifie la validité du chemin saisi en s'assurant qu'il existe et qu'il s'agit bien d'un répertoire.
     * @param scanner utilisé pour lire l'entrée de l'utilisateur.
     * @return le chemin du répertoire de destination validé.
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
                out.println("Répertoire invalide. Assurez-vous d'entrer un chemin de répertoire valide.");
            }
        }
        return repertoire;
    }

    /**
     * Obtient un nom de fichier unique dans le répertoire de destination pour éviter les conflits.
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     * @param cheminFichierDestination le chemin du répertoire de destination
     * @return un nom de fichier unique
     */
    public static String getNomFichierDestinationUnique(Scanner scanner, String cheminFichierDestination) {
        String nomFichier = GestionFichier.getNomFichierDestination(scanner);
        File fichier = new File(cheminFichierDestination, nomFichier);
        while (fichier.exists()) {
            out.println("Un fichier ou dossier avec ce nom existe déjà. Veuillez entrer un nom différent.");
            nomFichier = GestionFichier.getNomFichierDestination(scanner);
            fichier = new File(cheminFichierDestination, nomFichier);
        }
        return nomFichier;
    }

    /**
     * Cette méthode permet à l'utilisateur de saisir un nom de fichier pour le fichier compressé qui sera créé.
     * Elle vérifie la validité du nom de fichier saisi en s'assurant qu'il ne contient pas de caractères spéciaux.
     * @param scanner utilisé pour lire l'entrée de l'utilisateur.
     * @return
     */
    public static String getNomFichierDestination(Scanner scanner) {
        String nomFichier = "";
        boolean isNomFichierValide = false;
        while (!isNomFichierValide) {
            nomFichier = scanner.nextLine();
            if (!nomFichier.isEmpty() && nomFichier.matches("[a-zA-Z0-9._-]+")) { // regex pour les caracteres invalides
                isNomFichierValide = true;
            } else {
                out.println("Nom de fichier invalide. Assurez-vous d'entrer un nom de fichier valide sans caractères spéciaux autres que '.', '_', ' ', ou '-'.");
            }
        }
        return nomFichier;
    }

    /**
     * Obtient le contenu du fichier spécifié.
     * @param cheminFichier le chemin du fichier
     * @return le contenu du fichier
     * @throws IOException en cas d'erreur d'entrée/sortie lors de la lecture du fichier
     */
    public static String getContenuFichier(String cheminFichier) throws IOException {
        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
            return contenu.toString();
        }
    }

    /**
     * Trie les occurrences des caractères dans le contenu du fichier.
     * @param contenu le contenu du fichier
     * @return un tableau à deux dimensions des occurrences triées par ordre décroissant
     */
    public static int[][] getOccurrencesOrdonnee(String contenu) {
        Map<Character, Integer> occurrenceMap = getOccurrenceCaractere(contenu);

        int[][] occurrences = new int[occurrenceMap.size()][2];

        int index = 0;
        for (Map.Entry<Character, Integer> entry : occurrenceMap.entrySet()) {
            occurrences[index][0] = entry.getKey();
            occurrences[index][1] = entry.getValue();
            index++;
        }

        /* Trie le tableau d'occurrences en fonction du nombre d'occurrences de chaque caractère,
         * en ordre décroissant. Cela permet de placer les caractères les plus fréquents en premier.
         * Le "→" est un opérateur syntaxique qui sépare les paramètres de la lambda de son corps.
         * Dans une expression lambda, ce qui se trouve à gauche de → sont les paramètres de la lambda,
         * et ce qui se trouve à droite de → est le corps de la lambda.
         * Cela signifie que cette lambda prend deux arguments, a et b,
         * qui seront utilisés pour comparer deux éléments du tableau lors du tri.
         */
        Arrays.sort(occurrences, (a, b) -> {
            // Compare les occurrences des caractères a et b.
            // On compare les occurrences en utilisant : b[1] - a[1] pour obtenir un tri décroissant.
            // Si b[1] est plus grand que a[1], alors 'b' est considéré comme "plus grand" que a,
            // et donc "b" devrait être placé avant 'a' dans le tableau trié.
            return Integer.compare(b[1], a[1]);
        });


        return occurrences;
    }

    /**
     * Obtient les occurrences de chaque caractère dans le contenu du fichier.
     * @param contenu le contenu du fichier
     * @return une map des occurrences de caractères
     */
    private static Map<Character, Integer> getOccurrenceCaractere(String contenu) {
        Map<Character, Integer> occurrences = new HashMap<>();
        for (char caractere : contenu.toCharArray()) {
            occurrences.put(caractere, occurrences.getOrDefault(caractere, 0) + 1);
        }
        return occurrences;
    }

    /**
     * Calcule les fréquences des caractères dans le contenu du fichier.
     * @param contenu le contenu du fichier
     * @return une carte des fréquences de caractères
     */
    public static Map<Character, Double> getFrequences(String contenu) {
        Map<Character, Integer> occurrenceMap = getOccurrenceCaractere(contenu);
        int totalCaracteres = contenu.length();
        Map<Character, Double> frequenceMap = new HashMap<>();

        for (Map.Entry<Character, Integer> entry : occurrenceMap.entrySet()) {
            char caractere = entry.getKey();
            int occurrence = entry.getValue();
            double frequence = (double) occurrence / totalCaracteres;
            frequenceMap.put(caractere, frequence);
        }

        return frequenceMap;
    }

    /**
     * Nettoie le chemin du fichier en supprimant les guillemets entourant le chemin si présents.
     * @param chemin le chemin du fichier
     * @return le chemin du fichier nettoyé
     */
    public static String nettoyerChemin(String chemin) {
        if (chemin.startsWith("\"") && chemin.endsWith("\"")) {
            return chemin.substring(1, chemin.length() - 1);
        }
        return chemin;
    }

    /**
     * Vérifie que le répertoire de destination spécifié par l'utilisateur est valide et accessible en écriture.
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     */
    public static void verifierRepertoireValide(Scanner scanner, String cheminFichierDestination) {
        File repertoireFile = new File(cheminFichierDestination);
        while (!repertoireFile.exists() || !repertoireFile.isDirectory() || !repertoireFile.canWrite()) {
            out.println("Répertoire invalide ou vous n'avez pas les droits d'écriture. Assurez-vous d'entrer un chemin de répertoire valide et accessible en écriture.");
            cheminFichierDestination = GestionFichier.nettoyerChemin(GestionFichier.getCheminDestination(scanner));
            repertoireFile = new File(cheminFichierDestination);
        }
    }
}
