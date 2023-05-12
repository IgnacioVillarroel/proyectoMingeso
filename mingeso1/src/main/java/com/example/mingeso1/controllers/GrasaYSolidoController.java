package com.example.mingeso1.controllers;

import com.example.mingeso1.entities.AcopioLecheEntity;
import com.example.mingeso1.entities.GrasaYSolidoEntity;
import com.example.mingeso1.services.AcopioLecheService;
import com.example.mingeso1.services.GrasaYSolidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping
public class GrasaYSolidoController {
    @Autowired
    private GrasaYSolidoService grasaSolido;

    @GetMapping("/cargarGrasa")
    public String main() {
        return "uploadGrasaSolido";
    }

    @PostMapping("/cargarGrasa")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        grasaSolido.eliminarGS();
        grasaSolido.guardarGS(file);
        String filename = file.getOriginalFilename();
        redirectAttributes.addFlashAttribute("mensaje", "Archivo cargado con éxito");
        grasaSolido.leerCsv(filename);
        return "redirect:/cargarGrasa";
    }

    @GetMapping("/listGrasa")
    public String listar(Model model) {
        ArrayList<GrasaYSolidoEntity> grasaSolidos = grasaSolido.obtenerGS();
        model.addAttribute("grasaSolidos", grasaSolidos);
        return "listGrasaSolido";
    }
}
