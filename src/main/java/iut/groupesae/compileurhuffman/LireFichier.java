/*
 * LireFichier                          27 avr. 2024
 * IUT de Rodez, Info1 2023-2024 TP4, pas de copyright
 */
package iut.developpement.sae202;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LireFichier {
	private String cheminFichier;

	/*
	 * Connaître le chemin du fichier
	 * @param cheminFichier chemin
	 */
	public LireFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	/*
	 * Lire et afficher chaque ligne
	 */
	public void LireEtAfficherLigne() {
		try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
			String ligne;
			// Exemple d'utilisation : lire le fichier ligne par ligne
			while ((ligne = reader.readLine()) != null) {
				// Traiter chaque ligne (par exemple, l'afficher)
				System.out.println(ligne);
			}
		} catch (IOException e) {
			// Gérer les exceptions d'entrée/sortie (par exemple, le fichier n'existe pas)
			e.printStackTrace();
		}
	}

	/*
	 * récuperer le chemin du fichier
	 */
	public String getCheminFichier() {
		return cheminFichier;
	}

	public static void main(String[] args) {
		Scanner AnalyseurEntree = new Scanner(System.in);

		// Vérifier si un chemin de fichier a été fourni en argument
		if (args.length != 1) {
			String chemin;

			System.out.print("Inserrer le chemin du fichier : ");
			chemin = AnalyseurEntree.nextLine();

			// Créer une instance de LireFichier avec le chemin du fichier passé en ligne de commande
			LireFichier fileReader = new LireFichier(chemin);
			// Lire et afficher le contenu du fichier
			fileReader.LireEtAfficherLigne();

			// Utiliser la méthode getFichier pour obtenir le chemin du fichier
			System.out.println("Chemin du fichier : " + fileReader.getCheminFichier());

		} else {
			// Créer une instance de LireFichier avec le chemin du fichier passé en argument
			LireFichier fileReader = new LireFichier(args[0]);
			// Lire et afficher le contenu du fichier
			fileReader.LireEtAfficherLigne();

			// Utiliser la méthode getFichier pour obtenir le chemin du fichier
			System.out.println("Chemin du fichier : " + fileReader.getCheminFichier());
		}


	}
}