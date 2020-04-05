package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.entity.Podcast;
import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.keyLista;

@Repository
public interface PodcastRepository extends CrudRepository<Podcast,keyLista> {

	@Query(value = "SELECT * FROM Podcast WHERE lista_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	public Podcast findById(int i, String s);
	@Query(value = "SELECT * FROM Podcast WHERE usuario_id = ?1", nativeQuery = true)
	public Iterable<Podcast> getByUser(String s);
}
