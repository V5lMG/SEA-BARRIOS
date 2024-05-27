package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestCompressionHuffman {
    @Test
    public void testDemanderFichierACompiler() {

        String cheminFichierVide = "";

        Throwable erreur = assertThrows(IOException.class,

                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        //cheminFichierVide.GestionFichier.get
                    }
                });

        // on vérifie que le message de l'objet erreur est bien celui attendu
        assertEquals("Impossible de diviser par 0", erreur.getMessage());

    }
    }

