package Cliente;

import ServidorLivraria.Livro;
import ServidorLivraria.Mensagens.*;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.transport.Connection;

public class LivroC implements Livro {
    private final SingleThreadContext tc;
    private final Connection c;
    private final int bookid;

    public LivroC(SingleThreadContext tc, Connection c, int bookid) {
        this.tc = tc;
        this.c = c;
        this.bookid = bookid;
    }

    public int getBookid() {
        return bookid;
    }

    @Override
    public String getTitle() throws Exception{
        GetBookTitleRep r = (GetBookTitleRep) tc.execute(() ->
                c.sendAndReceive(new GetBookTitleReq(bookid))
        ).join().get();

        return r.title;
    }

    @Override
    public int getIsbn() throws Exception{
        GetBookIsbnRep r = (GetBookIsbnRep) tc.execute(() ->
                c.sendAndReceive(new GetBookIsbnReq(bookid))
        ).join().get();

        return r.isbn;
    }

    @Override
    public float getPreco() throws Exception{
        GetBookPrecoRep r = (GetBookPrecoRep) tc.execute(() ->
                c.sendAndReceive(new GetBookPrecoReq(bookid))
        ).join().get();

        return r.preco;
    }
}
