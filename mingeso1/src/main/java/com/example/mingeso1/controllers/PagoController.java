package com.example.mingeso1.controllers;

import com.example.mingeso1.entities.AcopioLecheEntity;
import com.example.mingeso1.entities.GrasaYSolidoEntity;
import com.example.mingeso1.entities.PagoEntity;
import com.example.mingeso1.entities.ProveedorEntity;
import com.example.mingeso1.services.AcopioLecheService;
import com.example.mingeso1.services.GrasaYSolidoService;
import com.example.mingeso1.services.PagoService;
import com.example.mingeso1.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private AcopioLecheService acopioLecheService;

    @Autowired
    private GrasaYSolidoService grasaSolidoService;

    @GetMapping("/listaDePagos")
    public String listar(Model model) {
        ArrayList<PagoEntity> pagos = pagoService.obtenerPagos();
        model.addAttribute("planillaPago", pagos);
        return "planillaPago";
    }

    @GetMapping("/pago")
    public String calcular(){
        //pagoService.eliminarPagos();
        ArrayList<ProveedorEntity> proveedores = proveedorService.obtenerProveedores();
        for(ProveedorEntity proveedor:proveedores){
            String codigo = proveedor.getCodigo();
            String categoria = proveedor.getCategoria();
            PagoEntity pago = new PagoEntity(null, "", codigo, proveedor.getNombre_proveedor(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            ArrayList<AcopioLecheEntity> acopios = acopioLecheService.obtenerPorProveedor(codigo);
            double kls_leche = acopioLecheService.sumarKls(acopios);
            double klsPorCategoria = acopioLecheService.klsPorCategoria(categoria, kls_leche);
            GrasaYSolidoEntity gs = grasaSolidoService.obtenerGSPorProveedor(codigo);
            double pagoGrasa;
            double grasa = 0.0;
            double st;
            double pagoST;
            if(gs == null){
                //GrasaSolidoEntity newGS = new GrasaSolidoEntity(null,codigo,0,0);
                pagoGrasa = grasaSolidoService.pagoPorGrasa(0.0, kls_leche);
                st = 0.0;
                pagoST = 0.0;

            }else{
                grasa = grasaSolidoService.obtenerGrasa(gs);
                pagoGrasa = grasaSolidoService.pagoPorGrasa(grasa, kls_leche);
                st = grasaSolidoService.obtenerST(gs);
                pagoST = grasaSolidoService.pagoPorST(st, kls_leche);
            }
            //double pagoGrasa = grasaSolidoService.pagoPorGrasa(grasa, kls_leche);
            double bono = acopioLecheService.bonoFrecuencia(codigo, kls_leche);
            double pagoAcopioLeche = klsPorCategoria + pagoGrasa + pagoST + bono;

            String quincena = acopioLecheService.obtenerQuincena(codigo);
            ArrayList<PagoEntity> pagosProveedor = pagoService.obtenerPagosByCodigoProveedor(codigo);
            PagoEntity ultimoPago = pagoService.obtenerUltimoPago(codigo);
            double totalKlsUltimoPago = pagoService.obtenerTotalKls(ultimoPago);
            int variacionLeche = pagoService.variacionLeche(totalKlsUltimoPago, kls_leche);
            double descuentoLeche = kls_leche * (variacionLeche / 100.0);
            double grasaUltimoPago = pagoService.obtenerGrasa(ultimoPago);
            int variacionGrasa = pagoService.variacionGrasa(grasaUltimoPago, grasa);
            double descuentoGrasa = grasa * (variacionGrasa / 100.0);
            double stUltimoPago = pagoService.obtenerSt(ultimoPago);
            int variacionSt = pagoService.variacionSt(stUltimoPago, st);
            double descuentoSt = st * (variacionSt / 100.0);
            double descuentos = descuentoLeche + descuentoGrasa + descuentoSt;
            double pagoTotal = pagoAcopioLeche - descuentos;

            double retencion = 0.0;
            if(pagoTotal >= 950000){
                retencion = pagoTotal * 0.13;
            }
            double pagoFinal = pagoTotal - retencion;

            pago.setQuincena(quincena);
            pago.setTotal_kls(kls_leche);
            pago.setDias(acopioLecheService.cantidadDias(codigo));
            pago.setPromedio_kls(acopioLecheService.promedioKls(codigo, acopios));
            pago.setVariacion_leche(variacionLeche);
            pago.setGrasa(grasa);
            pago.setVariacion_grasa(variacionGrasa);
            pago.setSolidos_totales(st);
            pago.setVariacion_st(variacionSt);
            pago.setPago_leche(klsPorCategoria);
            pago.setPago_grasa(pagoGrasa);
            pago.setPago_st(pagoST);
            pago.setBono(bono);
            pago.setDcto_leche(descuentoLeche);
            pago.setDcto_grasa(descuentoGrasa);
            pago.setDcto_st(descuentoSt);
            pago.setTotal(pagoTotal);
            pago.setMonto_retencion(retencion);
            pago.setMonto_final(pagoFinal);
            pagoService.guardarPago(pago);
        }
        return "redirect:/listaDePagos";
    }





    /* @GetMapping("/pago")
    public ResponseEntity<List<PagoEntity>> calcular(){
        //pagoService.eliminarPagos();
        List<ProveedorEntity> proveedores = proveedorService.obtenerProveedores();
        for(ProveedorEntity proveedor:proveedores){
            String codigo = proveedor.getCodigo();
            String categoria = proveedor.getCategoria();
            PagoEntity pago = new PagoEntity(null, "", codigo, proveedor.getNombre_proveedor(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            double kls_leche = acopioLecheService.sumarKls(codigo);
            double klsPorCategoria = acopioLecheService.klsPorCategoria(categoria, kls_leche);
            GrasaYSolidoEntity gs = grasaSolidoService.obtenerGSPorProveedor(codigo);
            double pagoGrasa;
            double grasa = 0.0;
            double st;
            double pagoST;
            if(gs == null){
                //GrasaSolidoEntity newGS = new GrasaSolidoEntity(null,codigo,0,0);
                pagoGrasa = grasaSolidoService.pagoPorGrasa(0.0, kls_leche);
                st = 0.0;
                pagoST = 0.0;

            }else{
                grasa = grasaSolidoService.obtenerGrasa(gs);
                pagoGrasa = grasaSolidoService.pagoPorGrasa(grasa, kls_leche);
                st = grasaSolidoService.obtenerST(gs);
                pagoST = grasaSolidoService.pagoPorST(st, kls_leche);
            }
            //double grasa = grasaSolidoService.obtenerGrasa(codigo);
            //double pagoGrasa = grasaSolidoService.pagoPorGrasa(grasa, kls_leche);
            //double st = grasaSolidoService.obtenerST(codigo);
            //double pagoST = grasaSolidoService.pagoPorST(st, kls_leche);
            double bono = acopioLecheService.bonoFrecuencia(codigo, kls_leche);
            //double pagoAcopioLeche = klsPorCategoria + pagoGrasa + pagoST + bono;

            String quincena = acopioLecheService.obtenerQuincena(codigo);
            //ArrayList<PagoEntity> pagosProveedor = pagoService.obtenerPagosByCodigoProveedor(codigo);
            PagoEntity ultimoPago = pagoService.obtenerUltimoPago(codigo);
            double totalKlsUltimoPago = pagoService.obtenerTotalKls(ultimoPago);
            int variacionLeche = pagoService.variacionLeche(totalKlsUltimoPago, kls_leche);
            double descuentoLeche = kls_leche * (variacionLeche / 100.0);
            double grasaUltimoPago = pagoService.obtenerGrasa(ultimoPago);
            //int variacionGrasa = pagoService.variacionGrasa(grasaUltimoPago, grasa);
            //double descuentoGrasa = grasa * (variacionGrasa / 100.0);
            double stUltimoPago = pagoService.obtenerSt(ultimoPago);
            //int variacionSt = pagoService.variacionSt(stUltimoPago, st);
            //double descuentoSt = st * (variacionSt / 100.0);
            //double descuentos = descuentoLeche + descuentoGrasa + descuentoSt;
            //double pagoTotal = pagoAcopioLeche - descuentos;

            double retencion = 0.0;
            //if(pagoTotal >= 950000){
            //    retencion = pagoTotal * 0.13;
            //}
            //double pagoFinal = pagoTotal - retencion;

            pago.setTotal_kls(kls_leche);
            pago.setPago_leche(klsPorCategoria);
            pago.setGrasa(grasa);
            //pago.setPago_grasa(pagoGrasa);
            //pago.setPago_st(pagoST);
            pago.setBono(bono);

            pago.setQuincena(quincena);
            pago.setVariacion_leche(variacionLeche);
            pago.setDcto_leche(descuentoLeche);
            //pago.setVariacion_grasa(variacionGrasa);
            //pago.setDcto_grasa(descuentoGrasa);
            //pago.setVariacion_st(variacionSt);
            //pago.setDcto_st(descuentoSt);

            pago.setDias(acopioLecheService.cantidadDias(codigo));
            pago.setPromedio_kls(acopioLecheService.promedioKls(codigo));
            pago.setSolidos_totales(st);
            //pago.setTotal(pagoTotal);
            pago.setMonto_retencion(retencion);
            //pago.setMonto_final(pagoFinal);
            pagoService.guardarPago(pago);
        }
        List<PagoEntity> pagos = pagoService.obtenerPagos();
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    } */



    //retornar pago por grasa
    /* @GetMapping("/gs/pagoGrasa/{proveedor}")
    public ResponseEntity<Double> pagoGrasa(@PathVariable String proveedor) {
        double kls_leche = acopioLecheService.sumarKls(proveedor);
        double grasa = grasaSolidoService.obtenerGrasa(proveedor);
        double pago = grasaSolidoService.pagoPorGrasa(grasa, kls_leche);
        if(pago == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pago, HttpStatus.OK);
    } */

    /* @GetMapping("/pagoST/{proveedor}")
    public ResponseEntity<Double> pagoST(@PathVariable String proveedor) {
        double kls_leche = acopioLecheService.sumarKls(proveedor);
        double st = grasaSolidoService.obtenerST(proveedor);
        double pago = grasaSolidoService.pagoPorST(st, kls_leche);
        if(pago == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pago, HttpStatus.OK);
    } */

    /* @GetMapping("/bono/{proveedor}")
    public ResponseEntity<Double> bono(@PathVariable String proveedor) {
        double kls_leche = acopioLecheService.sumarKls(proveedor);
        double bono = acopioLecheService.bonoFrecuencia(proveedor, kls_leche);
        if(bono == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bono, HttpStatus.OK);
    } */

}
