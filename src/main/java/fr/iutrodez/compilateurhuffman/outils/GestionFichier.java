/*
 * Pas de copyright, ni de droit d'auteur.
 * GestionFichier.java                       27/05/2024
 */
package fr.iutrodez.compilateurhuffman.outils;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;

/**
 * Classe de gestion des fichiers pour les opérations de lecture et écriture
 * nécessaires à la compression et décompression de Huffman.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class GestionFichier {

    /**
     * Lit tout le contenu d'un fichier texte et le renvoie sous forme
     * de tableau de chaînes de caractères.
     *
     * @param cheminFichier Le chemin du fichier à lire.
     * @return Un tableau de chaînes contenant toutes les lignes du fichier.
     */
    public static String[] lireToutLeFichier(String cheminFichier) {
        // Calculer le nombre de lignes pour dimensionner le tableau
        int nombreDeLignes = 0;
        try (BufferedReader compteurLignes =
                     new BufferedReader(new FileReader(cheminFichier))) {

            while (compteurLignes.readLine() != null) {
                nombreDeLignes++;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la comptabilisation des lignes "
                               + "du fichier : " + e.getMessage());
            return new String[0];
        }

        String[] lignes = new String[nombreDeLignes];

        try (BufferedReader lecteur =
                     new BufferedReader(new FileReader(cheminFichier))) {

            String ligne;
            int index = 0;
            while ((ligne = lecteur.readLine()) != null) {
                lignes[index++] = ligne;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : "
                               + e.getMessage());
            return new String[0];
        }
        return lignes;
    }

    /**
     * Écrit les lignes d'un arbre de Huffman dans un fichier texte.
     *
     * @param cheminFichier Le chemin du fichier à écrire.
     * @param arbreHuffman  Un tableau de chaînes représentant les lignes
     *                      de l'arbre de Huffman.
     */
    public static void ecrireArbreHuffman(String cheminFichier,
                                          String[] arbreHuffman) {

        try (BufferedWriter writer = new BufferedWriter(
                                           new FileWriter(cheminFichier))) {

            for (String ligne : arbreHuffman) {
                writer.write(ligne);
                writer.newLine();
            }
        } catch (IOException e) {
            err.println("Erreur lors de l'écriture dans le fichier : "
                        + e.getMessage());
        }
    }

    /**
     * Écrit une chaîne binaire dans un fichier binaire.
     *
     * @param chaineBinaire La chaîne binaire à écrire.
     * @param cheminFichier Le chemin du fichier de destination.
     */
    public static void ecrireChaineBinaireDansFichier(String chaineBinaire,
                                                      String cheminFichier)
            throws IOException {

        /*
         * Utilisation de streams pour écrire les données dans un fichier :
         * - FileOutputStream : un flux d'octets utilisé pour écrire des données
         *                      dans un fichier.
         * - DataOutputStream : un flux de données utilisé pour écrire des
         *                      données primitives de manière portable.
         *                      Il permet d'écrire des types de données tels
         *                      que des int et des octets dans le flux de sortie.
         */
        try (FileOutputStream streamDeSortie = new FileOutputStream(
                cheminFichier);
             DataOutputStream streamDeDonnees = new DataOutputStream(
                     streamDeSortie
             )) {

            streamDeDonnees.writeInt(chaineBinaire.length());

            int longueurOctet = (chaineBinaire.length() + 7) / 8;
            byte[] tableauOctets = new byte[longueurOctet];
            for (int i = 0; i < chaineBinaire.length(); i++) {
                if (chaineBinaire.charAt(i) == '1') {
                    /*
                     * Cette ligne convertit un bit de la chaîne binaire en
                     * son équivalent dans un tableau d'octets.
                     *
                     * - `i / 8` : Calcule l'index de l'octet où placer le bit.
                     *   Chaque octet contient 8 bits, donc diviser l'index du
                     *   bit par 8 donne l'index de l'octet.
                     *
                     * - `1 << (7 - (i % 8))` : Utilise le décalage à gauche
                     *   pour positionner un bit 1 à la position correcte dans
                     *   l'octet. `(i % 8)` donne la position du bit dans
                     *   l'octet (0 à 7), et `7 - (i % 8)` calcule la position
                     *   de gauche à droite.
                     *
                     * - `|= (byte)` : Effectue un OR bit à bit entre l'octet
                     *   actuel et le résultat du décalage pour positionner
                     *   le bit sans altérer les autres bits.
                     */
                    tableauOctets[i / 8] |= (byte) (1 << (7 - (i % 8)));
                }
            }
            streamDeDonnees.write(tableauOctets);
        }
    }

    /**
     * Lit le contenu d'un fichier binaire et le renvoie sous forme de chaîne.
     *
     * @param cheminFichier Le chemin du fichier à lire.
     * @return Une chaîne représentant le contenu binaire du fichier.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public static String lireFichierBinaire(String cheminFichier)
            throws IOException {
        /*
         * Utilisation de streams pour lire les données depuis un fichier.
         */
        StringBuilder chaineBinaire = new StringBuilder();

        try (FileInputStream streamDeFichier = new FileInputStream(
                cheminFichier
        );
             DataInputStream streamDeDonnees = new DataInputStream(
                     streamDeFichier
             )) {

            int longueurBits = streamDeDonnees.readInt();
            int longueurOctets = (longueurBits + 7) / 8;
            byte[] tableauOctets = new byte[longueurOctets];
            streamDeDonnees.readFully(tableauOctets);

            for (int i = 0; i < longueurBits; i++) {
                int indexOctet = i / 8;
                int indexBit = i % 8;

                /*
                 * Cette condition vérifie si le bit à la position
                 * (indexOctet * 8 + indexBit) est 1 ou 0.
                 *
                 * - `(128 >> indexBit)` : Déplace le bit 1 vers la droite.
                 *   Par exemple, si indexBit est 0, cela donne 128
                 *   (10000000 en binaire).
                 *   Si indexBit est 1, cela donne 64 (01000000 en binaire).
                 *
                 * - `tableauOctets[indexOctet] & (128 >> indexBit)` :
                 *   Effectue un AND bit à bit entre l'octet et le masque.
                 *   Si le résultat est différent de 0, cela signifie que le
                 *   bit à cette position est 1.
                 *
                 * - `chaineBinaire.append('1')` ou `chaineBinaire.append('0')`:
                 *   Ajoute '1' ou '0' à la chaîne binaire en fonction du
                 *   résultat du test précédent.
                 */
                if ((tableauOctets[indexOctet] & (128 >> indexBit)) != 0) {
                    chaineBinaire.append('1');
                } else {
                    chaineBinaire.append('0');
                }
            }
        }
        return chaineBinaire.toString();
    }

    /**
     * Lit le contenu d'un fichier et le renvoie sous forme de tableau de octets.
     *
     * @param cheminFichier Le chemin du fichier à lire.
     * @return Un tableau de octets contenant le contenu du fichier.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public static byte[] recupererOctets(String cheminFichier)
            throws IOException {
        try (FileInputStream streamEntreeFichier =
                                          new FileInputStream(cheminFichier)) {
            return streamEntreeFichier.readAllBytes();
        }
    }

    /**
     * Écrit un tableau de octets dans un fichier.
     *
     * @param donnees       Le tableau de octets à écrire.
     * @param cheminFichier Le chemin du fichier de destination.
     */
    public static void ecrireFichierDestination(byte[] donnees,
                                                String cheminFichier) {
        try (BufferedOutputStream streamDeSortie = new BufferedOutputStream(
                new FileOutputStream(cheminFichier))) {
            streamDeSortie.write(donnees);
        } catch (IOException e) {
            err.println("Erreur lors de l'écriture dans le fichier : "
                        + e.getMessage());
        }
    }
}