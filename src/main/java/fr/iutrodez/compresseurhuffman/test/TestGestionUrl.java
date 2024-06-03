package fr.iutrodez.compresseurhuffman.test;

import fr.iutrodez.compilateurhuffman.outils.GestionUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionUrl {

    private GestionUrl gestionUrl;

    /**
     * Initialise un nouvel objet GestionUrl avant chaque test.
     */
    @BeforeEach
    void setUp() {
        gestionUrl = new GestionUrl();
    }

    /**
     * Teste la méthode enleverGuillemet de la classe GestionUrl.
     * Vérifie que les guillemets sont correctement enlevés du chemin spécifié.
     */
    @Test
    void testEnleverGuillemet() {
        String chemin = "\"chemin/vers/le/fichier.txt\"";
        String resultat = gestionUrl.enleverGuillemet(chemin);
        assertEquals("chemin/vers/le/fichier.txt", resultat);
    }

    /**
     * Teste la méthode getNomFichierDestinationUnique de la classe GestionUrl.
     * Vérifie que le nom de fichier unique est correctement récupéré.
     */
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

    /**
     * Teste la méthode getNomFichierDestinationUnique de la classe GestionUrl pour un fichier existant.
     * Vérifie qu'une exception est levée lorsque le nom de fichier existe déjà.
     */
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

    /**
     * Teste la méthode getCheminDestination de la classe GestionUrl.
     * Vérifie que le chemin de destination est correctement récupéré.
     */
    @Test
    void testGetCheminDestination() {
        String input = "/chemin/vers/le/repertoire\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String chemin = gestionUrl.getCheminDestination(new Scanner(System.in));
        assertEquals("/chemin/vers/le/repertoire", chemin);
    }

    /**
     * Teste la méthode getCheminDestination de la classe GestionUrl pour un répertoire invalide.
     * Vérifie qu'une exception est levée lorsque le répertoire de destination est invalide.
     */
    @Test
    void testGetCheminDestination_RepertoireInvalide() {
        assertThrows(RuntimeException.class, () -> {
            String input = "/chemin/invalide\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            gestionUrl.getCheminDestination(new Scanner(System.in));
        });
    }

    /**
     * Teste la méthode verifierCheminFichierSourceValide de la classe GestionUrl.
     * Vérifie qu'une exception est levée lorsque le chemin du fichier source n'est pas valide pour le type spécifié.
     */
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