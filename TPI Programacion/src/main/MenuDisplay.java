package main;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

/**
 * Clase utilitaria para mostrar interfaces de menú en consola. 
 * Contiene métodos estáticos para mostrar menús y opciones al usuario.
 */
public class MenuDisplay {

    /**
     * Muestra el menú principal con todas las opciones de gestión disponibles.
     * Incluye secciones para productos y códigos de barras con sus respectivas operaciones CRUD.
     */
    public static void mostrarMenuPrincipal() {
    System.out.println("");
    System.out.println("┌──────────────────────────────────────────────────┐");
    System.out.println("│   ☰    MENÚ PRINCIPAL");
    System.out.println("├──────────────────────────────────────────────────┤");
    System.out.println("│   ✅    GESTIÓN DE PRODUCTOS 📦");
    System.out.println("├──────────────────────────────────────────────────┤");
    System.out.println("│   1.    ↪ Crear producto"); 
    System.out.println("│   2.    ↪ Listar productos");
    System.out.println("│   3.    ↪ Actualizar producto");
    System.out.println("│   4.    ↪ Eliminar producto");
    System.out.println("│   5.    ↪ Asignar codigo barras a producto"); 
    System.out.println("│   6.    ↪ Recuperar producto borrado");
    System.out.println("├──────────────────────────────────────────────────┤");
    System.out.println("│   ✅    GESTIÓN DE CODIGOS DE BARRAS 𝄃𝄃𝄂𝄂𝄀𝄁𝄃𝄂𝄂𝄃");
    System.out.println("├──────────────────────────────────────────────────┤");
    System.out.println("│   7.    ↪ Crear código de barras");
    System.out.println("│   8.    ↪ Listar códigos de barras"); 
    System.out.println("│   9.    ↪ Actualizar código de barras"); 
    System.out.println("│   10.  ↪ Eliminar código de barras");
    System.out.println("│   11.  ↪ Recuperar codigo barras eliminado");
    System.out.println("├──────────────────────────────────────────────────┤");
    System.out.println("│   0.   ↩ Salir");
    System.out.println("└──────────────────────────────────────────────────┘");
    System.out.print("\nSELECCIONE UNA OPCIÓN: ");
}
}
