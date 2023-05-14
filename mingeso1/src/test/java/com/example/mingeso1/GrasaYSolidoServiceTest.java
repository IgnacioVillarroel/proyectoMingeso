package com.example.mingeso1;

import com.example.mingeso1.entities.GrasaYSolidoEntity;
import com.example.mingeso1.repositories.GrasaYSolidoRepository;
import com.example.mingeso1.services.GrasaYSolidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GrasaSolidoServiceTest {

    @Autowired
    private GrasaYSolidoRepository grasaSolidoRepository;

    @Autowired
    private GrasaYSolidoService grasaSolidoService;

    @Test
    void pagoPorGrasa(){
        double kilos = 1.0;
        double grasa1 = 10.0;
        double pago1 = grasaSolidoService.pagoPorGrasa(grasa1,kilos);
        assertEquals(30.0,pago1);

        double grasa2 = 30.0;
        double pago2 = grasaSolidoService.pagoPorGrasa(grasa2,kilos);
        assertEquals(80.0,pago2);

        double grasa3 = 50.0;
        double pago3 = grasaSolidoService.pagoPorGrasa(grasa3,kilos);
        assertEquals(120.0,pago3);

        double grasa4 = -50.0;
        double pago4 = grasaSolidoService.pagoPorGrasa(grasa4,kilos);
        assertEquals(120.0,pago4);
    }

    @Test
    void pagoPorST(){
        double kilos = 1.0;
        double st1 = 5.0;
        double pago1 = grasaSolidoService.pagoPorST(st1,kilos);
        assertEquals(-130.0,pago1);

        double st2 = 10.0;
        double pago2 = grasaSolidoService.pagoPorST(st2,kilos);
        assertEquals(-90.0,pago2);

        double st3 = 20.0;
        double pago3 = grasaSolidoService.pagoPorST(st3,kilos);
        assertEquals(95.0,pago3);

        double st4 = 40.0;
        double pago4 = grasaSolidoService.pagoPorST(st4,kilos);
        assertEquals(150.0,pago4);

        double st5 = -40.0;
        double pago5 = grasaSolidoService.pagoPorST(st5,kilos);
        assertEquals(150.0,pago5);
    }

    @Test
    void obtenerGrasa(){
        GrasaYSolidoEntity grasaSolidoEntity = new GrasaYSolidoEntity();
        grasaSolidoEntity.setProveedor("01001");
        grasaSolidoEntity.setGrasa(20);
        grasaSolidoEntity.setSolido(10);
        grasaSolidoRepository.save(grasaSolidoEntity);
        double grasa = grasaSolidoService.obtenerGrasa(grasaSolidoEntity);

        assertEquals(20.0, grasa);
    }

    @Test
    void obtenerST(){
        GrasaYSolidoEntity grasaSolidoEntity = new GrasaYSolidoEntity();
        grasaSolidoEntity.setProveedor("01001");
        grasaSolidoEntity.setGrasa(20);
        grasaSolidoEntity.setSolido(10);
        grasaSolidoRepository.save(grasaSolidoEntity);
        double st = grasaSolidoService.obtenerST(grasaSolidoEntity);

        assertEquals(10.0, st);
    }

    @Test
    void obtenerGS(){
        GrasaYSolidoEntity grasaSolidoEntity = new GrasaYSolidoEntity();
        grasaSolidoEntity.setProveedor("01002");
        grasaSolidoEntity.setGrasa(20);
        grasaSolidoEntity.setSolido(10);
        grasaSolidoRepository.save(grasaSolidoEntity);
        ArrayList<GrasaYSolidoEntity> gs = grasaSolidoService.obtenerGS();

        assertEquals(2, gs.size());
    }

    @Test
    void guardarDataDB(){
        String proveedor = "02002";
        int grasa = 20;
        int solido = 10;

        grasaSolidoService.guardarDataDB(proveedor,grasa,solido);

        List<GrasaYSolidoEntity> dataGuardada = grasaSolidoRepository.findAll();
        assertEquals(5, dataGuardada.size());

        GrasaYSolidoEntity data = dataGuardada.get(4);
        assertEquals(proveedor, data.getProveedor());
        assertEquals(grasa, data.getGrasa());
        assertEquals(solido, data.getSolido());
    }

    @Test
    void obtenerGSPorProveedor(){
        GrasaYSolidoEntity gs = new GrasaYSolidoEntity();
        gs.setProveedor("Proveedor A");
        gs.setGrasa(30);
        gs.setSolido(10);

        grasaSolidoRepository.save(gs);

        GrasaYSolidoEntity gsObtenido = grasaSolidoService.obtenerGSPorProveedor("Proveedor A");

        //assertEquals(gs.getId(), gsObtenido.getId());
        assertEquals(gs.getProveedor(), gsObtenido.getProveedor());
        assertEquals(gs.getGrasa(), gsObtenido.getGrasa(), 0.01);
        assertEquals(gs.getSolido(), gsObtenido.getSolido(), 0.01);
    }

}