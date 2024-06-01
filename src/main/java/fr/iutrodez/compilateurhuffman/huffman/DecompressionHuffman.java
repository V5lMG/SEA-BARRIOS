package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.outils.GestionFichier;
import fr.iutrodez.compilateurhuffman.objets.Noeud;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

public class DecompressionHuffman {

    private String cheminFichierSource;
    private String cheminFichierDestination;

    public DecompressionHuffman(String cheminFichierSource, String cheminFichierDestination) {
        this.cheminFichierSource = cheminFichierSource;
        this.cheminFichierDestination = cheminFichierDestination;
    }

    public void decompresserFichier() throws IOException {

        String code = GestionFichier.readBinaryFileToString(cheminFichierSource);

        int dernierSeparateur = cheminFichierSource.lastIndexOf("\\");
        String cheminArbre  = cheminFichierSource.substring(0, dernierSeparateur + 1);
        cheminArbre = cheminArbre + "arbreHuffman.txt";

        File fichierArbreHuffman = new File(cheminArbre);
        if (!fichierArbreHuffman.exists()) {
            throw  new IOException("Erreur: fichier de l'arbre d'Huffman introuvable. " +
                                   "Veuillez le placer dans le même répertoire que le " +
                                   "fichier à décompresser avec le nom \"arbreHuffman.txt\"");
        }

        String[] arbreHuffman = GestionFichier.readAllFile(cheminArbre);

        Map<Byte, String> codageHuffman = genererTableDeCode(arbreHuffman);
        Noeud root = construireArbreHuffmanDepuisMap(codageHuffman);

        byte[] bytes = transformerChaineEnBytes(code, root);
        GestionFichier.ecritureFichierDestination(bytes, cheminFichierDestination);
    }

    private Map<Byte, String> genererTableDeCode(String[] arbre) {
        Map<Byte, String> tableCode = new HashMap<Byte, String>();
        for(String line : arbre) {
            out.println(line);
            if(!line.isBlank()) {
                String[] lineSplit = line.split(";");
                tableCode.put(Byte.valueOf(lineSplit[1].split("=")[1].trim()), lineSplit[0].split("=")[1].trim());
            }
        }
        return tableCode;
    }

    private static Noeud construireArbreHuffmanDepuisMap(Map<Byte, String> codeMap) {
        Noeud racine = new Noeud();

        for (Map.Entry<Byte, String> entry : codeMap.entrySet()) {
            byte caractere = entry.getKey();
            String chemin = entry.getValue();

            Noeud courant = racine;
            for (int i = 0; i < chemin.length(); i++) {
                char direction = chemin.charAt(i);

                if (direction == '0') {
                    if (courant.getGauche() == null) {
                        courant.setGauche(new Noeud());
                    }
                    courant = courant.getGauche();
                } else if (direction == '1') {
                    if (courant.getDroite() == null) {
                        courant.setDroite(new Noeud());
                    }
                    courant = courant.getDroite();
                }
            }

            courant.setCaractere(caractere);
        }
        return racine;
    }

    private static byte[] transformerChaineEnBytes(String bits, Noeud racine) {
        List<Byte> byteList = new ArrayList<>();
        Noeud courant = racine;
        for (int i = 0; i < bits.length(); i++) {
            char bit = bits.charAt(i);
            if (bit == '0') {
                courant = courant.getGauche();
            } else if (bit == '1') {
                courant = courant.getDroite();
            }

            if (courant.isFeuille()) {
                byteList.add(courant.getCharacter());
                courant = racine;
            }
        }

        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }

        return result;
    }
}