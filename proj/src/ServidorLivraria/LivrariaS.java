package ServidorLivraria;

import ServidorLivraria.Mensagens.*;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LivrariaS implements Livraria {

    public Map<Integer, LivroS> books = new HashMap<>();
    public Lock livrariaLock = new ReentrantLock();

    public LivrariaS() {
        books.put(1, new LivroS(111, "Titulo 1", 150));
        books.put(2, new LivroS(222, "Titulo 2", 25));
    }

    public LivroS get(int isbn) {
        return books.get(isbn);
    }

    public LivroS procurarLivro(String title) {
        for(LivroS b: books.values())
            if (b.getTitle().equals(title))
                return b;
        return null;
    }

    @Override
    public List<String> getHistorico(int nrUltimosLivros) {
        return null;
    }

    public ServerCarrinho novoCarrinho() {
        return new ServerCarrinho();
    }




    public class ServerCarrinho implements Carrinho {
        private List<Livro> content;
        public boolean aberto;
        public Lock carrinhoLock = new ReentrantLock();

        public ServerCarrinho() {
            content = new LinkedList<>();
            aberto = true;
        }

        @Override
        public void adicionaLivro(Livro livro) throws Exception{
            if(aberto == false) throw new Exception("Carrinho Fechado");
            content.add(livro);
            System.out.println("Livro adicionado com sucesso.");
        }

        @Override
        public int getCartid() {
            return 0;
        }

        @Override
        public void finalizarCompra(int nib) throws Exception{
            if(aberto){
                aberto = false;
                System.out.println("Compra finalizada com sucesso!");
            }
            else{
               throw new Exception("Carrinho fechado");
            }
        }

        public float getTotal() throws Exception{
            if(content.size()==0) throw new Exception("Nao existem livros no carrinho.");
            float total = 0;
            for(Livro l: content){
                try {
                    total += l.getPreco();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("O total é "+total);
            return total;
        }
    }
}
