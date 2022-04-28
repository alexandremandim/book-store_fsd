package Utils;

import Cliente.CarrinhoC;
import Cliente.LivroC;
import ServidorBanco.ContaBancaria;
import ServidorLivraria.ContaBancariaC;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.HashMap;
import java.util.Map;

public class Util{
    public static Map<Address, Connection> addrs = new HashMap<>(); /* Objetos do servidor - Livraria, Books, Carts.

    /* TODO: TIRAR PRINTS */
    public static Object makeRemote(SingleThreadContext tc, ObjRef ref) throws Exception{

        if(ref.cls.equals("CarrinhoC")){

            Transport t = new NettyTransport();
            if(addrs.get(ref.address) != null){

                CarrinhoC cart = new CarrinhoC(tc, addrs.get(ref.address), ref.id);
                return cart;
            }else{

                Connection c = tc.execute(() ->
                        t.client().connect(new Address(ref.address))
                ).join().get();
                CarrinhoC cart = new CarrinhoC(tc, c, ref.id);
                addrs.put(ref.address,c);
                return cart;
            }



        }
        else if(ref.cls.equals("LivroC")){
            Transport t = new NettyTransport();
            if(addrs.get(ref.address) != null){
                LivroC book = new LivroC(tc,addrs.get(ref.address),ref.id);

                return book;
            }else{

                Connection c = tc.execute(() ->
                        t.client().connect(new Address(ref.address))
                ).join().get();
                addrs.put(ref.address,c);
                LivroC book = new LivroC(tc,c,ref.id);
                return book;
           }

        }
        else if(ref.cls.equals("ContaBancariaC")){
            Transport t = new NettyTransport();
            Connection c = addrs.get(ref.address);
            ContaBancaria conta;
            if(c != null){

                conta = new ContaBancariaC(tc,c,ref.id);
            }else{

                c = tc.execute(() ->
                        t.client().connect(new Address(ref.address))
                ).join().get();
                addrs.put(ref.address,c);
                conta = new ContaBancariaC(tc,c,ref.id);
            }
            return conta;
        }
        return null;
    }

}
