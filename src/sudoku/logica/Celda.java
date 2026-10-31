package sudoku.logica;

/** Una celda: cero representa un espacio vacío. */
public final class Celda {
    private int valor;
    private final boolean fija;

    public Celda(int valor, boolean fija) {
        if (valor < 0 || valor > 9 || (fija && valor == 0)) {
            throw new IllegalArgumentException("Valor de celda inválido");
        }
        this.valor = valor;
        this.fija = fija;
    }

    public int getValor() { return valor; }
    public boolean esFija() { return fija; }
    public boolean estaVacia() { return valor == 0; }

    public void setValor(int valor) {
        if (fija) throw new IllegalStateException("No se puede modificar una pista");
        if (valor < 0 || valor > 9) throw new IllegalArgumentException("Use valores de 0 a 9");
        this.valor = valor;
    }
}
