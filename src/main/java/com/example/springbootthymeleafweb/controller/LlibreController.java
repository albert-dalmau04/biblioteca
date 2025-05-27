package com.example.springbootthymeleafweb.controller;

import com.example.springbootthymeleafweb.model.Llibre;
import com.example.springbootthymeleafweb.repository.LlibreRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class LlibreController {

    private LlibreRepository repository;

    public LlibreController(LlibreRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/llibres")
    public String getLlibres(Model model, @RequestParam(name = "error", required = false) String error) {
        List<Llibre> llibres = repository.findAll();
        model.addAttribute("llibres", llibres);
        model.addAttribute("error", error); // Agrega el mensaje de error al modelo
        return "mostrarLlibres";

    }

    @GetMapping("/")
    public String Inicio() {

        return "redirect:/llibres";

    }

    @GetMapping("/esborrar/{isbn}")
    public String esborrarLlibre(@PathVariable("isbn") String isbn) {
        try {
            repository.deleteById(isbn);
        } catch (Exception e) {
            String error = "No s'ha pogut esborrar el llibre amb isbn: " + isbn + " comprova que no estigui en cap prestec";
            return "redirect:/llibres?error=" + URLEncoder.encode(error, StandardCharsets.UTF_8);
        }
        return "redirect:/llibres";
    }


    @GetMapping("/afegir")
    public String afegirLlibre(Model model) {
        model.addAttribute("llibre", new Llibre());
        return "afegirLlibres";
    }

    @PostMapping("/insertar")
    public String insertarLlibre(@Valid Llibre llibre) {
        repository.save(llibre);
        return "redirect:/llibres";
    }

    @GetMapping("/editar/{isbn}")
    public String editarLlibre(@PathVariable("isbn") String isbn, Model model) {
        Llibre llibre = repository.findById(isbn).orElseThrow(() -> new IllegalArgumentException("Invalid llibre Id:" + isbn));
        model.addAttribute("llibre", llibre);
        return "afegirLlibres";
    }

    @PostMapping("/error")
    public String Error() {
        return "error";
    }

}