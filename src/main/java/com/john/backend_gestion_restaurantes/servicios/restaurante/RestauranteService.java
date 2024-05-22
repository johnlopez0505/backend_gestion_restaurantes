package com.john.backend_gestion_restaurantes.servicios.restaurante;

import com.john.backend_gestion_restaurantes.modelos.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteService {
    List<Restaurante> findAllRestaurantes();
    Restaurante save(Restaurante restaurante);
    Optional<Restaurante> findById(Integer id);
    void deleteById(Integer id);
     List<Restaurante> getRestaurantesCreatedByUser(String username);
}
