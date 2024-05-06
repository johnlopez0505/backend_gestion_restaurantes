package com.john.backend_gestion_restaurantes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.john.backend_gestion_restaurantes.modelos.RefreshToken;

@Repository
public interface RepoRefreshToken extends JpaRepository<RefreshToken,Integer>{  
} 
