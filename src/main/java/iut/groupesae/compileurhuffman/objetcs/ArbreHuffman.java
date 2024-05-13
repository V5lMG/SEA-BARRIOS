package iut.groupesae.compileurhuffman.objetcs;

import iut.groupesae.compileurhuffman.GestionFichier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class ArbreHuffman {
    private final NoeudHuffman racine;

    public ArbreHuffman(String cheminFichier) throws IOException {
        String contenu = GestionFichier.getContenuFichier(cheminFichier);
        Map<Character, Double> frequences = GestionFichier.getFrequences(contenu);
        this.racine = construireArbreHuffman(frequences);
    }

    public void trierArbre(BufferedWriter writer) throws IOException {
        List<NoeudHuffman> feuilles = new ArrayList<>();
        collecterFeuilles(racine, feuilles);
        feuilles.sort(Comparator.comparing(NoeudHuffman::getFrequence).reversed());

        for (NoeudHuffman feuille : feuilles) {
            String ligne = feuille.toString() + "\n";
            writer.write(ligne);
            System.out.print(ligne);
        }
    }

    private void collecterFeuilles(NoeudHuffman racine, List<NoeudHuffman> feuilles) {
        if (racine == null) {
            return;
        }

        if (racine.getGauche() == null && racine.getDroite() == null) {
            feuilles.add(racine);
        }

        collecterFeuilles(racine.getGauche(), feuilles);
        collecterFeuilles(racine.getDroite(), feuilles);
    }

    private NoeudHuffman construireArbreHuffman(Map<Character, Double> frequences) {
        List<NoeudHuffman> feuilles = new ArrayList<>();

        for (Map.Entry<Character, Double> entree : frequences.entrySet()) {
            feuilles.add(new NoeudHuffman(entree.getKey(), entree.getValue()));
        }

        while (feuilles.size() > 1) {
            NoeudHuffman noeudMin1 = feuilles.removeFirst();
            NoeudHuffman noeudMin2 = feuilles.removeFirst();

            NoeudHuffman nouveauNoeud = new NoeudHuffman('\0', noeudMin1.getFrequence() + noeudMin2.getFrequence());
            nouveauNoeud.setGauche(noeudMin1);
            nouveauNoeud.setDroite(noeudMin2);

            feuilles.add(nouveauNoeud);
            feuilles.sort(Comparator.comparingDouble(NoeudHuffman::getFrequence));
        }

        assignerCodesHuffman(feuilles.getFirst(), ""); // Appel de la méthode pour attribuer les codes Huffman
        return feuilles.getFirst();
    }

    private void assignerCodesHuffman(NoeudHuffman noeud, String code) {
        if (noeud == null) {
            return;
        }

        noeud.setCodeHuffman(code);

        assignerCodesHuffman(noeud.getGauche(), code + "0");
        assignerCodesHuffman(noeud.getDroite(), code + "1");
    }
}
