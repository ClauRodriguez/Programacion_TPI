/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import config.DatabaseConnection;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

public class main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTION DE PRODUCTOS ===");
        System.out.println("Iniciando aplicacion...\n");
        try {
            // Inicializar base de datos al inicio (crearla si no existe)
            System.out.println("Verificando conexión a base de datos...");
            DatabaseConnection.inicializarBaseDatos();
            System.out.println("✓ Base de datos lista.\n");
            
            AppMenu app = new AppMenu();
            app.run();
        } catch (Exception e) {
            System.err.println("Error fatal al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
