package com.example.mingeso1.controllers;

import com.example.mingeso1.entities.AcopioLecheEntity;
import com.example.mingeso1.services.AcopioLecheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class AcopioLecheController {
    @Autowired
    private AcopioLecheService acopioLeche;

    @GetMapping("/cargarAcopio")
    public String main() {
        return "uploadAcopio";
    }

    @PostMapping("/cargarAcopio")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        acopioLeche.eliminarAcopios();
        acopioLeche.guardarAcopio(file);
        String filename = file.getOriginalFilename();
        redirectAttributes.addFlashAttribute("mensaje", "Archivo cargado con éxito");
        acopioLeche.leerCsv(filename);
        return "redirect:/cargarAcopio";
    }

    @GetMapping("/listAcopio")
    public String listar(Model model) {
        ArrayList<AcopioLecheEntity> acopioLeches = acopioLeche.obtenerAcopio();
        model.addAttribute("acopioLeches", acopioLeches);
        return "listAcopio";
    }
}
