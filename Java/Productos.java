import java.util.Arrays;
import java.util.List;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.stream.Collectors;

class Producto {
    String nombre, categoria;
    double precio;
    int stock;
    Producto(String nombre, String categoria, double precio, int stock) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }
    public String toString() {
        return "Producto(" + nombre + "," + categoria + "," + precio + "," + stock + ")";
    }
}

public class Productos {
    public static void main(String[] args) {
        List<Producto> productos = Arrays.asList(
            new Producto("Laptop", "Electrónica", 1200.0, 15),
            new Producto("Mouse", "Electrónica", 25.0, 100),
            new Producto("Teclado", "Electrónica", 75.0, 50),
            new Producto("Silla", "Muebles", 150.0, 30),
            new Producto("Escritorio", "Muebles", 300.0, 20),
            new Producto("Monitor", "Electrónica", 400.0, 25),
            new Producto("Lámpara", "Muebles", 45.0, 60),
            new Producto("Audífonos", "Electrónica", 80.0, 75),
            new Producto("Cuaderno", "Oficina", 5.0, 200),
            new Producto("Bolígrafo", "Oficina", 2.0, 500)
        );

        System.out.println("--- TIPO 1: Selección (filter) ---");
        List<Producto> productosConStockAlto = productos.stream()
            .filter(p -> p.stock > 50)
            .collect(Collectors.toList());
        productosConStockAlto.forEach(System.out::println);

        System.out.println("\n--- TIPO 2: Transformación (map) ---");
        productos.stream()
            .map(p -> new AbstractMap.SimpleEntry<>(p.nombre, p.precio * 1.16))
            .forEach(System.out::println);

        System.out.println("\n--- TIPO 3: Validación ---");
        boolean hayAgotados = productos.stream()
            .anyMatch(p -> p.stock == 0);
        System.out.println("¿Hay productos agotados? " + hayAgotados);

        boolean todosPrecioMayor0 = productos.stream()
            .allMatch(p -> p.precio > 0);
        System.out.println("¿Todos tienen precio positivo? " + todosPrecioMayor0);

        System.out.println("\n--- TIPO 4: Extremos ---");
        Producto productoMasCaro = productos.stream()
            .max(Comparator.comparingDouble(p -> p.precio))
            .orElse(null);
        System.out.println("Producto más caro: " + productoMasCaro.nombre + " ($" + productoMasCaro.precio + ")");

        Producto productoMenosStock = productos.stream()
            .min(Comparator.comparingInt(p -> p.stock))
            .orElse(null);
        System.out.println("Producto con menos stock: " + productoMenosStock.nombre + " (" + productoMenosStock.stock + " unidades)");
    }
}
