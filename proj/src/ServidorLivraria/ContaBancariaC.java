package ServidorLivraria;


import ServidorBanco.Mensanges.*;
import ServidorBanco.ContaBancaria;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.transport.Connection;


import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContaBancariaC implements ContaBancaria{

    private final SingleThreadContext tc;
    private final Connection c;
    public final int idConta;
    public final Lock contaLock = new ReentrantLock();

    public ContaBancariaC(SingleThreadContext tc, Connection c, int idConta) {
        this.tc = tc;
        this.c = c;
        this.idConta = idConta;
    }

    @Override
    public float getSaldo() throws Exception{
        GetSaldoRep r = (GetSaldoRep) tc.execute(() ->
                c.sendAndReceive(new GetSaldoReq(idConta))
        ).join().get();

        return r.saldo;
    }

    @Override
    public void registarPagamento(float valor) throws Exception {
        PagamentoRep r = (PagamentoRep) tc.execute(() ->
                c.sendAndReceive(new PagamentoReq(idConta,valor))
        ).join().get();
        System.out.println("Efetuei o pagamento");
    }

    @Override
    public List<String> historicoPagamentos() throws Exception{
        GetHistoricoContaBRep r = (GetHistoricoContaBRep) tc.execute(() ->
                c.sendAndReceive(new GetHistoricoContaBReq(idConta))
        ).join().get();

        return r.historico;
    }
}
