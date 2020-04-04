package com.music.backend.service;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.keyCancion;

public interface CancionService {

	public Boolean createCancion(Cancion c) throws Exception;
}
