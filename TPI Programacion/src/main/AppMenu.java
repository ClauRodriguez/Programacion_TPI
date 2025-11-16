package main;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
import java.util.Scanner;
import service.ProductoService;
import service.CodigoBarrasService;

/**
 * Orquestador principal del menú de la aplicación. 
 * Gestiona el ciclo de vida del menú y coordina todas las dependencias.
 *
 * Responsabilidades: 
 * - Crear y gestionar el Scanner único (evita múltiples instancias de System.in)
 * - Inicializar toda la cadena de dependencias (DAOs → Services → Handler)
 * - Ejecutar el loop principal del menú
 * - Manejar la selección de opciones y delegarlas a MenuHandler
 * - Cerrar recursos al salir (Scanner)
 *
 * Patrón: Application Controller + Dependency Injection manual
 * Arquitectura: Punto de entrada que ensambla las 4 capas (Main → Service → DAO → Models)
 *
 * IMPORTANTE: Esta clase NO tiene lógica de negocio ni de UI. Solo coordina y delega.
 */

public class AppMenu {
    
    /**
     * Scanner único compartido por toda la aplicación.
     */
    private final Scanner scanner;

    /**
     * Handler que ejecuta las operaciones del menú.
     * Contiene toda la lógica de interacción con el usuario.
     */
    private final MenuHandler menuHandler; 

    /**
     * Flag que controla el loop principal del menú.
     * Se setea a false cuando el usuario selecciona "0 - Salir".
     */
    private boolean running;

    /**
     * Constructor que inicializa la aplicación.
     *
     * Flujo de inicialización:
     * 1. Crea Scanner único para toda la aplicación
     * 2. Crea cadena de dependencias (DAOs → Services) mediante createProductoService()
     * 3. Crea MenuHandler con Scanner, ProductoService y CodigoBarrasService
     * 4. Setea running=true para iniciar el loop
     *
     * Patrón de inyección de dependencias (DI) manual:
     * - CodigoBarrasDAO (sin dependencias)
     * - ProductoDAO (depende de CodigoBarrasDAO)
     * - CodigoBarrasService (depende de CodigoBarrasDAO)
     * - ProductoService (depende de ProductoDAO)
     * - MenuHandler (depende de Scanner, ProductoService y CodigoBarrasService)
     *
     * Esta inicialización garantiza que todas las dependencias estén correctamente conectadas.
     */
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        ProductoService productoService = createProductoService();
        CodigoBarrasService codigoBarrasService = createCodigoBarrasService();
        this.menuHandler = new MenuHandler(scanner, productoService, codigoBarrasService);
        this.running = true;
    }

    /**
     * Punto de entrada de la aplicación Java.
     * Crea instancia de AppMenu y ejecuta el menú principal.
     */
    /**
     * Loop principal del menú.
     * Manejo de errores:
     * - NumberFormatException: Captura entrada no numérica (ej: "abc")
     * - El usuario puede volver a intentar
     * El Scanner se cierra al salir del loop.
     */
    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                System.out.flush();
                String input = scanner.nextLine();
                if (input == null || input.trim().isEmpty()) {
                    continue;
                }
                int opcion = Integer.parseInt(input.trim());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                System.out.flush();
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
                System.out.flush();
            }
        }
        scanner.close();
    }

    /**
     * Procesa la opción seleccionada por el usuario y delega a MenuHandler.
     *
     * Mapeo de opciones (corresponde a MenuDisplay):
     * 1  → Crear producto (con codigo opcional)
     * 2  → Listar productos (todas o filtradas)
     * 3  → Actualizar producto
     * 4  → Eliminar producto (soft delete)
     * 0  → Salir (setea running=false para terminar el loop)
     *
     * Opción inválida: Muestra mensaje y continúa el loop.
     * 
     * @param opcion Número de opción ingresado por el usuario
     */
private void procesarOpcion(int opcion) {
    switch (opcion) {
        case 1 -> menuHandler.crearProducto();
        case 2 -> menuHandler.listarProductos();
        case 3 -> menuHandler.actualizarProducto();
        case 4 -> menuHandler.eliminarProducto();
        case 5 -> menuHandler.asignarCodigoDeBarras();  
        case 6 -> menuHandler.recuperarProducto(); 
        case 7 -> menuHandler.crearCodigoBarrasIndependiente();
        case 8 -> menuHandler.listarCodigos();   
        case 9 -> menuHandler.actualizarCodigoBarrasPorId(); 
        case 10 -> menuHandler.eliminarCodigoBarrasPorId();  
        case 11 -> menuHandler.recuperarCodigoBarrasPorId(); 
        
        case 0 -> {
            System.out.println("Saliendo...");
            running = false;
        }
        default -> System.out.println("Opcion no valida.");
    }
}
    
    /**
     * Crea e inicializa el servicio de productos.
     * Este método construye la cadena de dependencias:
     * ProductoDAO → ProductoService
     * 
     * @return ProductoService inicializado
     */
    private ProductoService createProductoService() {
        return new ProductoService();
    }
    
    /**
     * Crea e inicializa el servicio de códigos de barras.
     * Este método construye la cadena de dependencias:
     * CodigoBarrasDAO → CodigoBarrasService
     * 
     * @return CodigoBarrasService inicializado
     */
    private CodigoBarrasService createCodigoBarrasService() {
        return new CodigoBarrasService();
    }
}
