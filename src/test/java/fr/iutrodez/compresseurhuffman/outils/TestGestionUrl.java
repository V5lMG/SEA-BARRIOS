package fr.iutrodez.compresseurhuffman.outils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TestGestionUrl {

    @Test
    void getNomFichierDestinationUnique() {
        // Créer un flux de sortie pour capturer System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Créer un flux d'entrée avec la saisie de l'utilisateur simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("nom_fichier".getBytes());
        System.setIn(inputStream);

        // Créer un objet GestionUrl
        GestionUrl gestionUrl = new GestionUrl();

        // Appeler la méthode à tester
        String nomFichier = gestionUrl.getNomFichierDestinationUnique(new Scanner(System.in), "chemin/vers/repertoire/destination");

        // Récupérer la sortie capturée dans le flux de sortie
        String output = outputStream.toString();

        // Vérifier si la méthode a affiché le message attendu
        assertFalse(output.contains("Nom de fichier"));

        // Vérifier si le nom de fichier retourné est correct
        assertEquals("nom_fichier", nomFichier);

        // Restaurer la sortie standard de System.out
        System.setOut(System.out);
    }

    @Test
    void enleverGuillemet() {
        GestionUrl gestionUrl = new GestionUrl();
        String chemin1 = "\"chemin\"";
        String chemin2 = "chemin\"";
        String chemin3 = "\"chemin";

        assertEquals("chemin", gestionUrl.enleverGuillemet(chemin1));
        assertEquals("chemin", gestionUrl.enleverGuillemet(chemin2));
        assertEquals("chemin", gestionUrl.enleverGuillemet(chemin3));
    }

    @Test
    void getNomFichierDestination() {
        // Test pour un nom de fichier valide
        GestionUrl gestionUrl = new GestionUrl();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("nom_fichier.txt".getBytes());
        System.setIn(inputStream);
        assertEquals("nom_fichier.txt", gestionUrl.getNomFichierDestination(new Scanner(System.in)));

        // Test pour un nom de fichier invalide
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        inputStream = new ByteArrayInputStream("testPetit!@#".getBytes());
        System.setIn(inputStream);
        assertThrows(RuntimeException.class, () -> gestionUrl.getNomFichierDestination(new Scanner(System.in)));
        String output = outputStream.toString();
        assertFalse(output.contains("Nom de fichier invalide"));
    }

    @Test
    void getCheminDestination() {
        // Test pour un chemin de répertoire valide
        GestionUrl gestionUrl = new GestionUrl();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        System.setIn(inputStream);
        assertEquals("test", gestionUrl.getCheminDestination(new Scanner(System.in)));

        // Test pour un chemin de répertoire invalide
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        inputStream = new ByteArrayInputStream("testZ\\testPetit".getBytes());
        System.setIn(inputStream);
        assertThrows(RuntimeException.class, () -> gestionUrl.getCheminDestination(new Scanner(System.in)));
        String output = outputStream.toString();
        assertFalse(output.contains("Le chemin spécifié"));
    }

    @Test
    void verifierCheminFichierSourceValide() {
        GestionUrl gestionUrl = new GestionUrl();

        // Test pour un chemin de fichier source valide
        assertDoesNotThrow(() -> gestionUrl.verifierCheminFichierSourceValide("fichier.txt", "txt"));

        // Test pour un chemin de fichier source invalide (fichier vide)
        assertThrows(IOException.class, () -> gestionUrl.verifierCheminFichierSourceValide("", "txt"));

        // Test pour un chemin de fichier source invalide (mauvais type de fichier)
        assertThrows(IOException.class, () -> gestionUrl.verifierCheminFichierSourceValide("fichier.bin", "txt"));
    }
}
