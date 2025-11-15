package main;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
public class MenuStyle {
    public static void style(String[] args) {
        // Con esto configuramos UTF-8 al inicio del main para que el menu se vea correctamente
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }}
