# PrograFuncReactiva-Consulta

## Parte 1 – Explorar: la programación funcional en el mundo real

Antes de programar, investiga dónde y para qué se usa la programación funcional fuera del aula.

Fuera del aula, la programación funcional se usa en Big Data, telecomunicaciones, finanzas y desarrollo web (por empresas como WhatsApp, Netflix y bancos globales) porque su enfoque en datos inmutables y funciones puras permite procesar billones de datos en paralelo y mantener millones de usuarios conectados al mismo tiempo sin que el sistema sufra errores, se bloquee o colapse

### Caso 1: Apache Spark (Big Data)

- **Problema que resuelve:** Procesar y analizar grandes volúmenes de datos (terabytes o petabytes) de forma distribuida y en paralelo, de manera eficiente y tolerante a fallos
- **Lenguaje / Herramienta:** Scala (Spark está escrito originalmente en Scala)
- **Ventaja del enfoque funcional:** Las operaciones sobre RDDs y DataFrames son inmutables y se expresan como cadenas de transformaciones (`map`, `filter`, `flatMap`, `reduceByKey`). Esto permite que Spark optimice el plan de ejecución, paralelice el trabajo y reconstruya datos en caso de fallo sin efectos secundarios
- **Fuente:** https://spark.apache.org/docs/latest/

**Fragmento de código real con funciones anónimas:**

```scala
// Word Count clásico en Spark
val textFile = sc.textFile("hdfs://...")
val counts = textFile
  .flatMap(line => line.split(" "))   // lambda: divide cada línea en palabras
  .map(word => (word, 1))             // lambda: asigna 1 a cada palabra
  .reduceByKey((a, b) => a + b)       // lambda: suma los conteos
counts.collect()
```

Sin lambdas, este código sería mucho más largo y difícil de distribuir.

### Caso 2: Netflix(Sistemas de recomendación)

- **Problema que resuelve:** Procesar millones de eventos de visualización por día para generar recomendaciones personalizadas y operar su backend de forma confiable
- **Lenguaje / Herramienta:** Scala y java (con Streams)
- **Ventaja del enfoque funcional:** Un código más conciso, menos propenso a bugs por el estado mutable y es más fácil de poder arreglar en caso de errores
- **Fuente:** https://netflixtechblog.com/

**Fragmento de código real con lambdas (Streams de Java):**

```java
List<Movie> recommendations = userViewingHistory.stream()
    .filter(movie -> movie.getRating() >= 4.0)
    .map(movie -> new Recommendation(movie, score(movie)))
    .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
    .limit(10)
    .collect(Collectors.toList());
```

**¿Qué hace cada lambda?**
- `line => line.split(" ")`: recibe una string y la divide en palabras
- `word => (word, 1)`: convierte cada palabra en un par (palabra, 1)
- `(a, b) => a + b`: suma los conteos de palabras iguales

## Parte 2 - Entender: ¿Qué es una función anónima?

### 1. ¿Qué es una función anónima y de dónde viene el nombre lambda?

Una **función anónima** es una función que no tiene nombre o algo para que se la pueda identificar, esta se define en el momento en que se necesita, generalmente para pasársela como argumento a otra función (como `map`, `filter`, etc.), además es muy común crearlas y **guardarlas en un valor inmutable (`val`)** para poder utilizarlas de forma limpia y ordenada

El nombre **lambda** proviene del "Cálculo lambda" (λ-calculus), un sistema matemático creado por Alonzo Church en la decada de 1930 para estudiar la computación, funciones y algoritmos, ya que en ese sistema las funciones se representan con la letra griega λ. Por eso, en la programación a las funciones anónimas se les llama "lambdas"

##Ejemplo en Scala:##
```scala
// Función anónima para la potencia de un número
val duplicar = (x: Int) => x * 2
duplicar(5)  // Respuesta: 10
```
### 2. ¿En que se diferencia de una función nombrada? Escribe la lógica de ambas formas###

