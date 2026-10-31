# sudokuTp — Java con Ant para NetBeans

> **Estado: placeholder.** La estructura y el código actuales son solo un esqueleto inicial de referencia. No representan la arquitectura ni la entrega final del TP; se espera reemplazar o reorganizar esta base.

Proyecto dedicado únicamente a las clases lógicas, conforme a la aclaración del pedido. No incluye Swing, servidor, sockets ni puntuaciones. Es una base del componente de lógica del TP, no la entrega completa del sistema distribuido.

## Abrir en NetBeans

1. Descomprimir el ZIP.
2. Ir a **Archivo > Abrir proyecto** y seleccionar la carpeta `sudokuTp` (contiene `nbproject` y `build.xml`).
3. Seleccionar un JDK instalado si NetBeans lo solicita. Código compatible con Java 8 o posterior.
4. **Ejecutar proyecto (F6)** muestra un tablero generado y su solución en consola.
5. **Limpiar y construir** genera `dist/SudokuLogica.jar`.

El proyecto usa un archivo Ant explícito y no requiere dependencias externas. Ejecuta el proyecto completo; no incluye objetivos especiales para depurar o ejecutar un archivo individual.

## Clases principales

| Clase | Responsabilidad |
| --- | --- |
| `Celda` | Valor de 0 a 9 y condición de pista inmutable. |
| `Tablero` | Grilla 9x9, reglas, movimientos, copia de datos y reinicio. |
| `GeneradorSudoku` | Tableros aleatorios con una única solución. |
| `ResolvedorSudoku` | Backtracking y conteo limitado de soluciones. |
| `Main` | Demostración por consola. |

## Uso de la lógica

```java
Tablero tablero = new GeneradorSudoku().generar(36);
// Índices de 0 a 8. Las pistas no pueden modificarse.
if (!tablero.esFija(0, 0) && tablero.puedeColocar(0, 0, 7)) {
    tablero.colocar(0, 0, 7);
}
boolean terminado = tablero.estaResuelto();
boolean pudoResolver = new ResolvedorSudoku().resolver(tablero);
tablero.reiniciar(); // conserva las pistas originales
```

Para borrar, usar `colocar(fila, columna, 0)`. Para iniciar con pistas propias, usar `new Tablero(matriz9x9)`; cero representa una celda vacía. El constructor comprueba dimensiones, rango y duplicados, pero no garantiza que una matriz arbitraria tenga solución: consultar el resolvedor.

`generar(36)` intenta dejar 36 pistas; puede conservar más para asegurar solución única. La cantidad de pistas no mide por sí sola la dificultad. El algoritmo crea una solución por permutaciones válidas de filas, columnas y números, y retira pistas comprobando con backtracking que siga habiendo una sola solución.

`puedeColocar` comprueba las reglas numéricas; `colocar` además comprueba que la celda sea editable. `esValido` admite espacios vacíos; `estaResuelto` exige tablero completo y válido. El resolvedor respeta los números actuales: si un movimiento válido localmente deja el tablero sin solución, devuelve `false` y no cambia los datos. Para resolver desde las pistas, primero llamar a `reiniciar`.

## Construcción y pruebas

Con un JDK y Ant disponibles:

```text
ant clean jar
ant run
ant test
java -jar dist/SudokuLogica.jar
```

Las pruebas no usan JUnit: verifican restricciones de filas, columnas y bloques, pistas inmutables, entradas inválidas, copias defensivas, reinicio, doce tableros de solución única y un caso sin solución.

Las clases no están diseñadas para compartir una misma instancia entre hilos. Cada partida debe tener su propio tablero. La penalización por resolver automáticamente y la puntuación corresponden a un futuro controlador de partidas, no al tablero.
