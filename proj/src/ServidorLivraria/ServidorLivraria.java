package ServidorLivraria;

import ServidorBanco.ContaBancaria;
import ServidorBanco.Mensanges.GetHistoricoContaBRep;
import ServidorBanco.Mensanges.GetHistoricoContaBReq;
import ServidorLivraria.Mensagens.*;
import io.atomix.catalyst.concurrent.Futures;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServidorLivraria {
    public static void main(String[] args) throws Exception {
        Transport t = new NettyTransport();
        ThreadContext tc = new SingleThreadContext("srv-%d", new Serializer());
        AtomicInteger id = new AtomicInteger(0);
        final Lock lockMap = new ReentrantLock();

        Map<Integer, Object> objs = new ConcurrentHashMap<>(); /* Objetos do servidor - Livraria, Books, Carts.
         Sempre que enviamos um objeto ao cliente devemos inserir aqui o ID, para saber q objeto é qdo o cliente voltar a referir-se a esse objeto*/
        LivrariaS s = new LivrariaS();
        BancoC banco = new BancoC();

        objs.putIfAbsent(id.incrementAndGet(), s); /* add store aos objs (id = 1) */
        objs.putIfAbsent(id.incrementAndGet(), banco);

        /* Registar as mensagens */
        registarMensagens(tc);

        tc.execute(() -> {
            t.server().listen(new Address(":10000"), (Connection c) -> {

                /* Livraria */

                c.handler(StoreSearchReq.class, (m) -> {

                    LivrariaS store = (LivrariaS) objs.get(m.storeid); /* Get id da store */
                    if(store == null){
                        return Futures.exceptionalFuture(new Exception("Nao existe livraria!"));
                    }
                    store.livrariaLock.lock();

                    LivroS b = store.procurarLivro(m.title); /* Procurar o livro na store */
                    if(b == null){
                        return Futures.exceptionalFuture(new Exception("Nao existe livro!"));
                    }

                    int idLivro = id.incrementAndGet();

                    objs.putIfAbsent(idLivro,b);

                    store.livrariaLock.unlock();
                    return Futures.completedFuture(new StoreSearchRep(idLivro)); /* Retornar uma future ja completada com o id do livro */
                }); /* Pedido de pesquisa de um livro(titulo) e devolver livro */

                c.handler(StoreMakeCartReq.class, (m) -> {
                    LivrariaS store = (LivrariaS) objs.get(m.storeid); /* Get da loja q vamos criar o cart */
                    if(store == null)
                        return Futures.exceptionalFuture(new Exception("Nao existe livraria!"));

                    store.livrariaLock.lock();
                    int idNovoCart = id.incrementAndGet(); /* Get do id do novo cart */

                    objs.putIfAbsent(idNovoCart,store.novoCarrinho()); /* Adiciona-lo aos objetos */

                    store.livrariaLock.unlock();
                    return Futures.completedFuture(new StoreMakeCartRep(idNovoCart)); /* Devolver esse id */
                }); /* Pedido para criar um cart numa store */

                c.handler(CartAddReq.class, (m) -> {
                    LivrariaS.ServerCarrinho cart = (LivrariaS.ServerCarrinho) objs.get(m.cartid); /* Objeto cart */

                    LivroS b = (LivroS) objs.get(m.bookid); /* Objeto book */

                    if(cart == null || b == null)
                        return Futures.exceptionalFuture(new Exception("Nao existe livro ou livraria!"));

                    cart.carrinhoLock.lock();
                    b.livrolock.lock();
                    try {
                        cart.adicionaLivro(b); /* Adicionar o book ao cart */
                    } catch (Exception e) {
                        b.livrolock.unlock();
                        cart.carrinhoLock.unlock();
                        return Futures.exceptionalFuture(e);
                    }

                    b.livrolock.unlock();
                    cart.carrinhoLock.unlock();

                    return Futures.completedFuture(new CartAddRep());
                }); /* Adicionar um book a um carrinho */

                c.handler(GetBookIsbnReq.class, (m) -> {
                    LivroS book = (LivroS) objs.get(m.idBook);
                    if(book == null)
                        return Futures.exceptionalFuture(new Exception("Nao existe livro ou livraria!"));

                    return Futures.completedFuture(new GetBookIsbnRep(book.getIsbn()));
                }); /* Get isbn de um livro */

                c.handler(GetBookTitleReq.class, (m) -> {
                    LivroS book = (LivroS) objs.get(m.idBook);
                    if(book == null)
                        return Futures.exceptionalFuture(new Exception("Nao existe livro ou livraria!"));

                    return Futures.completedFuture(new GetBookTitleRep(book.getTitle()));
                }); /* Get title de um livro */

                c.handler(GetBookPrecoReq.class, (m) -> {
                    LivroS book = (LivroS) objs.get(m.idBook);
                    if(book == null)
                        return Futures.exceptionalFuture(new Exception("Nao existe livro ou livraria!"));

                    return Futures.completedFuture(new GetBookPrecoRep(book.getPreco()));
                }); /* Get preco de um livro */

                /* Banco */
                /* Pagamento */
                c.handler(StoreBuyReq.class, (m) -> {
                    LivrariaS.ServerCarrinho carrinho = (LivrariaS.ServerCarrinho) objs.get(m.idCart);
                    BancoC b = (BancoC)objs.get(2); /* Banco */

                    if(b == null || carrinho == null || carrinho.aberto == false)
                        return Futures.exceptionalFuture(new Exception("Nao existe carrinho ou banco!"));

                    ContaBancariaC conta = null;
                    try {
                        conta = (ContaBancariaC)b.getContaBancaria(m.nib);
                        if(conta == null) throw new Exception("Nao existe conta!");
                    } catch (Exception e) {
                        return Futures.exceptionalFuture(e);
                    }

                    /* Realizar Compra*/
                    try{
                        carrinho.carrinhoLock.lock();
                        float total = carrinho.getTotal();

                        conta.contaLock.lock();
                        conta.registarPagamento(total);

                        carrinho.finalizarCompra(m.nib);

                    }catch (Exception e ){
                        conta.contaLock.unlock();
                        carrinho.carrinhoLock.unlock();
                        return Futures.exceptionalFuture(e);
                    }

                    conta.contaLock.unlock();
                    carrinho.carrinhoLock.unlock();

                    return Futures.completedFuture(new StoreBuyRep());
                }); /* Efetuar Pagamento de um carrinho */

                c.handler(GetHistoricoContaReq.class, (m) -> {

                    BancoC b = (BancoC)objs.get(2);
                    if(b == null ) return Futures.exceptionalFuture(new Exception("Nao existe banco!"));

                    ContaBancariaC conta = null;
                    try {
                        conta = (ContaBancariaC)b.getContaBancaria(m.nib);
                        if(conta == null) throw new Exception("Nao existe conta!");
                    } catch (Exception e) {
                        return Futures.exceptionalFuture(e);
                    }

                    List<String> resultado;
                    try {
                        conta.contaLock.lock();
                        resultado = conta.historicoPagamentos();
                        if(resultado== null) throw new Exception("Sem resultados.");
                    } catch (Exception e) {
                        conta.contaLock.unlock();
                        return Futures.exceptionalFuture(e);
                    }

                    conta.contaLock.unlock();
                    return  Futures.completedFuture(new GetHistoricoContaRep(resultado));
                }); /* Get historico de uma conta bancaria */

            });
        });
    }

    private static void registarMensagens(ThreadContext tc){
        tc.serializer().register(StoreSearchReq.class);
        tc.serializer().register(StoreSearchRep.class);
        tc.serializer().register(StoreMakeCartReq.class);
        tc.serializer().register(StoreMakeCartRep.class);
        tc.serializer().register(CartAddRep.class);
        tc.serializer().register(CartAddReq.class);
        tc.serializer().register(GetBookIsbnReq.class);
        tc.serializer().register(GetBookIsbnRep.class);
        tc.serializer().register(GetBookTitleReq.class);
        tc.serializer().register(GetBookTitleRep.class);
        tc.serializer().register(GetBookPrecoRep.class);
        tc.serializer().register(GetBookPrecoReq.class);
        tc.serializer().register(StoreBuyRep.class);
        tc.serializer().register(StoreBuyReq.class);
        tc.serializer().register(GetHistoricoContaRep.class);
        tc.serializer().register(GetHistoricoContaReq.class);
    }
}