La diferencia principal radica en cómo se definen en código: una función nombrada se declara formalmente como un método del objeto o clase usando def (para definir como funcion), mientras que una función anónima no tiene nombre y se crea al vuelo

#### **Diferencias Clave:**

* **Sintaxis (`def` vs `=>`):** Las nombradas usan `def` y obligatoriamente requieren un nombre identificador. Encambio, las anónimas usan la flecha `=>` y no lo necesitan (aunque se pueden guardar en un `val` para no perderlas)
* **Nivel de evaluación:** El bloque `def` evalúa su contenido y **cada vez que se lo llama**, funcionando como un método tradicional, y una función anónima guardada en un `val` se evalúa como una expresión de **una sola vez** y almacena esa instancia de la función directamente en memoria
* **Uso principal:** Las nombradas se emplean para organizar bloques de código grandes, estructurar clases o definir métodos principales, mientras que las anónimas se usan para resolver problemas rápidas, compactas y para ser pasadas convenientemente como parámetros a métodos de orden superior (como `map` o `filter`)

```scala
// 1. Función nombrada 
def esMayorDeEdad(edad: Int): Boolean = edad >= 18

// 2. Función anónima (Lambda en un valor inmutable)
val esMayorDeEdadLambda = (edad: Int) => edad >= 18

// --- Ejecución ---
esMayorDeEdad(20)        // Resultado: true
esMayorDeEdadLambda(20)  // Resultado: true
```
### 3. ¿Qué significa que las funciones sean ciudadanos de primera clase? 

En programación, decir que las funciones son **ciudadanos de primera clase** (First-Class Citizens) significa que se tratan igual que a cualquier otro tipo de dato básico (como un `Int`, un `String` o un `Boolean`)

Esto implica que una función permite hacer lomismo que una variable común:
1. **Se pueden guardar en variables:** Como cuando se hace el `val duplicar = (x: Int) => x * 2`
2. **Se pueden pasar como argumentos:** Al igual, se puede meter una función dentro de los paréntesis de otra función
3. **Pueden ser devueltas por otra función:** Una función puede generar y entregar otra función como resultado

---

#### **¿Por qué esto permite pasar una lambda a métodos como `map` o `filter`?**

Permite hacer esto porque `map` y `filter` son **funciones de orden superior** (Higher-Order Functions) y estos métodos están programados específicamente para **esperar un una función en lugar de un valor estático**

* **La función `map`:** Espera una función que le diga *cómo transformar* cada elemento, ya que al ser ciudadana de primera clase, se pasa la lambda directamente en los parámetros para que haga el trabajo al momento
* **La función `filter`:** Espera una función que devuelva un `Boolean` para saber *con qué elementos quedarse*

#### **Ejemplo práctico en Scala:**

```scala
val numeros = List(1, 2, 3, 4, 5)

// 1. Pasando una lambda como argumento a map para obtener el doble
val dobles = numeros.map(x => x * 2)
dobles / Resultado: List(2, 4, 6, 8, 10)

// 2. Pasando una lambda a filter para obtener solo los pares
// La función anónima se pasa como si fuera un número o un texto cualquiera
val pares = numeros.filter(x => x % 2 == 0) 
pares //Resultado: List(2, 4)
```
### 4. ¿Qué es un closure? Muestra una lambda que use una variable definida fuera de ella

Un **closure** (clausura) es una función anónima recuerda y puede acceder a variables que fueron definidas fuera de ella, incluso después de que el ámbito donde se crearon haya terminado, osea envuelve variables externas en un bloque de ejecucion propio, permitiendo su uso si la función se ejecuta en otro momento o lugar del codigo

---

#### **Ejemplo práctico en Scala:**
```scala
// Variable definida fuera de la lambda
val factor = 3

// La función anónima captura y usa la variable 'factor' (eso es un CLOSURE)
val multiplicarPorFactor = (numero: Int) => numero * factor

// Ejecución 
val resultado = multiplicarPorFactor(5)
resultado //Resultado: 15 
```
#### **¿Por qué es especial?**
La lambda no recibió el `factor` como un parámetro (su único parámetro es `numero`), sino que fue a buscarlo al entorno de afuera, por eso en Scala, la función mantiene una referencia a ese `factor` externo para poder operar

