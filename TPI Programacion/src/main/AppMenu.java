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

/**
 * Orquestador principal del menú de la aplicación. 
 * Gestiona el ciclo de vida del menú y coordina todas las dependencias.
 *
 * Responsabilidades: 
 * - Crear y gestionar el Scanner único (evita múltiples instancias de System.in) // HAY QUE RESOLVERLAS AUN
 * - Inicializar toda la cadena de dependencias (DAOs → Services → Handler) // HAY QUE RESOLVERLAS AUN
 * - Ejecutar el loop principal del menú // RESUELTO
 * - Manejar la selección de opciones y delegarlas a MenuHandler // RESUELTO
 * - Cerrar recursos al salir (Scanner) // HAY QUE RESOLVERLAS AUN
 *
 * Patrón: Application Controller + Dependency Injection manual // HAY QUE RESOLVERLAS AUN
 * Arquitectura: Punto de entrada que ensambla las 4 capas (Main → Service → DAO → Models) // HAY QUE RESOLVERLAS AUN
 *
 * IMPORTANTE: Esta clase NO tiene lógica de negocio ni de UI. Solo coordina y delega.
 */

public class AppMenu {
    
    /**
     * Scanner único compartido por toda la aplicación.// HAY QUE RESOLVER AUN
     */
    private final Scanner scanner;

    /**
     * Handler que ejecuta las operaciones del menú. // RESUELTO
     * Contiene toda la lógica de interacción con el usuario.
     */
    private final MenuHandler menuHandler; 

    /**
     * Flag que controla el loop principal del menú. // RESUELTO
     * Se setea a false cuando el usuario selecciona "0 - Salir".
     */
    private boolean running;

    /**
     * Constructor que inicializa la aplicación.
     *
     * Flujo de inicialización:
     * 1. Crea Scanner único para toda la aplicación // HAY QUE RESOLVER AUN
     * 2. Crea cadena de dependencias (DAOs → Services) mediante createProductoService() // HAY QUE RESOLVER AUN
     * 3. Crea MenuHandler con Scanner y ProductoService // HAY QUE RESOLVER AUN ProductoService
     * 4. Setea running=true para iniciar el loop // RESUELTO
     *
     * Patrón de inyección de dependencias (DI) manual:
     * - CodigoDAO (sin dependencias) // HAY QUE RESOLVER AUN
     * - ProductoDAO (depende de CodigoDAO) // HAY QUE RESOLVER AUN
     * - CodigoServiceImpl (depende de CodigoDAO) // HAY QUE RESOLVER AUN
     * - ProductoServiceImpl (depende de ProductoDAO y CodigoServiceImpl) // HAY QUE RESOLVER AUN
     * - MenuHandler (depende de Scanner y ProductoServiceImpl)
     *
     * Esta inicialización garantiza que todas las dependencias estén correctamente conectadas.
     */
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.menuHandler = new MenuHandler(scanner);
        this.running = true;
    }

    /**
     * Punto de entrada de la aplicación Java.
     * Crea instancia de AppMenu y ejecuta el menú principal.
     */
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    /**
     * Loop principal del menú. // RESUELTO
     * Manejo de errores:
     * - NumberFormatException: Captura entrada no numérica (ej: "abc")
     * - El usuario puede volver a intentar
     * El Scanner se cierra al salir del loop.
     */
    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
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
    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearProducto();
            case 2 -> menuHandler.listarProductos();
            case 3 -> menuHandler.actualizarProducto();
            case 4 -> menuHandler.eliminarProducto();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }

    
    /*
    private ProductoServiceImpl createProductoService() { // FALTA RESOLVER ESTO
        CodigoDAO codigoDAO = new CodigoDAO();
        ProductoDAO productoDAO = new ProductoDAO(codigoDAO);
        CodigoServiceImpl codigoService = new CodigoServiceImpl(codigoDAO);
        return new ProductoServiceImpl(productoDAO, codigoService);
    }
    */
}
