package com.john.backend_gestion_restaurantes.servicios.menu;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.modelos.Menu;

public interface MenuService {

    List<Menu> findAll();
    Optional<Menu> findById(Integer id);
    Menu save(Menu menu);
    void deleteById(Integer id);
    void deleteAll();
    List<Menu> getMenusCreatedByUser(String username);
}
