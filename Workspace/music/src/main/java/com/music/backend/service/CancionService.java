package com.music.backend.service;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;

public interface CancionService {

	public Boolean createCancion(keyLista a, Cancion c) throws Exception;
	public Cancion getCancion(int i, String s, int c) throws Exception;
	public Boolean deleteCancion(int i, String s, int c) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
}
