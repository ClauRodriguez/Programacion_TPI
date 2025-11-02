package main;

import model.Producto;
import model.CodigoBarras;
import service.ProductoService;
import service.CodigoBarrasService;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        try {
            ProductoService productoService = new ProductoService();
            CodigoBarrasService codigoBarrasService = new CodigoBarrasService();

            //Insertar producto
            //Producto vino = new Producto(200001, "Vino Malbec", 4800);
            //productoService.insertar(vino);
            //System.out.println("Producto insertado con éxito.");
            
            productoService.eliminar(200001);

            //Insertar código de barras
            //CodigoBarras codigo = new CodigoBarras("EAN13", "CB541651", new Date());
            //codigoBarrasService.insertar(codigo);
            //System.out.println("Código de barras insertado con éxito.");

            // Listar productos
            //System.out.println("\nListado de productos:");
            //productoService.getAll().forEach(System.out::println);

            // Listar códigos
           // System.out.println("\nListado de códigos de barras:");
            // codigoBarrasService.getAll().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
