package com.music.backend.service;

import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Podcast;

public interface PodcastService {

	public Boolean createPodcast(Podcast p, byte[] b) throws Exception;
	public Podcast getPodcast(int i, String s) throws Exception;
	public Boolean deletePodcast(int i, String s) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public Podcast[] getPodcastByUser(String s) throws Exception;
	public Cancion[] listPodcast(int i, String s) throws Exception;
	public Podcast[] getPodcastsBySearch(String nombre) throws Exception;
	public Boolean changeImage(MultipartFile f, String correo, int id) throws Exception;
}
