package ServidorBanco;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContaBancariaS implements ContaBancaria {
    public final int nib;
    private float saldo;
    public List<Pagamento> pagamentos;
    public Lock contaLock = new ReentrantLock();

    public ContaBancariaS(int nib){
        this.nib=nib;
        saldo=1000;
        pagamentos=new LinkedList<Pagamento>();
    }

    @Override
    public float getSaldo(){
        return this.saldo;
    }

    @Override
    public void registarPagamento(float valor) throws Exception{
        if(valor > saldo)   throw new Exception("Nao existe saldo suficiente!");
        Pagamento p = new Pagamento(new Date(), valor);
        pagamentos.add(p);
        saldo = saldo - valor;
        System.out.println("Pagamento concluido.");
    }

    @Override
    public List<String> historicoPagamentos() {
        List<String> r = new ArrayList<>();
        for (Pagamento p : pagamentos){
            r.add(p.toString());
        }
        return r;
    }
}
