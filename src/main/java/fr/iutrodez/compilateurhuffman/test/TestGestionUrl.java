package fr.iutrodez.compilateurhuffman.test;

import fr.iutrodez.compilateurhuffman.outils.GestionUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionUrl {

    private GestionUrl gestionUrl;

    @BeforeEach
    void setUp() {
        gestionUrl = new GestionUrl();
    }

    @Test
    void testEnleverGuillemet() {
        String chemin = "\"chemin/vers/le/fichier.txt\"";
        String resultat = gestionUrl.enleverGuillemet(chemin);
        assertEquals("chemin/vers/le/fichier.txt", resultat);
    }

    @Test
    void testGetNomFichierDestinationUnique() {
        String input = "nouveauFichier\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String nomFichier = gestionUrl.getNomFichierDestinationUnique(
                new Scanner(System.in),
                "/chemin/vers/le/repertoire"
        );
        assertEquals("nouveauFichier", nomFichier);
    }

    @Test
    void testGetNomFichierDestinationUnique_FichierExistant() {
        assertThrows(RuntimeException.class, () -> {
            String input = "fichierExistant\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            gestionUrl.getNomFichierDestinationUnique(
                    new Scanner(System.in),
                    "/chemin/vers/le/repertoire"
            );
        });
    }

    @Test
    void testGetCheminDestination() {
        String input = "/chemin/vers/le/repertoire\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String chemin = gestionUrl.getCheminDestination(new Scanner(System.in));
        assertEquals("/chemin/vers/le/repertoire", chemin);
    }

    @Test
    void testGetCheminDestination_RepertoireInvalide() {
        assertThrows(RuntimeException.class, () -> {
            String input = "/chemin/invalide\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            gestionUrl.getCheminDestination(new Scanner(System.in));
        });
    }

    @Test
    void testVerifieCheminFichierSourceValide() {
        assertThrows(IOException.class, () -> {
            gestionUrl.verifierCheminFichierSourceValide("", "txt");
        });

        assertThrows(IOException.class, () -> {
            gestionUrl.verifierCheminFichierSourceValide("/chemin/vers/le/fichier.bin", "txt");
        });

        assertThrows(IOException.class, () -> {
            gestionUrl.verifierCheminFichierSourceValide("/chemin/vers/le/fichier.txt", "bin");
        });
    }
}
