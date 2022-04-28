package Cliente;

import ServidorLivraria.Livraria;
import ServidorLivraria.Mensagens.*;

import Utils.ObjRef;
import Utils.Util;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.List;

public class LivrariaC implements Livraria {
    private final SingleThreadContext tc;
    private final Connection c;
    private final int storeid;

    public LivrariaC() throws Exception {
        storeid = 1; /* Isto n está bem mas vais ser resolvido em aulas futuras */
        Transport t = new NettyTransport();
        tc = new SingleThreadContext("srv-%d", new Serializer());
        tc.serializer().register(StoreSearchReq.class);
        tc.serializer().register(StoreSearchRep.class);
        tc.serializer().register(StoreMakeCartReq.class);
        tc.serializer().register(StoreMakeCartRep.class);
        tc.serializer().register(GetBookIsbnReq.class);
        tc.serializer().register(GetBookIsbnRep.class);
        tc.serializer().register(GetBookTitleReq.class);
        tc.serializer().register(GetBookTitleRep.class);
        tc.serializer().register(GetBookPrecoReq.class);
        tc.serializer().register(GetBookPrecoRep.class);
        tc.serializer().register(CartAddRep.class);
        tc.serializer().register(CartAddReq.class);
        tc.serializer().register(StoreBuyRep.class);
        tc.serializer().register(StoreBuyReq.class);
        tc.serializer().register(GetHistoricoContaRep.class);
        tc.serializer().register(GetHistoricoContaReq.class);

        c = tc.execute(() ->
                t.client().connect(new Address("localhost", 10000))
        ).join().get();
    }

    public LivroC procurarLivro(String title) throws Exception {
        StoreSearchRep r = (StoreSearchRep) tc.execute(() ->
                c.sendAndReceive(new StoreSearchReq(title, storeid))
        ).join().get();

        ObjRef ref = new ObjRef(new Address("localhost", 10000), r.idBook, "LivroC");
        return (LivroC) Util.makeRemote(tc, ref);
    }

    public CarrinhoC novoCarrinho() throws Exception {
        StoreMakeCartRep r = (StoreMakeCartRep) tc.execute(() ->
                c.sendAndReceive(new StoreMakeCartReq(storeid))
        ).join().get();

        /* Vamos criar uma connection diferente e nao passamos esta porque o CarrinhoC poderia estar noutro servidor */
        ObjRef ref = new ObjRef(new Address("localhost", 10000), r.cartid, "CarrinhoC");
        return (CarrinhoC) Util.makeRemote(tc, ref);
    }

    @Override
    public List<String> getHistorico(int nib) throws Exception{
        GetHistoricoContaRep r = (GetHistoricoContaRep) tc.execute(() ->
                c.sendAndReceive(new GetHistoricoContaReq(nib))
        ).join().get();

        return r.historico;
    }
}
