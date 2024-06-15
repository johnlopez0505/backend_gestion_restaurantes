package com.john.backend_gestion_restaurantes.servicios.menu;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.dto.MenuDTO;
import com.john.backend_gestion_restaurantes.modelos.Menu;
import com.john.backend_gestion_restaurantes.repositorios.RepoMenu;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;

@Service
public class MenuServiceImpl implements MenuService {
    
    private RepoMenu repoMenu;

    private FirebaseStorageService firebaseStorageService;

    public MenuServiceImpl(RepoMenu repoMenu, FirebaseStorageService firebaseStorageService){
        this.repoMenu = repoMenu;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public List<MenuDTO> findAll() {
      List<Menu> menus = repoMenu.findAll();
      return menus.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    }

    @Override
    public Optional<MenuDTO> findById(Integer id) {
        Optional<Menu> menu = repoMenu.findById(id);
        return menu.map(this::convertToDTO);
    } 
    

    @Override
    public Optional<MenuDTO> save(Menu menu) {
        Menu savedMenu = repoMenu.save(menu);
        return Optional.ofNullable(convertToDTO(savedMenu));
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
    public List<MenuDTO> getMenusCreatedByUser(String userId) {
       // Consulta los menús creados por el usuario con el nombre de usuario especificado
       List<Menu> menus = repoMenu.findByCreatedBy(userId);
        return menus.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MenuDTO convertToDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setRestauranteId(menu.getRestaurante().getId());
        dto.setUsuarioId(menu.getRestaurante().getUsuario().getId());
        dto.setNombre(menu.getNombre());
        dto.setDescripcion(menu.getDescripcion());
        dto.setPrecio(menu.getPrecio());
        dto.setImagen(firebaseStorageService.getFileUrl(menu.getImagen()));
        return dto;
    }

    @Override
    public Optional<Menu> findMenuById(Integer id) {
       return repoMenu.findById(id);
    }

}
