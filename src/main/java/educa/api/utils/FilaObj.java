package educa.api.utils;

public class FilaObj<T> {

    private int tamanho;
    private T[] fila;


    public FilaObj(int capacidade) {
        tamanho = 0;
        fila = (T[]) new Object[capacidade];

    }

    public Boolean isEmpty() {
        return tamanho == 0;
    }

    public Boolean isFull() {
        return tamanho == fila.length;
    }

    public void insert(T info) {


        if (isFull()) {
            throw  new  IllegalStateException("Fila Cheia!");
        }

        else {

            fila[tamanho++] = info;
        }
    }

    public T peek() {
        return fila[0];
    }


    public T pool() {
        T primeiro = peek();
        if (!isEmpty()) {
            for (int i = 0; i < tamanho-1; i++) {
                fila[i] = fila[i+1];
            }
            fila[tamanho-1] = null;
            tamanho--;
        }
        return primeiro;
    }

    public void exibe() {
        if (isEmpty()) {
            System.out.println("Fila vazia!\n");
        }
        else {
            System.out.println("Elementos da fila: ");
            for (int i = 0; i < tamanho; i++) {
                System.out.println(fila[i]);
            }
        }
    }


    public T[] getFila() {
        return fila;
    }

    public int getTamanho() {
        return tamanho;
    }
}