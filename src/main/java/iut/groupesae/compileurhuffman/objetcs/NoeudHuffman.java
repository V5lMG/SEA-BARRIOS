package iut.groupesae.compileurhuffman.objetcs;

public class NoeudHuffman implements Comparable<NoeudHuffman> {
    private final char caractere;
    private final double frequence;
    private NoeudHuffman gauche;
    private NoeudHuffman droite;
    private String codeHuffman;

    public NoeudHuffman(char caractere, double frequence) {
        this.caractere = caractere;
        this.frequence = frequence;
    }

    public char getCaractere() {
        return caractere;
    }

    public double getFrequence() {
        return frequence;
    }

    public NoeudHuffman getGauche() {
        return gauche;
    }

    public NoeudHuffman getDroite() {
        return droite;
    }

    public String getCodeHuffman() {
        return codeHuffman;
    }

    public void setGauche(NoeudHuffman gauche) {
        this.gauche = gauche;
    }

    public void setDroite(NoeudHuffman droite) {
        this.droite = droite;
    }

    public void setCodeHuffman(String codeHuffman) {
        this.codeHuffman = codeHuffman;
    }

    @Override
    public String toString() {
        return "codeHuffman = " + getCodeHuffman() + " ; encode = " + getUTF8Binary(getCaractere()) + " ; symbole = " + getCaractere();
    }

    public static String getUTF8Binary(char caractere) {
        byte[] utf8Bytes = String.valueOf(caractere).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        StringBuilder binaryRepresentation = new StringBuilder();
        for (byte b : utf8Bytes) {
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryRepresentation.append(binary);
        }
        return binaryRepresentation.toString();
    }

    @Override
    public int compareTo(NoeudHuffman autre) {
        return Double.compare(this.frequence, autre.frequence);
    }
}
