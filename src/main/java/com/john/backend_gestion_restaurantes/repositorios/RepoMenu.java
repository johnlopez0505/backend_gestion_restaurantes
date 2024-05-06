package com.john.backend_gestion_restaurantes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.john.backend_gestion_restaurantes.modelos.Menu;

@Repository
public interface RepoMenu extends JpaRepository<Menu, Integer>{
}
