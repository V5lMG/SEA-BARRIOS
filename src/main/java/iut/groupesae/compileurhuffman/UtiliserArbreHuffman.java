package iut.groupesae.compileurhuffman;

public class UtiliserArbreHuffman {

    public static void afficherArbreHuffman(NoeudHuffman racine) {
        if (racine == null) {
            System.out.println("L'arbre de Huffman est vide.");
        } else {
            System.out.println("Affichage de l'arbre de Huffman :");
            afficherArbreHuffmanRecursive(racine, "");
        }
    }

    private static void afficherArbreHuffmanRecursive(NoeudHuffman racine, String code) {
        if (racine == null)
            return;

        // Nœud feuille, imprimer le caractère et le code Huffman
        if (racine.gauche == null && racine.droite == null) {
            System.out.println(racine.caractere + " : " + code);
            return;
        }

        // Parcours récursif à gauche avec un ajout de '0' au code
        afficherArbreHuffmanRecursive(racine.gauche, code + "0");
        // Parcours récursif à droite avec un ajout de '1' au code
        afficherArbreHuffmanRecursive(racine.droite, code + "1");
    }
}
