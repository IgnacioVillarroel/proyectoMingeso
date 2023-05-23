package com.example.mingeso1.controllers;

import com.example.mingeso1.entities.ProveedorEntity;
import com.example.mingeso1.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/listProveedores")
    public String listar(Model model) {
        ArrayList<ProveedorEntity> proveedores = proveedorService.obtenerProveedores();
        model.addAttribute("proveedores", proveedores);
        return "index";
    }

    @GetMapping("/nuevoproveedor")
    public String proveedor(){
        return "nuevoproveedor";
    }

    @PostMapping("/nuevoproveedor")
    public String nuevoProveedor(@RequestParam("codigo") String codigo,
                                 @RequestParam("afecto_retencion") String retencion,
                                 @RequestParam("categoria") String categoria,
                                 @RequestParam("nombre_proveedor") String nombre){
        proveedorService.guardarProveedor(codigo, retencion, categoria, nombre);
        return "redirect:/listProveedores";
    }
}