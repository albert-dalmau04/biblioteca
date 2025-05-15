package com.example.springbootthymeleafweb.controller;

import com.example.springbootthymeleafweb.model.Usuari;
import com.example.springbootthymeleafweb.repository.UsuariRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Controller
public class UsuariController {

    private UsuariRepository repository;

    public UsuariController(UsuariRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/usuaris")
    public String getUsuaris(Model model, @RequestParam(name = "error", required = false) String error) {
        List<Usuari> usuaris = repository.findAll();
        model.addAttribute("usuaris", usuaris);
        model.addAttribute("error", error); // Agrega el mensaje de error al modelo
        return "mostrarUsuaris";

    }

    @GetMapping("/afegir/usuaris")
    public String afegirUsuari(Model model) {
        model.addAttribute("usuari", new Usuari());
        return "afegirUsuaris";
    }

    @PostMapping("/insertar/usuaris")
    public String insertarUsuari(@Valid Usuari usuari) {
        repository.save(usuari);
        return "redirect:/usuaris";
    }

    @GetMapping("/editar/usuari/{dni}")
    public String editarUsuari(@PathVariable("dni") String dni, Model model) {
        Usuari usuari = repository.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + dni));
        model.addAttribute("usuari", usuari);
        return "afegirUsuaris";
    }

    @GetMapping("/esborrar/usuari/{dni}")
    public String esborrarUsuari(@PathVariable("dni") String dni){
        try{
            repository.deleteById(dni);
        } catch (Exception e) {
            String error = "No s'ha pogut esborrar el usuari amb dni: " + dni;
            return "redirect:/usuaris?error=" + URLEncoder.encode(error, StandardCharsets.UTF_8);
        }
        return "redirect:/usuaris";
    }

}
