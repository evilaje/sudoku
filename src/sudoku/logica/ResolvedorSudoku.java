package sudoku.logica;

/** Algoritmo de backtracking independiente de la interfaz. */
public final class ResolvedorSudoku {
    /** No modifica el tablero si no encuentra solución. */
    public boolean resolver(Tablero tablero) {
        int[][] matriz = tablero.aMatriz();
        if (!buscar(matriz)) return false;
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                if (!tablero.esFija(f, c)) tablero.colocar(f, c, matriz[f][c]);
            }
        }
        return true;
    }

    public int contarSoluciones(int[][] matriz, int limite) {
        Tablero.validarMatriz(matriz);
        if (limite < 1) throw new IllegalArgumentException("El límite debe ser positivo");
        // Valida también las reglas y copia los datos del llamador.
        return contar(new Tablero(matriz).aMatriz(), limite);
    }

    private boolean buscar(int[][] matriz) {
        int[] posicion = siguienteVacia(matriz);
        if (posicion == null) return true;
        int f = posicion[0], c = posicion[1];
        for (int valor = 1; valor <= 9; valor++) {
            if (permitido(matriz, f, c, valor)) {
                matriz[f][c] = valor;
                if (buscar(matriz)) return true;
                matriz[f][c] = 0;
            }
        }
        return false;
    }

    private int contar(int[][] matriz, int limite) {
        int[] posicion = siguienteVacia(matriz);
        if (posicion == null) return 1;
        int f = posicion[0], c = posicion[1], total = 0;
        for (int valor = 1; valor <= 9 && total < limite; valor++) {
            if (permitido(matriz, f, c, valor)) {
                matriz[f][c] = valor;
                total += contar(matriz, limite - total);
                matriz[f][c] = 0;
            }
        }
        return total;
    }

    private int[] siguienteVacia(int[][] matriz) {
        // Selecciona la celda con menos candidatos para reducir la búsqueda.
        int[] mejor = null;
        int minimo = 10;
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                if (matriz[f][c] != 0) continue;
                int candidatos = 0;
                for (int v = 1; v <= 9; v++) if (permitido(matriz, f, c, v)) candidatos++;
                if (candidatos < minimo) {
                    minimo = candidatos;
                    mejor = new int[]{f, c};
                    if (minimo == 0) return mejor;
                }
            }
        }
        return mejor;
    }

    private boolean permitido(int[][] matriz, int fila, int columna, int valor) {
        for (int i = 0; i < 9; i++) {
            if (matriz[fila][i] == valor || matriz[i][columna] == valor) return false;
        }
        for (int f = fila / 3 * 3; f < fila / 3 * 3 + 3; f++) {
            for (int c = columna / 3 * 3; c < columna / 3 * 3 + 3; c++) {
                if (matriz[f][c] == valor) return false;
            }
        }
        return true;
    }
}
