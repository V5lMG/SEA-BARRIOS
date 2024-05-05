package test;

import iut.groupesae.compileurhuffman.GestionFichier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Classe de tests pour la classe GestionFichier.
 */
public class TestGestionFichier {

    private final String CHEMIN_VALIDE = "test.txt";
    private final String CHEMIN_VALIDE_AVEC_GUILLEMETS = "\"test.txt\"";
    private final String CHEMIN_INEXISTANT = "chemin/vers/un/fichier/inexistant.txt";
    private final String CONTENUE_VALIDE = "azertyuiop : c'est la première ligne du clavier\nFrancais ! \n";
    private final String CONTENU_TEST = "aaabbbccc";
    private final String CONTENU_TEST_VIDE = "";
    private final String CONTENU_TEST_UN_CHAR = "a";
    private final String CONTENU_TEST_CHAR_DIFFERENT = "abcabc";
    private final String CONTENU_TEST_CHAR_IDENTIQUE = "aaabbbccc";
    private final String CONTENU_TEST_CHAR_FREQUENCE = "aaabbc";

    @BeforeEach
    public void creerFichier() throws IOException {
        File fichier = new File("test.txt");

        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("azertyuiop : c'est la première ligne du clavier\nFrancais ! \n");
        }
    }

    /**
     * Teste la méthode getContenuFichier pour un fichier existant.
     * Le résultat attendu est le contenu du fichier.
     * Le cas d'erreur est une IOException si le fichier n'existe pas.
     */
    @Test
    void testGetContenuFichier() throws IOException {
        String contenu = GestionFichier.getContenuFichier(CHEMIN_VALIDE);
        assertEquals(CONTENUE_VALIDE, contenu, "Le contenu du fichier ne correspond pas.");
    }

    /**
     * Teste la méthode getContenuFichier pour un fichier inexistant.
     * Le cas d'erreur attendu est une IOException.
     */
    @Test
    void testGetContenuFichierInexistant() {
        assertThrows(IOException.class, () -> GestionFichier.getContenuFichier(CHEMIN_INEXISTANT),
                "Une IOException devrait être lancée car le fichier est inexistant.");
    }

    /**
     * Teste la méthode obtenirCheminFichier où l'argument est encadré par des guillemets.
     * Le résultat attendu est le chemin du fichier sans les guillemets.
     */
    @Test
    void testObtenirCheminFichierAvecArgsAvecGuillemets() throws IOException {
        String[] args = {CHEMIN_VALIDE_AVEC_GUILLEMETS};
        String cheminFichier = GestionFichier.getCheminFichier(args);
        assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
    }

    /**
     * Teste la méthode obtenirCheminFichier sans argument, où l'URL est encadré par des guillemets.
     * Le résultat attendu est le chemin du fichier sans les guillemets.
     */
    @Test
    void testObtenirCheminFichierSansArgsAvecGuillemets() throws IOException {
        // Simule une saisie utilisateur depuis la console
        System.setIn(new java.io.ByteArrayInputStream(CHEMIN_VALIDE_AVEC_GUILLEMETS.getBytes()));

        String cheminFichier = GestionFichier.getCheminFichier(new String[]{});
        assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
    }

    /**
     * Teste la méthode obtenirCheminFichier avec des arguments.
     * Le résultat attendu est le chemin du fichier.
     */
    @Test
    void testObtenirCheminFichierAvecArgs() throws IOException {
        String[] args = {CHEMIN_VALIDE};
        String cheminFichier = GestionFichier.getCheminFichier(args);
        assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
    }

    /**
     * Teste la méthode obtenirCheminFichier sans arguments.
     * Le résultat attendu est le chemin du fichier.
     */
    @Test
    void testObtenirCheminFichierSansArgs() throws IOException {
        String[] args = {};
        // Simule une saisie utilisateur depuis la console
        System.setIn(new java.io.ByteArrayInputStream(CHEMIN_VALIDE.getBytes()));

        String cheminFichier = GestionFichier.getCheminFichier(args);
        assertEquals(CHEMIN_VALIDE, cheminFichier, "Le chemin du fichier récupéré ne correspond pas.");
    }

    /**
     * Teste la méthode compterCaracteres pour un contenu valide.
     * Le résultat attendu est une map des occurrences de chaque caractère.
     */
    @Test
    void testCompterCaracteres() {
        Map<Character, Integer> occurrences = GestionFichier.getOccurrenceCaractere(CONTENU_TEST);
        assertEquals(3, occurrences.get('a'));
        assertEquals(3, occurrences.get('b'));
        assertEquals(3, occurrences.get('c'));
    }

    /**
     * Teste la méthode compterCaracteres pour un contenu vide.
     * Le résultat attendu est une map vide.
     */
    @Test
    void testCompterCaracteresContenuVide() {
        Map<Character, Integer> occurrences = GestionFichier.getOccurrenceCaractere("");
        assertEquals(0, occurrences.size());
    }

    /**
     * Teste la méthode ordonnerOccurrences avec un contenu vide.
     * Le résultat attendu est un tableau d'occurrences vide.
     */
    @Test
    void testOrdonnerOccurrencesContenuVide() {
        int[][] occurrences = GestionFichier.ordonnerOccurrences(CONTENU_TEST_VIDE);
        assertEquals(0, occurrences.length, "Le tableau des occurrences ne devrait pas contenir d'éléments.");
    }

    /**
     * Teste la méthode ordonnerOccurrences avec un contenu contenant un seul caractère.
     * Le résultat attendu est un tableau d'occurrences avec un seul élément pour ce caractère.
     */
    @Test
    void testOrdonnerOccurrencesContenuUnCaractere() {
        int[][] occurrences = GestionFichier.ordonnerOccurrences(CONTENU_TEST_UN_CHAR);
        assertEquals(1, occurrences.length, "Le tableau des occurrences devrait contenir un élément.");
        assertEquals('a', occurrences[0][0], "Le caractère devrait être 'a'.");
        assertEquals(1, occurrences[0][1], "Le nombre d'occurrences devrait être 1.");
    }

    /**
     * Teste la méthode ordonnerOccurrences avec un contenu contenant plusieurs caractères différents.
     * Le résultat attendu est un tableau d'occurrences avec les caractères triés par nombre d'occurrences décroissant.
     */
    @Test
    void testOrdonnerOccurrencesContenuPlusieursCaracteres() {
        int[][] occurrences = GestionFichier.ordonnerOccurrences(CONTENU_TEST_CHAR_DIFFERENT);
        assertEquals(3, occurrences.length, "Le tableau des occurrences devrait contenir trois éléments.");
        assertEquals('a', occurrences[0][0], "Le premier caractère devrait être 'a'.");
        assertEquals(2, occurrences[0][1], "Le nombre d'occurrences de 'a' devrait être 2.");
        assertEquals('b', occurrences[1][0], "Le deuxième caractère devrait être 'b'.");
        assertEquals(2, occurrences[1][1], "Le nombre d'occurrences de 'b' devrait être 2.");
        assertEquals('c', occurrences[2][0], "Le troisième caractère devrait être 'c'.");
        assertEquals(2, occurrences[2][1], "Le nombre d'occurrences de 'c' devrait être 2.");
    }

    /**
     * Teste la méthode ordonnerOccurrences avec un contenu contenant plusieurs occurrences du même caractère.
     * Le résultat attendu est un tableau d'occurrences avec les caractères triés par nombre d'occurrences décroissant.
     */
    @Test
    void testOrdonnerOccurrencesContenuOccurrencesMultiples() {
        int[][] occurrences = GestionFichier.ordonnerOccurrences(CONTENU_TEST_CHAR_IDENTIQUE);
        assertEquals(3, occurrences.length, "Le tableau des occurrences devrait contenir trois éléments.");
        assertEquals('a', occurrences[0][0], "Le premier caractère devrait être 'a'.");
        assertEquals(3, occurrences[0][1], "Le nombre d'occurrences de 'a' devrait être 3.");
        assertEquals('b', occurrences[1][0], "Le deuxième caractère devrait être 'b'.");
        assertEquals(3, occurrences[1][1], "Le nombre d'occurrences de 'b' devrait être 3.");
        assertEquals('c', occurrences[2][0], "Le troisième caractère devrait être 'c'.");
        assertEquals(3, occurrences[2][1], "Le nombre d'occurrences de 'c' devrait être 3.");
    }

    /**
     * Teste la méthode getFrequence de la classe GestionFichier.
     * Vérifie si la méthode calcule correctement les fréquences de chaque caractère dans le contenu donné.
     */
    @Test
    void testGetFrequence() {
        Map<Character, Double> frequenceMap = GestionFichier.getFrequences(CONTENU_TEST_CHAR_FREQUENCE);

        assertEquals(3, frequenceMap.size(), "La taille du map de fréquences est incorrecte.");

        assertTrue(frequenceMap.containsKey('a'));
        assertEquals(0.5, frequenceMap.get('a'), "La fréquence de 'a' est incorrecte.");

        assertTrue(frequenceMap.containsKey('b'));
        assertEquals(0.3333333333333333, frequenceMap.get('b'), "La fréquence de 'b' est incorrecte.");

        assertTrue(frequenceMap.containsKey('c'));
        assertEquals(0.16666666666666666, frequenceMap.get('c'), "La fréquence de 'c' est incorrecte.");
    }
}