/**
 * 
 */
package fr.iutrodez.compilateurhuffman.outils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.System.err;

/**
 * 
 */
public class GestionFichier {

	public static String[] readAllFile(String filepath) {
		
		String[] lines = new String[0];
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	lines = Arrays.copyOf(lines, lines.length + 1);
                lines[lines.length-1] = line;
            }
        } catch (IOException e) {
            err.println("Une erreur est survenue lors de la lecture du fichier : " + e.getMessage());
        }
		return lines;
	}
	
	public static void ecrireArbreHuffman(String filePath, String[] arbreHuffman) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : arbreHuffman) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            err.println("Une erreur est survenue lors de l'écriture dans le fichier : " + e.getMessage());
        }
	}
	
	public static void writeBinaryStringToFile(String binaryString, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            dos.writeInt(binaryString.length());
            int byteLength = (binaryString.length() + 7) / 8;
            byte[] byteArray = new byte[byteLength];

            for (int i = 0; i < binaryString.length(); i++) {
                if (binaryString.charAt(i) == '1') {
                    byteArray[i / 8] |= (128 >> (i % 8));
                }
            }

            dos.write(byteArray);
        }
    }
	
	public static String readBinaryFileToString(String filePath) throws IOException {
        StringBuilder binaryString = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {
            int bitLength = dis.readInt();
            int byteLength = (bitLength + 7) / 8;
            byte[] byteArray = new byte[byteLength];
            dis.readFully(byteArray);

            for (int i = 0; i < bitLength; i++) {
                int byteIndex = i / 8;
                int bitIndex = i % 8;
                if ((byteArray[byteIndex] & (128 >> bitIndex)) != 0) {
                    binaryString.append('1');
                } else {
                    binaryString.append('0');
                }
            }
        }

        return binaryString.toString();
    }
	
	public static byte[] readFileToBytes(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        return bytes;
    }
	
	public static void ecritureFichierDestination(byte[] data, String filePath) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            bos.write(data);
        } catch (IOException erreur) {
            erreur.printStackTrace();
        }
    }
	
}
