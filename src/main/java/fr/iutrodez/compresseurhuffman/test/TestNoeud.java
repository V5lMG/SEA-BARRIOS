package fr.iutrodez.compresseurhuffman.test;

import fr.iutrodez.compilateurhuffman.objets.Noeud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestNoeud {

    /**
     * Teste le constructeur de la classe Noeud avec caractère et fréquence.
     * Vérifie que le nœud est correctement initialisé avec le caractère, la fréquence et les enfants à null.
     */
    @Test
    void testConstructeurAvecCaractereEtFrequence() {
        byte caractere = 'a';
        int frequence = 5;
        Noeud noeud = new Noeud(caractere, frequence);

        assertEquals(caractere, noeud.getCaractere());
        assertEquals(frequence, noeud.getFrequence());
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
    }

    /**
     * Teste le constructeur de la classe Noeud avec fréquence seulement.
     * Vérifie que le nœud est correctement initialisé avec la fréquence et les enfants à null.
     */
    @Test
    void testConstructeurAvecFrequenceSeulement() {
        int frequence = 10;
        Noeud noeud = new Noeud(frequence);

        assertEquals(frequence, noeud.getFrequence());
        assertNull(noeud.getCaractere());
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
    }

    /**
     * Teste les getters et setters de la classe Noeud.
     * Vérifie que les getters retournent les valeurs correctes et que les setters définissent les valeurs correctement.
     */
    @Test
    void testGettersAndSetters() {
        byte caractere = 'a';
        int frequence = 5;
        Noeud noeud = new Noeud();

        noeud.setCaractere(caractere);
        assertEquals(caractere, noeud.getCaractere());

        assertEquals(frequence, noeud.getFrequence());

        Noeud gauche = new Noeud();
        Noeud droite = new Noeud();
        noeud.setGauche(gauche);
        assertEquals(gauche, noeud.getGauche());
        noeud.setDroite(droite);
        assertEquals(droite, noeud.getDroite());
    }

    /**
     * Teste la méthode isFeuille de la classe Noeud.
     * Vérifie que la méthode renvoie true si le nœud est une feuille et false sinon.
     */
    @Test
    void testIsFeuille() {
        Noeud feuille = new Noeud();
        assertTrue(feuille.isFeuille());

        Noeud nonFeuille = new Noeud();
        nonFeuille.setGauche(new Noeud());
        assertFalse(nonFeuille.isFeuille());
    }
}

