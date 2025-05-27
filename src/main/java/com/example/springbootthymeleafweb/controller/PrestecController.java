package com.example.springbootthymeleafweb.controller;

import com.example.springbootthymeleafweb.model.Prestec;
import com.example.springbootthymeleafweb.repository.PrestecRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;

@Controller
public class PrestecController {

    private PrestecRepository repository;

    public PrestecController(PrestecRepository repository) {
        this.repository = repository;
    }

    public static String isbnLlibre ;


    @GetMapping("/prestecs")
    public String getPrestecs(Model model, @RequestParam(name = "error", required = false) String error) {
        List<Prestec> prestecs = repository.findAll();
        model.addAttribute("prestecs", prestecs);
        model.addAttribute("error", error); // Agrega el mensaje de error al modelo
        return "mostrarPrestecs";

    }

    @GetMapping("/prestec/{isbn}")
    public String afegirPrestec(@PathVariable String isbn, Model model) {
        model.addAttribute("isbn", isbn);
        model.addAttribute("prestec", new Prestec());
        isbnLlibre = isbn;
        return "afegirPrestecs";
    }

    @PostMapping("/insertar/prestec")
    public String insertarPrestec(@Valid Prestec prestec, @RequestParam("retornat") String retornat) {

        prestec.setIsbn(isbnLlibre);

        prestec.setRetornat(retornat);

        // Obtiene la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Convierte LocalDate a java.sql.Date
        Date data_prestec = Date.valueOf(currentDate);

        // Agrega un mes a la fecha actual
        LocalDate nextMonth = currentDate.plusMonths(1);

        // Convierte LocalDate a java.sql.Date
        Date data_retorn = Date.valueOf(nextMonth);

        prestec.setDataPrestec(data_prestec);
        prestec.setDataRetorn(data_retorn);

        repository.save(prestec);
        return "redirect:/prestecs";
    }

    @GetMapping("/editar/prestec/{codi}")
    public String editarPrestec(@PathVariable("codi") int codi, Model model) {
        Prestec prestec = repository.findById(codi).orElseThrow(() -> new IllegalArgumentException("Invalid prestec Id:" + codi));
        model.addAttribute("prestec", prestec);
        isbnLlibre = prestec.getIsbn();
        return "editarPrestecs";
    }

    @PostMapping("/actualitzar/prestec")
    public String actualitzarPrestec(@Valid Prestec prestec, @RequestParam("retornat") String retornat) {

        prestec.setIsbn(isbnLlibre);

        repository.save(prestec);
        return "redirect:/prestecs";
    }


    @GetMapping("/esborrar/prestec/{codi}")
    public String esborrarPrestec(@PathVariable("codi") int codi){
        try{
            repository.deleteById(codi);
        } catch (Exception e) {
            String error = "No s'ha pogut esborrar el prestec amb codi: " + codi;
            return "redirect:/prestecs?error=" + URLEncoder.encode(error, StandardCharsets.UTF_8);
        }
        return "redirect:/prestecs";
    }

    @GetMapping("afegir/prestecs")
    public String afegirPrestecs(Model model) {
        model.addAttribute("prestec", new Prestec());
        return "afegirPrestecs";
    }


}
