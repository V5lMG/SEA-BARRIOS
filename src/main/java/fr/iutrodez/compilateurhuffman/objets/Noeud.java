package fr.iutrodez.compilateurhuffman.objets;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class Noeud {

    private byte caractere;
    private int frequence;
    private Noeud droite;
    private Noeud gauche;

    public Noeud(byte key, int frequence) {
        this.caractere = key;
        this.frequence = frequence;
    }

    public Noeud(int frequence) {
        this.frequence = frequence;
    }

    public Noeud() {

    }
    
    public int getValue() {
        return frequence;
    }

    public byte getCharacter() {
        return caractere;
    }

    public void setCaractere(byte caractere) {
        this.caractere = caractere;
    }

    public Noeud getDroite() {
        return droite;
    }

    public void setDroite(Noeud droite) {
        this.droite = droite;
    }

    public Noeud getGauche() {
        return gauche;
    }

    public void setGauche(Noeud gauche) {
        this.gauche = gauche;
    }

    public boolean hasEnfant() {
        return gauche != null || droite != null;
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean isFeuille() {
        return !hasEnfant();
    }

    private void generationRecursiveCodeHuffman(Noeud noeud, String chemin, Map<Byte, String> codes) {
        if (noeud == null) {
            return;
        }

        if (!noeud.hasEnfant()) {
            codes.put(noeud.getCharacter(), chemin);
            return;
        }

        if (noeud.getGauche() != null) {
            generationRecursiveCodeHuffman(noeud.getGauche(), chemin + "0", codes);
        }
        if (noeud.getDroite() != null) {
            generationRecursiveCodeHuffman(noeud.getDroite(), chemin + "1", codes);
        }
    }

    public Map<Byte, String> genererTableDeCodesHuffman() {
        Map<Byte, String> codes = new HashMap<>();
        generationRecursiveCodeHuffman(this, "", codes);
        return codes;
    }

    public void afficherArbre() {
        afficherArbreRecursive(this, "");
    }

    private void afficherArbreRecursive(Noeud noeud, String indent) {
        if (noeud == null) {
            return;
        }

        if (noeud.hasEnfant()) {
            out.println(indent + "Noeud (frequence: " + noeud.getValue() + ")");
        } else {
            out.println(indent + "Feuille (caractere : " + (char) noeud.getCharacter() + ", frequence : " + noeud.getValue() + ")");
        }

        if (noeud.getGauche() != null) {
            out.println(indent + "Gauche : ");
            afficherArbreRecursive(noeud.getGauche(), indent + "  ");
        }

        if (noeud.getDroite() != null) {
            out.println(indent + "Droite : ");
            afficherArbreRecursive(noeud.getDroite(), indent + "  ");
        }
    }
}

