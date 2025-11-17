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
import java.util.Scanner;
import model.CategoriaProducto;
import model.CodigoBarras;
import model.EnumTipo;
import service.ProductoService;
import service.CodigoBarrasService;

/**
 * Manejador de operaciones del menú para gestión de productos y códigos de barras. 
 * Controla la interacción con el usuario y delega operaciones a los servicios correspondientes.
 */
public class MenuHandler {

    /** Scanner para entrada de usuario, inyectado desde AppMenu */
    private final Scanner scanner;

    /** Servicio para operaciones de productos */
    private final ProductoService productoService;

    /** Servicio para operaciones de códigos de barras */
    private final CodigoBarrasService codigoBarrasService;

    /**
     * Construye MenuHandler con dependencias requeridas.
     *
     * @param scanner Scanner para entrada de usuario
     * @param productoService Servicio de productos
     * @param codigoBarrasService Servicio de códigos de barras
     * @throws IllegalArgumentException si cualquier dependencia es null
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

    // MÉTODOS PRINCIPALES DEL MENÚ (CRUD PRODUCTOS)

    /**
     * Crea un nuevo producto con código de barras opcional. 
     * Captura datos del producto y opcionalmente asocia un código de barras.
     */
    public void crearProducto() {
        try {
            String nombre = validarEntradaString(scanner, "Nombre", 120);
            String marca = validarEntradaString(scanner, "Marca", 80);
            double precio = validarDoublePositivo("Precio: ", scanner);
            double peso = validarDoublePositivo("Peso: ", scanner);
            int stock = validarIntPositivo("Stock: ", scanner);

            Producto producto = new Producto(nombre, marca, precio, peso, stock, 0);
            CategoriaProducto categoria = seleccionarCategoria();
            producto.setCategoria(categoria);

            CodigoBarras codigo = null;
            System.out.print("\n¿Desea agregar un codigo de barras? (ingrese \"s\" para Si o cualquier otro caracter para no): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                codigo = crearCodigoBarras();
            }

            try {
                if (codigo != null) {
                    productoService.insertarConCodigoBarras(producto, codigo);
                    System.out.println("\n✓ Producto con código de barras creado exitosamente: " + producto.getNombre());
                } else {
                    productoService.insertar(producto);
                    System.out.println("\n✓ Producto creado exitosamente: " + producto.getNombre());
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Error de validación: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al crear producto: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }

    /**
     * Muestra submenú para listar productos con diferentes criterios. 
     * Opciones: todos, por ID, por nombre, por categoría.
     */
    public void listarProductos() {
        try {
            System.out.println("\n**** LISTAR PRODUCTOS ****");
            System.out.println("1. Listar todos los productos");
            System.out.println("2. Listar por ID");
            System.out.println("3. Listar por nombre");
            System.out.println("4. Listar por categoría");
            System.out.println("0. ↩ Volver al menú anterior\n");

            int subopcion = validarIntPositivo("INGRESE OPCIÓN: ", scanner);
            List<Producto> productos = new ArrayList<>();

            switch (subopcion) {
                case 1 -> productos = listarTodosProductos();
                case 2 -> productos = listarPorId();
                case 3 -> productos = listarPorNombre();
                case 4 -> productos = listarPorCategoria();
                case 0 -> {
                    System.out.println("\n↩ Volviendo al menu principal...");
                    return;
                }
                default ->
                    System.out.println("\nOpción inválida.");
            }

            if (productos == null || productos.isEmpty()) {
                System.out.println("No se encontraron productos.");
            } else {
                System.out.println("\n**** PRODUCTOS ENCONTRADOS ****");
                for (Producto p : productos) {
                    System.out.println(p);
                }
                System.out.println("\nTotal: " + productos.size() + " producto(s)");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    /**
     * Recupera todos los productos activos del sistema.
     *
     * @return Lista de productos, lista vacía si no hay resultados
     */
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

    /**
     * Busca producto por ID específico.
     *
     * @return Lista con el producto encontrado o vacía si no existe
     */
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

    /**
     * Busca productos por nombre o parte del nombre.
     *
     * @return Lista de productos que coinciden con el criterio
     */
    private List<Producto> listarPorNombre() {
        try {
            String filtro = validarEntradaString(scanner, "nombre a buscar", 120);
            if (filtro.isEmpty()) {
                System.out.println("El filtro no puede estar vacío.");
                return new ArrayList<>();
            }

            Producto producto = productoService.getByNombre(filtro);
            List<Producto> resultado = new ArrayList<>();

            if (producto != null) {
                resultado.add(producto);
            } else {
                List<Producto> todos = productoService.getAll();
                if (todos != null) {
                    for (Producto p : todos) {
                        if (p.getNombre() != null
                                && p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
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

    /**
     * Filtra productos por categoría específica.
     *
     * @return Lista de productos de la categoría seleccionada
     */
    private List<Producto> listarPorCategoria() {
        try {
            CategoriaProducto categoriaElegida = seleccionarCategoria();
            System.out.println("\nBuscando productos de la categoría: " + categoriaElegida.name() + "\n");

            List<Producto> todos = productoService.getAll();
            List<Producto> productosCategoria = new ArrayList<>();

            if (todos != null) {
                for (Producto producto : todos) {
                    if (producto.getCategoria() != null
                            && producto.getCategoria().equals(categoriaElegida)) {
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
     * Actualiza un producto existente permitiendo modificar campos individualmente.
     *
     * @throws NumberFormatException si se ingresan valores numéricos inválidos
     */
    public void actualizarProducto() {
        try {
            int id = validarIntPositivo("ID del producto a actualizar: ", scanner);
            Producto productoActualizar = productoService.getById(id);

            if (productoActualizar == null) {
                System.out.println("Producto no encontrado con ID: " + id);
                return;
            }

            System.out.println("\nEl producto a modificar es: " + productoActualizar.getNombre()
                    + " - ID: " + productoActualizar.getId());
            System.out.println("-".repeat(30));
            System.out.println("Ingrese los datos nuevos (presione Enter para mantener el valor actual):");

            // Campos actualizables
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
                productoActualizar.setPrecio(precio);
            }

            System.out.print("Peso actual: " + productoActualizar.getPeso() + "\nIngrese el nuevo peso (Enter para mantener): ");
            String pesoStr = scanner.nextLine().trim();
            if (!pesoStr.isEmpty()) {
                double peso = Double.parseDouble(pesoStr);
                productoActualizar.setPeso(peso);
            }

            System.out.print("Stock actual: " + productoActualizar.getStock() + "\nIngrese el nuevo stock (Enter para mantener): ");
            String stockStr = scanner.nextLine().trim();
            if (!stockStr.isEmpty()) {
                int stock = Integer.parseInt(stockStr);
                productoActualizar.setStock(stock);
            }

            System.out.print("Categoria actual: " + productoActualizar.getCategoria() + "\n");
            System.out.print("¿Desea cambiar la categoría? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                CategoriaProducto nuevaCategoria = seleccionarCategoria();
                productoActualizar.setCategoria(nuevaCategoria);
            }

            try {
                productoService.actualizar(productoActualizar);
                System.out.println("\n✓ Producto actualizado exitosamente: " + productoActualizar.getNombre());
            } catch (IllegalArgumentException e) {
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
     * Elimina un producto mediante soft delete. 
     * Solicita confirmación antes de proceder con la eliminación.
     */
    public void eliminarProducto() {
        try {
            int id = validarIntPositivo("Ingrese el ID del producto a eliminar: ", scanner);
            Producto productoEliminar = productoService.getById(id);

            if (productoEliminar == null) {
                System.out.println("Producto no encontrado con ID: " + id);
                return;
            }

            System.out.println("\nEl producto a eliminar es: " + productoEliminar.getNombre());
            System.out.print("\n¿Está seguro de que desea eliminar este producto? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
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
        }
    }

    /**
     * Asigna un código de barras existente a un producto.
     *
     * @throws NumberFormatException si los IDs ingresados no son válidos
     */
    public void asignarCodigoDeBarras() {
        try {
            int idProducto = validarIntPositivo("ID del producto al que se le asignará un código de barras: ", scanner);
            Producto productoActualizar = productoService.getById(idProducto);

            if (productoActualizar == null) {
                System.out.println("Producto no encontrado con ID: " + idProducto);
                return;
            }

            System.out.println("\nEl producto a modificar es: " + productoActualizar.getNombre()
                    + " - ID: " + productoActualizar.getId());
            System.out.println("-".repeat(30));

            int idCodigo = validarIntPositivo("ID del código de barras a asignar: ", scanner);
            CodigoBarras codigoParaAsignar = codigoBarrasService.getById(idCodigo);

            if (codigoParaAsignar == null) {
                System.out.println("Código de barras no encontrado con ID: " + idCodigo);
                return;
            }

            productoActualizar.setCodigoBarras(codigoParaAsignar);

            try {
                productoService.asignarCodigoDeBarras(productoActualizar);
                System.out.println("\n✓ Código de barras asignado exitosamente");
            } catch (IllegalArgumentException e) {
                System.err.println("Error de validación: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al asignar el código: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    /**
     * Recupera un producto previamente eliminado (soft undelete). 
     * Solicita confirmación antes de proceder.
     */
    public void recuperarProducto() {
        try {
            int id = validarIntPositivo("Ingrese el ID del producto a recuperar: ", scanner);
            System.out.print("\n¿Está seguro de que desea recuperar este producto? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
            String confirmacion = scanner.nextLine().trim();

            if (confirmacion.equalsIgnoreCase("s")) {
                try {
                    productoService.recuperar(id);
                    System.out.println("\n✓ Producto recuperado exitosamente (soft undelete)");
                    System.out.println(productoService.getById(id));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error al recuperar producto: " + e.getMessage());
                }
            } else {
                System.out.println("\nRecuperación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al recuperar producto: " + e.getMessage());
        }
    }

    // MÉTODOS DE CÓDIGOS DE BARRAS (CRUD)

    /**
     * Crea un código de barras independiente sin asociar a producto. 
     * Captura tipo, valor y observaciones del código.
     *
     * @return CodigoBarras creado con los datos ingresados
     */
    private CodigoBarras crearCodigoBarras() {
        int id = 0;
        EnumTipo tipo = elegirTipoCodigo();
        String valor = validarEntradaString(scanner, "Valor", 20);
        LocalDate fechaAsignacion = LocalDate.now();
        String observaciones = validarEntradaString(scanner, "Observaciones (opcional):");
        String observacionesFinal = observaciones;

        return new CodigoBarras(id, false, tipo, valor, fechaAsignacion, observacionesFinal);
    }

    /**
     * Crea un código de barras independiente y lo persiste en la base de datos.
     * Útil para precargar códigos antes de asignarlos a productos.
     */
    public void crearCodigoBarrasIndependiente() {
        try {
            CodigoBarras codigoBarras = crearCodigoBarras();
            codigoBarrasService.insertar(codigoBarras);
            System.out.println("\n**** Operación exitosa ****\nCódigo de Barra nuevo:\n - ID: "
                    + codigoBarras.getId() + "\n - Valor: " + codigoBarras.getValor()
                    + "\n - Tipo: " + codigoBarras.getTipo());
        } catch (Exception e) {
            System.err.println("Error al crear el Codigo de barras: " + e.getMessage());
        }
    }

    /**
     * Muestra submenú para listar códigos de barras. 
     * Opciones: todos los códigos o por ID específico.
     */
    public void listarCodigos() {
        try {
            System.out.println("\n**** LISTAR CODIGOS DE BARRA ****");
            System.out.println("1. Listar todos los codigos");
            System.out.println("2. Listar por ID");
            System.out.println("0. ↩ Volver al menú anterior\n");

            int subopcion = validarIntPositivo("INGRESE OPCIÓN: ", scanner);
            List<CodigoBarras> codigoBarras = new ArrayList<>();

            switch (subopcion) {
                case 1 ->
                    codigoBarras = listarCodigoBarras();
                case 2 ->
                    codigoBarras = listarPorIdCodigo();
                case 0 -> {
                    System.out.println("\n↩ Volviendo al menu principal...");
                    return;
                }
                default ->
                    System.out.println("Opción inválida.");
            }

            if (codigoBarras == null || codigoBarras.isEmpty()) {
                System.out.println("No se encontraron códigos.");
            } else {
                System.out.println("\n**** CÓDIGOS ENCONTRADOS ****");
                for (CodigoBarras p : codigoBarras) {
                    System.out.println(p);
                }
                System.out.println("\nTotal: " + codigoBarras.size() + " codigo(s)");
            }
        } catch (NumberFormatException e) {
            System.err.println("\nError: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("\nError al listar productos: " + e.getMessage());
        }
    }

    /**
     * Recupera todos los códigos de barras activos del sistema.
     *
     * @return Lista de códigos de barras, lista vacía si no hay resultados
     */
    private List<CodigoBarras> listarCodigoBarras() {
        System.out.println("\nBuscando todos los códogos de barra...");
        List<CodigoBarras> codigos = new ArrayList<>();
        try {
            List<CodigoBarras> desdeService = codigoBarrasService.getAll();
            if (desdeService != null) {
                codigos.addAll(desdeService);
            }
        } catch (Exception e) {
            System.err.println("Error al listar códogos de barra: " + e.getMessage());
        }
        return codigos;
    }

    /**
     * Busca código de barras por ID específico.
     *
     * @return Lista con el código encontrado o vacía si no existe
     */
    private List<CodigoBarras> listarPorIdCodigo() {
        try {
            System.out.print("Ingrese el ID a buscar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            CodigoBarras codigo = codigoBarrasService.getById(id);
            List<CodigoBarras> resultado = new ArrayList<>();
            if (codigo != null) {
                resultado.add(codigo);
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

    /**
     * Actualiza un código de barras existente. 
     * Permite modificar tipo, valor y observaciones.
     */
    public void actualizarCodigoBarrasPorId() {
        try {
            int id = validarIntPositivo("ID del codigo de barras a actualizar: ", scanner);
            CodigoBarras codigoBarrasActualizar = codigoBarrasService.getById(id);

            if (codigoBarrasActualizar == null) {
                System.out.println("Codigo de Barras no encontrado con ID: " + id);
                return;
            }

            System.out.println("\nEl Codigo de Barras a modificar es: " + codigoBarrasActualizar.getValor()
                    + " - ID: " + codigoBarrasActualizar.getId());
            System.out.println("-".repeat(30));
            System.out.println("Ingrese los datos nuevos:");

            codigoBarrasActualizar.setTipo(elegirTipoCodigo());
            codigoBarrasActualizar.setFechaAsignacion(LocalDate.now());

            System.out.print("Valor actual (Enter para mantener el valor actual): " + codigoBarrasActualizar.getValor() + "\nO ingrese el nuevo valor: ");
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) {
                codigoBarrasActualizar.setValor(valor);
            }

            System.out.print("Observaciones actuales (Enter para mantener el valor actual): " + codigoBarrasActualizar.getObservaciones() + "\n O ingrese nuevas observaciones: ");
            String observaciones = scanner.nextLine().trim();
            if (!observaciones.isEmpty()) {
                codigoBarrasActualizar.setObservaciones(observaciones);
            }

            try {
                codigoBarrasService.actualizar(codigoBarrasActualizar);
                System.out.println("Codigo de barras actualizado exitosamente.");
            } catch (IllegalArgumentException e) {
                System.err.println("Error de validación: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al actualizar el codigo de barras: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar el codigo de barras: " + e.getMessage());
        }
    }

    /**
     * Elimina un código de barras mediante soft delete. 
     * Solicita confirmación antes de proceder.
     *
     * @warning No verifica si el código está asociado a productos
     */
    public void eliminarCodigoBarrasPorId() {
        try {
            int id = validarIntPositivo("Ingrese el ID del código de barras a eliminar: ", scanner);
            CodigoBarras codigoEliminar = codigoBarrasService.getById(id);

            if (codigoEliminar == null) {
                System.out.println("Código de barras no encontrado con ID: " + id);
                return;
            }

            System.out.println("\nEl código de barras a eliminar es: " + codigoEliminar.getValor());
            System.out.print("¿Está seguro de que desea eliminar este producto? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
            String confirmacion = scanner.nextLine().trim();

            if (confirmacion.equalsIgnoreCase("s")) {
                codigoBarrasService.eliminar(id);
                System.out.println("\n✓ Código de barras eliminado exitosamente (soft delete)");
            } else {
                System.out.println("\nEliminación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al eliminar Código de barras: " + e.getMessage());
        }
    }

    /**
     * Recupera un código de barras previamente eliminado (soft undelete).
     * Solicita confirmación antes de proceder.
     */
    public void recuperarCodigoBarrasPorId() {
        try {
            int id = validarIntPositivo("Ingrese el ID del código de barras a recuperar: ", scanner);
            System.out.print("¿Confirma que desea recuperar este código de barras? (ingrese \"s\" para Si o cualquier otro caracter para cancelar): ");
            String confirmacion = scanner.nextLine().trim();

            if (confirmacion.equalsIgnoreCase("s")) {
                try {
                    codigoBarrasService.recuperar(id);
                    System.out.println("\n✓ Código de barras recuperado exitosamente");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error al recuperar código de barras: " + e.getMessage());
                }
            } else {
                System.out.println("\nRecuperación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.err.println("Error al recuperar código de barras: " + e.getMessage());
        }
    }

    // MÉTODOS DE SELECCIÓN (CATEGORÍA Y TIPO)

    /**
     * Permite al usuario seleccionar una categoría de producto desde la lista disponible.
     *
     * @return CategoriaProducto seleccionada
     */
    private CategoriaProducto seleccionarCategoria() {
        CategoriaProducto[] categorias = CategoriaProducto.values();
        System.out.println("\n**** SELECCIONE CATEGORIA ****");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + "). " + categorias[i].name() + " - " + categorias[i].getDescripcion());
        }

        while (true) {
            System.out.print("\nINGRESE OPCIÓN: ");
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
     * Permite al usuario seleccionar un tipo de código de barras.
     *
     * @return EnumTipo seleccionado
     */
    private EnumTipo elegirTipoCodigo() {
        EnumTipo[] tipos = EnumTipo.values();
        System.out.println("Seleccione el tipo de Código de Barras:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + "). " + tipos[i].name());
        }

        while (true) {
            System.out.print("\nINGRESE OPCIÓN: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                if (opcion >= 1 && opcion <= tipos.length) {
                    return tipos[opcion - 1];
                } else {
                    System.out.println("\nLa opción debe estar entre 1 y " + tipos.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("\nIngrese un número válido.");
            }
        }
    }

    // MÉTODOS DE VALIDACIÓN
    
    /**
     * Valida y obtiene un número entero positivo desde la entrada del usuario.
     *
     * @param mensaje Mensaje para solicitar la entrada
     * @param scanner Scanner para leer la entrada
     * @return Número entero positivo válido
     */
    static int validarIntPositivo(String mensaje, Scanner scanner) {
        boolean bandera = false;
        int num = 0;
        do {
            try {
                System.out.print(mensaje);
                num = Integer.parseInt(scanner.nextLine());
                if (num >= 0) {
                    bandera = true;
                } else {
                    System.out.println("*ERROR. No puede ser un número negativo. ");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Solo admite caracteres numericos.");
            }
        } while (!bandera);
        return num;
    }

    /**
     * Valida y obtiene un número decimal positivo desde la entrada del usuario.
     *
     * @param mensaje Mensaje para solicitar la entrada
     * @param scanner Scanner para leer la entrada
     * @return Número decimal positivo válido
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
            } catch (NumberFormatException nfe) {
                System.out.println("Solo admite caracteres numericos.");
            }
        } while (!bandera);
        return num;
    }

    /**
     * Valida una cadena de texto con longitud máxima.
     *
     * @param scanner Scanner para leer la entrada
     * @param nombreVariable Nombre del campo a validar
     * @param maxChar Longitud máxima permitida
     * @return Cadena válida, no vacía y dentro del límite de caracteres
     * @throws IllegalArgumentException si maxChar es menor o igual a cero
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

    /**
     * Valida una cadena de texto sin límite de longitud.
     *
     * @param scanner Scanner para leer la entrada
     * @param nombreVariable Nombre del campo a validar
     * @return Cadena válida y no vacía
     */
    static String validarEntradaString(Scanner scanner, String nombreVariable) {
        return validarEntradaString(scanner, nombreVariable, Integer.MAX_VALUE);
    }
}
