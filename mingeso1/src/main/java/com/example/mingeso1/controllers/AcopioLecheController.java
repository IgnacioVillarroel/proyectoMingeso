package com.example.mingeso1.controllers;

import com.example.mingeso1.entities.AcopioLecheEntity;
import com.example.mingeso1.services.AcopioLecheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping
public class AcopioLecheController {
    @Autowired
    private AcopioLecheService acopioLeche;

    @GetMapping("/fileUpload")
    public String main() {
        return "fileUpload";
    }

    @PostMapping("/fileUpload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        acopioLeche.guardarAcopio(file);
        redirectAttributes.addFlashAttribute("mensaje", "Archivo cargado con éxito");
        acopioLeche.leerCsv("Acopio.csv");
        return "redirect:/fileUpload";
    }

    @GetMapping("/fileInformation")
    public String listar(Model model) {
        ArrayList<AcopioLecheEntity> acopioLeches = acopioLeche.obtenerAcopio();
        model.addAttribute("acopioLeches", acopioLeches);
        return "fileInformation";
    }
}
