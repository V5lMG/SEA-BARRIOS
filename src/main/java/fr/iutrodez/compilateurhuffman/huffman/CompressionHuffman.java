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


    /**
     * Compresse le fichier spécifié en utilisant l'algorithme de Huffman.
     * Cette méthode lit le fichier source,
     * calcule la fréquence de chaque byte, construit l'arbre de Huffman,
     * génère les codes de Huffman,
     * et écrit le fichier compressé ainsi qu'un fichier de référence des codes.
     * </br>
     * Le processus comprend plusieurs étapes :
     * 1. Lecture du fichier et calcul des fréquences des caractères.
     * 2. Construction de l'arbre de Huffman à partir des fréquences.
     * 3. Génération de la table des codes de Huffman.
     * 4. Encodage du contenu du fichier source avec les codes d'Huffman.
     * 5. Écriture du fichier compressé et des codes de Huffman
     *    dans un fichier texte séparé.
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors
     *                     de la lecture du fichier source,
     *                     de l'écriture du fichier compressé
     *                     ou des données des codes Huffman.
     */
    public void compresserFichier() throws IOException {
        byte[] chaine = GestionFichier.readFileToBytes(cheminFichierSource);
        Map<Byte, Integer> occurencesDesCaracteres =
                                               getFrequenceDesBytes(chaine);

        /* Calcul du nombre total d'occurrences de tous les caractères
         * dans le fichier source.
         * Cette opération est réalisée en plusieurs étapes
         * utilisant les flux (streams) Java :
         */
        int totalOccurrences =
                /* Créer un flux (stream) à partir des valeurs de la map.
                 * Chaque valeur dans cette map représente la fréquence
                 * d'occurrence d'un byte spécifique dans le fichier source.
                 */
                occurencesDesCaracteres.values().stream()
                /* Convertir le Stream<Integer> en un IntStream,
                 * permettant des opérations sur des entiers.
                 * La méthode `intValue()` est une méthode référence
                 * qui est utilisée pour convertir un Integer en int.
                 */
                .mapToInt(Integer::intValue)
                /* Additionner tous les entiers dans le IntStream
                 * pour obtenir le total des occurrences.
                 * La méthode `sum()` est une opération de réduction
                 * qui combine tous les éléments du flux pour produire
                 * une seule valeur.
                 */
                .sum();

        for (Entry<Byte, Integer> entry : occurencesDesCaracteres.entrySet()) {
            byte cle = entry.getKey();
            int occurrences = entry.getValue();
            double frequence = 100 * ((double) occurrences / totalOccurrences);
            char symbole = (char) cle;
            out.printf(    "Caractère : %c ; "
                       + "Occurrences : %d ; "
                       +  " Fréquence : %.2f%%\n",
                       symbole, occurrences, frequence);
        }

        Noeud[] tableau = new Noeud[occurencesDesCaracteres.size()];
        int entree = 0;
        for(Entry<Byte, Integer> encode : occurencesDesCaracteres.entrySet()) {
            byte key = encode.getKey();
            int value = encode.getValue();
            tableau[entree++] = new Noeud(key, value);
        }

        Noeud racine = construireArbre(tableau);
        Map<Byte, String> codageHuffman = racine.genererTableDeCodesHuffman();

        String[] tableHuffman = new String[codageHuffman.size()];
        int occ = 0;
        for(Entry<Byte, String> code : codageHuffman.entrySet()) {
            byte key = code.getKey();
            String value = code.getValue();
            tableHuffman[occ++] =   "codeHuffman = " + value + " ; "
                                  + "encode = "      + key + " ; "
                                  + "symbole = "     + (char) key;
        }

        String codage = encoder(chaine, codageHuffman);
        GestionFichier.ecrireChaineBinaireDansFichier(codage,
                                                      cheminFichierDestination);

        int dernierSeparateur = cheminFichierDestination.lastIndexOf("\\");
        String cheminDossier  = cheminFichierDestination
                                          .substring(0, dernierSeparateur + 1);
        cheminFichierDestination = cheminDossier  + "arbreHuffman.txt";

        GestionFichier.ecrireArbreHuffman(cheminFichierDestination,
                                          tableHuffman);
    }

    /**
     * Encode une chaîne de bytes en utilisant
     * un mappage spécifié de codes Huffman.
     * La méthode parcourt chaque byte du tableau
     * et utilise le mappage de Huffman pour convertir chaque byte
     * en sa représentation de chaîne de bits correspondante.
     * Ces chaînes de bits sont ensuite concaténées pour former
     * une seule chaîne binaire.
     *
     * @param chaineBinaire Le tableau de bytes à encoder.
     * @param codageHuffman Le mappage de chaque byte à sa chaîne
     *                      de code Huffman correspondante.
     * @return La chaîne binaire complète résultante de l'encodage de tous
     *         les bytes de la chaîne initiale.
     */
    private String encoder(byte[] chaineBinaire,
                           Map<Byte, String> codageHuffman) {

        StringBuilder chaineBinaireEncodee = new StringBuilder();
        for (byte b : chaineBinaire) {
            chaineBinaireEncodee.append(codageHuffman.get(b));
        }
        return chaineBinaireEncodee.toString();
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
             * par la méthode `getValue` de chaque objet `Noeud`.
             * Noeud::getValue est une référence de méthode qui renvoie
             * la valeur de fréquence du nœud.
             * Cette opération de tri place les noeuds avec
             * les plus petites valeurs (fréquences) en premier,
             * ce qui est nécessaire pour la construction de l'arbre de Huffman.
             */
            noeuds.sort(Comparator.comparingInt(Noeud::getValue));

            Noeud premierNoeud = noeuds.remove(0);
            Noeud deuxiemeNoeud = noeuds.remove(0);

            Noeud parent = new Noeud(premierNoeud.getValue()
                                     + deuxiemeNoeud.getValue());

            parent.setGauche(premierNoeud);
            parent.setDroite(deuxiemeNoeud);

            noeuds.add(parent);
        }
        return noeuds.get(0);
    }

    /**
     * Calcule et renvoie la fréquence de chaque byte dans un tableau de bytes.
     * Cette méthode utilise une HashMap pour stocker
     * et compter la fréquence de chaque byte rencontré.
     *
     * @param bytes Le tableau de bytes dont les fréquences
     *              doivent être calculées.
     * @return Une HashMap où chaque clé est un byte et chaque valeur
     *         est la fréquence de ce byte dans le tableau.
     */
    private HashMap<Byte, Integer> getFrequenceDesBytes(byte[] bytes) {
        /*
         * HashMap est une structure de données qui stocke les éléments
         * sous forme de paires clé-valeur.
         * Elle permet des insertions, des suppressions
         * et des accès en temps constant en moyenne.
         */
        HashMap<Byte, Integer> bytesFrequence = new HashMap<>();
        for (byte b : bytes) {
            bytesFrequence.put(b,
                               /*
                                * La méthode `getOrDefault` est utilisée pour
                                * obtenir la fréquence actuelle d'un byte
                                * (représenté par la variable 'b') dans
                                * la HashMap `bytesFrequence`.
                                * Si 'b' est déjà une clé dans la map,
                                * cette méthode retournera sa valeur associée.
                                * Si 'b' n'est pas encore une clé dans la map,
                                * `getOrDefault` retournera la valeur
                                * par défaut spécifiée, ici `0`.
                                * Ensuite, on ajoute 1 à cette valeur
                                * pour comptabiliser une nouvelle
                                * occurrence de 'b'.
                                */
                               bytesFrequence.getOrDefault(b, 0)
                               + 1);
        }
        return bytesFrequence;
    }
}
