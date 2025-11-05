/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author hcoceres
 */
public class main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTION DE PRODUCTOS ===");
        System.out.println("Iniciando aplicacion...\n");
        try {
            AppMenu app = new AppMenu();
            app.run();
        } catch (Exception e) {
            System.err.println("Error fatal al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
