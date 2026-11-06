
### Tipo 1: Selección (filter)

```scala
//EJERCICIO 1
println("--- TIPO 1: Selección (filter) ---")
val productosConStockAlto = productos.filter(_.stock > 50).foreach(println)
```

---

### Tipo 2: Transformación (map)

```scala
//EJERCICIO 2
println("\n--- TIPO 2: Transformación (map) ---")
val preciosConIVA = productos.map(p => (p.nombre, p.precio * 1.16)).foreach(println)
```

---

### Tipo 3: Validación (exists, forall)

```scala
//EJERCICIO 3
println("\n--- TIPO 3: Validación (exists, forall) ---")
// ¿Existe algún producto agotado?
val hayAgotados = productos.exists(_.stock == 0)
// ¿Todos los productos tienen precio mayor a 0?
val todosPrecioMayor0 = productos.forall(_.precio > 0)
```

---

### Tipo 4: Extremos (maxBy / minBy)

```scala
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
```

---
