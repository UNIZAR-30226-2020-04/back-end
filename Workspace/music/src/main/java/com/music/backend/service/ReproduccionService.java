package com.music.backend.service;

import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyLista;

public interface ReproduccionService {

	public Boolean createReproduccion(Reproduccion r) throws Exception;
	public Reproduccion getReproduccion(int i, String s) throws Exception;
	public Boolean deleteReproduccion(int i, String s) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
}