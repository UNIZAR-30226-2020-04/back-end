package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.entity.Capitulo;
import com.music.backend.entity.Podcast;
import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.keyCancion;

@Repository
public interface CapituloRepository extends CrudRepository<Capitulo,keyCancion> {

	@Query(value = "SELECT * FROM zzcapitulo WHERE lista_id = ?1 AND usuario_id = ?2 AND cancion_id = ?3", nativeQuery = true)
	public Capitulo findById(int i, String s, int c);
	@Query(value = "SELECT * FROM zzcapitulo WHERE usuario_id = ?1", nativeQuery = true)
	public Iterable<Capitulo> getByUser(String s);
	@Query(value = "SELECT * FROM zzcapitulo WHERE lista_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	public Capitulo[] listSongsInAlbumUser(int i, String s);
	@Query(value = "SELECT * FROM zzcapitulo WHERE lista_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	public Iterable<Capitulo> getListFromAlbum(int i, String s);
	@Query(value = "SELECT * FROM zzcapitulo WHERE nombre LIKE %?1%", nativeQuery = true)
	public Capitulo[] getSongsBySearch(String nombre);
}
