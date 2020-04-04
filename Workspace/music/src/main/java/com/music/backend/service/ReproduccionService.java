package com.music.backend.service;

import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyLista;

public interface ReproduccionService {

	public Boolean createReproduccion(Reproduccion r) throws Exception;
}
