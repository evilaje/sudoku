package sudoku.logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Genera tableros aleatorios y conserva una única solución. */
public final class GeneradorSudoku {
    private final Random aleatorio;
    private final ResolvedorSudoku resolvedor = new ResolvedorSudoku();

    public GeneradorSudoku() { this(new Random()); }
    public GeneradorSudoku(Random aleatorio) {
        if (aleatorio == null) throw new IllegalArgumentException("Random requerido");
        this.aleatorio = aleatorio;
    }

    public Tablero generar() { return generar(36); }

    /**
     * pistasObjetivo es un objetivo, no una garantía: puede conservar más pistas
     * cuando quitar otra produciría múltiples soluciones. Rango: 17 a 81.
     */
    public Tablero generar(int pistasObjetivo) {
        if (pistasObjetivo < 17 || pistasObjetivo > 81) {
            throw new IllegalArgumentException("Solicite entre 17 y 81 pistas");
        }
        List<Integer> filas = indicesMezclados();
        List<Integer> columnas = indicesMezclados();
        List<Integer> numeros = new ArrayList<Integer>();
        for (int n = 1; n <= 9; n++) numeros.add(n);
        Collections.shuffle(numeros, aleatorio);
        int[][] matriz = new int[9][9];
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                int fila = filas.get(f), columna = columnas.get(c);
                matriz[f][c] = numeros.get((fila * 3 + fila / 3 + columna) % 9);
            }
        }
        List<Integer> posiciones = new ArrayList<Integer>();
        for (int i = 0; i < 81; i++) posiciones.add(i);
        Collections.shuffle(posiciones, aleatorio);
        int pistas = 81;
        for (int posicion : posiciones) {
            if (pistas <= pistasObjetivo) break;
            int f = posicion / 9, c = posicion % 9, anterior = matriz[f][c];
            matriz[f][c] = 0;
            if (resolvedor.contarSoluciones(matriz, 2) == 1) pistas--;
            else matriz[f][c] = anterior;
        }
        return new Tablero(matriz);
    }

    private List<Integer> indicesMezclados() {
        List<Integer> grupos = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) grupos.add(i);
        Collections.shuffle(grupos, aleatorio);
        List<Integer> resultado = new ArrayList<Integer>();
        for (int grupo : grupos) {
            List<Integer> internos = new ArrayList<Integer>();
            for (int i = 0; i < 3; i++) internos.add(grupo * 3 + i);
            Collections.shuffle(internos, aleatorio);
            resultado.addAll(internos);
        }
        return resultado;
    }
}
