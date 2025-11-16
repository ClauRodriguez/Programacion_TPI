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
 * Controlador principal que gestiona el ciclo de vida de la aplicación.
 * Coordina dependencias y ejecuta el bucle principal del menú.
 */
public class AppMenu {

    /** Scanner para entrada de usuario, compartido en toda la aplicación */
    private final Scanner scanner;

    /** Manejador de operaciones del menú */
    private final MenuHandler menuHandler;

    /** Controla la ejecución del bucle principal */
    private boolean running;

    /**
     * Construye e inicializa la aplicación con todas las dependencias. 
     * Crea servicios y configura el manejador del menú.
     */
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        ProductoService productoService = createProductoService();
        CodigoBarrasService codigoBarrasService = createCodigoBarrasService();
        this.menuHandler = new MenuHandler(scanner, productoService, codigoBarrasService);
        this.running = true;
    }

    /**
     * Ejecuta el bucle principal de la aplicación. 
     * Muestra menú, procesa entrada y maneja excepciones hasta que el usuario sale.
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
     * Procesa la opción del menú seleccionada delegando al handler correspondiente.
     *
     * @param opcion Opción numérica del menú a ejecutar
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
            default ->
                System.out.println("Opcion no valida.");
        }
    }

    /**
     * Crea el servicio de productos con sus dependencias.
     * ProductoDAO → ProductoService
     *
     * @return Instancia configurada de ProductoService
     */
    private ProductoService createProductoService() {
        return new ProductoService();
    }

    /**
     * Crea el servicio de códigos de barras con sus dependencias.
     * CodigoBarrasDAO → CodigoBarrasService
     *
     * @return Instancia configurada de CodigoBarrasService
     */
    private CodigoBarrasService createCodigoBarrasService() {
        return new CodigoBarrasService();
    }
}
