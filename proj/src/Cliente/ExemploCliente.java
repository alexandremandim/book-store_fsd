package Cliente;

import ServidorBanco.ServidorBanco;

import java.util.List;

public class ExemploCliente {
    public static void main(String[] args){

        try {
        /* Criar uma nova livraria */
            LivrariaC livraria = new LivrariaC();

        /* Procurar 2 livros na livraria */
            LivroC livro1 = livraria.procurarLivro("Titulo 1");
            System.out.println("Titulo: " + livro1.getTitle() + " ISBN: " + livro1.getIsbn() + " Preço: " + livro1.getPreco());

            LivroC livro2 = livraria.procurarLivro("Titulo 2");
            System.out.println("Titulo: " + livro2.getTitle() + " ISBN: " + livro2.getIsbn() + " Preço: " + livro2.getPreco());

        /* Criar e adicionar os livros ao carrinho */
            CarrinhoC cart = livraria.novoCarrinho();
            cart.adicionaLivro(livro1);
            cart.adicionaLivro(livro2);

        /* Efetuar compra */

            cart.finalizarCompra(1);

        /* Mostrar histórico de compras */
        System.out.println("Historico:");
            int nib = 1;
            List<String> historico = livraria.getHistorico(nib);
            for(String s: historico) System.out.println("Historico: "+s);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }



    }
}
