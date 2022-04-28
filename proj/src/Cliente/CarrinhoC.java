package Cliente;


import ServidorLivraria.Livro;
import ServidorLivraria.Carrinho;
import ServidorLivraria.Mensagens.CartAddRep;
import ServidorLivraria.Mensagens.CartAddReq;
import ServidorLivraria.Mensagens.StoreBuyRep;
import ServidorLivraria.Mensagens.StoreBuyReq;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.transport.Connection;

public class CarrinhoC implements Carrinho {
    private final SingleThreadContext tc;
    private final Connection c;
    private final int cartid;


    public CarrinhoC(SingleThreadContext tc, Connection c, int cartid) {
        this.tc = tc;
        this.c = c;
        this.cartid = cartid;
    }

    public void adicionaLivro(Livro b) throws Exception {
        int id = ((LivroC)b).getBookid();
        CartAddRep r = (CartAddRep) tc.execute(() ->
                c.sendAndReceive(new CartAddReq(cartid,id)) /* TODO: Posso fazer um getBookId? ->
                Sim, deviamos ter 1 classe base em q todos poderiam ver os ids uns dos outros */
        ).join().get();
    }

    public int getCartid(){
        return this.cartid;
    }

    @Override
    public void finalizarCompra(int nib) throws Exception {
        StoreBuyRep r = (StoreBuyRep) tc.execute(() ->
            c.sendAndReceive(new StoreBuyReq(cartid, nib))
    ).join().get();}

}