
package main;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */


/**
 * Clase utilitaria para mostrar el menú de la aplicación.
 * Solo contiene métodos estáticos de visualización (no tiene estado).
 *
 * Responsabilidades:
 * - Mostrar el menú principal con todas las opciones disponibles
 * - Formatear la salida de forma consistente
 *
 * Patrón: Utility class (solo métodos estáticos, no instanciable)
 *
 * IMPORTANTE: Esta clase NO lee entrada del usuario.
 * Solo muestra el menú. AppMenu es responsable de leer la opción.
 */
public class MenuDisplay {
    /**
     * Muestra el menú principal con todas las opciones CRUD.
     *
     * Opciones de Productos (1-4):
     * 1. Crear producto: Permite crear producto con codigo
     * 2. Listar productos: Lista todas o busca por nombre/categoria
     * 3. Actualizar producto: Actualiza datos de producto y su codigo
     * 4. Eliminar producto: Soft delete de producto (NO elimina codigo asociado)
     * 0. Salir: Termina la aplicación
     *
     * Nota: Los números de opción corresponden al switch en AppMenu.procesarOpcion().
     */
public static void mostrarMenuPrincipal() {
    System.out.println("");
    System.out.println("┌───────────────────────────────────────────────────────────┐");
    System.out.println("│  ☰  MENÚ PRINCIPAL\t\t\t");
    System.out.println("├───────────────────────────────────────────────────────────┤");
    System.out.println("│  ✅ GESTIÓN DE PRODUCTOS 📦");
    System.out.println("│   1.  ↪ Crear producto"); // Pedir y validar todos los campos y guardarlo en un Instancia de producto
    System.out.println("│   2.  ↪ Listar productos"); // Submenu preguntar criterio(opciones: 1. nombre, 2. marca, 3. categoria).
    System.out.println("│   3.  ↪ Actualizar producto"); // Método Submenu Pedir (1. ID). 
    System.out.println("│   4.  ↪ Eliminar producto"); // Método Submenu Pedir (1. ID) -> manda true eliminado.
    System.out.println("│   5.  ↪ Asignar codigo barras a producto"); // Método Submenu Pedir (1. ID) -> manda true eliminado.
    System.out.println("│   6.  ↪ Recuperar producto borrado");
    System.out.println("│                                      ");
    System.out.println("│  ✅ GESTIÓN DE CODIGOS DE BARRAS 𝄃𝄃𝄂𝄂𝄀𝄁𝄃𝄂𝄂𝄃");
    System.out.println("│   7.  ↪ Crear código de barras"); // Pedir y validar todos los campos y guardarlo en un Instancia de Codigo de barras
    System.out.println("│   8.  ↪ Listar códigos de barras"); // Listar por ID o por codigo. 
    System.out.println("│   9.  ↪ Actualizar código de barras"); // Pedir (ID) y actualiza valor, tipo y observaciones. 
    System.out.println("│   10.  ↪ Eliminar código de barras"); // Pedir (ID) -> manda true eliminado.
    System.out.println("│   11.  ↪ Recuperar codigo barras eliminado");
    System.out.println("│                                      ");
    System.out.println("│   0.  ↩ Salir");
    System.out.println("└───────────────────────────────────────────────────────────┘");
    System.out.print("Seleccione una opción: ");
}
}
