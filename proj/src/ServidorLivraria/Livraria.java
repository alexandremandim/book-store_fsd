package ServidorLivraria;
import java.util.List;

public interface Livraria {

    public Carrinho novoCarrinho() throws Exception;
    public Livro procurarLivro(String titulo) throws Exception;
    public List<String> getHistorico(int nib) throws Exception;
}
