package com.example.mingeso1.repositories;

import com.example.mingeso1.entities.GrasaYSolidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface GrasaYSolidoRepository extends JpaRepository<GrasaYSolidoEntity, String> {

    //Obtener grasaSolido por proveedor
    @Query(value = "select * from grasas a where a.proveedor = :proveedor", nativeQuery = true)
    public GrasaYSolidoEntity findGSByProveedor(@Param("proveedor") String proveedor);


}
