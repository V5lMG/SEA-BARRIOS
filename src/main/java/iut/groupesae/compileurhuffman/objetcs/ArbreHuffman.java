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

    public String encoderContenu(String contenu) {
        Map<Character, String> codesHuffman = getCodesHuffman();
        StringBuilder contenuEncode = new StringBuilder();

        for (char caractere : contenu.toCharArray()) {
            contenuEncode.append(codesHuffman.get(caractere));
        }

        return contenuEncode.toString();
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

    private Map<Character, String> getCodesHuffman() {
        Map<Character, String> codesHuffman = new HashMap<>();
        collecterCodesHuffman(racine, codesHuffman);
        return codesHuffman;
    }

    private void collecterCodesHuffman(NoeudHuffman noeud, Map<Character, String> codesHuffman) {
        if (noeud == null) {
            return;
        }

        if (noeud.getGauche() == null && noeud.getDroite() == null) {
            codesHuffman.put(noeud.getCaractere(), noeud.getCodeHuffman());
        }

        collecterCodesHuffman(noeud.getGauche(), codesHuffman);
        collecterCodesHuffman(noeud.getDroite(), codesHuffman);
    }
}
