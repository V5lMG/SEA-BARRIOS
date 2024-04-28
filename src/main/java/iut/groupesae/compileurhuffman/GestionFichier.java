package iut.groupesae.compileurhuffman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GestionFichier {

    public static String getCheminFichier(String[] args) throws IOException {
        if (args.length > 0) {
            return nettoyerChemin(args[0]);
        } else {
            BufferedReader lecteurConsole = new BufferedReader(new InputStreamReader(System.in));
            return nettoyerChemin(lecteurConsole.readLine());
        }
    }

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

    public static Map<Character, Integer> getOccurrenceCaractere(String contenu) {
        Map<Character, Integer> occurrences = new HashMap<>();
        for (char caractere : contenu.toCharArray()) {
            occurrences.put(caractere, occurrences.getOrDefault(caractere, 0) + 1);
        }
        return occurrences;
    }

    public static int[][] ordonnerOccurrences(String contenu) {
        Map<Character, Integer> occurrenceMap = getOccurrenceCaractere(contenu);

        int[][] occurrences = new int[occurrenceMap.size()][2];

        int index = 0;
        for (Map.Entry<Character, Integer> entry : occurrenceMap.entrySet()) {
            occurrences[index][0] = entry.getKey();
            occurrences[index][1] = entry.getValue();
            index++;
        }
        Arrays.sort(occurrences, (a, b) -> Integer.compare(b[1], a[1]));

        return occurrences;
    }

    public static Map<Character, Double> getFrequence(String contenu) {
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

    private static String nettoyerChemin(String chemin) {
        if (chemin.startsWith("\"") && chemin.endsWith("\"")) {
            return chemin.substring(1, chemin.length() - 1);
        }
        return chemin;
    }
}