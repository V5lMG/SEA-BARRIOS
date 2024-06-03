package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.objets.Noeud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestNoeud {

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

    @Test
    void testConstructeurAvecFrequenceSeulement() {
        int frequence = 10;
        Noeud noeud = new Noeud(frequence);

        assertEquals(frequence, noeud.getFrequence());
        assertNull(noeud.getCaractere());
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
    }

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

    @Test
    void testIsFeuille() {
        Noeud feuille = new Noeud();
        assertTrue(feuille.isFeuille());

        Noeud nonFeuille = new Noeud();
        nonFeuille.setGauche(new Noeud());
        assertFalse(nonFeuille.isFeuille());
    }
}

