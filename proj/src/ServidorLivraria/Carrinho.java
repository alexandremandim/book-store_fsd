package ServidorLivraria;

public interface Carrinho {

    public void adicionaLivro(Livro livro) throws Exception;
    public int getCartid();
    public void finalizarCompra(int nib) throws Exception ;

}

