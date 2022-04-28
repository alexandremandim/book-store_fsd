package ServidorBanco;

import java.util.List;

public interface ContaBancaria {

    float getSaldo() throws Exception;
    void registarPagamento(float valor) throws Exception;
    List<String> historicoPagamentos() throws Exception;
}
