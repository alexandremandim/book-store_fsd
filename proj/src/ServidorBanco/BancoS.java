package ServidorBanco;

import ServidorLivraria.Mensagens.*;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BancoS {

    public String nomeBanco;
    public Map<Integer, ContaBancaria> contasBancarias;
    public Lock bancoLock = new ReentrantLock();

    public BancoS(String nome){
        this.nomeBanco=nome;
        this.contasBancarias = new HashMap<>();

        /* TODO: Tb estou a assumir a criação de 2 contas bancarias quando crio um banco */
        contasBancarias.put(1, new ContaBancariaS(1));
        contasBancarias.put(1, new ContaBancariaS(2));
    }

    public ContaBancaria getConta(int nib){
        return contasBancarias.get(nib);
    }

}
