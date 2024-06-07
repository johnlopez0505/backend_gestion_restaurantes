package com.john.backend_gestion_restaurantes.servicios.restaurante;

import com.john.backend_gestion_restaurantes.dto.RestauranteDTO;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.repositorios.RepoRestaurante;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestauranteServiceImpl implements RestauranteService { 

    private RepoRestaurante repoRestaurante;

    private FirebaseStorageService firebaseStorageService;

    public RestauranteServiceImpl(RepoRestaurante repoRestaurante, FirebaseStorageService firebaseStorageService) {
        this.repoRestaurante = repoRestaurante;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public List<RestauranteDTO> findAllRestaurantes() {
        List<Restaurante> restaurantes = repoRestaurante.findAll();
        return restaurantes.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<RestauranteDTO> findById(Integer id) {
        Optional<Restaurante> restaurante = repoRestaurante.findById(id);
        return restaurante.map(this::convertToDTO);
    }

    @Override
    public Optional<Restaurante> findRestauranteById(Integer id) {
       return repoRestaurante.findById(id);
    }

    @Override
    public Optional<RestauranteDTO> save(Restaurante restaurante) {
        Restaurante savedRestaurante = repoRestaurante.save(restaurante);
        return Optional.ofNullable(convertToDTO(savedRestaurante));
    }

    @Override
    public void deleteById(Integer id) {
       this.repoRestaurante.deleteById(id);
    }

    @Override
    public void deleteAll() {
       this.repoRestaurante.deleteAll();
    }

    @Override
    public List<RestauranteDTO> getRestaurantesCreatedByUser(String userId) {
        List<Restaurante> restaurantes = repoRestaurante.findByCreatedBy(userId);
        return restaurantes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private RestauranteDTO convertToDTO(Restaurante restaurante) {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setId(restaurante.getId());
        dto.setUsuarioId(restaurante.getUsuario().getId());
        dto.setNombre(restaurante.getNombre());
        dto.setCiudad(restaurante.getCiudad());
        dto.setProvincia(restaurante.getProvincia());
        dto.setTelefono(restaurante.getTelefono());
        dto.setImagen(firebaseStorageService.getFileUrl(restaurante.getImagen()));
        dto.setDireccion(restaurante.getDireccion());
        return dto;
    }

}
