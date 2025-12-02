package com.pedro.projeto1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pedro.projeto1.model.Produto;
import com.pedro.projeto1.service.ProdutoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // ================== MVC ==================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", service.listarTodos());
        return "produtos/listar";
    }

    @GetMapping("/criar")
    public String criarForm(Produto produto) {
        return "produtos/criar";
    }

    @PostMapping("/criar")
    public String criar(@Valid Produto produto, BindingResult result) {
        if(result.hasErrors()) return "produtos/criar";
        service.salvar(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Produto p = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("produto", p);
        return "produtos/editar";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @Valid Produto produto, BindingResult result) {
        if(result.hasErrors()) return "produtos/editar";
        produto.setId(id);
        service.salvar(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/produtos";
    }

    // ================== API REST ==================
    @GetMapping("/api")
    @ResponseBody
    public List<Produto> listarApi() {
        return service.listarTodos();
    }

    @PostMapping("/api")
    @ResponseBody
    public Produto criarApi(@RequestBody Produto produto) {
        return service.salvar(produto);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Produto> atualizarApi(@PathVariable Long id, @RequestBody Produto produto) {
        Produto p = service.buscarPorId(id).orElseThrow();
        p.setNome(produto.getNome());
        p.setPreco(produto.getPreco());
        service.salvar(p);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarApi(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
