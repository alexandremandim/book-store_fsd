package ServidorBanco;

import ServidorBanco.Mensanges.*;
import io.atomix.catalyst.concurrent.Futures;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorBanco {
    public static void main(String[] args) throws Exception {

        Transport t = new NettyTransport();
        ThreadContext tc = new SingleThreadContext("srv-%d", new Serializer());
        AtomicInteger id = new AtomicInteger(0);
        Map<Integer, Object> objs = new ConcurrentHashMap<>();

        /* TODO: Para já adicionamos este banco. Isto pode mudar consoante o email do prof */
        objs.putIfAbsent(id.incrementAndGet(), new BancoS(new String("bpi")));

        registarMensagens(tc);

        tc.execute(() -> {
            t.server().listen(new Address(":11000"), (c) -> {
                /* Dado um banco -> GetContaBancaria */
                c.handler(GetContaBancariaReq.class, (m) -> {
                    BancoS banco = (BancoS)objs.get(m.idBanco);
                    if(banco == null){
                        return Futures.exceptionalFuture(new Exception("Nao existe conta banco."));
                    }
                    banco.bancoLock.lock();
                    ContaBancaria conta = banco.getConta(m.nib);
                    if(conta == null ){
                        return Futures.exceptionalFuture(new Exception("Nao existe conta bancaria."));
                    }

                    int idContaBancaria = id.incrementAndGet();
                    objs.putIfAbsent(idContaBancaria,conta);

                    banco.bancoLock.unlock();
                    return Futures.completedFuture(new GetContaBancariaRep(idContaBancaria));
                });

                /* Dado uma ContaBancaria -> GetSaldo */
                c.handler(GetSaldoReq.class, (m) -> {
                   ContaBancariaS conta = (ContaBancariaS)objs.get(m.idConta);
                   if(conta == null){
                       return Futures.exceptionalFuture(new Exception("Nao existe conta bancaria/banco."));
                   }
                   conta.contaLock.lock();
                   float saldo = conta.getSaldo();
                   conta.contaLock.unlock();

                   return Futures.completedFuture(new GetSaldoRep(saldo));
                });

                /* Dado uma ContaBancaria -> Devolve historico */
                c.handler(GetHistoricoContaBReq.class, (m) -> {
                    ContaBancariaS conta = (ContaBancariaS)objs.get(m.idConta);
                    if(conta == null)   return Futures.exceptionalFuture(new Exception("Nao existe conta bancaria."));

                    conta.contaLock.lock();
                    List<String> x = conta.historicoPagamentos();
                    conta.contaLock.unlock();

                    return Futures.completedFuture(new GetHistoricoContaBRep(x));
                });

                /* Dado uma ContaBancaria -> Efetua pagamento */
                c.handler(PagamentoReq.class, (m) -> {
                    ContaBancariaS conta = (ContaBancariaS)objs.get(m.idConta);
                    if(conta == null)   return Futures.exceptionalFuture(new Exception("Nao existe conta bancaria/banco."));
                    conta.contaLock.lock();
                    try{
                        conta.registarPagamento(m.valor);
                    }catch (Exception e){
                        conta.contaLock.unlock();
                        return Futures.exceptionalFuture(e);
                    }
                    conta.contaLock.unlock();
                    return Futures.completedFuture(new PagamentoRep());
                });
            });
        });
    }

    private static void registarMensagens(ThreadContext tc){
        tc.serializer().register(GetContaBancariaReq.class);
        tc.serializer().register(GetContaBancariaRep.class);
        tc.serializer().register(GetSaldoReq.class);
        tc.serializer().register(GetSaldoRep.class);
        tc.serializer().register(GetHistoricoContaBReq.class);
        tc.serializer().register(GetHistoricoContaBRep.class);
        tc.serializer().register(PagamentoReq.class);
        tc.serializer().register(PagamentoRep.class);
    }
}
