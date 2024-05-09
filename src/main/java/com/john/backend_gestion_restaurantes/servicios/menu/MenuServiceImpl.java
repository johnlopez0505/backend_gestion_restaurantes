package com.john.backend_gestion_restaurantes.servicios.menu;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.modelos.Menu;
import com.john.backend_gestion_restaurantes.repositorios.RepoMenu;

@Service
public class MenuServiceImpl implements MenuService {
    
    private RepoMenu repoMenu;

    public MenuServiceImpl(RepoMenu repoMenu){
        this.repoMenu = repoMenu;
    }

    @Override
    public List<Menu> findAll() {
      return this.repoMenu.findAll();
    }

    @Override
    public Optional<Menu> findById(Integer id) {
        return this.repoMenu.findById(id);
    }

    @Override
    public Menu save(Menu menu) {
        return this.repoMenu.save(menu);
    }

    @Override
    public void deleteById(Integer id) {
        this.repoMenu.deleteById(id);
    }

    @Override
    public void deleteAll() {
      this.repoMenu.deleteAll();
    }

    @Override
    public List<Menu> getMenusCreatedByUser(String username) {
       // Consulta los menús creados por el usuario con el nombre de usuario especificado
       return repoMenu.findByCreatedBy(username);
    }


}
