case class Producto(nombre: String, categoria: String, precio: Double, stock: Int)

val productos = List(
  Producto("Laptop", "Electrónica", 1200.0, 15),
  Producto("Mouse", "Electrónica", 25.0, 100),
  Producto("Teclado", "Electrónica", 75.0, 50),
  Producto("Silla", "Muebles", 150.0, 30),
  Producto("Escritorio", "Muebles", 300.0, 20),
  Producto("Monitor", "Electrónica", 400.0, 25),
  Producto("Lámpara", "Muebles", 45.0, 60),
  Producto("Audífonos", "Electrónica", 80.0, 75),
  Producto("Cuaderno", "Oficina", 5.0, 200),
  Producto("Bolígrafo", "Oficina", 2.0, 500)
)

//EJERCICIO 1
println("--- TIPO 1: Selección (filter) ---")
productos.filter(_.stock > 50).foreach(println)

//EJERCICIO 2
println("\n--- TIPO 2: Transformación (map) ---")
productos.map(p => (p.nombre, p.precio * 1.16)).foreach(println)

//EJERCICIO 3
println("\n--- TIPO 3: Validación (exists, forall) ---")
// ¿Existe algún producto agotado?
val hayAgotados = productos.exists(_.stock == 0)
// ¿Todos los productos tienen precio mayor a 0?
val todosPrecioMayor0 = productos.forall(_.precio > 0)

//EJERCICIO 4
println("\n--- TIPO 4: Extremos (maxBy, minBy) ---")
// Producto más caro
val productoMasCaro = productos.maxBy(_.precio)
productoMasCaro.nombre
productoMasCaro.precio
// Producto con menos stock
val productoMenosStock = productos.minBy(_.stock)
productoMenosStock.nombre
productoMenosStock.stock
