package sudoku.logica;

/** Tablero 9x9. Los índices de fila y columna van de 0 a 8. */
public final class Tablero {
    public static final int TAMANIO = 9;
    private final Celda[][] celdas = new Celda[TAMANIO][TAMANIO];

    public Tablero(int[][] pistas) {
        validarMatriz(pistas);
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                celdas[f][c] = new Celda(pistas[f][c], pistas[f][c] != 0);
            }
        }
        if (!esValido()) throw new IllegalArgumentException("Las pistas contienen duplicados");
    }

    public static void validarMatriz(int[][] matriz) {
        if (matriz == null || matriz.length != TAMANIO) {
            throw new IllegalArgumentException("Se requiere una matriz 9x9");
        }
        for (int[] fila : matriz) {
            if (fila == null || fila.length != TAMANIO) {
                throw new IllegalArgumentException("Se requiere una matriz 9x9");
            }
            for (int valor : fila) {
                if (valor < 0 || valor > 9) throw new IllegalArgumentException("Use valores de 0 a 9");
            }
        }
    }

    private void validarPosicion(int fila, int columna) {
        if (fila < 0 || fila >= TAMANIO || columna < 0 || columna >= TAMANIO) {
            throw new IndexOutOfBoundsException("Fila y columna deben estar entre 0 y 8");
        }
    }

    public int getValor(int fila, int columna) {
        validarPosicion(fila, columna);
        return celdas[fila][columna].getValor();
    }

    public boolean esFija(int fila, int columna) {
        validarPosicion(fila, columna);
        return celdas[fila][columna].esFija();
    }

    /** Comprueba el movimiento ignorando el valor actual de la celda destino. */
    public boolean puedeColocar(int fila, int columna, int valor) {
        validarPosicion(fila, columna);
        if (valor < 1 || valor > 9) return false;
        for (int i = 0; i < TAMANIO; i++) {
            if (i != columna && getValor(fila, i) == valor) return false;
            if (i != fila && getValor(i, columna) == valor) return false;
        }
        int inicioFila = fila / 3 * 3;
        int inicioColumna = columna / 3 * 3;
        for (int f = inicioFila; f < inicioFila + 3; f++) {
            for (int c = inicioColumna; c < inicioColumna + 3; c++) {
                if ((f != fila || c != columna) && getValor(f, c) == valor) return false;
            }
        }
        return true;
    }

    /** Rechaza movimientos que violan las reglas; cero borra una celda editable. */
    public void colocar(int fila, int columna, int valor) {
        validarPosicion(fila, columna);
        if (esFija(fila, columna)) throw new IllegalStateException("No se puede modificar una pista");
        if (valor < 0 || valor > 9) throw new IllegalArgumentException("Use valores de 0 a 9");
        if (valor != 0 && !puedeColocar(fila, columna, valor)) {
            throw new IllegalArgumentException("Número repetido en fila, columna o bloque");
        }
        celdas[fila][columna].setValor(valor);
    }

    public boolean esValido() {
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                int valor = getValor(f, c);
                if (valor != 0 && !puedeColocar(f, c, valor)) return false;
            }
        }
        return true;
    }

    public boolean estaResuelto() {
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                if (getValor(f, c) == 0) return false;
            }
        }
        return esValido();
    }

    /** Elimina los movimientos y conserva las pistas originales. */
    public void reiniciar() {
        for (Celda[] fila : celdas) {
            for (Celda celda : fila) if (!celda.esFija()) celda.setValor(0);
        }
    }

    /** Devuelve una copia, para que nadie pueda modificar el tablero indirectamente. */
    public int[][] aMatriz() {
        int[][] matriz = new int[TAMANIO][TAMANIO];
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) matriz[f][c] = getValor(f, c);
        }
        return matriz;
    }

    @Override public String toString() {
        StringBuilder texto = new StringBuilder();
        for (int f = 0; f < TAMANIO; f++) {
            if (f % 3 == 0) texto.append("+-------+-------+-------+\n");
            for (int c = 0; c < TAMANIO; c++) {
                if (c % 3 == 0) texto.append("| ");
                int valor = getValor(f, c);
                texto.append(valor == 0 ? "." : Integer.toString(valor)).append(' ');
            }
            texto.append("|\n");
        }
        return texto.append("+-------+-------+-------+\n").toString();
    }
}
