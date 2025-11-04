
package tpi_p2.Models;

/**
 * @author Hernán E. Bula
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GeneradorProductosPrueba { // ESTO ES SOLO PARA PROBAR. UNA VEZ CONECTEMOS CON LA BD HAY QUE BORRAR TODO LO QUE NO FUNCIONE CON LA BD.
   
    public static List<Producto> generarProductosDePrueba() {
        List<Producto> productos = new ArrayList<>();
        
        // 1. Productos de ALIMENTOS (8 productos)
        productos.add(crearProducto(1, "Leche Entera", "La Serenísima", CategoriaProducto.ALIMENTOS, 2.50, 1.0, 45, 
                                   crearCodigoBarras(1, "7791234567890", LocalDate.of(2024, 1, 15), "Lote L123")));
        
        productos.add(crearProducto(2, "Pan de Molde", "Bimbo", CategoriaProducto.ALIMENTOS, 1.80, 0.5, 32, 
                                   crearCodigoBarras(2, "7791234567891", LocalDate.of(2024, 1, 16), "Pan integral")));
        
        productos.add(crearProducto(3, "Arroz Largo Fino", "Gallo", CategoriaProducto.ALIMENTOS, 3.20, 1.0, 67, 
                                   crearCodigoBarras(3, "7791234567892", LocalDate.of(2024, 1, 17), "Arroz premium")));
        
        productos.add(crearProducto(4, "Fideos Tallarín", "Matarazzo", CategoriaProducto.ALIMENTOS, 1.90, 0.5, 54, 
                                   crearCodigoBarras(4, "7791234567910", LocalDate.of(2024, 1, 18), "Harina trigo")));
        
        productos.add(crearProducto(5, "Galletas de Agua", "Terrabusi", CategoriaProducto.ALIMENTOS, 2.10, 0.4, 28, 
                                   crearCodigoBarras(5, "7791234567911", LocalDate.of(2024, 1, 19), "Pack x 300g")));
        
        productos.add(crearProducto(6, "Aceite de Girasol", "Natura", CategoriaProducto.ALIMENTOS, 4.80, 1.0, 39, 
                                   crearCodigoBarras(6, "7791234567912", LocalDate.of(2024, 1, 20), "Aceite refinado")));
        
        productos.add(crearProducto(7, "Yogur Bebible", "La Serenísima", CategoriaProducto.ALIMENTOS, 1.20, 0.2, 72, 
                                   crearCodigoBarras(7, "7791234567913", LocalDate.of(2024, 1, 21), "Sabor frutilla")));
        
        productos.add(crearProducto(8, "Queso Cremoso", "Sancor", CategoriaProducto.ALIMENTOS, 5.40, 0.3, 23, 
                                   crearCodigoBarras(8, "7791234567914", LocalDate.of(2024, 1, 22), "Queso untable")));
        
        // 2. Productos de BEBIDAS (4 productos)
        productos.add(crearProducto(9, "Agua Mineral", "Villavicencio", CategoriaProducto.BEBIDAS, 1.50, 1.5, 88, 
                                   crearCodigoBarras(9, "7791234567893", LocalDate.of(2024, 1, 23), "Agua sin gas")));
        
        productos.add(crearProducto(10, "Coca-Cola", "Coca-Cola", CategoriaProducto.BEBIDAS, 2.80, 2.25, 56, 
                                   crearCodigoBarras(10, "7791234567894", LocalDate.of(2024, 1, 24), "Gaseosa cola")));
        
        productos.add(crearProducto(11, "Jugo de Naranja", "Cepita", CategoriaProducto.BEBIDAS, 2.10, 1.0, 41, 
                                   crearCodigoBarras(11, "7791234567895", LocalDate.of(2024, 1, 25), "Jugo natural")));
        
        productos.add(crearProducto(12, "Cerveza Rubia", "Quilmes", CategoriaProducto.BEBIDAS, 3.50, 0.7, 95, 
                                   crearCodigoBarras(12, "7791234567915", LocalDate.of(2024, 1, 26), "Lata 473ml")));
        
        // 3. Productos de ELECTRODOMÉSTICOS (3 productos)
        productos.add(crearProducto(13, "Licuadora", "Philips", CategoriaProducto.ELECTRODOMESTICOS, 89.90, 2.5, 12, 
                                   crearCodigoBarras(13, "7791234567900", LocalDate.of(2024, 1, 27), "600W vaso vidrio")));
        
        productos.add(crearProducto(14, "Tostadora", "Atma", CategoriaProducto.ELECTRODOMESTICOS, 45.30, 1.8, 18, 
                                   crearCodigoBarras(14, "7791234567901", LocalDate.of(2024, 1, 28), "2 ranuras")));
        
        productos.add(crearProducto(15, "Ventilador de Pie", "Liliana", CategoriaProducto.ELECTRODOMESTICOS, 67.80, 3.2, 7, 
                                   crearCodigoBarras(15, "7791234567916", LocalDate.of(2024, 1, 29), "3 velocidades")));
        
        // 4. Productos de FERRETERÍA (3 productos)
        productos.add(crearProducto(16, "Martillo de Carpintero", "Truper", CategoriaProducto.FERRETERIA, 15.60, 0.6, 34, 
                                   crearCodigoBarras(16, "7791234567902", LocalDate.of(2024, 1, 30), "Mango fibra")));
        
        productos.add(crearProducto(17, "Destornillador Plano", "Stanley", CategoriaProducto.FERRETERIA, 8.90, 0.1, 67, 
                                   crearCodigoBarras(17, "7791234567903", LocalDate.of(2024, 1, 31), "Punta 6mm")));
        
        productos.add(crearProducto(18, "Cinta Métrica", "Irwin", CategoriaProducto.FERRETERIA, 12.40, 0.3, 29, 
                                   crearCodigoBarras(18, "7791234567917", LocalDate.of(2024, 2, 1), "5 metros")));
        
        // 5. Productos de LIMPIEZA (2 productos)
        productos.add(crearProducto(19, "Detergente Líquido", "Ala", CategoriaProducto.LIMPIEZA, 5.60, 1.1, 78, 
                                   crearCodigoBarras(19, "7791234567906", LocalDate.of(2024, 2, 2), "Ropa color")));
        
        productos.add(crearProducto(20, "Lavandina", "Ayudín", CategoriaProducto.LIMPIEZA, 3.20, 1.0, 63, 
                                   crearCodigoBarras(20, "7791234567907", LocalDate.of(2024, 2, 3), "Desinfectante")));
        
        return productos;
    }
    
    private static Producto crearProducto(int id, String nombre, String marca, CategoriaProducto categoria, 
                                         double precio, double peso, int stock, CodigoBarras codigoBarras) {
        // Usar el constructor actualizado con stock
        Producto producto = new Producto(nombre, marca, precio, peso, stock, id, false);
        
        // Usar reflection para settear la categoría y código de barras
        try {
            // Setear categoría
            java.lang.reflect.Field campoCategoria = Producto.class.getDeclaredField("categoria");
            campoCategoria.setAccessible(true);
            campoCategoria.set(producto, categoria);
            
            // Setear código de barras
            java.lang.reflect.Field campoCodigoBarras = Producto.class.getDeclaredField("codigoBarras");
            campoCodigoBarras.setAccessible(true);
            campoCodigoBarras.set(producto, codigoBarras);
            
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
        
        return producto;
    }
    
    private static CodigoBarras crearCodigoBarras(int id, String valor, LocalDate fecha, String observaciones) {
        CodigoBarras cb = new CodigoBarras();
        
        try {
            // Setear ID
            cb.setId(id);
            
            // Setear tipo (alternar entre EAN13, EAN8, UPC)
            EnumTipo tipo;
            switch (id % 3) {
                case 0: tipo = EnumTipo.EAN13; break;
                case 1: tipo = EnumTipo.EAN8; break;
                default: tipo = EnumTipo.UPC; break;
            }
            
            java.lang.reflect.Field campoTipo = CodigoBarras.class.getDeclaredField("tipo");
            campoTipo.setAccessible(true);
            campoTipo.set(cb, tipo);
            
            // Setear valor
            java.lang.reflect.Field campoValor = CodigoBarras.class.getDeclaredField("valor");
            campoValor.setAccessible(true);
            campoValor.set(cb, valor);
            
            // Setear fecha
            java.lang.reflect.Field campoFecha = CodigoBarras.class.getDeclaredField("fechaAsignacion");
            campoFecha.setAccessible(true);
            campoFecha.set(cb, fecha);
            
            // Setear observaciones
            java.lang.reflect.Field campoObs = CodigoBarras.class.getDeclaredField("observaciones");
            campoObs.setAccessible(true);
            campoObs.set(cb, observaciones);
            
        } catch (Exception e) {
            System.err.println("Error al crear código de barras: " + e.getMessage());
        }
        
        return cb;
    }
}