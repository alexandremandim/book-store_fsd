package ServidorBanco;

import java.util.Date;

public class Pagamento {
    public final Date dataPagamento;
    public float valor;

    public Pagamento(Date dataPagamento, float valor){
        this.dataPagamento = dataPagamento;
        this.valor = valor;
    }

    @Override
    public String toString(){
        return "Data: " + dataPagamento.toString() + " Valor: " + valor;
    }
}
