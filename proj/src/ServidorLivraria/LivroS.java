package ServidorLivraria;

import ServidorLivraria.Livro;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LivroS implements Livro {

    private final int isbn;
    private final String title;
    private final float preco;
    public Lock livrolock = new ReentrantLock();

    public LivroS(int isbn, String title, float preco) {
        this.isbn = isbn;
        this.title = title;
        this.preco = preco;
    }

    public int getIsbn() { return isbn; }

    public String getTitle() {
        return title;
    }

    public float getPreco() {return preco;}
}
