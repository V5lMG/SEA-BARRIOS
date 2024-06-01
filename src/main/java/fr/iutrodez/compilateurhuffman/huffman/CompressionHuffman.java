package fr.iutrodez.compilateurhuffman.huffman;

import fr.iutrodez.compilateurhuffman.outils.GestionFichier;
import fr.iutrodez.compilateurhuffman.objets.Noeud;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.System.out;

public class CompressionHuffman {

    private String cheminFichierSource;
    private String cheminFichierDestination;

    public CompressionHuffman(String cheminFichierSource, String cheminFichierDestination) {
        this.cheminFichierSource = cheminFichierSource;
        this.cheminFichierDestination = cheminFichierDestination;
    }

    public void compresserFichier() throws IOException {

        byte[] chaine = GestionFichier.readFileToBytes(cheminFichierSource);

        Map<Byte, Integer> occurencesDesCaracteres = getFrequenceDesBytes(chaine);

        /* TODO explique chaque outils utilisé pour cette ligne */
        int totalOccurrences = occurencesDesCaracteres.values().stream().mapToInt(Integer::intValue).sum();

        for (Entry<Byte, Integer> entry : occurencesDesCaracteres.entrySet()) {
            byte cle = entry.getKey();
            int occurrences = entry.getValue();
            double frequence = 100 * ((double) occurrences / totalOccurrences);
            char symbole = (char) cle;
            out.printf("Caractère : %c ; Occurrences : %d ; Fréquence : %.2f%%\n", symbole, occurrences, frequence);
        }

        Noeud[] tableau = new Noeud[occurencesDesCaracteres.size()];

        int entree = 0;
        for(Entry<Byte, Integer> encode : occurencesDesCaracteres.entrySet()) {
            byte key = encode.getKey();
            int value = encode.getValue();

            tableau[entree] = new Noeud(key, value);
            entree++;
        }

        Noeud racine = contruireArbre(tableau);

        Map<Byte, String> codageHuffman = racine.genererTableDeCodesHuffman();
        String[] tableHuffman = new String[codageHuffman.size()];
        // TODO affichage
        // TODO trier

        int occ = 0;
        for(Entry<Byte, String> code : codageHuffman.entrySet()) {
            byte key = code.getKey();
            String value = code.getValue();
            tableHuffman[occ] = "codeHuffman = " + value + " ; encode = " + key + " ; symbole = " + (char) key;
            occ++;
        }

        String codage = encoder(chaine, codageHuffman);
        GestionFichier.writeBinaryStringToFile(codage, cheminFichierDestination);

        /* Enlever l'extension et le nom du fichier et le remplacer par arbreHuffman.txt */
        int dernierSeparateur = cheminFichierDestination.lastIndexOf("\\");
        String cheminDossier  = cheminFichierDestination.substring(0, dernierSeparateur + 1);
        cheminFichierDestination = cheminDossier  + "arbreHuffman.txt";

        GestionFichier.ecrireArbreHuffman(cheminFichierDestination, tableHuffman);
    }

    private String encoder(byte[] chaine, Map<Byte, String> codageHuffman) {
        StringBuilder chaineBinaireEncodee = new StringBuilder();

        for (byte b : chaine) {
            chaineBinaireEncodee.append(codageHuffman.get(b));
        }

        return chaineBinaireEncodee.toString();
    }

    private Noeud contruireArbre(Noeud[] listDeNoeuds) {
        Noeud[] noeuds = listDeNoeuds;

        while(noeuds.length > 1) {
            Noeud premierNoeud = null;
            Noeud deuxiemeNoeud = null;

            for(Noeud noeud : noeuds) {
                if (premierNoeud == null || noeud.getValue() < premierNoeud.getValue()) {
                    deuxiemeNoeud = premierNoeud;
                    premierNoeud = noeud;
                } else if (deuxiemeNoeud == null || noeud.getValue() < deuxiemeNoeud.getValue()) {
                    deuxiemeNoeud = noeud;
                }
            }

            Noeud[] nouveauTableau = new Noeud[noeuds.length - 2];
            int index = 0;
            for(Noeud noeud : noeuds) {
                if(noeud != premierNoeud && noeud != deuxiemeNoeud) {
                    nouveauTableau[index++] = noeud;
                }
            }

            Noeud nouveauNoeud = new Noeud(premierNoeud.getValue() + deuxiemeNoeud.getValue());
            nouveauNoeud.setGauche(premierNoeud);
            nouveauNoeud.setDroite(deuxiemeNoeud);

            Noeud[] tableauAvecNouveauNoeud = new Noeud[nouveauTableau.length + 1];
            System.arraycopy(nouveauTableau, 0, tableauAvecNouveauNoeud, 0, nouveauTableau.length);
            tableauAvecNouveauNoeud[tableauAvecNouveauNoeud.length - 1] = nouveauNoeud;
            noeuds = tableauAvecNouveauNoeud;

        }

        return noeuds[0];

    }

    private HashMap<Byte, Integer> getFrequenceDesBytes(byte[] bytes) {
        HashMap<Byte, Integer> bytesFrequency = new HashMap<>();
        for(byte b : bytes) {
            if(bytesFrequency.containsKey(b)) {
                bytesFrequency.put(b, bytesFrequency.get(b) + 1);
            } else {
                bytesFrequency.put(b, 1);
            }
        }
        return bytesFrequency;
    }
}