---

## Parte 3 – Aplicar: ¿en qué problemas sirven las funciones anónimas?

### Lista base de productos (usada en todos los ejemplos)

Usaré una lista de 10 productos con la estructura: `Producto(nombre, categoria, precio, stock)`

**Scala (case class):**
```scala
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
```
**Java (class):**

```java
class Producto {
    String nombre;
    String categoria;
    double precio;
    int stock;
    
    Producto(String nombre, String categoria, double precio, int stock) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }
}

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
```
**Python (class):**

```python
class Producto:
    def __init__(self, nombre, categoria, precio, stock):
        self.nombre = nombre
        self.categoria = categoria
        self.precio = precio
        self.stock = stock

productos = [
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
]
```

**Catalógo de Lista de Problemas**

-**Tipo de Problema:** Selección
-**Pregunta que responde:** ¿Qué elementos cumplen una condición?
-**Situación real:** Una tienda en línea necesita mostrar solo los productos con stock mayor a 50 unidades para evitar mostrar artículos agotados o con poco inventario

### **Solución con funciones anónimas**

**Scala**

```scala
val productosConStockAlto = productos.filter(_.stock > 50).foreach(println)
// Salida:
// Producto(Mouse,Electrónica,25.0,100)
// Producto(Lámpara,Muebles,45.0,60)
// Producto(Audífonos,Electrónica,80.0,75)
// Producto(Cuaderno,Oficina,5.0,200)
// Producto(Bolígrafo,Oficina,2.0,500)
```

**Java**

```java
List<Producto> productosConStockAlto = productos.stream()
    .filter(p -> p.stock > 50)
    .collect(Collectors.toList());
productosConStockAlto.forEach(System.out::println);
// Salida
// Producto(Mouse,Electrónica,25.0,100)
// Producto(Lámpara,Muebles,45.0,60)
// Producto(Audífonos,Electrónica,80.0,75)
// Producto(Cuaderno,Oficina,5.0,200)
// Producto(Bolígrafo,Oficina,2.0,500)
```

**Python**

```python
productos_con_stock_alto = list(filter(lambda p: p.stock > 50, productos))
for p in productos_con_stock_alto:
    print(f"{p.nombre} - Stock: {p.stock}")
# Salida:
# Mouse - Stock: 100
# Lámpara - Stock: 60
# Audífonos - Stock: 75
# Cuaderno - Stock: 200
# Bolígrafo - Stock: 500
```

-**Tipo de Problema:** Transformación
-**Pregunta que responde:** ¿Cómo convierto cada elemento en otro valor?
-**Situación real:** Un sistema de facturación necesita calcular el precio con IVA (16%) para cada producto y mostrar solo los nombres con sus precios finales

### **Solución con funciones anónimas**

**Scala**

```scala
val preciosConIVA = productos.map(p => (p.nombre, p.precio * 1.16)).foreach(println)
// Salida:
// (Laptop,1392.0)
// (Mouse,29.0)
// (Teclado,87.0)
// (Silla,174.0)
// (Escritorio,348.0)
// (Monitor,464.0)
// (Lámpara,52.2)
// (Audífonos,92.8)
// (Cuaderno,5.8)
// (Bolígrafo,2.32)
```

**Java**

```java
productos.stream()
    .map(p -> new AbstractMap.SimpleEntry<>(p.nombre, p.precio * 1.16))
    .forEach(System.out::println);
// Salida
// Salida:
// (Laptop,1392.0)
// (Mouse,29.0)
// (Teclado,87.0)
// (Silla,174.0)
// (Escritorio,348.0)
// (Monitor,464.0)
// (Lámpara,52.2)
// (Audífonos,92.8)
// (Cuaderno,5.8)
// (Bolígrafo,2.32)
```

**Python**

