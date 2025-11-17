package main;

import config.DatabaseConnection;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

// CLASE PRINCIPAL Y MÉTODO MAIN

/**
 * Clase principal que inicia la aplicación de gestión de productos y códigos (CRUD).
 */
public class main {
    
    /**
     * Punto de entrada del sistema que coordina la inicialización y ejecución.
     * Inicializa estilos, verifica la base de datos y lanza el menú principal.
     */
    public static void main(String[] args) {
        MenuStyle.style(args);
        System.out.println("\n─────────────────────────────────────────────");
        System.out.println("* * * *  SISTEMA DE GESTION DE PRODUCTOS  * * * *");
        System.out.println("\nIniciando aplicacion...");
        try {
            System.out.println("\nVerificando conexión a base de datos...");
            DatabaseConnection.inicializarBaseDatos();
            System.out.println("\n✓ Base de datos lista.");
            AppMenu app = new AppMenu();
            app.run();
        } catch (Exception e) {
            System.err.println("\nError fatal al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
