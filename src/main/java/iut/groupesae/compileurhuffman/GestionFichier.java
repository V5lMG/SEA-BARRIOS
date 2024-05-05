package iut.groupesae.compileurhuffman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Cette classe fournit des méthodes pour la gestion des fichiers.
 */
public class GestionFichier {

    /**
     * Obtient le chemin du fichier à partir des arguments de la ligne de commande ou de l'entrée utilisateur.
     * @param args les arguments de la ligne de commande
     * @return le chemin du fichier nettoyé
     * @throws IOException en cas d'erreur d'entrée/sortie lors de la lecture de la ligne de commande
     */
    public static String getCheminFichier(String[] args) throws IOException {
        if (args.length > 0) {
            return nettoyerChemin(args[0]);
        } else {
            BufferedReader lecteurConsole = new BufferedReader(new InputStreamReader(System.in));
            return nettoyerChemin(lecteurConsole.readLine());
        }
    }

    /**
     * Obtient le contenu du fichier spécifié.
     * @param cheminFichier le chemin du fichier
     * @return le contenu du fichier
     * @throws IOException en cas d'erreur d'entrée/sortie lors de la lecture du fichier
     */
    public static String getContenuFichier(String cheminFichier) throws IOException {
        // try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier, StandardCharsets.UTF_8))) { FIXME : exclure les utf non 8
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
     * Obtient les occurrences de chaque caractère dans le contenu du fichier.
     * @param contenu le contenu du fichier
     * @return une map des occurrences de caractères
     */
    public static Map<Character, Integer> getOccurrenceCaractere(String contenu) {
        Map<Character, Integer> occurrences = new HashMap<>();
        for (char caractere : contenu.toCharArray()) {
            occurrences.put(caractere, occurrences.getOrDefault(caractere, 0) + 1);
        }
        return occurrences;
    }

    /**
     * Trie les occurrences des caractères dans le contenu du fichier.
     * @param contenu le contenu du fichier
     * @return un tableau à deux dimensions des occurrences triées par ordre décroissant
     */
    public static int[][] ordonnerOccurrences(String contenu) {
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
         * Le "->" est un opérateur syntaxique qui sépare les paramètres de la lambda de son corps.
         * Dans une expression lambda, ce qui se trouve à gauche de -> sont les paramètres de la lambda,
         * et ce qui se trouve à droite de -> est le corps de la lambda.
         * Cela signifie que cette lambda prend deux arguments, a et b,
         * qui seront utilisés pour comparer deux éléments du tableau lors du tri.
         */
        Arrays.sort(occurrences, (a, b) -> {
            // Compare les occurrences des caractères a et b.
            // On compare les occurrences en utilisant b[1] - a[1] pour obtenir un tri décroissant.
            // Si b[1] est plus grand que a[1], alors b est considéré comme "plus grand" que a,
            // et donc "b" devrait être placé avant "a" dans le tableau trié.
            return Integer.compare(b[1], a[1]);
        });


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
    private static String nettoyerChemin(String chemin) {
        if (chemin.startsWith("\"") && chemin.endsWith("\"")) {
            return chemin.substring(1, chemin.length() - 1);
        }
        return chemin;
    }
}
