package main;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Producto;
import java.util.Scanner; // HAY QUE RESOLVER QUE ESTÉ SOLO EN UN LUGAR
import model.CategoriaProducto;
import model.CodigoBarras;
import model.EnumTipo;
import service.ProductoService;
import service.CodigoBarrasService;

/**
 * Controlador de las operaciones del menú (Menu Handler). 
 * Gestiona toda la lógica de interacción con el usuario para operaciones CRUD.
 *
 * Responsabilidades: 
 * - Capturar entrada del usuario desde consola (Scanner) 
 * - Validar entrada básica (conversión de tipos, valores vacíos) 
 * - Invocar servicios de negocio (ProductoService, CodigoService) 
 * - Mostrar resultados y mensajes de error al usuario - Coordinar operaciones complejas (crear producto con codigo, etc.)
 *
 * Patrón: Controller (MVC) - capa de presentación en arquitectura de 4 capas
 * Arquitectura: Main → Service → DAO → Models
 *
 * IMPORTANTE: Este handler NO contiene lógica de negocio. Todas las validaciones de negocio están en la capa Service.
 */
public class MenuHandler {

    /**
     * Scanner compartido para leer entrada del usuario. Inyectado desde AppMenu
     * para evitar múltiples Scanners de System.in.
     */
    private final Scanner scanner;

    /**
     * Servicio de productos para operaciones CRUD.
     */
    private final ProductoService productoService;
    
    /**
     * Servicio de códigos de barras para operaciones CRUD.
     */
    private final CodigoBarrasService codigoBarrasService;

