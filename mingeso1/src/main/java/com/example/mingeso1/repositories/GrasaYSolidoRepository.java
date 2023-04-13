package com.example.mingeso1.repositories;

import com.example.mingeso1.entities.GrasaYSolidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrasaYSolidoRepository extends JpaRepository<GrasaYSolidoEntity, String> {
}
