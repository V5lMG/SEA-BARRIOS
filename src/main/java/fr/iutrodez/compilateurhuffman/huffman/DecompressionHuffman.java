/*
 * Pas de copyright, ni de droit d'auteur.
 * DecompressionHuffman.java                 27/05/2024
 */
package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.outils.GestionFichier;
import fr.iutrodez.compilateurhuffman.objets.Noeud;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe permettant la décompression de fichiers
 * utilisant l'algorithme de Huffman.
 * Cette classe prend en charge le processus de décompression
 * d'un fichier compressé, en utilisant un arbre de Huffman
 * stocké séparément pour déterminer la table des codes
 * utilisée lors de la compression.
 * Le processus inclut la lecture d'un fichier binaire compressé,
 * la reconstruction de l'arbre de Huffman à partir d'un fichier texte externe,
 * et la transformation des données binaires compressées
 * en leur format d'origine.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class DecompressionHuffman {

    /**
     * Chemin vers le fichier compressé qui doit être décompressé.
     */
    private String cheminFichierSource;

    /**
     * Chemin de destination où le fichier décompressé sera sauvegardé.
     */
    private String cheminFichierDestination;

    /**
     * Construit une instance de DecompressionHuffman avec
     * les chemins spécifiés pour le fichier source et destination.
     *
     * @param cheminFichierSource Le chemin vers le fichier
     *                            compressé à décompresser.
     * @param cheminFichierDestination Le chemin où le fichier
     *                                 décompressé sera sauvegardé.
     */
    public DecompressionHuffman(String cheminFichierSource,
                                String cheminFichierDestination) {
        this.cheminFichierSource = cheminFichierSource;
        this.cheminFichierDestination = cheminFichierDestination;
    }

    /**
     * Décompresse le fichier spécifié à l'aide de l'arbre de Huffman.
     * Le fichier d'arbre Huffman doit être présent dans le même répertoire
     * que le fichier compressé et nommé "arbreHuffman.txt".
     * Lance une IOException si le fichier de l'arbre n'est pas trouvé ou
     * si une erreur de lecture/écriture survient.
     *
     * @throws IOException Si une erreur se produit pendant la lecture
     *                     du fichier ou lors de la reconstruction de l'arbre.
     */
    public void decompresserFichier() throws IOException {

        String code = GestionFichier
                               .lireFichierBinaireEnChaine(cheminFichierSource);

        /*
         * Trouve la position du dernier séparateur de dossier
         * dans le chemin du fichier source.
         * Utilise la position du dernier séparateur pour obtenir le chemin
         * du dossier contenant le fichier source.
         * Ensuite, construit le chemin complet vers le fichier de l'arbre
         * de Huffman en ajoutant "arbreHuffman.txt" au chemin du dossier.
         */
        int dernierSeparateur = cheminFichierSource.lastIndexOf("\\");
        String cheminArbre = cheminFichierSource
                                            .substring(0, dernierSeparateur + 1)
                             + "arbreHuffman.txt";

        File fichierArbreHuffman = new File(cheminArbre);
        if (!fichierArbreHuffman.exists()) {
            throw  new IOException("Erreur: fichier de l'arbre d'Huffman "
                                   + "introuvable. Veuillez le placer dans "
                                   + "le même répertoire que le fichier à "
                                   + "décompresser avec le nom "
                                   + "\"arbreHuffman.txt\"");
        }

        String[] arbreHuffman = GestionFichier.lireToutLeFichier(cheminArbre);
        Map<Byte, String> codageHuffman = genererTableDeCode(arbreHuffman);
        Noeud racine = construireArbreHuffmanDepuisMap(codageHuffman);

        byte[] bytes = transformerChaineEnBytes(code, racine);
        GestionFichier.ecrireFichierDestination(bytes,
                                                cheminFichierDestination);
    }

    /**
     * Génère une table de codage Huffman à partir des données
     * de l'arbre Huffman. Chaque ligne de l'arbre contient un
     * mappage entre un byte et son code Huffman correspondant.
     *
     * @param arbre Un tableau de chaînes représentant l'arbre Huffman.
     *              Chaque élément contient un byte et son code Huffman
     *              séparés par un point-virgule.
     * @return Une map où chaque clé est un byte et chaque valeur
     *         est le code Huffman correspondant.
     */
    private Map<Byte, String> genererTableDeCode(String[] arbre) {
        /*
         * Utilisation d'une HashMap, la description de HashMap
         * a été faites dans CompressionHuffman.java
         */
        Map<Byte, String> tableDeCodage = new HashMap<>();
        for (String line : arbre) {
            if (!line.isBlank()) {
                String[] lineSplit = line.split(";");

                Byte key = Byte.valueOf(lineSplit[1]
                                             .split("=")[1]
                                             .trim());

                String value = lineSplit[0].split("=")[1].trim();
                tableDeCodage.put(key, value);
            }
        }
        return tableDeCodage;
    }

    /**
     * Construit un arbre de Huffman à partir d'une map de codes Huffman.
     * Chaque entrée de la map représente un caractère et son chemin binaire
     * correspondant dans l'arbre de Huffman.
     *
     * @param codeMap Une map où chaque clé est un byte (caractère)
     *                et chaque valeur est une chaîne de bits représentant
     *                le chemin dans l'arbre de Huffman.
     * @return La racine de l'arbre de Huffman reconstruit.
     */
    private static Noeud construireArbreHuffmanDepuisMap(
                                                      Map<Byte,String> codeMap
                                                        ) {
        Noeud racine = new Noeud();

        /*
         * Parcours de chaque entrée de la map
         * Map.Entry<Byte, String> : Représente une association clé-valeur
         *                           dans la map.
         * entrySet() : Renvoie un ensemble des entrées
         *              (associations clé-valeur) contenues dans la map.
         */
        for (Map.Entry<Byte, String> entry : codeMap.entrySet()) {

            // getKey() : Renvoie la clé de l'entrée actuelle.
            byte caractere = entry.getKey();

            // getValue() : Renvoie la valeur de l'entrée actuelle.
            String chemin = entry.getValue();

            Noeud noeudCourant = racine;
            // toCharArray() : Convertit la chaîne en tableau de caractères.
            for (char direction : chemin.toCharArray()) {
                if (direction == '0')
                {
                    if (noeudCourant.getGauche() == null) {
                        noeudCourant.setGauche(new Noeud());
                    }
                    noeudCourant = noeudCourant.getGauche();
                }
                else if (direction == '1')
                {
                    if (noeudCourant.getDroite() == null) {
                        noeudCourant.setDroite(new Noeud());
                    }
                    noeudCourant = noeudCourant.getDroite();
                }
            }
            noeudCourant.setCaractere(caractere);
        }
        return racine;
    }

    /**
     * Transforme une chaîne binaire en un tableau de bytes en utilisant
     * l'arbre de Huffman fourni.
     * Chaque chemin binaire dans la chaîne représente un caractère dans
     * l'arbre de Huffman.
     *
     * @param bits La chaîne binaire à transformer en bytes.
     * @param racine La racine de l'arbre de Huffman utilisé pour
     *               la transformation.
     * @return Un tableau de bytes représentant les caractères décodés.
     */
    private static byte[] transformerChaineEnBytes(String bits, Noeud racine) {

        // Fonctionnement d'ArrayList décrite dans la classe CompressionHuffman
        List<Byte> byteList = new ArrayList<>();
        Noeud noeudCourant = racine;

        for (char bit : bits.toCharArray()) {
            /*
             * Opérateur ternaire est utilisé pour décider si le `noeudCourant`
             * doit se déplacer vers son enfant gauche ou droit.
             * La condition est basée sur la valeur du bit actuel.
             *
             * Si le bit actuel est '0', alors `noeudCourant` est mis à jour
             * pour être son enfant gauche (`noeudCourant.getGauche()`).
             * Sinon, si le bit actuel est '1', alors `noeudCourant` est
             * mis à jour pour être son enfant droit.
             */
            noeudCourant = (bit == '0') ? noeudCourant.getGauche()
                                        : noeudCourant.getDroite();

            if (noeudCourant.isFeuille()) {
                byteList.add(noeudCourant.getCaractere());
                noeudCourant = racine;
            }
        }

        /* Conversion de la liste de bytes en tableau de bytes */
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }

        return result;
    }
}