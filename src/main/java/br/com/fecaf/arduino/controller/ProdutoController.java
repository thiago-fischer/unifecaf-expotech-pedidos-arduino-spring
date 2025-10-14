package br.com.fecaf.arduino.controller;

import br.com.fecaf.arduino.model.Produto;
import br.com.fecaf.arduino.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    // O Spring injeta o Service automaticamente
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listarTodos();
    }

    @PostMapping
    public Produto adicionar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }
}

