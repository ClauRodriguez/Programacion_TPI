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
import model.GeneradorProductosPrueba;
import service.ProductoService;

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
     * Servicio de productos para operaciones CRUD. También proporciona acceso a
     * CodigoService mediante getCodigoService().
     */
    private final ProductoService productoService = null; // NO DEBERIA ESTAR EN NULL AQUI (solo está asi para que no de error)

    /**
     * CONSTRUCTOR DE MENU con inyección de dependencias Valida que las
     * dependencias no sean null
     */
    public MenuHandler(Scanner scanner) { // FALTA AGREGAR AQUI productoServiceImpl Servicio de productos para operaciones CRUD.
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        this.scanner = scanner; // Scanner compartido para entrada de usuario
    }

    /**
     * Opción 1: Crear nueva producto (con codigo opcional). Manejo de errores:
     * - IllegalArgumentException: Validaciones de negocio (muestra mensaje al
     * usuario)
     *
     * // NOTA: Todos los errores deberían capturarse aquí y mostrarse, NO se
     * debe propagar al menú principal
     */
    public void crearProducto() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                return;
            }
            if (nombre.length() > 120) {
                System.out.println("El nombre no puede tener más de 120 caracteres.");
                return;
            }
            System.out.print("Marca: ");
            String marca = scanner.nextLine().trim();
            if (marca.isEmpty()) {
                System.out.println("La marca no puede estar vacía.");
                return;
            }
            if (marca.length() > 80) {
                System.out.println("La marca no puede tener más de 80 caracteres.");
                return;
                }
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            if (precio < 0) {
                System.out.println("El precio debe ser mayor a 0.");
                return;
            }
            System.out.print("Peso: ");
            double peso = Double.parseDouble(scanner.nextLine());
            if (peso < 0) {
                System.out.println("El peso no puede ser negativo (pero puede ser 0).");
                return;
            }
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            if (stock < 0) {
                System.out.println("El stock no puede ser negativo.");
                return;
            }

            Producto producto = new Producto(nombre, marca, precio, peso, stock, 0, true); // COMO SE AGREGA EL ID AUTOMATICAMENTE?
            System.out.print("Asignar categoría al producto: ");
            producto.setCategoria();

            CodigoBarras codigo = null;
            System.out.print("¿Desea agregar un codigo? (s/n): "); // Pregunta si desea agregar Codigo de Barras
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                producto.setCodigoBarras(crearCodigo());
            }

            // productoService.insertar(producto); // FALTA HACER
            System.out.println("Creado exitosamente el producto: " + producto);

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
            System.out.println("4. Listar por categoría"); // SE PUEDE AGREGAR UN METODO SIMILAR POR MARCA
            System.out.print("Ingrese opción: ");

            List<Producto> productos = GeneradorProductosPrueba.generarProductosDePrueba(); // PROVISORIO HASTA QUE SE PUEDA TRAER LA LISTA DE LA BD
            int subopcion = Integer.parseInt(scanner.nextLine());

            switch (subopcion) {
                case 1:
                    productos = listarTodosProductos(productos);
                    break;
                case 2:
                    productos = listarPorId(productos);
                    break;
                case 3:
                    productos = listarPorNombre(productos);
                    break;
                case 4:
                    productos = listarPorCategoria(productos);
                    break;
                default:
                    System.out.println("Opción inválida.");
                    return; // Salir si la opción es inválida
            }
            
            if (productos.isEmpty()) {
                System.out.println("No se encontraron productos.");
            } else {
                for (Producto p : productos) {
                    System.out.println(p);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    public List<Producto> listarTodosProductos(List<Producto> productos) {
        System.out.println("Se listan todos los productos (getAll())");
        // return productoService.getAll(); // FALTA HACER
        return productos;
    }

    public List<Producto> listarPorId(List<Producto> productos) {
        System.out.print("Ingrese el ID a buscar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        List<Producto> productoId = new ArrayList<>();
        if (productoId != null) {
        productoId.add(productos.get(id-1));
        }
        // return productoService.buscarPorId(id); // FALTA HACER
        return productoId;
    }

    public List<Producto> listarPorNombre(List<Producto> productos) {
        System.out.print("Ingrese texto a buscar: ");
        String filtro = scanner.nextLine().trim();
        System.out.println("Se listan todos los productos que contienen: " + filtro);
        List<Producto> productosFiltro = new ArrayList<>();
        for (Producto producto : productos) {
            // Convertimos el nombre del producto a minúsculas y vemos si contiene el filtro
            if (producto.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                productosFiltro.add(producto);
            }
        }
        // return productoService.buscarPorNombre(filtro); // FALTA HACER
        return productosFiltro;
    }

    private List<Producto> listarPorCategoria(List<Producto> productos) {
        CategoriaProducto[] categorias = CategoriaProducto.values();
        System.out.println("\nSeleccione una opción para la categoría: ");

        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + "). " + categorias[i].name() + " - " + categorias[i].getDescripcion());
        }

        while (true) {
            try {
                System.out.print("\n * Ingrese opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                int indice = (opcion - 1);

                if (indice >= 0 && indice < categorias.length) {
                    CategoriaProducto categoriaElegida = categorias[indice];
                    System.out.println("\n**** Categoría seleccionada: " + categoriaElegida.name() + " ****");
                    // return productoService.buscarPorCategoria(categoriaElegida); // FALTA HACER
                    List<Producto> productosCategoria = new ArrayList<>();
                    for (Producto producto : productos) {
                        if (producto.getCategoria().equals(categoriaElegida)){
                            productosCategoria.add(producto);
                        }
                    }
                    return productosCategoria;
                } else {
                    System.out.println("La opción elegida (" + opcion + ") está fuera de rango. Debe ingresar una entre 1 y " + categorias.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
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
            System.out.print("\nIngrese el ID del producto a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            List<Producto> productos = GeneradorProductosPrueba.generarProductosDePrueba(); // PROVISORIO HASTA QUE SE PUEDA TRAER LA LISTA DE LA BD
            // Producto productoActualizar = productoService.getById(id); // FALTA HACER

            if (productos == null) {
                System.out.println("Producto no encontrado.");
                return;
            }

            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getId() == id) {
                    Producto productoActualizar = productos.get(i);
                    System.out.println("\nEl producto a modificar es: " + productoActualizar.getNombre() 
                            + " - ID: " + productoActualizar.getId());
                    System.out.println("-".repeat(30));
                    System.out.println("Ingrese los datos nuevos para actualizar el producto.");

                    System.out.print("Nombre actual: " + productoActualizar.getNombre() + "\n Ingrese el nuevo nombre: ");
                    String nombre = scanner.nextLine().trim();
                    if (!nombre.isEmpty()) {
                        productoActualizar.setNombre(nombre);
                    }

                    System.out.print("Marca actual: " + productoActualizar.getMarca() + "\n Ingrese la nueva marca: ");
                    String marca = scanner.nextLine().trim();
                    if (!marca.isEmpty()) {
                        productoActualizar.setMarca(marca);
                    }

                    System.out.print("Precio actual: " + productoActualizar.getPrecio() + "\n Ingrese el nuevo precio : ");
                    double precio = Double.parseDouble(scanner.nextLine());
                    if (precio != 0) {
                        productoActualizar.setPrecio(precio);
                    }

                    System.out.print("Peso actual: " + productoActualizar.getPeso() + "\n Ingrese el nuevo peso : ");
                    double peso = Double.parseDouble(scanner.nextLine());
                    if (peso != 0) {
                        productoActualizar.setPeso(peso);
                    }

                    System.out.print("Stock actual: " + productoActualizar.getStock() + "\n Ingrese el nuevo stock : ");
                    int stock = Integer.parseInt(scanner.nextLine());
                    if (stock != 0) {
                        productoActualizar.setStock(stock);
                    }

                    System.out.print("Categoria actual: " + productoActualizar.getCategoria() + "\n Ingrese la nueva categoria: ");
                    productoActualizar.setCategoria();

                    System.out.println("**** Producto actualizado exitosamente: " + productoActualizar + " ****");
                }
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
            System.out.print("\nIngrese el ID del producto a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            List<Producto> productos = GeneradorProductosPrueba.generarProductosDePrueba(); // PROVISORIO HASTA QUE SE PUEDA TRAER LA LISTA DE LA BD
            // productoService.eliminar(id); // FALTA COMPLETAR

            if (productos == null) {
                System.out.println("Producto no encontrado.");
                return;
            }

            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getId() == id) {
                    Producto productoEliminar = productos.get(i);
                    System.out.println("\nEl producto a eliminar es: " + productoEliminar.getNombre());
                    System.out.print("\n¿Está seguro de que desea eliminar este producto? (s/n): ");
                    String confirmacion = scanner.nextLine().trim();
                    if (confirmacion.equalsIgnoreCase("s")) {
                        productoEliminar.setEliminado(true);
                        System.out.println("\n**** Producto eliminado exitosamente ****");
                    } else {
                        System.out.println("\nEliminación cancelada.");
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
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
    private CodigoBarras crearCodigo() {
        int id = 0; // Crea objeto Codigo con ID=0 (será asignado por BD al insertar)
        System.out.print("Valor: ");
        String valor = scanner.nextLine().trim();
        LocalDate fechaAsignacion = LocalDate.now();
        System.out.print("Observaciones (opcional): ");
        String observaciones = scanner.nextLine().trim();
        CodigoBarras cod = new CodigoBarras(id, false, valor, fechaAsignacion, observaciones);
        return cod;
    }

}
