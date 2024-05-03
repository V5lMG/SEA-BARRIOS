package iut.groupesae.compileurhuffman;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ArbreHuffman {
    private NoeudHuffman racine;

    public ArbreHuffman(String cheminFichier) throws IOException {
        String contenu = GestionFichier.getContenuFichier(cheminFichier);
        Map<Character, Double> frequences = GestionFichier.getFrequences(contenu);
        this.racine = construireArbreHuffman(frequences);
    }

    private NoeudHuffman construireArbreHuffman(Map<Character, Double> frequences) {
        List<NoeudHuffman> feuilles = new ArrayList<>();

        // Création des nœuds feuilles pour chaque caractère avec leur fréquence
        for (Map.Entry<Character, Double> entree : frequences.entrySet()) {
            feuilles.add(new NoeudHuffman(entree.getKey(), entree.getValue()));
        }

        // Construction de l'arbre Huffman
        while (feuilles.size() > 1) {
            // Trouver les deux feuilles avec les plus petites fréquences
            NoeudHuffman min1 = feuilles.get(0);
            NoeudHuffman min2 = feuilles.get(1);

            for (int i = 2; i < feuilles.size(); i++) {
                if (feuilles.get(i).frequence < min1.frequence) {
                    min2 = min1;
                    min1 = feuilles.get(i);
                } else if (feuilles.get(i).frequence < min2.frequence) {
                    min2 = feuilles.get(i);
                }
            }

            // Création d'un nouveau nœud avec la somme des fréquences des nœuds fils
            NoeudHuffman parent = new NoeudHuffman('\0', min1.frequence + min2.frequence);
            parent.gauche = min1;
            parent.droite = min2;

            feuilles.remove(min1);
            feuilles.remove(min2);
            feuilles.add(parent);
        }

        return feuilles.get(0);
    }

    public void afficherArbreHuffman() {
        afficherArbreHuffmanRecursive(this.racine, "");
    }

    private void afficherArbreHuffmanRecursive(NoeudHuffman racine, String code) {
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

    public NoeudHuffman getRacine() {
        return this.racine;
    }
}
