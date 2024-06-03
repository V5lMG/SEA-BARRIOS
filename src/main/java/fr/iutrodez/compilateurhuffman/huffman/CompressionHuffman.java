/*
 * Pas de copyright, ni de droit d'auteur.
 * CompressionHuffman.java                   27/05/2024
 */
package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.outils.GestionFichier;
import fr.iutrodez.compilateurhuffman.objets.Noeud;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static java.lang.System.out;

/**
 * Classe de compression de fichiers utilisant l'algorithme de Huffman.
 * Cette classe permet de lire un fichier source,
 * de compter la fréquence des caractères, de construire l'arbre de Huffman,
 * et de compresser le fichier en une chaîne de bits selon le codage de Huffman.
 * Le résultat est écrit dans un fichier de destination
 * avec les codes Huffman utilisés.
 * Un résumé des fréquences de caractère et
 * des codes de Huffman est également produit.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class CompressionHuffman {

    /**
     * Chemin du fichier source à compresser.
     */
    private String cheminFichierSource;

    /**
     * Chemin du fichier destination où sera sauvegardé le fichier compressé.
     */
    private String cheminFichierDestination;

    /**
     * Constructeur qui initialise les chemins du fichier source
     * et du fichier destination.
     * Ces chemins sont utilisés pour la lecture du fichier à compresser
     * et l'écriture du fichier compressé.
     *
     * @param cheminFichierSource Chemin complet du fichier à compresser.
     * @param cheminFichierDestination Chemin complet où le fichier compressé
     *                                 sera enregistré.
     */
    public CompressionHuffman(String cheminFichierSource,
                              String cheminFichierDestination) {
        this.cheminFichierSource = cheminFichierSource;
        this.cheminFichierDestination = cheminFichierDestination;
    }

    public void compresserFichier() throws IOException {
        byte[] chaine = GestionFichier.recupererOctets(cheminFichierSource);
        Map<Byte, Integer> occurencesDesCaracteres = preparerDonnees(chaine);
        Map<Byte, String> codageHuffman =
                                     compresserDonnees(occurencesDesCaracteres);
        String codage = convertirOctetsEnCodeHuffman(chaine, codageHuffman);
        ecrireResultats(codage, cheminFichierDestination, codageHuffman);
    }

    private Map<Byte, Integer> preparerDonnees(byte[] chaine) {
        Map<Byte, Integer> occurencesDesCaracteres =
                                                  getFrequenceDesOctets(chaine);
        Map<Byte, Integer> occurencesTriees =
                                       trierOccurences(occurencesDesCaracteres);
        afficherOccurencesTriees(occurencesTriees);
        return occurencesDesCaracteres;
    }

    private Map<Byte, String> compresserDonnees(Map<Byte, Integer> occurencesDesCaracteres) {
        Noeud[] tableau = new Noeud[occurencesDesCaracteres.size()];
        int entree = 0;
        for (Map.Entry<Byte, Integer> encode : occurencesDesCaracteres.entrySet()) {
            tableau[entree++] = new Noeud(encode.getKey(), encode.getValue());
        }

        Noeud racine = construireArbre(tableau);
        Map<Byte, String> codes = new HashMap<>();
        generationCodeHuffman(racine, "", codes);
        return codes;
    }

    private void generationCodeHuffman(Noeud noeud, String chemin, Map<Byte, String> codes) {
        if (noeud == null) {
            return;
        }
        if (noeud.isFeuille()) {
            codes.put(noeud.getCaractere(), chemin);
        } else {
            if (noeud.getGauche() != null) {
                generationCodeHuffman(noeud.getGauche(), chemin + "0", codes);
            }
            if (noeud.getDroite() != null) {
                generationCodeHuffman(noeud.getDroite(), chemin + "1", codes);
            }
        }
    }


    private void ecrireResultats(String codage,
                                 String cheminFichierDestination,
                                 Map<Byte, String> codageHuffman)
            throws IOException {

        GestionFichier.ecrireChaineBinaireDansFichier(codage,
                                                      cheminFichierDestination);

        int dernierSeparateur = cheminFichierDestination.lastIndexOf("\\");
        String cheminDossier = cheminFichierDestination
                                            .substring(0,dernierSeparateur + 1);

        String cheminArbreHuffman = cheminDossier + "arbreHuffman.txt";

        ecrireArbreHuffmanTrie(cheminArbreHuffman, codageHuffman);
    }

    /**
     * Affiche les occurrences des caractères triées
     * par ordre décroissant de fréquence.
     *
     * @param occurencesTriees La map des occurrences triée.
     */
    private void afficherOccurencesTriees(Map<Byte, Integer> occurencesTriees) {
        out.println("Occurrences des caractères :");

        int totalOccurrences = occurencesTriees.values()
                                               .stream()
                                               .mapToInt(Integer::intValue)
                                               .sum();

        for (Entry<Byte, Integer> entry : occurencesTriees.entrySet()) {
            byte cle = entry.getKey();
            int occurrences = entry.getValue();
            double frequence = (double) occurrences / totalOccurrences;
            char symbole = (char) cle;
            out.printf("Caractère : %c ; "
                       + "Occurrences : %d ; "
                       + "Fréquence : %.4f\n",
                       symbole, occurrences, frequence);
        }
    }

    /**
     * Trie les occurrences des caractères par ordre décroissant de fréquence.
     *
     * @param occurencesDesCaracteres La map des occurrences à trier.
     * @return Une LinkedHashMap triée par ordre décroissant de fréquence.
     */
    private Map<Byte, Integer> trierOccurences(
                                      Map<Byte, Integer> occurencesDesCaracteres
                                               ) {

        List<Entry<Byte, Integer>> list =
                new ArrayList<>(occurencesDesCaracteres.entrySet());
        list.sort(Entry.<Byte, Integer>comparingByValue().reversed());

        Map<Byte, Integer> sortedMap = new LinkedHashMap<>();
        for (Entry<Byte, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * Encode une chaîne de octets en utilisant
     * un mappage spécifié de codes Huffman.
     * La méthode parcourt chaque byte du tableau
     * et utilise le mappage de Huffman pour convertir chaque byte
     * en sa représentation de chaîne de bits correspondante.
     * Ces chaînes de bits sont ensuite concaténées pour former
     * une seule chaîne binaire.
     *
     * @param chaine Le tableau de octets à encoder.
     * @param codageHuffman Le mappage de chaque byte à sa chaîne
     *                      de code Huffman correspondante.
     * @return La chaîne binaire complète résultante de l'encodage de tous
     *         les octets de la chaîne initiale.
     */
    private String convertirOctetsEnCodeHuffman(byte[] chaine,
                                               Map<Byte, String> codageHuffman){

        StringBuilder codage = new StringBuilder();
        for (byte octet : chaine) {
            codage.append(codageHuffman.get(octet));
        }
        return codage.toString();
    }

    /**
     * Construit un arbre de Huffman à partir d'une liste de nœuds.
     * Les nœuds sont initialement triés et les deux nœuds avec
     * les plus petites valeurs sont fusionnés à chaque itération,
     * formant un nouvel arbre binaire jusqu'à ce qu'un seul nœud reste,
     * servant de racine à l'arbre Huffman.
     *
     * @param listeDeNoeuds Tableau initial de nœuds, où chaque nœud contient
     *                     un caractère (ou byte) et sa fréquence.
     * @return La racine de l'arbre de Huffman,
     *         représentant la hiérarchie complète des fusions.
     */
    private Noeud construireArbre(Noeud[] listeDeNoeuds) {
        /* Création d'une ArrayList à partir d'un tableau de noeuds.
         * `Arrays.asList(listeDeNoeuds)` convertit le tableau `listeDeNoeuds`
         * en une liste fixe de taille, ce qui signifie que la liste résultante
         * aura la même taille que le tableau et contiendra tous ses éléments.
         * Cette liste fixe est ensuite utilisée pour initialiser
         * une nouvelle ArrayList modifiable.
         * Une ArrayList est une implémentation de la liste basée
         * sur un tableau redimensionnable qui permet
         * des accès aléatoires rapides et des ajouts de taille dynamique.
         * Contrairement à la liste fixe retournée par `Arrays.asList`,
         * la ArrayList peut être modifiée librement,
         * ajoutant ou supprimant des éléments.
         */
        List<Noeud> noeuds = new ArrayList<>(Arrays.asList(listeDeNoeuds));


        while (noeuds.size() > 1) {
            /* Trie la liste `noeuds` en utilisant un comparateur.
             * Le comparateur est défini par Comparator.comparingInt,
             * qui crée un comparateur qui compare les entiers retournés
             * par la méthode `getFrequence` de chaque objet `Noeud`.
             * Noeud::getFrequence est une référence de méthode qui renvoie
             * la valeur de fréquence du nœud.
             * Cette opération de tri place les noeuds avec
             * les plus petites valeurs (fréquences) en premier,
             * ce qui est nécessaire pour la construction de l'arbre de Huffman.
             */
            noeuds.sort(Comparator.comparingInt(Noeud::getFrequence));

            Noeud premierNoeud = noeuds.remove(0);
            Noeud deuxiemeNoeud = noeuds.remove(0);

            Noeud parent = new Noeud(premierNoeud.getFrequence()
                                     + deuxiemeNoeud.getFrequence());

            parent.setGauche(premierNoeud);
            parent.setDroite(deuxiemeNoeud);

            noeuds.add(parent);
        }
        return noeuds.get(0);
    }

    /**
     * Écrit l'arbre de Huffman trié dans un fichier texte.
     *
     * @param cheminFichierDestination Le chemin du fichier où écrire l'arbre.
     * @param codageHuffman            La map des codes Huffman à écrire.
     */
    private void ecrireArbreHuffmanTrie(String cheminFichierDestination,
                                        Map<Byte, String> codageHuffman) {

        List<Entry<Byte, String>> arbreTrie = trierArbreHuffman(codageHuffman);
        String[] tableHuffmanTrie = new String[arbreTrie.size()];
        int occ = 0;
        for (Entry<Byte, String> code : arbreTrie) {
            byte key = code.getKey();
            String value = code.getValue();
            tableHuffmanTrie[occ++] = "codeHuffman = " + value + " ;"
                                      + " encode = " + key + " ;"
                                      + " symbole = " + (char) key;
        }
        GestionFichier.ecrireArbreHuffman(cheminFichierDestination,
                                          tableHuffmanTrie);
    }

    /**
     * Trie les entrées de la table de codes Huffman par longueur de code.
     *
     * @param codageHuffman La map des codes Huffman à trier.
     * @return Une liste d'entrées triées par longueur de code.
     */
    private List<Entry<Byte, String>> trierArbreHuffman(
                                                 Map<Byte,String> codageHuffman
                                                       ) {
        List<Entry<Byte, String>> list =
                new ArrayList<>(codageHuffman.entrySet());

        /*
         * Trie la liste d'entrées de la map en fonction de la valeur (String),
         * d'abord par la longueur de la chaîne (ordre croissant),
         * puis par ordre lexicographique (ordre alphabétique) en cas d'égalité.
         *
         * - `Entry.comparingByValue(...)` : Crée un comparateur qui compare
         *   les entrées de la map en utilisant leurs valeurs.
         *
         * - `Comparator.comparingInt(String::length)` : Premier comparateur
         *   qui compare les chaînes en fonction de leur longueur.
         *
         * - `thenComparing(String::compareTo)` : Second comparateur qui compare
         *   les chaînes de manière lexicographique (alphabétique) si elles
         *   ont la même longueur.
         */
        list.sort(Entry.comparingByValue(
                Comparator.comparingInt(String::length)
                        .thenComparing(String::compareTo)));
        return list;
    }

    /**
     * Calcule et renvoie la fréquence de chaque byte dans un tableau de octets.
     * Cette méthode utilise une HashMap pour stocker
     * et compter la fréquence de chaque byte rencontré.
     *
     * @param octets Le tableau de octets dont les fréquences
     *              doivent être calculées.
     * @return Une HashMap où chaque clé est un byte et chaque valeur
     *         est la fréquence de ce byte dans le tableau.
     */
    private HashMap<Byte, Integer> getFrequenceDesOctets(byte[] octets) {
        /*
         * HashMap est une structure de données qui stocke les éléments
         * sous forme de paires clé-valeur.
         * Elle permet des insertions, des suppressions
         * et des accès en temps constant en moyenne.
         */
        HashMap<Byte, Integer> frequenceDesOctets = new HashMap<>();
        for (byte octet : octets) {
            frequenceDesOctets.put(octet,
                               /*
                                * La méthode `getOrDefault` est utilisée pour
                                * obtenir la fréquence actuelle d'un byte
                                * (représenté par la variable 'octet') dans
                                * la HashMap `frequenceDesOctets`.
                                * Si 'octet' est déjà une clé dans la map,
                                * cette méthode retournera sa valeur associée.
                                * Si 'octet' n'est pas encore une clé dans la 
                                * map, `getOrDefault` retournera la valeur
                                * par défaut spécifiée, ici `0`.
                                * Ensuite, on ajoute 1 à cette valeur
                                * pour comptabiliser une nouvelle
                                * occurrence de 'octet'.
                                */
                                frequenceDesOctets.getOrDefault(octet, 0)
                                + 1);
        }
        return frequenceDesOctets;
    }
}