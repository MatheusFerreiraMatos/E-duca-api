package educa.api.utils;

import educa.api.domain.Conteudo;

import java.util.ArrayList;
import java.util.List;

public class ListObj<T> {

    private T[] vetor;
    private int nroElem;

    public ListObj(int tam) {
        this.vetor = (T[]) new Object[tam];
        this.nroElem = 0;
    }

    public void add(T elemento) {
        if (nroElem != vetor.length) {
            for (int i = 0; i < vetor.length; i++) {
                vetor[nroElem] = elemento;
                nroElem++;
                break;
            }
        } else {
            System.out.println("Lista Cheia!");
        }
    }

    public void adicionaIndice(int indice, T elemento) {
        vetor[indice] = elemento;
    }

    public List<T> all() {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < nroElem; i++) {
            list.add(vetor[i]);
        }
        return list;
    }



    public int get(T elemento) {
        for (int i = 0; i < nroElem; i++) {
            if (elemento.equals(vetor[i])) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeI(int index) {
        if (!(index < 0 || index > nroElem)) {
            for (int i = index; i < nroElem; i++) {
                vetor[i] = vetor[i + 1];
            }
            nroElem--;
            return true;
        }
        return false;
    }

    public boolean removeE(T elemento) {
        return removeI(get(elemento));
    }

    public Integer getTamanho() {
        return nroElem;
    }

    public T getElemento(int indice) {
        if (!(indice <0 || indice >= nroElem)) {
            return vetor[indice];
        }
        return null;
    }

    public void limpa() {
        for (int i = 0; i < nroElem; i++) {
            vetor[i] = null;
        }
        nroElem = 0;
    }

    public void adiciona(T elemento) {
        if (nroElem >= vetor.length) {
            throw new IllegalStateException();
        }
        else {
            vetor[nroElem++] = elemento;
        }
    }

    public void exibe() {
        if (nroElem == 0) {
            System.out.println("\nA lista está vazia.");
        }
        else {
            System.out.println("\nElementos da lista:");
            for (int i = 0; i < nroElem; i++) {
                System.out.println(vetor[i]);
            }
        }
    }



}