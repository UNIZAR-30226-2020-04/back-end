package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyLista;

@Repository
public interface ReproduccionRepository extends CrudRepository<Reproduccion,keyLista> {

	@Query(value = "SELECT * FROM reproduccion WHERE lista_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	public Reproduccion findById(int i, String s);
	@Query(value = "SELECT * FROM reproduccion WHERE usuario_id = ?1", nativeQuery = true)
	public Iterable<Reproduccion> getByUser(String s);
	@Query(value = "SELECT * FROM reproduccion WHERE usuario_id = ?1", nativeQuery = true)
	public Reproduccion[] listPlaylistsUser(String s);
	@Query(value = "SELECT * FROM reproduccion WHERE nombre LIKE %?1%", nativeQuery = true)
	public Reproduccion[] getPlaylistsBySearch(String nombre);
}
