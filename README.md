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





















