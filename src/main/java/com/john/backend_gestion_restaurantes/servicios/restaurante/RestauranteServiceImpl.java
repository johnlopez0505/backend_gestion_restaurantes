package com.john.backend_gestion_restaurantes.servicios.restaurante;

import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.repositorios.RepoRestaurante;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteServiceImpl implements RestauranteService{
    private RepoRestaurante repoRestaurante;

    public RestauranteServiceImpl(RepoRestaurante repoRestaurante) {
        this.repoRestaurante = repoRestaurante;
    }

    @Override
    public List<Restaurante> findAllRestaurantes() {
        return repoRestaurante.findAll();
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        return repoRestaurante.save(restaurante);
    }

    @Override
    public Optional<Restaurante> findById(Integer id) {
        return repoRestaurante.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        repoRestaurante.deleteById(id);
    }

}
