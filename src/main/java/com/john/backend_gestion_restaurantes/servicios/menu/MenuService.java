package com.john.backend_gestion_restaurantes.servicios.menu;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.dto.MenuDTO;
import com.john.backend_gestion_restaurantes.modelos.Menu;

public interface MenuService {

    List<MenuDTO> findAll();
    Optional<MenuDTO> findById(Integer id);
    Optional<Menu> findMenuById(Integer id);
    Optional<MenuDTO> save(Menu menu);
    void deleteById(Integer id);
    void deleteAll();
    List<MenuDTO> getMenusCreatedByUser(String userId);
}
