package fr.iutrodez.compresseurhuffman.outils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class TestGestionPrompt {

    private final String cheminDossierCreer = "test\\nouveauDossier";

    @Test
    void getContenuFichier() {
        GestionPrompt gestionPrompt = new GestionPrompt();
        String cheminFichier = "test\\testPetit.txt";

        try {
            String contenu = gestionPrompt.getContenuFichier(cheminFichier);
            assertNotNull(contenu);
            assertTrue(contenu.length() > 0);
        } catch (IOException e) {
            fail("Une exception ne devrait pas être lancée ici : " + e.getMessage());
        }
    }

    @AfterEach
    void NettoyerDossierCreer() {
        File dossier = new File(cheminDossierCreer);
        if (dossier.exists()) {
            if (!dossier.delete()) {
                System.err.println("Impossible de supprimer le dossier : " + cheminDossierCreer);
            }
        }
    }

    @Test
    void creerDossierPourCompilation() {
        GestionPrompt gestionPrompt = new GestionPrompt();
        String cheminDossier = cheminDossierCreer;

        try {
            gestionPrompt.creerDossierPourCompilation(cheminDossier);
            // Si aucune exception n'est levée, le test réussit
        } catch (IOException e) {
            fail("Une exception ne devrait pas être lancée ici : " + e.getMessage());
        }
    }

    @Test
    void demanderOuiOuNon() {
        GestionPrompt gestionPrompt = new GestionPrompt();
        // Test avec "oui" en entrée utilisateur
        System.setIn(new ByteArrayInputStream("oui".getBytes()));
        assertTrue(gestionPrompt.demanderOuiOuNon("Message de test"));

        // Test avec "non" en entrée utilisateur
        System.setIn(new ByteArrayInputStream("non".getBytes()));
        assertFalse(gestionPrompt.demanderOuiOuNon("Message de test"));

        // Rétablir System.in pour éviter les effets de bord
        System.setIn(System.in);
    }
}