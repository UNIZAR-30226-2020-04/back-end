package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Podcast;
import com.music.backend.entity.keyCancion;

@Repository
public interface CancionRepository extends CrudRepository<Cancion,keyCancion> {

	@Query(value = "SELECT * FROM Cancion WHERE lista_id = ?1 AND usuario_id = ?2 AND cancion_id = ?3", nativeQuery = true)
	public Cancion findById(int i, String s, int c);
	@Query(value = "SELECT * FROM Cancion WHERE usuario_id = ?1", nativeQuery = true)
	public Iterable<Cancion> getByUser(String s);
}