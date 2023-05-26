package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Produto;
import com.intraviologistica.intravio.model.ProdutoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    public void testSalvarProduto() {
        Produto produto =  ProdutoTest.getProduto1();

        produtoRepository.save(produto);

        assertThat(produto.getId()).isNotNull();
    }

    @Test
    public void testBuscarProdutoPorNome() {
        Produto produto =  ProdutoTest.getProduto1();

        produtoRepository.save(produto);

        Produto encontrado = produtoRepository.findByNome("Notebook");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(produto.getId());
    }

    @Test
    public void testBuscarTodosProdutos() {
        List<Produto> produtos = produtoRepository.findAll();

        assertThat(produtos).isEmpty();

        Produto notebook =  ProdutoTest.getProduto1();
        notebook = produtoRepository.save(notebook);

        Produto smartphone =  ProdutoTest.getProduto2();
        smartphone = produtoRepository.save(smartphone);

        produtos = produtoRepository.findAll();

        assertThat(produtos).hasSize(2);
        assertThat(produtos).contains(notebook, smartphone);
    }

    @Test
    public void testBuscarProdutoPorId() {
        Produto produto =  ProdutoTest.getProduto1();

        produtoRepository.save(produto);

        Optional<Produto> produtoEncontrado = produtoRepository.findById(produto.getId());

        assertThat(produtoEncontrado).isPresent().get().isEqualToComparingFieldByField(produto);
        assertThat(produtoEncontrado.isPresent()).isTrue();
        assertThat(produtoEncontrado.get().getNome()).isEqualTo(produto.getNome());
        assertThat(produtoEncontrado.get().getCodigo()).isEqualTo(produto.getCodigo());
        assertThat(produtoEncontrado.get().getDescricao()).isEqualTo(produto.getDescricao());
        assertThat(produtoEncontrado.get().getPreco()).isEqualTo(produto.getPreco());
        assertThat(produtoEncontrado.get().getPeso()).isEqualTo(produto.getPeso());
        assertThat(produtoEncontrado.get().getFabricante()).isEqualTo(produto.getFabricante());
        assertThat(produtoEncontrado.get().getModelo()).isEqualTo(produto.getModelo());
        assertThat(produtoEncontrado.get().getDataCriacao()).isEqualTo(produto.getDataCriacao());
        assertThat(produtoEncontrado.get().getDataAtualizacao()).isEqualTo(produto.getDataAtualizacao());
    }

    @Test
    public void testDeleteProdutoById() {
        Produto produto =  ProdutoTest.getProduto1();
        Produto produtoSalvo = produtoRepository.save(produto);

        assertThat(produtoSalvo.getId()).isNotNull();

        produtoRepository.deleteById(produtoSalvo.getId());

        Optional<Produto> produtoRemovido = produtoRepository.findById(produtoSalvo.getId());
        assertThat(produtoRemovido).isNotPresent();
    }

    @Test
    public void testAtualizaProduto() {
        Produto produto = ProdutoTest.getProduto1();

        Produto produtoSalvo = produtoRepository.save(produto);

        produtoSalvo = new Produto(produtoSalvo.getId(), "Smartphone", "Smartphone Samsung Galaxy S20", 4000.0, 0.3, "Samsung", "Galaxy S20", LocalDateTime.now());
        produtoSalvo.setDataCriacao(produto.getDataCriacao());
        produto.setCodigo(1245);

        Produto produtoAtualizado = produtoRepository.save(produtoSalvo);

        assertThat(produtoAtualizado.getId()).isEqualTo(produtoSalvo.getId());
        assertThat(produtoAtualizado.getNome()).isEqualTo(produtoSalvo.getNome());
        assertThat(produtoAtualizado.getPreco()).isEqualTo(4000.0);
        assertThat(produtoAtualizado.getPeso()).isEqualTo(0.3);
        assertThat(produtoAtualizado.getDescricao()).isEqualTo(produtoSalvo.getDescricao());
        assertThat(produtoAtualizado.getModelo()).isEqualTo(produtoSalvo.getModelo());
        assertThat(produtoAtualizado.getDataAtualizacao()).isEqualTo(produtoSalvo.getDataAtualizacao());
        assertThat(produtoAtualizado.getDataCriacao()).isEqualTo(produtoSalvo.getDataCriacao());
        assertThat(produtoAtualizado.getFabricante()).isEqualTo(produtoSalvo.getFabricante());
        assertThat(produtoAtualizado.getCodigo()).isEqualTo(produtoSalvo.getCodigo());
    }
}