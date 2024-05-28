/*
 * Pas de copyright, ni de droit d'auteur.
 * ArbreHuffman.java                       27/05/2024
 */
package fr.iutrodez.compilateurhuffman.objets;

import fr.iutrodez.compilateurhuffman.outils.OutilsAnalyseFichier;
import fr.iutrodez.compilateurhuffman.outils.OutilsGestionBinaire;

import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Classe utilisée pour représenter et manipuler un arbre de Huffman
 * permettant la compression et décompression de fichiers.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class ArbreHuffman {
    /**
     * Nœud racine de l'arbre de Huffman.
     */
    private static NoeudHuffman racine;

    /**
     * Crée un arbre de Huffman à partir des fréquences des caractères dans un fichier texte.
     *
     * @param cheminFichier le chemin d'accès au fichier texte
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    public ArbreHuffman(String cheminFichier) throws IOException {
        String contenu = OutilsAnalyseFichier.getContenuFichier(cheminFichier);
        Map<Character, Double> frequences = OutilsAnalyseFichier.getFrequences(contenu);
        racine = construireArbreHuffman(frequences);
    }

    /**
     * Trie l'arbre de Huffman dans l'ordre décroissant des fréquences des caractères et écrit le résultat.
     *
     * @param writer le flux d'écriture vers lequel écrire le résultat
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    public void trierArbre(BufferedWriter writer) throws IOException {
        List<NoeudHuffman> feuilles = getFeuilles();

        /*
         * Trie la liste 'feuilles' en fonction des fréquences de chaque nœud de Huffman, dans l'ordre décroissant.
         * Sort : https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html
         * "NoeudHuffman::getFrequence" fait référence à une méthode de l'objet NoeudHuffman.
         * Cette syntaxe est utilisée pour faire référence à une méthode d'instance ou statique d'une classe
         * sans l'appeler. La syntaxe :: est utilisée pour créer une référence à une méthode.
         * Cela signifie que NoeudHuffman::getFrequence fait référence à la méthode getFrequence
         * de l'objet NoeudHuffman. Cette référence à la méthode est utilisée pour fournir un comparateur
         * à la méthode Comparator.comparing, qui peut ensuite être utilisée pour trier une liste d'objets NoeudHuffman
         * en fonction de leur fréquence d'apparition.
         */
        feuilles.sort(
                Comparator.comparing(NoeudHuffman::getFrequence)  // Crée un comparateur basé sur les fréquences des nœuds
                        .reversed()  // Inverse l'ordre de tri pour qu'il soit décroissant
        );

        /*
         * Parcourt la liste des feuilles de l'arbre de Huffman pour l'afficher en console et l'écrire dans un fichier
         */
        for (NoeudHuffman feuille : feuilles) {
            String ligne = feuille.toString() + "\n";
            writer.write(ligne);
            System.out.print(ligne);
        }
    }

    /**
     * Collecte les feuilles de l'arbre de Huffman.
     *
     * @return la liste des feuilles de l'arbre
     */
    private List<NoeudHuffman> getFeuilles() {
        List<NoeudHuffman> feuilles = new ArrayList<>();
        collecterFeuilles(racine, feuilles);
        return feuilles;
    }

    /**
     * Parcourt l'arbre de Huffman et collecte les feuilles dans une liste.
     * Utilise une méthode récursive.
     *
     * @param noeud   le nœud courant
     * @param feuilles la liste dans laquelle les feuilles collectées sont stockées
     */
    private void collecterFeuilles(NoeudHuffman noeud, List<NoeudHuffman> feuilles) {
        if (noeud == null) {
            return;
        }

        if (noeud.estFeuille()) {
            feuilles.add(noeud);
        }

        collecterFeuilles(noeud.getGauche(), feuilles);
        collecterFeuilles(noeud.getDroite(), feuilles);
    }

    /**
     * Crée un arbre de Huffman à partir d'une map des fréquences des caractères.
     * Utilise une méthode récursive.
     *
     * @param frequences une map associant chaque caractère à sa fréquence d'apparition
     * @return le nœud racine de l'arbre de Huffman construit
     */
    private NoeudHuffman construireArbreHuffman(Map<Character, Double> frequences) {
        List<NoeudHuffman> feuilles = new ArrayList<>();

        /*
         * Crée une feuille pour chaque caractère et sa fréquence, et les ajoute à la liste.
         * Pour cela, on utilise une Map.
         * Une Map est une structure de données qui associe des clés à des valeurs.
         * Ici, la clé est de type Character (caractère) et la valeur est de type Double (fréquence).
         * La méthode entrySet() de la Map retourne un ensemble de paires clé-valeur,
         * où chaque paire est un objet Map.Entry. Un Map.Entry représente une paire clé-valeur dans une Map.
         * La boucle for itère à travers chaque entrée de la Map (chaque caractère avec sa fréquence).
         * Pour chaque entrée, un nouveau NoeudHuffman est créé avec le caractère comme clé
         * et la fréquence comme valeur, puis ajouté à la liste des feuilles.
         */
        for (Map.Entry<Character, Double> entree : frequences.entrySet()) {
            feuilles.add(new NoeudHuffman(entree.getKey(), entree.getValue()));
        }

        /*
         * Continuer jusqu'à ce qu'il ne reste qu'un seul nœud dans la liste (le nœud racine)
         */
        while (feuilles.size() > 1) {
            /*
             * On retrouve l'utilisation d'un comparateur et de la référence à la méthode getFrequence
             * expliqué ci-dessus.
             * Récupère et supprime les deux premiers nœuds de la liste (les deux avec les fréquences les plus basses).
             */
            feuilles.sort(Comparator.comparingDouble(NoeudHuffman::getFrequence));
            NoeudHuffman noeudMin1 = feuilles.removeFirst();
            NoeudHuffman noeudMin2 = feuilles.removeFirst();

            /*
             * Crée un nouveau nœud en combinant les deux nœuds précédents et l'ajoute à la liste
             */
            NoeudHuffman nouveauNoeud = new NoeudHuffman(noeudMin1, noeudMin2);
            feuilles.add(nouveauNoeud);
        }
        setCodesHuffman(feuilles.getFirst(), "");
        return feuilles.getFirst();
    }

    /**
     * Attribue un code Huffman à chaque nœud de l'arbre.
     * Utilisation de méthode récursive.
     *
     * @param noeud le nœud courant auquel il faut attribuer un code
     * @param code  le code Huffman préfixe du nœud
     */
    private void setCodesHuffman(NoeudHuffman noeud, String code) {
        if (noeud == null) {
            return;
        }
        noeud.setCodeHuffman(code);
        setCodesHuffman(noeud.getGauche(), code + "1");
        setCodesHuffman(noeud.getDroite(), code + "0");
    }

    /**
     * Renvoie une map associant chaque caractère à son code Huffman.
     *
     * @return une Map contenant les associations entre les caractères et leurs codes Huffman.
     */
    public static Map<Character, String> getCodesHuffman() {
        /*
         * Une Map est une structure de données qui associe des clés à des valeurs.
         *
         * Il existe différentes implémentations de l'interface Map.
         * HashMap est l'une des implémentations les plus couramment utilisées.
         * HashMap est une classe qui implémente l'interface Map en utilisant une table de hachage
         * pour stocker les données. Cette implémentation offre généralement des performances élevées
         * pour les opérations de recherche, d'insertion et de suppression.
         *
         * Contrairement à d'autres implémentations de Map telles que TreeMap,
         * HashMap ne garantit pas un ordre spécifique des éléments.
         * Les éléments dans HashMap sont stockés de manière désordonnée.
         *
         * La méthode collecterCodesHuffman() est appelée pour parcourir l'arbre de Huffman et collecter les codes Huffman de chaque nœud dans la Map.
         * Une fois que tous les codes Huffman sont collectés, la Map est retournée.
         */
        Map<Character, String> codesHuffman = new HashMap<>();
        collecterCodesHuffman(racine, codesHuffman);
        return codesHuffman;
    }

    /**
     * Parcourt l'arbre de Huffman et collecte les codes Huffman de chaque nœud dans une map.
     * Utilisation de méthode recursive.
     *
     * @param noeud        le nœud courant à partir duquel collecter le code Huffman
     * @param codesHuffman la map dans laquelle stocker les codes Huffman collectés
     */
    private static void collecterCodesHuffman(NoeudHuffman noeud, Map<Character, String> codesHuffman) {
        if (noeud == null) {
            return;
        }

        if (noeud.estFeuille()) {
            codesHuffman.put(noeud.getCaractere(), noeud.getCodeHuffman());
        }

        collecterCodesHuffman(noeud.getGauche(), codesHuffman);
        collecterCodesHuffman(noeud.getDroite(), codesHuffman);
    }

    /**
     * Encode une chaîne de caractères en binaire en utilisant les codes de l'arbre Huffman.
     *
     * @param contenu la chaîne de caractères à encoder.
     * @return un tableau d'octets représentant la chaîne de caractères encodée.
     */
    public byte[] encoderFichier(String contenu) {
        String contenuEncode = OutilsGestionBinaire.convertirCaractereEnBinaire(contenu);
        return OutilsGestionBinaire.convertirBinaireEnBytes(contenuEncode);
    }

    /* *============================================================*
     *
     *                          DÉCODAGE
     *
     * *============================================================*
     */
    /**
     * Décode un tableau d'octets en une chaîne de caractères en utilisant l'arbre Huffman fourni.
     *
     * @param arbreHuffman l'arbre de Huffman utilisé pour décoder.
     * @param bytesADecompiler le tableau d'octets à décoder.
     * @return la chaîne de caractères décodée.
     */
    public static String decoderFichier(List<String> arbreHuffman, String bytesADecompiler) {
        /*
         * Utilisation de Map bien décrite ci-dessus.
         */
        Map<String, Character> huffmanMap = creerHuffmanMap(arbreHuffman);
        return OutilsGestionBinaire.decoderBytes(huffmanMap, bytesADecompiler);
    }

    /**
     * Crée une map associant les codes Huffman à leurs caractères correspondants.
     *
     * @param arbreHuffman l'arbre de Huffman sous forme de liste de chaînes.
     * @return une map des codes Huffman et des caractères correspondants.
     */
    private static Map<String, Character> creerHuffmanMap(List<String> arbreHuffman) {
        /*
         * Utilisation de HashMap bien décrite au-dessus.
         */
        Map<String, Character> huffmanMap = new HashMap<>();
        for (String entry : arbreHuffman) {
            String[] parts = entry.split(" ; ");
            if (parts.length == 3) {
                String codeHuffman = parts[0].split(" = ")[1];
                char symbole = parts[2].split(" = ")[1].charAt(0);
                huffmanMap.put(codeHuffman, symbole);
            }
        }
        return huffmanMap;
    }
}