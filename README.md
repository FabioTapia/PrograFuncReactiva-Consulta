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








































