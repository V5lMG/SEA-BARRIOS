/*
 * Pas de copyright, ni de droit d'auteur.
 * Noeud.java                                27/05/2024
 */
package fr.iutrodez.compilateurhuffman.objets;

import java.util.HashMap;
import java.util.Map;

/**
 * Représente un nœud dans l'arbre de Huffman.
 * Chaque nœud peut avoir un caractère, une fréquence,
 * un enfant gauche et un enfant droit.
 * Cette classe inclut des méthodes pour générer des codes de Huffman
 * pour les feuilles de l'arbre.
 *
 * @author ValMG, R. Xaviertaborda, J. Seychelles, B. Thenieres
 * @version 1.0
 */
public class Noeud {
    /**
     * Le caractère représenté par ce nœud.
     */
    private byte caractere;

    /**
     * La fréquence d'apparition de ce caractère.
     */
    private int frequence;

    /**
     * L'enfant droit de ce nœud.
     */
    private Noeud droite;

    /**
     * L'enfant gauche de ce nœud.
     */
    private Noeud gauche;

    /**
     * Construit un nœud avec un caractère et une fréquence spécifiés.
     *
     * @param cle Le caractère représenté par ce nœud.
     * @param frequence La fréquence d'apparition de ce caractère.
     */
    public Noeud(byte cle, int frequence) {
        this.caractere = cle;
        this.frequence = frequence;
    }

    /**
     * Construit un nœud avec une fréquence spécifiée.
     * Utilisé pour les noeuds intermédiaires dans l'arbre de Huffman.
     *
     * @param frequence La fréquence d'apparition des caractères
     *                  dans ce sous-arbre.
     */
    public Noeud(int frequence) {
        this.frequence = frequence;
    }

    /**
     * Construit un nœud vide.
     */
    public Noeud() {

    }

    /**
     * Retourne la fréquence d'apparition du caractère représenté par ce nœud.
     *
     * @return La fréquence d'apparition du caractère.
     */
    public int getValue() {
        return frequence;
    }

    /**
     * Retourne le caractère représenté par ce nœud.
     *
     * @return Le caractère représenté par ce nœud.
     */
    public byte getCaractere() {
        return caractere;
    }

    /**
     * Définit le caractère représenté par ce nœud.
     *
     * @param caractere Le caractère à définir.
     */
    public void setCaractere(byte caractere) {
        this.caractere = caractere;
    }

    /**
     * Retourne l'enfant droit de ce nœud.
     *
     * @return L'enfant droit de ce nœud.
     */
    public Noeud getDroite() {
        return droite;
    }

    /**
     * Définit l'enfant droit de ce nœud.
     *
     * @param droite L'enfant droit à définir.
     */
    public void setDroite(Noeud droite) {
        this.droite = droite;
    }

    /**
     * Retourne l'enfant gauche de ce nœud.
     *
     * @return L'enfant gauche de ce nœud.
     */
    public Noeud getGauche() {
        return gauche;
    }

    /**
     * Définit l'enfant gauche de ce nœud.
     *
     * @param gauche L'enfant gauche à définir.
     */
    public void setGauche(Noeud gauche) {
        this.gauche = gauche;
    }

    /**
     * Vérifie si le nœud a des enfants.
     *
     * @return true si le nœud a au moins un enfant (gauche ou droite),
     *         false sinon.
     */
    public boolean hasEnfant() {
        return gauche != null || droite != null;
    }

    /**
     * Vérifie si ce nœud est une feuille (n'a pas d'enfants).
     *
     * @return true si ce nœud est une feuille, sinon false.
     */
    public boolean isFeuille() {
        return !hasEnfant();
    }

    /**
     * Génère récursivement les codes de Huffman pour chaque caractère
     * dans l'arbre de Huffman.
     *
     * @param noeud Le nœud courant dans la récursion.
     * @param chemin Le chemin binaire actuel.
     * @param codes La map de codes de Huffman à remplir.
     */
    private void generationRecursiveCodeHuffman(Noeud noeud,
                                                String chemin,
                                                Map<Byte, String> codes) {
        if (noeud == null) {
            return;
        }

        if (!noeud.hasEnfant()) {
            codes.put(noeud.getCaractere(), chemin);
            return;
        }

        if (noeud.getGauche() != null) {
            generationRecursiveCodeHuffman(noeud.getGauche(),
                                           chemin + "0",
                                           codes);
        }
        if (noeud.getDroite() != null) {
            generationRecursiveCodeHuffman(noeud.getDroite(),
                                           chemin + "1",
                                           codes);
        }
    }

    /**
     * Génère une table de codes de Huffman pour les caractères dans cet arbre.
     *
     * @return Une map où chaque clé est un byte (caractère) et chaque valeur
     *         est la chaîne binaire représentant le code de Huffman.
     */
    public Map<Byte, String> genererTableDeCodesHuffman() {
        Map<Byte, String> codes = new HashMap<>();
        generationRecursiveCodeHuffman(this, "", codes);
        return codes;
    }
}
