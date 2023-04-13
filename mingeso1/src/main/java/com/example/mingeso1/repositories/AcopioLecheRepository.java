package com.example.mingeso1.repositories;

import com.example.mingeso1.entities.AcopioLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcopioLecheRepository extends JpaRepository <AcopioLecheEntity, Integer> {
}
