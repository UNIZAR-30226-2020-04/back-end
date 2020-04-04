package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.entity.Podcast;
import com.music.backend.entity.keyLista;

@Repository
public interface PodcastRepository extends CrudRepository<Podcast,keyLista> {

}
