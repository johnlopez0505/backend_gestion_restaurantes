package com.john.backend_gestion_restaurantes.servicios.restaurante;


import com.john.backend_gestion_restaurantes.dto.RestauranteDTO;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteService {

    List<RestauranteDTO> findAllRestaurantes();
    Optional<RestauranteDTO> findById(Integer id);
    Optional<Restaurante> findRestauranteById(Integer id);
    Optional<RestauranteDTO> save(Restaurante restaurante);
    void deleteById(Integer id);
    void deleteAll();
    List<RestauranteDTO> getRestaurantesCreatedByUser(String userId);

}