```python
precios_con_iva = list(map(lambda p: (p.nombre, p.precio * 1.16), productos))
for nombre, precio in precios_con_iva:
    print(f"{nombre}: ${precio:.2f}")
# Salida:
# Laptop: $1392.00
# Mouse: $29.00
# Teclado: $87.00
# Silla: $174.00
# Escritorio: $348.00
# Monitor: $464.00
# Lámpara: $52.20
# Audífonos: $92.80
# Cuaderno: $5.80
# Bolígrafo: $2.32
```

-**Tipo de Problema:** Validación
-**Pregunta que responde:** ¿Existe alguno que cumpla?, ¿Todos cumplen?
-**Situación real:** Un sistema de inventario necesita verificar si hay al menos un producto agotado (stock = 0) para generar una alerta, y también verificar si todos los productos tienen precio mayor a 0.

### **Solución con funciones anónimas**

**Scala**

```scala
// ¿Existe algún producto agotado?
val hayAgotados = productos.exists(_.stock == 0)
hayAgotados
// Salida: false

// ¿Todos los productos tienen precio mayor a 0?
val todosPrecioMayor0 = productos.forall(_.precio > 0)
todosPrecioMayor0
// Salida: true
```

**Java**

```java
boolean hayAgotados = productos.stream()
    .anyMatch(p -> p.stock == 0);
System.out.println("¿Hay productos agotados? " + hayAgotados);
// Salida: false

boolean todosPrecioMayor0 = productos.stream()
    .allMatch(p -> p.precio > 0);
System.out.println("¿Todos tienen precio positivo? " + todosPrecioMayor0);
// Salida: true
```

**Python**

```python
hay_agotados = any(p.stock == 0 for p in productos)
print(f"¿Hay productos agotados? {hay_agotados}")
# Salida: False

todos_con_precio_positivo = all(p.precio > 0 for p in productos)
print(f"¿Todos tienen precio positivo? {todos_con_precio_positivo}")
# Salida: True
```

-**Tipo de Problema:** Extremos (maxBy / minBy)
-**Pregunta que responde:** ¿Cuál es el mejor o el peor según un criterio?
-**Situación real:** Un sistema de inventario necesita identificar el producto más caro para destacarlo en la página principal, y el producto con menos stock para generar una alerta de reabastecimiento urgente.

### **Solución con funciones anónimas**

**Scala**
// Producto más caro
val productoMasCaro = productos.maxBy(_.precio)
productoMasCaro.nombre
productoMasCaro.precio
//Salida:
//Laptop
//1200.0

//Producto con menos stock
val productoMenosStock = productos.minBy(_.stock)
productoMenosStock.nombre
productoMenosStock.stock
//Salida:
//Laptop
//15
```scala

```

**Java**

```java
// Producto más caro
Producto productoMasCaro = productos.stream()
    .max(Comparator.comparingDouble(p -> p.precio))
    .orElse(null);
System.out.println("Producto más caro: " + productoMasCaro.nombre + " ($" + productoMasCaro.precio + ")");
// Salida: Producto más caro: Laptop ($1200.0)

// Producto con menos stock
Producto productoMenosStock = productos.stream()
    .min(Comparator.comparingInt(p -> p.stock))
    .orElse(null);
System.out.println("Producto con menos stock: " + productoMenosStock.nombre + " (" + productoMenosStock.stock + " unidades)");
// Salida: Producto con menos stock: Laptop (15 unidades)
```

**Python**

```python
# Producto más caro
producto_mas_caro = max(productos, key=lambda p: p.precio)
print(f"Producto más caro: {producto_mas_caro.nombre} (${producto_mas_caro.precio})")
# Salida: Producto más caro: Laptop ($1200.0)

# Producto con menos stock
producto_menos_stock = min(productos, key=lambda p: p.stock)
print(f"Producto con menos stock: {producto_menos_stock.nombre} ({producto_menos_stock.stock} unidades)")
# Salida: Producto con menos stock: Laptop (15 unidades)
```





