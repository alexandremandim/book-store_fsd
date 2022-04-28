package ServidorLivraria;

import ServidorBanco.Banco;
import ServidorBanco.ContaBancaria;
import ServidorBanco.Mensanges.*;
import ServidorLivraria.Mensagens.*;
import Utils.ObjRef;
import Utils.Util;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

public class BancoC implements Banco {
    private final SingleThreadContext tc;
    private final Connection c;
    private final int bancoid;

    public BancoC() throws Exception {
        bancoid = 1; /*TODO: Isto n está bem mas vais ser resolvido em aulas futuras */
        Transport t = new NettyTransport();
        tc = new SingleThreadContext("srv-%d", new Serializer());

        registarMensagens(tc);

        c = tc.execute(() ->
                t.client().connect(new Address("localhost",11000))
        ).join().get();
    }

    @Override
    public ContaBancaria getContaBancaria(int nib) throws Exception{
        GetContaBancariaRep r = (GetContaBancariaRep) tc.execute(() ->
                c.sendAndReceive(new GetContaBancariaReq(bancoid,nib))
        ).join().get();

        ObjRef ref = new ObjRef(new Address("localhost",11000), r.idContaBancaria, "ContaBancariaC");
        return (ContaBancariaC) Util.makeRemote(tc,ref);
    }

    private static void registarMensagens(ThreadContext tc){
        tc.serializer().register(GetContaBancariaReq.class);
        tc.serializer().register(GetContaBancariaRep.class);
        tc.serializer().register(GetSaldoReq.class);
        tc.serializer().register(GetSaldoRep.class);
        tc.serializer().register(GetHistoricoContaReq.class);
        tc.serializer().register(GetHistoricoContaRep.class);
        tc.serializer().register(PagamentoReq.class);
        tc.serializer().register(PagamentoRep.class);
        tc.serializer().register(GetHistoricoContaBRep.class);
        tc.serializer().register(GetHistoricoContaBReq.class);
    }
}
