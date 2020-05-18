package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.entity.Album;
import com.music.backend.entity.Podcast;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyLista;


@Repository
public interface AlbumRepository extends CrudRepository<Album,keyLista> {

	@Query(value = "SELECT * FROM album WHERE lista_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	public Album findById(int i, String s);
	@Query(value = "SELECT * FROM album WHERE usuario_id = ?1", nativeQuery = true)
	public Iterable<Album> getByUser(String s);
	@Query(value = "SELECT * FROM album WHERE usuario_id = ?1", nativeQuery = true)
	public Album[] listAlbumsUser(String s);
	@Query(value = "SELECT * FROM album WHERE nombre LIKE %?1%", nativeQuery = true)
	public Album[] getAlbumsBySearch(String nombre);
}
