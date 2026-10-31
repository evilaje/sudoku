package sudoku;

import sudoku.logica.GeneradorSudoku;
import sudoku.logica.ResolvedorSudoku;
import sudoku.logica.Tablero;

/** Ejemplo mínimo de uso; la lógica no depende de esta clase. */
public final class Main {
    public static void main(String[] args) {
        Tablero tablero = new GeneradorSudoku().generar();
        System.out.println("Sudoku generado:");
        System.out.println(tablero);
        if (new ResolvedorSudoku().resolver(tablero)) {
            System.out.println("Solución por backtracking:");
            System.out.println(tablero);
            System.out.println("Resuelto: " + tablero.estaResuelto());
        }
    }
}