    /**
     * CONSTRUCTOR DE MENU con inyección de dependencias.
     * Valida que las dependencias no sean null.
     * 
     * @param scanner Scanner compartido para entrada de usuario
     * @param productoService Servicio de productos para operaciones CRUD
     * @param codigoBarrasService Servicio de códigos de barras para operaciones CRUD
     */
    public MenuHandler(Scanner scanner, ProductoService productoService, CodigoBarrasService codigoBarrasService) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        if (productoService == null) {
            throw new IllegalArgumentException("ProductoService no puede ser null");
        }
        if (codigoBarrasService == null) {
            throw new IllegalArgumentException("CodigoBarrasService no puede ser null");
        }
        this.scanner = scanner;
        this.productoService = productoService;
        this.codigoBarrasService = codigoBarrasService;
    }

    /**
     * Opción 1: Crear nueva producto (con codigo opcional). Manejo de errores:
     * - IllegalArgumentException: Validaciones de negocio (muestra mensaje al
     * usuario)
     *
     * NOTA: Todos los errores deberían capturarse aquí y mostrarse, NO se
     * debe propagar al menú principal
     */
    public void crearProducto() {
        try {
            // Capturar datos con validaciones. Después se vuelven a chequear en el Service)
            String nombre = validarEntradaString(scanner, "Nombre", 120); // Valida que el nombre no esté vacio y no tenga más de 120 caracteres
            
            String marca = validarEntradaString(scanner, "Marca", 80); // Valida que el nombre no esté vacio y no tenga más de 80 caracteres
                     
            double precio = validarDoublePositivo("Precio", scanner); // Valida que el precio sea numero y positivo
            
            double peso = validarDoublePositivo("Peso", scanner); // Valida que el peso sea numero y positivo
            
            int stock = validarIntPositivo("Stock", scanner); // Valida que el stock sea numero entero y positivo

            Producto producto = new Producto(nombre, marca, precio, peso, stock, 0);
            
            // Seleccionar categoría
            CategoriaProducto categoria = seleccionarCategoria();
            producto.setCategoria(categoria);

            // Manejar código de barras si el usuario lo desea
            CodigoBarras codigo = null;
            System.out.print("¿Desea agregar un codigo de barras? (ingrese \"s\" para Si o cualquier otro caracter para no): "); // Pregunta si desea agregar Codigo de Barras
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                codigo = crearCodigoBarras();
            }

            // Guardar producto (el Service valida y maneja errores)
            try {
                // Si hay código de barras, usar método transaccional que inserta ambos en una sola transacción
                if (codigo != null) {
                    productoService.insertarConCodigoBarras(producto, codigo);
                    System.out.println("✓ Producto con código de barras creado exitosamente: " + producto.getNombre());
                } else {
                    // Si no hay código de barras, insertar solo el producto
                    productoService.insertar(producto);
                    System.out.println("✓ Producto creado exitosamente: " + producto.getNombre());
                }
                
            } catch (IllegalArgumentException e) {
                // Errores de validación - mostrar mensaje amigable
                // El rollback se maneja automáticamente en el Service
                System.err.println("Error de validación: " + e.getMessage());
            } catch (Exception e) {
                // Errores de base de datos - el rollback se maneja automáticamente en el Service
                System.err.println("Error al crear producto: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }

    /**
     * Opción 2: Listar productos (todas o filtradas por nombre).
     *
     * Si no hay productos: Muestra "No se encontraron productos"
     *
     * // ME DIJERON EN CONSULTA QUE HAY QUE PEDIRLE A LA DB LA LISTA, 
     * // Y SI ES NECESARIO UN CRITERIO APLICAR UN FILTRO EN EL MISMO PEDIDO. 
     * // IGUAL SI HAY QUE ORDENAR.
     */
    public void listarProductos() {
        try {
            System.out.println("\n**** LISTAR PRODUCTOS ****");
            System.out.println("1. Listar todos los productos");
            System.out.println("2. Listar por ID");
            System.out.println("3. Listar por nombre");
            System.out.println("4. Listar por categoría");
            System.out.println("0. ↩ Volver al menú anterior");

            int subopcion = validarIntPositivo("Ingrese opción: ", scanner);
            List<Producto> productos = new ArrayList<>();

            switch (subopcion) {
                case 1:
                    productos = listarTodosProductos();
                    break;
                case 2:
                    productos = listarPorId();
                    break;
                case 3:
                    productos = listarPorNombre();
                    break;
                case 4:
                    productos = listarPorCategoria();
                    break;
                case 0:
                    System.out.println("\n↩ Volviendo al menu principal...");
                    return;
                default:
                    System.out.println("Opción inválida.");
                    return; // Salir si la opción es inválida
            }
            
            if (productos == null || productos.isEmpty()) {
                System.out.println("No se encontraron productos.");
            } else {
                System.out.println("\n**** PRODUCTOS ENCONTRADOS ****\n");
                for (Producto p : productos) {
                    System.out.println(p);
                }
                System.out.println("\nTotal: " + productos.size() + " producto(s)");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Producto> listarTodosProductos() {
        try {
            System.out.println("\nBuscando todos los productos...");
            List<Producto> productos = productoService.getAll();
            return productos != null ? productos : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Producto> listarPorId() {
        try {
            System.out.print("Ingrese el ID a buscar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Producto producto = productoService.getById(id);
            List<Producto> resultado = new ArrayList<>();
            if (producto != null) {
                resultado.add(producto);
            }
            return resultado;
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Producto> listarPorNombre() {
        try {
            String filtro = validarEntradaString(scanner, "nombre a buscar", 120);
            if (filtro.isEmpty()) {
                System.out.println("El filtro no puede estar vacío.");
                return new ArrayList<>();
            }
            
            // Buscar por nombre exacto primero
            Producto producto = productoService.getByNombre(filtro);
            List<Producto> resultado = new ArrayList<>();
            
            if (producto != null) {
                resultado.add(producto);
            } else {
                // Si no hay coincidencia exacta, buscar en todos los productos
                List<Producto> todos = productoService.getAll();
                if (todos != null) {
                    for (Producto p : todos) {
                        if (p.getNombre() != null && 
                            p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                            resultado.add(p);
                        }
                    }
                }
            }
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Producto> listarPorCategoria() {
        try {
            CategoriaProducto categoriaElegida = seleccionarCategoria();
            System.out.println("\nBuscando productos de la categoría: " + categoriaElegida.name());
            
            // Obtener todos los productos y filtrar por categoría
            List<Producto> todos = productoService.getAll();
            List<Producto> productosCategoria = new ArrayList<>();
            
            if (todos != null) {
                for (Producto producto : todos) {
                    if (producto.getCategoria() != null && 
                        producto.getCategoria().equals(categoriaElegida)) {
                        productosCategoria.add(producto);
                    }
                }
            }
            
            return productosCategoria;
        } catch (Exception e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Opción 3: Actualizar producto existente.
     *
     * 1. Solicita ID de la producto 2. Obtiene producto actual de la BD 3.
     * Muestra valores actuales y permite actualizar (Ingresa uno nuevo o deja
     * el actual con enter)
     */
    public void actualizarProducto() {
        try {
            int id = validarIntPositivo("ID del producto a actualizar: ", scanner);

            // Obtener producto de la base de datos
            Producto productoActualizar = productoService.getById(id);

            if (productoActualizar == null) {
                System.out.println("Producto no encontrado con ID: " + id);
                return;
            }
            
            System.out.println("\nEl producto a modificar es: " + productoActualizar.getNombre() 
                    + " - ID: " + productoActualizar.getId());
            System.out.println("-".repeat(30));
            System.out.println("Ingrese los datos nuevos (presione Enter para mantener el valor actual):");

            System.out.print("Nombre actual (Enter para mantener): " + productoActualizar.getNombre() + "\nO ingrese el nuevo nombre: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                productoActualizar.setNombre(nombre);
            }

            System.out.print("Marca actual (Enter para mantener): " + productoActualizar.getMarca() + "\n O ingrese la nueva marca: ");
            String marca = scanner.nextLine().trim();
            if (!marca.isEmpty()) {
                productoActualizar.setMarca(marca);
            }

            System.out.print("Precio actual (Enter para mantener): " + productoActualizar.getPrecio() + "\nO ingrese el nuevo precio: ");
            String precioStr = scanner.nextLine().trim();
            if (!precioStr.isEmpty()) {
                double precio = Double.parseDouble(precioStr);
                productoActualizar.setPrecio(precio); // La validación la hace el Service
            }

            System.out.print("Peso actual: " + productoActualizar.getPeso() + "\nIngrese el nuevo peso (Enter para mantener): ");
            String pesoStr = scanner.nextLine().trim();
            if (!pesoStr.isEmpty()) {
                double peso = Double.parseDouble(pesoStr);
                productoActualizar.setPeso(peso); // La validación la hace el Service
            }

            System.out.print("Stock actual: " + productoActualizar.getStock() + "\nIngrese el nuevo stock (Enter para mantener): ");
            String stockStr = scanner.nextLine().trim();
            if (!stockStr.isEmpty()) {
                int stock = Integer.parseInt(stockStr);
                productoActualizar.setStock(stock); // La validación la hace el Service
            }

            System.out.print("Categoria actual: " + productoActualizar.getCategoria() + "\n");
            System.out.print("¿Desea cambiar la categoría? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                CategoriaProducto nuevaCategoria = seleccionarCategoria();
                productoActualizar.setCategoria(nuevaCategoria);
            }

            // Guardar cambios en la base de datos (validaciones en Service)
            try {
                productoService.actualizar(productoActualizar);
                System.out.println("\n✓ Producto actualizado exitosamente: " + productoActualizar.getNombre());
            } catch (IllegalArgumentException e) {
                // Errores de validación - mostrar mensaje amigable
                System.err.println("Error de validación: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al actualizar producto: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    /**
     * Opción 4: Eliminar producto (soft delete).
     *
     * Flujo: 1. Solicita ID de la producto 2. Invoca productoService.eliminar()
     * que: - Marca producto.eliminado = TRUE
     */
    public void eliminarProducto() {
        try {
            int id = validarIntPositivo("Ingrese el ID del producto a eliminar: ", scanner);

            // Obtener producto de la base de datos
            Producto productoEliminar = productoService.getById(id);

            if (productoEliminar == null) {
                System.out.println("Producto no encontrado con ID: " + id);
                return;
            }

            System.out.println("\nEl producto a eliminar es: " + productoEliminar.getNombre());
            System.out.print("¿Está seguro de que desea eliminar este producto? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
            String confirmacion = scanner.nextLine().trim();
            
            if (confirmacion.equalsIgnoreCase("s")) {
                productoService.eliminar(id);
                System.out.println("\n✓ Producto eliminado exitosamente (soft delete)");
            } else {
                System.out.println("\nEliminación cancelada.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar privado: Permite al usuario seleccionar una categoría de producto.
     * 
     * @return CategoriaProducto seleccionada
     */
    private CategoriaProducto seleccionarCategoria() {
        CategoriaProducto[] categorias = CategoriaProducto.values();
        System.out.println("\nSeleccione una opción para la categoría: ");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + "). " + categorias[i].name() + " - " + categorias[i].getDescripcion());
        }
        
        while (true) {
            System.out.print("Opción: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                int indice = opcion - 1;
                if (indice >= 0 && indice < categorias.length) {
                    return categorias[indice];
                } else {
                    System.out.println("La opción debe estar entre 1 y " + categorias.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }
    
    /**
     * Método auxiliar privado: Crea un objeto Codigo de Barras capturando Valor
     * y observaciones (opcionales).
     *
     * Usado por: - crearProducto(): Para agregar codigo al crear producto
     *
     *
     * @return CodigoBarras nuevo (ID=0)
     */
    
    private EnumTipo elegirTipoCodigo() {
        EnumTipo[] tipos = EnumTipo.values();
        System.out.println("Seleccione el tipo de Código de Barras:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + "). " + tipos[i].name());
        }

        while (true) {
            System.out.print("Opción: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                if (opcion >= 1 && opcion <= tipos.length) {
                    return tipos[opcion - 1]; // índice correcto
                } else {
                    System.out.println("La opción debe estar entre 1 y " + tipos.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private CodigoBarras crearCodigoBarras() {
        int id = 0; // lo asignará la BD posteriormente
        EnumTipo tipo = elegirTipoCodigo(); // <-- ahora sí es EnumTipo

        System.out.print("Valor: ");
        String valor = scanner.nextLine().trim();

        LocalDate fechaAsignacion = LocalDate.now();

        System.out.print("Observaciones (opcional): ");
        String observaciones = scanner.nextLine().trim();

        // IMPORTANTE: Guardar el valor TAL CUAL, sin convertir a null
        // Si está vacío, guardar como cadena vacía, NO como null
        // Esto asegura que si el usuario ingresa algo, se guarde
        String observacionesFinal = observaciones; // NO convertir a null

        return new CodigoBarras(id, false, tipo, valor, fechaAsignacion, observacionesFinal);
    }
    
    // METODOS PARA VALIDACIONES 
    
         /**
     * Valida y obtiene un número entero positivo desde la entrada del usuario.
     *
     * Este método solicita repetidamente al usuario que ingrese un valor hasta
     * que se proporcione un número entero válido y positivo. 
     * Maneja errores de formato y valores no positivos mostrando mensajes de error apropiados.
     *
     * @param mensaje El mensaje que se muestra al usuario para solicitar la entrada
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario
     * @return Un número entero positivo válido introducido por el usuario
     */
        static int validarIntPositivo(String mensaje, Scanner scanner) {
        boolean bandera = false;
        int num = 0;
        do {
            try {
                System.out.print(mensaje);
                num = Integer.parseInt(scanner.nextLine()); // Si se ingresa un double se transforma en int
                if (num >= 0) {
                    bandera = true;
                } else {
                    System.out.println("*ERROR. No puede ser un número negativo. ");
                }
            } catch (NumberFormatException nfe) { // Captura error si no se ingresa numero
                System.out.println("Solo admite caracteres numericos.");
            }
        } while (!bandera);
        return num;
        }
    
     /**
     * Valida y obtiene un número decimal positivo desde la entrada del usuario.
     *
     * Este método solicita repetidamente al usuario que ingrese un valor hasta
     * que se proporcione un número decimal válido y positivo. 
     * Maneja errores de formato y valores no positivos mostrando mensajes de error apropiados.
     *
     * @param mensaje El mensaje que se muestra al usuario para solicitar la entrada
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario
     * @return Un número decimal positivo válido introducido por el usuario
     */
    static double validarDoublePositivo(String mensaje, Scanner scanner) {
        boolean bandera = false;
        double num = 0;
        do {
            try {
                System.out.print(mensaje);
                num = Double.parseDouble(scanner.nextLine());
                if (num >= 0) {
                    bandera = true;
                } else {
                    System.out.println("*ERROR. No puede ser un número negativo. ");
                }
            } catch (NumberFormatException nfe) { // Captura error si no se ingresa numero
                System.out.println("Solo admite caracteres numericos.");
            }
        } while (!bandera);
        return num;
    }
 
    /**
     * Valida y obtiene una cadena de texto desde la entrada del usuario 
     * que cumple con criterios específicos de longitud y contenido.
     *
     * Este método solicita repetidamente al usuario que ingrese un valor hasta que se 
     * proporcione una cadena no vacía y que no exceda la longitud máxima especificada. 
     * La función elimina automáticamente espacios en blanco al inicio y final de la entrada.
     *
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario
     * @param nombreVariable El nombre descriptivo del campo que se está validando (para mensajes)
     * @param maxChar La longitud máxima permitida para la cadena (debe ser positiva)
     * @return Una cadena de texto válida, sin espacios al inicio/final, no vacía y dentro del límite de caracteres
     *
     * @throws IllegalArgumentException Si maxChar es menor o igual a cero
     */
    static String validarEntradaString(Scanner scanner, String nombreVariable, int maxChar) {
        if (maxChar <= 0) {
            throw new IllegalArgumentException("El máximo de caracteres debe ser positivo.");
        }

        String variable = "";
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print("Ingrese " + nombreVariable + ": ");
            variable = scanner.nextLine().trim();

            if (variable.isEmpty()) {
                System.out.println(nombreVariable + " no puede estar vacío. Inténtelo de nuevo.");
            } else if (variable.length() > maxChar) {
                System.out.println(nombreVariable + " no puede tener más de " + maxChar + " caracteres. Inténtelo de nuevo.");
            } else {
                entradaValida = true;
            }
        }
        return variable;
    }
}
