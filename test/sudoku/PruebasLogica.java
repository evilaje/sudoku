package sudoku;

import java.util.Random;
import sudoku.logica.*;

/** Pruebas sin bibliotecas externas. Ejecutar con ant test. */
public final class PruebasLogica {
    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
    private static void rechaza(Runnable accion) {
        try { accion.run(); }
        catch (IllegalArgumentException | IllegalStateException esperado) { return; }
        throw new AssertionError("La operación inválida no fue rechazada");
    }
    public static void main(String[] args) {
        Celda celda = new Celda(0, false);
        celda.setValor(9);
        verificar(celda.getValor() == 9, "Celda editable");
        rechaza(() -> new Celda(0, true));
        rechaza(() -> new Celda(4, true).setValor(5));
        rechaza(() -> new Tablero(new int[8][9]));
        Tablero tablero = new Tablero(new int[9][9]);
        tablero.colocar(0, 0, 5);
        rechaza(() -> tablero.colocar(0, 8, 5));
        rechaza(() -> tablero.colocar(8, 0, 5));
        rechaza(() -> tablero.colocar(1, 1, 5));
        rechaza(() -> tablero.colocar(1, 1, 10));
        tablero.colocar(0, 0, 0);
        verificar(!tablero.estaResuelto(), "Vacío no está resuelto");

        ResolvedorSudoku resolvedor = new ResolvedorSudoku();
        verificar(resolvedor.contarSoluciones(new int[9][9], 2) == 2, "Límite de soluciones");
        for (int semilla = 0; semilla < 12; semilla++) {
            Tablero juego = new GeneradorSudoku(new Random(semilla)).generar(36);
            int[][] pistas = juego.aMatriz();
            verificar(juego.esValido(), "Generación válida");
            verificar(!juego.estaResuelto(), "Hay celdas vacías");
            verificar(resolvedor.contarSoluciones(pistas, 2) == 1, "Solución única");
            verificar(resolvedor.resolver(juego) && juego.estaResuelto(), "Resolver");
            for (int f = 0; f < 9; f++) {
                for (int c = 0; c < 9; c++) {
                    if (pistas[f][c] != 0) {
                        verificar(juego.getValor(f, c) == pistas[f][c], "Conservar pistas");
                    }
                }
            }
            int[][] copia = juego.aMatriz();
            copia[0][0] = 0;
            verificar(juego.getValor(0, 0) != 0, "Copia defensiva");
            juego.reiniciar();
            verificar(java.util.Arrays.deepEquals(pistas, juego.aMatriz()), "Reiniciar");
        }
        // Reglas locales válidas, pero la celda (0,8) no tiene candidatos.
        int[][] imposible = new int[9][9];
        for (int c = 0; c < 8; c++) imposible[0][c] = c + 1;
        imposible[1][8] = 9;
        Tablero sinSolucion = new Tablero(imposible);
        verificar(!resolvedor.resolver(sinSolucion), "Detectar tablero imposible");
        verificar(java.util.Arrays.deepEquals(imposible, sinSolucion.aMatriz()), "No modificar si falla");
        int[][] duplicados = new int[9][9];
        duplicados[0][0] = 1;
        duplicados[0][1] = 1;
        rechaza(() -> new Tablero(duplicados));
        System.out.println("OK: validación, pistas, copias, generación única, resolución y reinicio.");
    }
}
