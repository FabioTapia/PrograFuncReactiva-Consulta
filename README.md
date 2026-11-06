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

---

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

Código completo de la clase y la lista en cada lenguaje:

- **Scala (case class):** [`Scala/Productos.scala` (líneas 1-14)](Scala/Productos.scala#L1-L14)
- **Java (class):** [`Java/Productos.java` (líneas 7-35)](Java/Productos.java#L7-L35)
- **Python (class):** [`Python/Productos.py` (líneas 1-23)](Python/Productos.py#L1-L23)

---

### Catalógo de Lista de Problemas

### Tipo 1: Selección (filter)

-**Pregunta que responde:** ¿Qué elementos cumplen una condición?

-**Situación real:** Una tienda en línea necesita mostrar solo los productos con stock mayor a 50 unidades para evitar mostrar artículos agotados o con poco inventario

### Solución con funciones anónimas

- **Scala:** [`Scala/Productos.scala` (líneas 16-18)](Scala/Productos.scala#L16-L18)
- **Java:** [`Java/Productos.java` (líneas 37-41)](Java/Productos.java#L37-L41)
- **Python:** [`Python/Productos.py` (líneas 26-30)](Python/Productos.py#L26-L30)

---

### Tipo 2: Transformación (map)

-**Pregunta que responde:** ¿Cómo convierto cada elemento en otro valor?

-**Situación real:** Un sistema de facturación necesita calcular el precio con IVA (16%) para cada producto y mostrar solo los nombres con sus precios finales

### **Solución con funciones anónimas**

- **Scala:** [`Scala/Productos.scala` (líneas 20-22)](Scala/Productos.scala#L20-L22)
- **Java:** [`Java/Productos.java` (líneas 43-46)](Java/Productos.java#L43-L46)
- **Python:** [`Python/Productos.py` (líneas 33-37)](Python/Productos.py#L33-L37)

---

### Tipo 3: Validación (exists, forall)

-**Pregunta que responde:** ¿Existe alguno que cumpla?, ¿Todos cumplen?

-**Situación real:** Un sistema de inventario necesita verificar si hay al menos un producto agotado (stock = 0) para generar una alerta, y también verificar si todos los productos tienen precio mayor a 0.

### **Solución con funciones anónimas**

- **Scala:** [`Scala/Productos.scala` (líneas 24-29)](Scala/Productos.scala#L24-L29)
- **Java:** [`Java/Productos.java` (líneas 48-55)](Java/Productos.java#L48-L55)
- **Python:** [`Python/Productos.py` (líneas 40-46)](Python/Productos.py#L40-L46)

---

### Tipo 4: Extremos (maxBy / minBy)

-**Pregunta que responde:** ¿Cuál es el mejor o el peor según un criterio?

-**Situación real:** Un sistema de inventario necesita identificar el producto más caro para destacarlo en la página principal, y el producto con menos stock para generar una alerta de reabastecimiento urgente.

### **Solución con funciones anónimas**

- **Scala:** [`Scala/Productos.scala` (líneas 31-40)](Scala/Productos.scala#L31-L40)
- **Java:** [`Java/Productos.java` (líneas 57-66)](Java/Productos.java#L57-L66)
- **Python:** [`Python/Productos.py` (líneas 49-55)](Python/Productos.py#L49-L55)

---

## Parte 4 – Ampliar: cuatro operaciones nuevas

A continuación, presento 4 operaciones distintas a las vistas en clase, con su ficha técnica y un ejemplo usando la misma lista de `productos`

### 1. Operación: `groupBy` (Agrupar)

1. **Qué hace:** Agrupa los elementos de una lista en un diccionario donde la clave es el resultado de la función y el valor es la lista de elementos que coinciden
2. **Función anónima que recibe:** Recibe una función que devuelve el criterio de agrupación (en este caso, un `String` con la categoría)
3. **Ejemplo en Scala:** [`Operaciones/NuevasOperaciones.scala` (líneas 16-20)](Operaciones/NuevasOperaciones.scala#L16-L20)
4. **Problema que resuelve:** Una tienda en línea necesita mostrar los productos organizados por secciones (categorías) en su menú . Se parece al tipo de problema de Transformación, pero en lugar de cambiar el elemento, cambia la estructura de la colección.

---

### 2. Operación: `sortBy` (Ordenar)

- **Qué hace:** Ordena los elementos de la lista de menor a mayor según el valor que devuelve la función.
- **Función anónima que recibe:** Recibe una función que extrae el valor por el cual se quiere ordenar (debe ser un tipo ordenable, como `Int` o `Double`)
- **Ejemplo en Scala:** [`Operaciones/NuevasOperaciones.scala` (líneas 22-24)](Operaciones/NuevasOperaciones.scala#L22-L24)

4. **Problema que resuelve:** Un usuario quiere ver los productos ordenados del más barato al más caro para encontrar opciones dentro de su presupuesto (es un tipo de problema de Organización/Visualización)

---

### 3. Operación: `find` (Buscar)

- **Qué hace:** Busca y devuelve el primer elemento que cumple la condición
- **Función anónima que recibe:** Recibe una función que devuelve un Boolean (la condición de búsqueda)
- **Ejemplo en Scala:** [`Operaciones/NuevasOperaciones.scala` (líneas 26-30)](Operaciones/NuevasOperaciones.scala#L26-L30)

4. **Problema que resuelve:** Un sistema de inventario necesita buscar un producto específico por su ID o nombre exacto para actualizar su stock. Se parece al tipo de Selección (filter), pero en lugar de devolver una lista, devuelve un solo elemento (o nada).

---

### 4. Operación: `partition` (dividir)

- **Qué hace:** Divide la lista en dos listas separadas (una tupla de dos listas): la primera con los elementos que cumplen la condición, y la segunda con los que no
- **Función anónima que recibe:** Recibe una función que devuelve un Boolean.
- **Ejemplo en Scala:** [`Operaciones/NuevasOperaciones.scala` (líneas 32-35)](Operaciones/NuevasOperaciones.scala#L32-L35)

4. **Problema que resuelve:** Un almacén necesita separar los productos en dos lotes para aplicar diferentes tarifas de envío: una para productos con mucho stock (envío estándar) y otra para productos con poco stock (envío prioritario). Es una evolución del tipo de problema de Selección

---

### 4.3 Combinar Operaciones

- **Pregunta:** ¿Qué productos tienen stock mayor a 50 unidades, ordenados del más barato al más caro?

**Cadena de operaciones:**
  
Código de la combinación: [`Operaciones/NuevasOperaciones.scala` (líneas 37-47)](Operaciones/NuevasOperaciones.scala#L37-L47)

---

## Parte 5 – Comparar y concluir

### 5.1 Scala, Java y Python frente a frente

| Criterio | Scala | Java | Python |
|---|---|---|---|
| **Sintaxis de la lambda** | `(p => p.precio > 100)` — muy corta y directa | `(p -> p.getPrecio() > 100)` — usa `->` y requiere getters | `lambda p: p.precio > 100` — usa la palabra `lambda` |
| **Cómo se declara el tipo** | Con `case class` (es inmutable) | Con una `class` tradicional, su constructor y getters | Con una `class` y `__init__` |
| **Operaciones con listas disponibles** | Nativas en `List`: `map`, `filter`, `foldLeft`, `groupBy`, etc. | A través de `stream()` desde Java 8: `.stream().map(...).filter(...)` | Funciones incluidas `map()`, `filter()`, y `functools.reduce` |
| **Restricción del lenguaje** | Muy flexible, variabels inmutables | Confuso, requiere `Collectors` y `Optional` para algunos casos | El tipado es dinámico, no exige declarar tipos pero puede ser menos seguro |
| **Legibilidad (mi opinión)** |  La más limpia y facil de entender | Codigo más código repetitivo | Buena pero la sintaxis `lambda` es menos natural que en Scala |

#### ¿En qué lenguaje fue más natural resolver los problemas?

Para mí, Scala fue el más natural porque las operaciones como `map`, `filter`, `maxBy` y `groupBy` están integradas directamente en el lenguaje y la sintaxis de las lambdas (`p => ...`) es muy corta, en Java hay que convertir la lista a `stream()` primero y usar `Collectors` al final, lo que es más confuso en comparación con el anterior y, en Python se puede etender mas qeu Java, pero la palabra `lambda` se siente más limitada ya que solo permite una expresión

#### ¿En qué tipo de problema encontraste más diferencias entre lenguajes y por qué?

El tipo de problema donde más diferencias noté fue en **Acumulación / Extremos** Ya que en Scala, `maxBy` y `foldLeft` son métodos directos de la lista, pero en Java, hay que usar `stream().max(Comparator.comparingDouble(...))` que es más largo y confuso , y en Python, `max()` con el parámetro `key=lambda` es elegante pero funciona distinto internamente, demostrando que Scala está diseñado desde el inicio para programación funcional, mientras que Java la adoptó después y Python la usa como complemento

---

### 5.2 Conclusión

Las funciones anónimas son fundamentales en la programación funcional porque permiten expresar qué queremos hacer con los datos sin preocuparnos por el cómo ya que en lugar de escribir bucles `for` con variables mutables, pasamos una funci´ñon anónima a operaciones como `map`, `filter` o `foldLeft`, y el lenguaje se encarga de resolver el ejercicio y esto produce código más corto, legible y menos propenso a errores, ya que lso datosd no se pueden modificar y es mas seguro

Un ejemplo claro de su importancia en el mundo real es **Apache Spark**, ya que spark procesa petabytes de datos distribuidos en clusters, y cada transformación (`map`, `filter`, `reduceByKey`) se define con funciones anónimas. Y gracias a que estas funciones que son **ciudadanas de primera clase** y no tienen efectos secundarios, Spark puede serializarlas, enviarlas a otros nodos del cluster y ejecutarlas en paralelo sin riesgo de corrupción de datos. Por eso sin funciones anónimas, procesar big data de forma distribuida sería muchísimo más complejo y propenso a errores

---













