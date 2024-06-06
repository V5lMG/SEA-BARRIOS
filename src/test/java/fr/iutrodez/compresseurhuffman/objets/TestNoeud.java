package fr.iutrodez.compresseurhuffman.objets;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestNoeud {

    @Test
    public void testConstructorWithCharacterAndFrequency() {
        byte caractere = 'a';
        int frequence = 5;
        Noeud noeud = new Noeud(caractere, frequence);

        assertEquals(caractere, noeud.getCaractere());
        assertEquals(frequence, noeud.getFrequence());
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
    }

    @Test
    public void testConstructorWithFrequency() {
        int frequence = 10;
        Noeud noeud = new Noeud(frequence);

        assertEquals(frequence, noeud.getFrequence());
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
    }

    @Test
    public void testDefaultConstructor() {
        Noeud noeud = new Noeud();

        assertEquals(0, noeud.getFrequence());
        assertNull(noeud.getGauche());
        assertNull(noeud.getDroite());
    }

    @Test
    public void testGettersAndSetters() {
        Noeud noeud = new Noeud();

        byte caractere = 'b';
        noeud.setCaractere(caractere);
        assertEquals(caractere, noeud.getCaractere());

        int frequence = 15;
        noeud = new Noeud(frequence);
        assertEquals(frequence, noeud.getFrequence());

        Noeud gauche = new Noeud();
        Noeud droite = new Noeud();

        noeud.setGauche(gauche);
        assertEquals(gauche, noeud.getGauche());

        noeud.setDroite(droite);
        assertEquals(droite, noeud.getDroite());
    }

    @Test
    public void testIsFeuille() {
        Noeud feuille = new Noeud((byte) 'c', 1);
        assertTrue(feuille.isFeuille());

        Noeud nonFeuille = new Noeud(10);
        nonFeuille.setGauche(new Noeud());
        nonFeuille.setDroite(new Noeud());
        assertFalse(nonFeuille.isFeuille());
    }
}
