package com.example.mingeso1.services;

import com.example.mingeso1.entities.AcopioLecheEntity;
import com.example.mingeso1.entities.GrasaYSolidoEntity;
import com.example.mingeso1.repositories.AcopioLecheRepository;
import com.example.mingeso1.repositories.GrasaYSolidoRepository;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
@Service
public class GrasaYSolidoService {
    @Autowired
    private GrasaYSolidoRepository grasaSolidoRepository;

    private final Logger logg = LoggerFactory.getLogger(GrasaYSolidoService.class);

    public ArrayList<GrasaYSolidoEntity> obtenerGS(){return (ArrayList<GrasaYSolidoEntity>) grasaSolidoRepository.findAll();}

    @Generated
    public String guardarGS(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                }
                catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    @Generated
    public void leerCsv(String direccion){
        String texto = "";
        BufferedReader bf = null;
        grasaSolidoRepository.deleteAll();
        try{
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            int count = 1;
            while((bfRead = bf.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    guardarDataDB(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2]);
                    temp = temp + "\n" + bfRead;
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close();
                }catch(IOException e){
                    logg.error("ERROR", e);
                }
            }
        }
    }

    public void guardarData(GrasaYSolidoEntity grasaSolido){
        grasaSolidoRepository.save(grasaSolido);
    }

    public void guardarDataDB(String proveedor, String grasa, String solido){
        GrasaYSolidoEntity newData = new GrasaYSolidoEntity();
        newData.setProveedor(proveedor);
        newData.setGrasa(grasa);
        newData.setSolido(solido);
        guardarData(newData);
    }
}
